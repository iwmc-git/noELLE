package noelle.database.credentials;

/**
 * Private implementation for a ${@link Credentials}.
 */
record CredentialsImpl(String host, String database, String username, String password) implements Credentials {

    @Override
    public String host() {
        return null;
    }

    @Override
    public String database() {
        return null;
    }

    @Override
    public String username() {
        return null;
    }

    @Override
    public String password() {
        return null;
    }
}
