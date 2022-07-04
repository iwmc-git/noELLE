package noelle.utils.update;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Updates check module, author - <a href="https://github.com/Elytrium">Elytrium</a>
 */
public class UpdateUtil {

    public static boolean checkVersionByURL(@NotNull String url, @NotNull String currentVersion) {
        try {
            var connection = new URL(url).openConnection();
            var timeout = (int) TimeUnit.SECONDS.toMillis(5000L);

            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            var scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
            return checkVersion(scanner.nextLine().trim(), currentVersion);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to check for updates.", e);
        }
    }

    private static boolean checkVersion(@NotNull String latestVersion, @NotNull String currentVersion) {
        var latestVersionChar = latestVersion.toCharArray();
        var currentVersionChar = currentVersion.toCharArray();

        var padding = 1;
        var paddingLatest = 1;
        var paddingCurrent = 1;
        var idLatest = 0;
        var idCurrent = 0;
        var indexLatest = latestVersionChar.length - 1;
        var indexCurrent = currentVersionChar.length - 1;

        var snapshotIndex = currentVersion.indexOf("-");

        if (snapshotIndex != -1) {
            indexCurrent = snapshotIndex - 1;
            idCurrent = -1;
        }

        while (indexCurrent != 0 && indexLatest != 0) {
            if (currentVersionChar[indexCurrent] == '.' && latestVersionChar[indexLatest] == '.') {
                --indexCurrent;
                --indexLatest;

                padding = Math.max(paddingCurrent, paddingLatest) * 10;
                paddingCurrent = 1;
                paddingLatest = 1;

                continue;
            }

            if (currentVersionChar[indexCurrent] != '.') {
                idCurrent += (currentVersionChar[indexCurrent--] - '0') * paddingCurrent * padding;
                paddingCurrent *= 10;
            }

            if (latestVersionChar[indexLatest] != '.') {
                idLatest += (latestVersionChar[indexLatest--] - '0') * paddingLatest * padding;
                paddingLatest *= 10;
            }
        }

        return idLatest <= idCurrent;
    }
}
