package nl.gertjanidema.netex.dataload.ndov;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdovSession implements AutoCloseable {
    
    private static Logger LOG = LoggerFactory.getLogger(NdovSession.class);

    private final FTPClient ftpClient;
    private final NdovService service;
    
    public NdovSession(NdovService service, FTPClient ftpClient) {
        super();
        this.service = service;
        this.ftpClient = ftpClient;
    }

//    @Inject StNetexDeliveryRepository deliveryRepository;

    /**
     * Collect a list of folders in the NDOV NeTex directory;
     * 
     * @param ftpClient The FTP Client connection
     * @return A list of sub folder names
     * @throws IOException 
     */
    public List<String> getNetexNdovSourceFolders() throws IOException {
        List<String> sourceFolders;  
        try {
            FTPFile[] folders = ftpClient.listDirectories("netex");
            sourceFolders = new ArrayList<>(folders.length);
            for (FTPFile folder : folders) {
                sourceFolders.add("netex/" + folder.getName());
            }
        } catch (@SuppressWarnings("unused") IOException e) {
            return Collections.emptyList();
        }
        return sourceFolders;
    }
    
    public File getFile(Path directory, String fileName) throws IOException {
//        ftpClient.enterLocalPassiveMode();
        var cachedFile = service.getCachePath(directory).resolve(fileName).toFile();
        if (cachedFile.exists()) {
            return cachedFile;
        }
        try (FileOutputStream outputStream = new FileOutputStream(cachedFile)) {
            var sourceFile = directory.resolve(fileName).toString();
            var succes  = ftpClient.retrieveFile(sourceFile, outputStream);
            if (succes) {
                return cachedFile;
            }
            throw new FileNotFoundException(sourceFile);
        }
    }

    public FTPFile[] listFiles(String folder) throws IOException {
        return ftpClient.listFiles(folder);
    }

    @Override
    public void close() throws IOException {
        if (ftpClient != null) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
