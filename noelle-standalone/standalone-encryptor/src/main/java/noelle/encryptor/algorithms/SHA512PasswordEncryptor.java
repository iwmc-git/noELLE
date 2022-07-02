package noelle.encryptor.algorithms;

import com.google.common.hash.Hashing;

import noelle.encryptor.PasswordEncryptor;
import noelle.encryptor.utils.SHAEncoderUtils;

import java.util.Objects;

public final class SHA512PasswordEncryptor implements PasswordEncryptor {

    @Override
    public String encode(CharSequence rawPassword) {
        var encode = SHAEncoderUtils.encode(rawPassword, Hashing.sha512());
        return encode.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        var encode = SHAEncoderUtils.encode(rawPassword, Hashing.sha512());
        return Objects.equals(encodedPassword, encode.toString());
    }
}
