package nl.gertjanidema.netex.dataload.ndov;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;
import nl.gertjanidema.netex.dataload.dto.NdovNetexFileInfo;
import nl.gertjanidema.netex.dataload.dto.RefNdovIgnoreFileset;
import nl.gertjanidema.netex.dataload.dto.RefNdovIgnoreFilesetRepository;
import nl.gertjanidema.netex.dataload.dto.RefNdovSource;
import nl.gertjanidema.netex.dataload.dto.RefNdovSourceRepository;

@Component
public class NdovFileInfoService {
    private static Logger LOG = LoggerFactory.getLogger(NdovFileInfoService.class);
    private static Map<String, DateTimeFormatter> dateFormatters = new HashMap<>();

    @Inject NdovService ndovService;
    @Inject RefNdovSourceRepository ndovSourceRepository;
    @Inject RefNdovIgnoreFilesetRepository ndovIgnoreFilesetRepository;

    private Map<String, RefNdovSource> ndovSources = new HashMap<>();
    private List<String> ignorableFilesets;
    private Map<String, NdovFileSet> fileSets = new HashMap<>(50);
    private boolean initialized = false;
    
    private void initialize() {
        if (!initialized ) {
            this.initializeNdovSources();
            initialized = true;
        }
    }

    private void initializeNdovSources() {
        ndovSourceRepository.findAll().forEach(source -> {
            ndovSources .put(source.getSourceName(), source);
        });
        this.ignorableFilesets = ndovIgnoreFilesetRepository.findAll().stream()
                .map(RefNdovIgnoreFileset::getFileSetId)
                .toList();
    }

    public List<NdovNetexFileInfo> getAll() {
        initialize();
        List<NdovNetexFileInfo> fileInfoList = new LinkedList<>();
        try (
            var session = ndovService.createSession();
        ){
            for (var folder : session.getNetexNdovSourceFolders()) {
                var ndovSource = ndovSources.get(folder);
                if (ndovSource == null) {
                    LOG.warn("Unknown NDOV source: {}", folder);
                    continue;
                }
                if (ndovSource.getIgnore()) {
                    LOG.info("Ignoring source: {}", folder);
                }
                else {
                    fileInfoList.addAll(getFileInfo(session, ndovSource));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileInfoList;
    }

    private List<NdovNetexFileInfo> getFileInfo(NdovSession session, RefNdovSource source) throws IOException {
        List<NdovNetexFileInfo> fileInfoList = new LinkedList<>();
        String folder = source.getSourceName();
        for (FTPFile ftpFile :  session.listFiles(folder)) {
            boolean ignore = false;
            for (String id : ignorableFilesets) {
                if (ftpFile.getName().contains(id)) {
                    ignore = true;
                    break;
                }
            }
            if (ignore) {
                LOG.info("Ignoring file: {}", ftpFile.getName());
            }
            else {
                var fileInfo = getFileInfo(ftpFile, folder);
                if (fileInfo != null) {
                    var fileSetId = fileInfo.getFileSetId();
                    var fileSet = fileSets.computeIfAbsent(fileSetId, f -> new NdovFileSet(f));
                    fileSet.addFile(fileInfo);
                    fileInfoList.add(fileInfo);
                }
            }
        }
        return fileInfoList;
    }

    /**
     * Create a fileInfo object from the FTPFile object and the source folder name.
     * 
     * @param ftpFile
     * @param folder
     * @return 
     */
    private NdovNetexFileInfo getFileInfo(FTPFile ftpFile, String folder) {
        var ref = ndovSources.get(folder);
        final var fileNamePattern = Pattern.compile(ref.getPattern() + ".+");
        Matcher m = fileNamePattern.matcher(ftpFile.getName());
        if (!m.matches()) {
            LOG.warn("Unexpected file name format for: {}", ftpFile.getName());
            return null;
        }
        final var matchResult = m.toMatchResult();
        final var fileInfo = new NdovNetexFileInfo();
        ref.getFields().forEach(field -> {
            var value = matchResult.group(field.getIndex());
            switch (field.getTarget()) {
            case "file_set_id":
                fileInfo.setFileSetId(value);
                break;
            case "version_date":
                var formatter = dateFormatters.computeIfAbsent(field.getFormat(), DateTimeFormatter::ofPattern);
                var date = LocalDate.parse(value, formatter);
                fileInfo.setVersionDate(date);
                break;
            case "start_date":
                formatter = dateFormatters.computeIfAbsent(field.getFormat(), DateTimeFormatter::ofPattern);
                date = LocalDate.parse(value, formatter);
//                fileInfo.setStartDate(date);
                break;
            case "end_date":
                formatter = dateFormatters.computeIfAbsent(field.getFormat(), DateTimeFormatter::ofPattern);
                date = LocalDate.parse(value, formatter);
//                fileInfo.setEndDate(date);
                break;
            case "version":
//                fileInfo.setVersion(value);
                break;
            default:
                throw new RuntimeException("Unknown NDOV field : " + field.getTarget());
            }
        });
//        if (fileInfo.getStartDate() == null) fileInfo.setStartDate(fileInfo.getVersionDate());
//        if (fileInfo.getEndDate() == null) fileInfo.setEndDate(LocalDate.MAX);
        fileInfo.setNdovSourceId(folder.toUpperCase());
        fileInfo.setFileName(ftpFile.getName());
        fileInfo.setDirectory(folder);
        fileInfo.setLastModified(ftpFile.getTimestamp().toInstant());
        fileInfo.setSize(ftpFile.getSize());
        return fileInfo;
    }
}
