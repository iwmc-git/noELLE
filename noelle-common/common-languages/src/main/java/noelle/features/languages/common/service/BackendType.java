package noelle.features.languages.common.service;

public enum BackendType {
    YAML(".yaml"),
    HOCON(".conf");

    private final String format;

    BackendType(String format) {
        this.format = format;
    }

    public String format() {
        return format;
    }
}
