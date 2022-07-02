package noelle.encryptor.utils;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.Random;

public final class SaltGeneratorUtil {
    private final static Random RANDOM = new SecureRandom();

    public static byte @NotNull [] generateSalt(int keyLength) {
        var bytes = new byte[keyLength];
        RANDOM.nextBytes(bytes);

        return bytes;
    }
}
