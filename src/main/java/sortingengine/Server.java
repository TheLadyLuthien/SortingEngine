package sortingengine;

import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinJte;
import sortingengine.conf.LaunchConfig;

public class Server implements Runnable
{
    private static final LaunchConfig LAUNCH_CONFIG = LaunchConfig.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("server");
    
    public final Router router;
    public final Javalin javalin;

    private final TemplateEngine templateEngine;
    
    public final int port;

    public Server(int port)
    {
        this.port = port;

        this.templateEngine = TemplateEngine.create(new DirectoryCodeResolver(Path.of("../src/main/jte")), ContentType.Html);

        this.javalin = Javalin.create(this::configure);
        this.router = new Router(this);

    }

    private void configure(JavalinConfig config)
    {
        config.fileRenderer(new JavalinJte(this.templateEngine));

        if (LAUNCH_CONFIG.enableStaticHotReload)
        {
            config.staticFiles.add(LAUNCH_CONFIG.staticHotRelaodPath, Location.EXTERNAL);
            LOGGER.info("Enabled static hot reload");
        }
        else
        {
            config.staticFiles.add("/web/static", Location.CLASSPATH);
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
