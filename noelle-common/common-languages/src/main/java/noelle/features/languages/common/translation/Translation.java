package noelle.features.languages.common.translation;

import net.kyori.adventure.text.Component;
import noelle.features.languages.common.enums.MessageType;

import java.util.List;

public interface Translation {
    String rawTranslated(String... replacements);
    List<String> rawTranslatedList(String... replacements);

    Component translated(String... replacements);
    List<Component> translatedList(String... replacements);

    void sendMessage(MessageType messageType, String... replacements);
    void sendMessageList(MessageType messageType, String... replacements);
}
