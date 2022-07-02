package noelle.encryptor.algorithms;

import at.favre.lib.crypto.bcrypt.BCrypt;
import noelle.encryptor.PasswordEncryptor;

import java.nio.charset.StandardCharsets;

/**
 *  BCrypt implementation for ${@link PasswordEncryptor}.
 */
public final class BCryptPasswordEncryptor implements PasswordEncryptor {
    private static final BCrypt.Version BCRYPT_VERSION = BCrypt.Version.VERSION_2X;

    @Override
    public String encode(CharSequence rawPassword) {
        var hashedBytes = BCrypt.with(BCRYPT_VERSION).hash(12, rawPassword.toString().getBytes(StandardCharsets.UTF_8));
        return new String(hashedBytes, StandardCharsets.UTF_8);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var result = BCrypt.verifyer(BCRYPT_VERSION).verify(
                rawPassword.toString().getBytes(StandardCharsets.UTF_8), encodedPassword.getBytes(StandardCharsets.UTF_8)
        );

        if (!result.validFormat) {
            throw new IllegalArgumentException("Invalid crypt format!");
        }

        return result.verified;
    }
}
