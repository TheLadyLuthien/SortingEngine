package sortingengine;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Router
{
    private final Server server;

    public Router(Server server)
    {
        this.server = server;
    }

    public void apply()
    {
        server.javalin.get("/", ctx -> ctx.render("index.jte"));

        server.javalin.get("/protected", ctx -> {
            ctx.render("pages/protected.jte");
        });

        server.javalin.get("/profile", ctx -> {
            ctx.render("pages/profile.jte");
        });
    }
}
