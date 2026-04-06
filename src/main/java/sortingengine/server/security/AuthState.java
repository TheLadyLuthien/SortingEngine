package sortingengine.server.security;

import io.javalin.security.RouteRole;

public enum AuthState implements RouteRole
{
    API_KEY_VALIDATED,
    UNAUTHORIZED
}
