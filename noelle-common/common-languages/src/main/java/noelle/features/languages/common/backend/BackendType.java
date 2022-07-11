package noelle.features.languages.common.backend;

public enum BackendType {
    YAML(".yaml"),
    HOCON(".conf"),
    JSON(".json");

    private final String extension;

    BackendType(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}
