package sortingengine.server.security;

import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;

import io.javalin.http.Context;
import io.javalin.http.Header;
import jakarta.servlet.http.HttpServletRequest;
import sortingengine.engine.FileHelper;
import sortingengine.server.Server;
import sortingengine.util.LoggerHelper;

public class AuthenticationManager
{
    private static final String SESSION_KEY_NAME = "AuthenticationValidation";
    private static final String KEY_HASH_FILE_PATH = "crypt/api_keys.secret.json";

    private List<KeyHashPackage> keyHashPackages = null;
    private final AuthenticationSessionObjectManager validatedSessions = new AuthenticationSessionObjectManager();

    private final SecureRandom random = new SecureRandom();
    private static final int REFRESH_TIME = 5_000;
    private static final int EXPIRE_TIME = 15_000;

    private static final Logger LOGGER = LoggerHelper.getLogger("Auth");

    public void loadKeyHashPackages()
    {
        KeyHashPackage[] keys = FileHelper.readFileAsObjectAndSaveIfAbsent(Path.of(KEY_HASH_FILE_PATH), KeyHashPackage[].class, new KeyHashPackage[0]);
        this.keyHashPackages = Arrays.stream(keys).toList();
    }

    @Nullable
    private KeyHashPackage getKeyHashPackageById(UUID id)
    {
        return keyHashPackages.stream().filter(khp -> khp.keyId().equals(id)).findFirst().orElse(null);
    }

    public AuthState getAuthState(Context ctx)
    {
        @Nullable
        String header = ctx.req().getHeader(Header.AUTHORIZATION);

        if (header != null)
        {
            String keyAndId = header.substring("Bearer ".length());

            String[] parts = keyAndId.split(":");
            if (parts.length == 3)
            {
                // key format:
                // uuidB64_saltb64_keyB64

                String uuidStringB64 = parts[0];
                String saltB64 = parts[1];
                String unhashedKeyStringB64 = parts[2];

                UUID keyId = UUID.fromString(new String(Base64.getDecoder().decode(uuidStringB64)));

                if (keyId != null)
                {
                    String requestIp = getIpAddressFromRequest(ctx.req());

                    @Nullable
                    AuthenticationSessionObject val = this.validatedSessions.get(unhashedKeyStringB64, requestIp);

                    // If not present or expired, revalidate
                    if (val != null)
                    {
                        if (!val.isExpired())
                        {
                            // VALID! Woo

                            if (val.needsRefresh())
                            {
                                // TODO: schedule refresh
                            }

                            // We can assume that since it is present, it is valid and authorized
                            return AuthState.API_KEY_VALIDATED;
                        }
                        else
                        {
                            // INVALIDATE! (Said like Grian's "Obliterate")
                            ctx.req().getSession().removeAttribute(SESSION_KEY_NAME);
                        }
                    }

                    // Compute hash and check
                    final KeyHashPackage storedKey = getKeyHashPackageById(keyId);

                    byte[] salt = Base64.getDecoder().decode(saltB64);
                    try
                    {
                        byte[] suppliedHash = hashKeyWithSalt(new String(Base64.getDecoder().decode(unhashedKeyStringB64)), salt);
                        if (Arrays.equals(suppliedHash, storedKey.getHashBytes()))
                        {
                            // Validated

                            // add to session cache
                            long time = System.currentTimeMillis();
                            AuthenticationSessionObject newSessionObject = new AuthenticationSessionObject(keyAndId, requestIp, time + EXPIRE_TIME, time + REFRESH_TIME);
                            validatedSessions.add(newSessionObject);

                            return AuthState.API_KEY_VALIDATED;
                        }
                    }
                    catch (NoSuchAlgorithmException | InvalidKeySpecException e)
                    {
                        LOGGER.error("Failed to hash inbound password. Rejecting Auth.", e);
                        return AuthState.UNAUTHORIZED;
                    }
                }
            }
        }
        
        return AuthState.UNAUTHORIZED;
    }

    @Nullable
    public String getIpAddressFromRequest(HttpServletRequest request)
    {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null)
        {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    private byte[] hashKeyWithSalt(char[] key, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        KeySpec spec = new PBEKeySpec(key, salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        return hash;
    }

    private byte[] hashKeyWithSalt(String key, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return hashKeyWithSalt(key.toCharArray(), salt);
    }

    public void generateAndSaveKey(String description) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        final Encoder encoder = Base64.getEncoder();

        UUID keyId = UUID.randomUUID();

        // generate salt
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        byte[] apiKeyBytes = new byte[16];
        random.nextBytes(salt);
        String apiKey = encoder.encodeToString(apiKeyBytes);

        byte[] hash = hashKeyWithSalt(apiKey, salt);
        
        KeyHashPackage khp = new KeyHashPackage(description, keyId, encoder.encodeToString(salt), encoder.encodeToString(hash));
        this.keyHashPackages.add(khp);

        LOGGER.info("API Key: {}", apiKey);
    }
}
