package noelle.libraries.utilities;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public final class ChecksumUtility {

    public static boolean check(File file, String hash) {
        try {
            var code = fileHash(file);
            return hash.equalsIgnoreCase(code);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static @NotNull String fileHash(File file) throws Exception {
        var digest = MessageDigest.getInstance("SHA-1");
        var inputStream = new FileInputStream(file);

        var byteArray = new byte[1024];
        var bytesCount = 0;

        while ((bytesCount = inputStream.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        inputStream.close();

        var bytes = digest.digest();
        var stringBuilder = new StringBuilder();

        for (var b : bytes) {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }

    public static @NotNull String bytesToHexString(byte @NotNull [] bytes) {
        var stringBuilder = new StringBuilder();
        for (var b : bytes) {
            var value = b & 0xFF;

            if (value < 16) {
                stringBuilder.append("0");
            }

            stringBuilder.append(Integer.toHexString(value).toUpperCase());
        }

        return stringBuilder.toString();
    }
}
