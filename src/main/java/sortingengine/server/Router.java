package sortingengine.server;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Header;
import io.javalin.http.UnauthorizedResponse;
import sortingengine.engine.Engine;
import sortingengine.engine.data.item.LocationData;
import sortingengine.server.security.AuthState;
import sortingengine.server.security.AuthenticationManager;

public class Router
{
    private final Server server;

    public Router(Server server)
    {
        this.server = server;
    }

    public void apply()
    {
        // server.javalin.get("/", ctx -> ctx.render("index.jte"));

        // server.javalin.get("/protected", ctx -> {
        // ctx.render("pages/protected.jte");
        // });

        // server.javalin.get("/profile", ctx -> {
        // ctx.render("pages/profile.jte");
        // });

        server.javalin.get("/", ctx -> {
            ctx.json("hellooooo!");
        });
        server.javalin.get("/test/open", ctx -> {
            Server.LOGGER.info("Opened");
            ctx.json("OPEN!");
        });
        server.javalin.get("/test/close", ctx -> {
            Server.LOGGER.info("Closed");
            ctx.json("CLOSE!");
        });

        server.javalin.beforeMatched("/api/secure/*", (ctx) -> {
            if (server.authenticationManager.getAuthState(ctx) != AuthState.API_KEY_VALIDATED)
            {
                throw new UnauthorizedResponse("No valid api key included in request");
            }
        });

        server.javalin.get("/api/secure/test", ctx -> {
            ctx.json(new LocationData(10, 30));
        });




    }
}
