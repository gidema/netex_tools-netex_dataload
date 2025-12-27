package nl.gertjanidema.netex.dataload;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
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
import nl.gertjanidema.netex.dto.NetexNetworkRepository;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(mixinStandardHelpOptions = true, description = "Updates the NeTex data.")
public class NetexDataload implements Callable<Integer>{

    private static Logger LOG = LoggerFactory.getLogger(NetexDataload.class);
    private final static Comparator<NdovNetexFileInfo> FileInfoComparator = Comparator.comparing(NdovNetexFileInfo::getStartDate)
            .thenComparing(NdovNetexFileInfo::getVersionDate);

    @Inject NdovService ndovService;
    @Inject NdovFileInfoService fileInfoService;
    @Inject NetexHeaderProcessor netexHeaderProcessor;
    @Inject NetexFileProcessor netexFileProcessor;
    @Inject StNetexDeliveryRepository deliveryRepository;
    @Inject NetexNetworkRepository networkRepository;
    @Inject NdovNetexFileInfoRepository fileInfoRepository;
    
    private boolean refreshFiles = false;

    @SuppressWarnings("unused")
    private boolean showSql = false;

    @Option(names = {"--refresh-files"}, description = "Refresh files")
    public void setRefreshFiles(boolean refreshFiles) {
        this.refreshFiles = refreshFiles;
    }

    @Option(names = {"--spring.jpa.show-sql"}, description = "Show SQL")
    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    @Override
    public Integer call() {
        if (refreshFiles) {
            LOG.info("Refreshing file info");
            refreshFileInfo();
        }
        try (var session = ndovService.createSession()) {
            updateData(session);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                newFiles.forEach(fileInfo -> {;
                    netexHeaderProcessor.processHeader(fileInfo, session);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileInfoRepository.saveAll(newFiles);
    }
    
    private void updateData(NdovSession session) {
        //Group the file info by fileSet and process each fileSet
        fileInfoRepository.findAll().stream()
            .forEach(fi -> {
                if (fi.getImportedAt() == null) {
                    processFile(fi, session);
                }
            });
    }
    
    private void processFile(NdovNetexFileInfo fileInfo, NdovSession session) {
        netexFileProcessor.processData(fileInfo, session);
        fileInfo.setImportedAt(Instant.now());
        fileInfoRepository.save(fileInfo);
    }
}