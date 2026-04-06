package sortingengine.server.security;

import java.util.UUID;

public class AuthenticationSessionObject
{
    private final String hashedUuidAndKey;
    private final String ipAddress;
    private final long absoluteExpire;
    private final long autoRecheckAt;

    public AuthenticationSessionObject(String hashedUuidAndKey, String ipAddress, long absoluteExpire, long autoRecheckAt)
    {
        this.hashedUuidAndKey = hashedUuidAndKey;
        this.ipAddress = ipAddress;
        this.absoluteExpire = absoluteExpire;
        this.autoRecheckAt = autoRecheckAt;
    }

    public boolean isExpired()
    {
        return System.currentTimeMillis() > absoluteExpire;
    }

    public boolean needsRefresh()
    {
        return System.currentTimeMillis() > autoRecheckAt;
    }

    public String getHashedUuidAndKey()
    {
        return hashedUuidAndKey;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public long getAbsoluteExpire()
    {
        return absoluteExpire;
    }

    public long getAutoRecheckAt()
    {
        return autoRecheckAt;
    }
}
