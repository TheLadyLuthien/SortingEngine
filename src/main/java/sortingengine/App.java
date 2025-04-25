package sortingengine;

import java.nio.file.Path;

import sortingengine.conf.LaunchConfig;
import sortingengine.data.tag.TagCatagories;

public class App
{
    public static void main(String[] args) throws Exception
    {
        final String launchConfigPath = args.length > 0 ? args[0] : LaunchConfig.DEFAULT_CONFIG_PATH;
        LaunchConfig.load(Path.of(launchConfigPath));

        TagCatagories.init();

        Server server = new Server(LaunchConfig.getInstance().port);
        server.run();
    }
}
