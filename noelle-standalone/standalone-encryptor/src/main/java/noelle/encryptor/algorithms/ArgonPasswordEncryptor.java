package noelle.encryptor.algorithms;

import noelle.encryptor.PasswordEncryptor;
import noelle.encryptor.utils.ArgonEncoderUtil;
import noelle.encryptor.utils.SaltGeneratorUtil;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

/**
 *  Argon implementation for ${@link PasswordEncryptor}.
 */
public final class ArgonPasswordEncryptor implements PasswordEncryptor {
    private static final int DEFAULT_SALT_LENGTH = 16;
    private static final int DEFAULT_HASH_LENGTH = 32;
    private static final int DEFAULT_PARALLELISM = 1;
    private static final int DEFAULT_MEMORY = 1 << 12;
    private static final int DEFAULT_ITERATIONS = 3;

    @Override
    public String encode(CharSequence rawPassword) {
        var salt = SaltGeneratorUtil.generateSalt(DEFAULT_SALT_LENGTH);
        var hash = new byte[DEFAULT_HASH_LENGTH];

        var params = new Argon2Parameters
                .Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(DEFAULT_PARALLELISM)
                .withMemoryAsKB(DEFAULT_MEMORY)
                .withIterations(DEFAULT_ITERATIONS)
                .build();

        var generator = new Argon2BytesGenerator();
        generator.init(params);
        generator.generateBytes(rawPassword.toString().toCharArray(), hash);

        return ArgonEncoderUtil.encode(hash, params);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        ArgonEncoderUtil.Argon2Hash decoded;

        try {
            decoded = ArgonEncoderUtil.decode(encodedPassword);
        } catch (IllegalArgumentException argumentException) {
            argumentException.printStackTrace();
            return false;
        }

        var hashBytes = new byte[decoded.getHash().length];
        var generator = new Argon2BytesGenerator();

        generator.init(decoded.getParameters());
        generator.generateBytes(rawPassword.toString().toCharArray(), hashBytes);

        return constantTimeArrayEquals(decoded.getHash(), hashBytes);
    }

    private static boolean constantTimeArrayEquals(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        }

        var result = 0;
        for (var i = 0; i < expected.length; i++) {
            result |= expected[i] ^ actual[i];
        }

        return result == 0;
    }
}
