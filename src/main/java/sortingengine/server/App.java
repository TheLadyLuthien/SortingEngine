package sortingengine.server;

import java.nio.file.Path;

import sortingengine.conf.LaunchConfig;
import sortingengine.conf.RuntimeConfig;
import sortingengine.engine.FileHelper;
import sortingengine.engine.data.tag.TagCatagory;
import sortingengine.server.security.CertGenerator;

public class App
{
    public static void main(String[] args) throws Exception
    {
        final String launchConfigPath = args.length > 0 ? args[0] : LaunchConfig.DEFAULT_CONFIG_PATH;
        LaunchConfig.load(Path.of(launchConfigPath));

        FileHelper.ensureBaseFileStructure();

        RuntimeConfig.loadAll();
        
        CertGenerator.generateSslCertificateIfAbsent();

        Server server = new Server(LaunchConfig.getInstance().port);
        server.run();
    }
}
