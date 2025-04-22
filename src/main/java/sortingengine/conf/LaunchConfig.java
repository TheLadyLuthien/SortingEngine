package sortingengine.conf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class LaunchConfig
{
    public static final String DEFAULT_CONFIG_PATH = "./launch_config.json";

    private static LaunchConfig instance = null;
    private static final Gson GSON = new Gson();

    public static final Logger LOGGER = LoggerFactory.getLogger("Launch Config");

    public final int port;
    public final boolean enableStaticHotReload;
    public final boolean allowSessionCache;
    public final String staticHotRelaodPath;

    private LaunchConfig()
    {
        port = 0;
        enableStaticHotReload = false;
        staticHotRelaodPath = null;
        allowSessionCache = true;
    }

    public static void load(Path filePath) throws IOException
    {
        final String data = new String(Files.readAllBytes(filePath));
        instance = GSON.fromJson(data, LaunchConfig.class);

        instance.validate();
    }

    private void validate()
    {
        if (this.port < 80)
        {
            throw new InvalidParameterException("port must be > than 80");
        }
        
        if (this.enableStaticHotReload && (staticHotRelaodPath == null))
        {
            throw new InvalidParameterException("static hot reload path must be specified if enabled");
        }

        LOGGER.debug("Launch Config validated");
    }

    @Nonnull
    public static LaunchConfig getInstance()
    {
        if (instance == null)
        {
            throw new NullPointerException("Launch config must never be null, was it properly loaded?");
        }

        return instance;
    }
}
