package noelle.loaders.common.utils;

import com.google.gson.Gson;
import noelle.loaders.common.objects.JsonObjects;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;

public class JsonObjectsUtil {
    private final static Gson GSON = new Gson();

    public static JsonObjects objects(String fileName, @NotNull Class<?> clazz) {
        var resource = clazz.getResourceAsStream(fileName);

        if (resource != null) {
            var reader = new InputStreamReader(resource);
            return GSON.fromJson(reader, JsonObjects.class);
        } else {
            throw new RuntimeException(fileName + " not found!");
        }
    }
}
