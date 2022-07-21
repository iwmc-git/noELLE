package noelle.features.languages.common;

import noelle.features.languages.common.enums.Language;
import noelle.features.languages.common.enums.LanguageBackend;

import java.nio.file.Path;

public interface LanguagedPlugin {
    String directoryName();
    String includedResource();

    Language defaultLanguage();
    LanguageBackend languageFileBackend();

    Class<?> targetClass();
    Path rootDirectory();
}
