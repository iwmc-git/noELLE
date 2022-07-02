package noelle.encryptor.algorithms;

import noelle.encryptor.PasswordEncryptor;
import noelle.encryptor.utils.SaltGeneratorUtil;
import noelle.encryptor.utils.UTF8EncoderUtil;

import org.bouncycastle.crypto.generators.SCrypt;

import java.security.MessageDigest;
import java.util.Base64;

/**
 *  SCrypt implementation for ${@link PasswordEncryptor}.
 */
public final class SCryptPasswordEncryptor implements PasswordEncryptor {
    private static final int CPU_COST = 4;
    private static final int MEMORY_COST = 32;
    private static final int PARALLELIZATION = 1;
    private static final int KEY_LENGTH = 2;
    private static final int DEFAULT_SALT_LENGTH = 16;

    @Override
    public String encode(CharSequence rawPassword) {
        var salt = SaltGeneratorUtil.generateSalt(DEFAULT_SALT_LENGTH);
        var derived = SCrypt.generate(UTF8EncoderUtil.encode(rawPassword), salt, CPU_COST, MEMORY_COST, PARALLELIZATION, KEY_LENGTH);
        var params = Long.toString(((int) (Math.log(CPU_COST) / Math.log(2)) << 16L) | MEMORY_COST << 8 | PARALLELIZATION, 16);

        return "$" + params + '$' + encodePart(salt) + '$' + encodePart(derived);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() < KEY_LENGTH) {
            return false;
        }

        var parts = encodedPassword.split("\\$");

        if (parts.length != 4) {
            return false;
        }

        var params = Long.parseLong(parts[1], 16);
        var salt = decodePart(parts[2]);
        var derived = decodePart(parts[3]);
        var cpuCost = (int) Math.pow(2, params >> 16 & 0xffff);
        var memoryCost = (int) params >> 8 & 0xff;
        var parallelization = (int) params & 0xff;
        var generated = SCrypt.generate(UTF8EncoderUtil.encode(rawPassword), salt, cpuCost, memoryCost, parallelization, KEY_LENGTH);

        return MessageDigest.isEqual(derived, generated);
    }

    private byte[] decodePart(String part) {
        return Base64.getDecoder().decode(UTF8EncoderUtil.encode(part));
    }

    private String encodePart(byte[] part) {
        return UTF8EncoderUtil.decode(Base64.getEncoder().encode(part));
    }
}