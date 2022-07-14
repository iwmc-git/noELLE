package noelle.database.credentials;

/**
 * Private implementation for a ${@link Credentials}.
 */
record CredentialsImpl(String host, String database, String username, String password) implements Credentials {

    @Override
    public String host() {
        return host;
    }

    @Override
    public String database() {
        return database;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}
