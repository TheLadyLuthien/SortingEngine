package sortingengine.server.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

public class AuthenticationSessionObjectManager
{
    private final List<AuthenticationSessionObject> validatedSessions = new ArrayList<>();

    @Nullable
    public AuthenticationSessionObject get(String hashedUuidAndKey, String ip)
    {
        return validatedSessions.stream().filter(e -> e.getHashedUuidAndKey().equals(hashedUuidAndKey) && e.getIpAddress().equals(ip)).findFirst().orElse(null);
    }

    public void add(AuthenticationSessionObject object)
    {
        validatedSessions.add(object);
    }

    public void remove(AuthenticationSessionObject object)
    {
        validatedSessions.remove(object);

    }
}
