package sortingengine.server.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sortingengine.engine.Engine;
import sortingengine.util.LoggerHelper;

public class CertGenerator
{
    public static final Logger LOGGER = LoggerHelper.getLogger("Certificate Generator");

    public static final String CRYPT_PATH = "crypt/";
    public static final String SSL_CERT_PATH = "crypt/sortingengine_ssl.cer";
    public static final String SSL_KEYSTORE_PATH = "crypt/keystore.jks";

    // public static final String SSL_CERTGEN_COMMAND = "keytool -v -export -file sortingengine_ssl.cer -alias sortingengine_ssl";
    public static final String SSL_CERTGEN_COMMAND = "keytool -genkey -alias sortingengine_ssl -keyalg RSA -keysize 2048 -keystore keystore.jks -storepass default -validity 3650";
    public static final String SSL_CERTEXPORT_COMMAND = "keytool -v -export -file sortingengine_ssl.cer -keystore keystore.jks -alias sortingengine_ssl";

    // keytool -v -export -file mytrustCA.cer -keystore keystore.jks -alias mytrustCA
    public static void regenerateSslCertificate() throws IOException
    {
        var l = new ArrayList<String>(Arrays.stream(SSL_CERTGEN_COMMAND.split(" ")).toList());
        l.add("-dname");
        l.add("CN=Unknown, OU=Unknown, O=SortingEngine, L=Unknown, ST=Unknown, C=Unknown");
        
        ProcessBuilder processBuilder = new ProcessBuilder().directory(Path.of(CRYPT_PATH).toAbsolutePath().toFile()).command(l);

        LOGGER.info("Generating SSL Certificates");
        Process process = processBuilder.start();
        
        while (process.isAlive())
        {
            try
            {
                Thread.sleep(10);
            }
            catch (Exception e)
            {
                break;
            }
        }
        
        if (process.exitValue() == 0)
        {
            LOGGER.info("Successfully generated certificate");
        }
        else
        {
            LOGGER.error("Failed to generated certificate. Keytool exited with value {}", process.exitValue());
        }
    }

    public static void exportSslCertificate() throws IOException
    {
        var l = new ArrayList<String>(Arrays.stream(SSL_CERTEXPORT_COMMAND.split(" ")).toList());
        
        ProcessBuilder processBuilder = new ProcessBuilder().directory(Path.of(CRYPT_PATH).toAbsolutePath().toFile()).command(l);

        LOGGER.info("Exporting SSL Certificates");
        Process process = processBuilder.start();
        
        while (process.isAlive())
        {
            try
            {
                Thread.sleep(10);
            }
            catch (Exception e)
            {
                break;
            }
        }
        
        if (process.exitValue() == 0)
        {
            LOGGER.info("Successfully exported certificate");
        }
        else
        {
            LOGGER.error("Failed to export certificate. Keytool exited with value {}", process.exitValue());
        }
    }

    public static void generateSslCertificateIfAbsent() throws IOException
    {
        if (!Files.exists(Path.of(SSL_KEYSTORE_PATH)) && !Files.exists(Path.of(SSL_CERT_PATH)))
        {
            regenerateSslCertificate();
        }
    }
    public static void exportSslCertificateIfAbsent() throws IOException
    {
        if (Files.exists(Path.of(SSL_KEYSTORE_PATH)) && !Files.exists(Path.of(SSL_CERT_PATH)))
        {
            exportSslCertificate();
        }
    }
}
