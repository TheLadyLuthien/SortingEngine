package sortingengine.server.security;

import java.util.Base64;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record KeyHashPackage(
    @JsonProperty("description")
    String description,

    @JsonProperty("keyId")
    UUID keyId,

    @JsonProperty("salt")
    String salt,

    @JsonProperty("hash")
    String hash)
{
    @JsonIgnore
    public byte[] getSaltBytes()
    {
        return Base64.getDecoder().decode(salt);
    }

    @JsonIgnore
    public byte[] getHashBytes()
    {
        return Base64.getDecoder().decode(hash);
    }

    public static KeyHashPackage of(String description, UUID keyId, byte[] salt, byte[] hash)
    {
        final var encoder = Base64.getEncoder();
        String s = encoder.encodeToString(salt);
        String h = encoder.encodeToString(hash);

        return new KeyHashPackage(description, keyId, s, h);
    }
}
