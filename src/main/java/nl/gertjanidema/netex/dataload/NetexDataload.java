package nl.gertjanidema.netex.dataload;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;
import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfoRepository;
import nl.gertjanidema.netex.dataload.dto.StNetexDeliveryRepository;
import nl.gertjanidema.netex.dataload.ndov.NdovFileInfoService;
import nl.gertjanidema.netex.dataload.ndov.NdovService;
import nl.gertjanidema.netex.dataload.ndov.NdovSession;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(mixinStandardHelpOptions = true, description = "Updates the NeTex data.")
public class NetexDataload implements Callable<Integer>{

    private static Logger LOG = LoggerFactory.getLogger(NetexDataload.class);
    private final static Comparator<NdovNetexFileInfo> FileInfoComparator = Comparator.comparing(NdovNetexFileInfo::getStartDate)
            .thenComparing(NdovNetexFileInfo::getEndDate)
            .thenComparing(NdovNetexFileInfo::getVersion)
            .thenComparing(NdovNetexFileInfo::getLastModified);

    @Inject NdovService ndovService;
    @Inject NdovFileInfoService fileInfoService;
    @Inject NetexHeaderProcessor netexHeaderProcessor;
    @Inject NetexFileProcessor netexFileProcessor;
    @Inject StNetexDeliveryRepository deliveryRepository;
    @Inject NdovNetexFileInfoRepository fileInfoRepository;
    
    private boolean refreshFileInfo = false;

    @SuppressWarnings("unused")
    private boolean showSql = false;

    @Option(names = {"--refresh-file-info"}, description = "Refresh files")
    public void setRefreshFiles(boolean refreshFileInfo) {
        this.refreshFileInfo = refreshFileInfo;
    }

    @Option(names = {"--spring.jpa.show-sql"}, description = "Show SQL")
    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    @Override
    public Integer call() {
        if (refreshFileInfo) {
            LOG.info("Refreshing file info");
            refreshFileInfo();
        }
        updateData();
        return 0;
    }
    
    @Transactional
    private void refreshFileInfo() {
        Map<String, NdovNetexFileInfo> fileInfoMap = fileInfoRepository.findAll().stream().
          collect(Collectors.toMap(NdovNetexFileInfo::getFileName, f->f));
        var newFiles = new ArrayList<NdovNetexFileInfo>();
        fileInfoService.getAll().forEach(fileInfo -> {
            if (!fileInfoMap.containsKey(fileInfo.getFileName())) {
                newFiles.add(fileInfo);
            }
        });
        if (!newFiles.isEmpty()) {
            try (NdovSession session = ndovService.createSession()) {
                newFiles.forEach(fileInfo -> {
                    netexHeaderProcessor.processHeader(fileInfo, session);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileInfoRepository.saveAll(newFiles);
    }
    
    private void updateData() {
        //Group the file info by fileSet and process each fileSet
        fileInfoRepository.findAll().stream()
            .collect(Collectors.groupingBy(fi -> fi.getFileSetId()))
            .forEach((fileSetId, fileInfoList) -> {
                processFileSet(fileSetId, fileInfoList);
            });
    }
    
    private void processFileSet(String fileSetId, List<NdovNetexFileInfo> fileInfoList) {
        final var fileSet = Set.of("CXX_UBI","DOVA_authorities", "DOVA_epiap",
                "DOVA_networks", "DOVA_tariffzones");
        if (!fileSet.contains(fileSetId)) return;
        var latest = fileInfoList.stream()
            .filter(fi -> fi.getStartDate().compareTo(LocalDateTime.now()) <= 0)
            .max(FileInfoComparator)
            .orElse(null);
        var currentDelivery = deliveryRepository.findByFileSetId(fileSetId).orElse(null);
        if (latest != null) {
            if (currentDelivery == null) {
                processFile(latest);
            }
            else if (!latest.equals(currentDelivery.getFileInfo())) {
                processFile(latest);
            }
        }
    }

    private void processFile(NdovNetexFileInfo fileInfo) {
        LOG.info("Updating {}", fileInfo.getFileSetId());
        try (NdovSession session = ndovService.createSession()) {
            netexFileProcessor.processData(fileInfo, session);
            fileInfo.setImportedAt(Instant.now());
            fileInfo.setIsCurrent(true);
            fileInfoRepository.save(fileInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}