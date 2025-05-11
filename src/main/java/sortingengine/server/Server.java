package sortingengine.server;

import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import sortingengine.conf.LaunchConfig;

public class Server implements Runnable
{
    private static final LaunchConfig LAUNCH_CONFIG = LaunchConfig.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("server");
    
    public final Router router;
    public final Javalin javalin;

    public final int port;

    public Server(int port)
    {
        this.port = port;

        this.javalin = Javalin.create(this::configure);
        this.router = new Router(this);

    }

    private void configure(JavalinConfig config)
    {
        if (LAUNCH_CONFIG.enableStaticHotReload)
        {
            config.staticFiles.add(LAUNCH_CONFIG.staticHotRelaodPath, Location.EXTERNAL);
            LOGGER.info("Enabled static hot reload");
        }
        else
        {
            config.staticFiles.add("/web/dist", Location.CLASSPATH);
        }
    }

    @Override
    public void run()
    {
        // try
        // {
        
        this.router.apply();
        this.javalin.start(port);

        // }
        // finally
        // {
        //     if (mongoClient != null)
        //     {
        //         LOGGER.info("mongo client closed");
        //         this.mongoClient.close();
        //     }
        //     else
        //     {
        //         LOGGER.warn("mongo client was null upon closing");
        //     }
        // }
    }
}
