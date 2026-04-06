package sortingengine.server;

import java.nio.file.Path;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpCookie.SameSite;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.community.ssl.SslPlugin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import sortingengine.conf.LaunchConfig;
import sortingengine.engine.Engine;
import sortingengine.server.security.AuthenticationManager;
import sortingengine.util.LoggerHelper;

public class Server implements Runnable
{
    private static final LaunchConfig LAUNCH_CONFIG = LaunchConfig.getInstance();
    public static final Logger LOGGER = LoggerHelper.getLogger("server");

    private final SslPlugin ssl;

    public final Router router;
    public final Javalin javalin;
    public final Engine engine;
    public final AuthenticationManager authenticationManager;

    public final int port;

    public Server(int port)
    {
        this.port = port;

        this.ssl = new SslPlugin(conf -> {
            conf.redirect = false;
            conf.securePort = this.port;
            conf.sniHostCheck = false;

            conf.keystoreFromPath("crypt/keystore.jks", "default");
        });

        this.javalin = Javalin.create(this::configure);
        this.authenticationManager = new AuthenticationManager();
        this.authenticationManager.loadKeyHashPackages();

        this.router = new Router(this);

        this.engine = new Engine();
        this.engine.loadAndReplaceDatabases();
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
            config.staticFiles.add("/web", Location.CLASSPATH);
        }

        config.registerPlugin(ssl);

        config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(sessionHandler()));

        config.bundledPlugins.enableCors(cors -> {
            cors.addRule(it -> {
                it.allowHost("sortingengine.luthienfinch.com", "http://127.0.0.1:7268", "http://localhost:7268");
                it.allowCredentials = false;
            });
        });
    }

    private static SessionHandler sessionHandler()
    {
        final SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setHttpOnly(true);
        sessionHandler.setSecureRequestOnly(true);
        sessionHandler.setSameSite(SameSite.NONE);
        return sessionHandler;
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
        // if (mongoClient != null)
        // {
        // LOGGER.info("mongo client closed");
        // this.mongoClient.close();
        // }
        // else
        // {
        // LOGGER.warn("mongo client was null upon closing");
        // }
        // }
    }
}
