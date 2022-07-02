package noelle.encryptor.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SHAEncoderUtils {
    private final static Charset CHARSET = StandardCharsets.UTF_8;

    public static @NotNull HashCode encode(CharSequence sequence, @NotNull HashFunction function) {
        return function.hashString(sequence, CHARSET);
    }
}
