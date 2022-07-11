package noelle.loaders.common.utils;

import com.google.gson.Gson;
import noelle.loaders.common.objects.JsonObjects;
import noelle.loaders.common.objects.JsonPlatformObjects;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;

public final class JsonObjectsUtil {
    private final static Gson GSON = new Gson();

    public static JsonObjects objects(String fileName, @NotNull ClassLoader classLoader) {
        var resource = classLoader.getResourceAsStream(fileName);

        if (resource != null) {
            var reader = new InputStreamReader(resource);
            return GSON.fromJson(reader, JsonObjects.class);
        } else {
            throw new RuntimeException(fileName + " not found!");
        }
    }

    public static JsonPlatformObjects platformObjects(String fileName, @NotNull ClassLoader classLoader) {
        var resource = classLoader.getResourceAsStream(fileName);

        if (resource != null) {
            var reader = new InputStreamReader(resource);
            return GSON.fromJson(reader, JsonPlatformObjects.class);
        } else {
            throw new RuntimeException(fileName + " not found!");
        }
    }
}
