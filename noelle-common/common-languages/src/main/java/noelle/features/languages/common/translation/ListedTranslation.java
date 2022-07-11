package noelle.features.languages.common.translation;

import net.kyori.adventure.text.Component;

import noelle.features.utils.common.text.TextUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListedTranslation implements Translation {
    private final List<String> list;

    public ListedTranslation(List<String> list) {
        this.list = list;
    }

    @Override
    public @Nullable Component translatedComponent(String... replacements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable List<Component> translatedList(String... replacements) {
        var componentMap = new ArrayList<Component>();

        if (list == null || list.isEmpty()) {
            return List.of(Component.empty());
        }

        list.forEach(s -> {
            if (s == null || s.isEmpty() || s.isBlank()) {
                componentMap.add(Component.empty());
                return;
            }

            if (replacements.length != 0) {
                for (var i = 0; i < replacements.length; i += 2) {
                    s = s.replace(replacements[i], replacements[i + 1]);
                }
            }

            var parsed = TextUtils.toComponent(s.replace("&", "§"));
            componentMap.add(parsed);
        });

        return componentMap;
    }

    @Override
    public @Nullable String rawTranslatedComponent(String... replacements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable List<String> rawTranslatedList(String... replacements) {
        var newList = new ArrayList<String>();

        list.forEach(s -> {
            if (s == null || s.isEmpty() || s.isBlank()) {
                newList.add("");
                return;
            }

            if (replacements.length != 0) {
                for (var i = 0; i < replacements.length; i += 2) {
                    s = s.replace(replacements[i], replacements[i + 1]);
                }
            }

            newList.add(s);
        });

        return newList;
    }
}
