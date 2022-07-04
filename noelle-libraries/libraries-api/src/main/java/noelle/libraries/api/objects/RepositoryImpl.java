package noelle.libraries.api.objects;

final class RepositoryImpl implements Repository {
    private final String url;

    public RepositoryImpl(String url) {
        this.url = url;
    }

    @Override
    public String url() {
        return url;
    }
}
