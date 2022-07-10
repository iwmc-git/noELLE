package noelle.features.languages.common.translation;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Translation {
    @Nullable Component translatedComponent(String... replacements);
    @Nullable List<Component> translatedList(String... replacements);
}
