package noelle.encryptor.algorithms;

import com.google.common.hash.Hashing;
import noelle.encryptor.PasswordEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *  SHA265 implementation for ${@link PasswordEncryptor}.
 */
public final class SHA256PasswordEncryptor implements PasswordEncryptor {

    @Override
    public String encode(CharSequence rawPassword) {
        var hasher = Hashing.sha256();
        var hashed = hasher.hashString(rawPassword, StandardCharsets.UTF_8);

        return hashed.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var hasher = Hashing.sha256();
        var hashed = hasher.hashString(rawPassword, StandardCharsets.UTF_8).toString();

        return Objects.equals(encodedPassword, hashed);
    }
}
