package nl.gertjanidema.netex.dataload.ndov;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.file.Path;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NdovService {
    
//    private static Pattern fileNamePattern = Pattern.compile("(NeTEx_)?(.+?)_(20\\d{6})(.+)");

    private static Logger LOG = LoggerFactory.getLogger(NdovService.class);

    @Value("${ndov.server.ftp}")
    private String FTP_SERVER;

    @Value("${ndov.server.usesftp}")
    private boolean useSftp = true;
    
    @Value("${ndov.username}")
    private String username;

    @Value("${ndov.password}")
    private String password;
    
    @Value("${osm_netex.path.cache}")
    private Path cacheRootPath;

    public Path getCachePath(Path subPath) {
        var path = subPath == null ? cacheRootPath : cacheRootPath.resolve(subPath);
        var folder = path.toFile();
        if (!folder.exists()) {
            folder.mkdirs();
            LOG.info("Created cache folder: {}", folder.toString());
        }
        return path;
    }

    public NdovSession createSession() throws IOException {
        FTPClient ftpClient;
        if (useSftp) {
            ftpClient = new FTPSClient();
        }
        else {
            ftpClient = new FTPClient();
        }
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpClient.connect(InetAddress.getByName(FTP_SERVER));
        ftpClient.login(username, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        return new NdovSession(this, ftpClient);
    }
}
