package noelle.features.languages.common.enums;

public enum LanguageBackend {
    YAML(".yaml"),
    HOCON(".conf"),
    JSON(".json");

    private final String extension;

    LanguageBackend(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}
