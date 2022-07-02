package noelle.database.credentials;

/**
 * Default database credentials.
 */
public interface Credentials {

    /**
     * Returns the hostname of the database in the format `[ip]:[port]`
     */
    String host();

    /**
     * Returns a database name.
     */
    String database();

    /**
     * Returns the name of the user through
     * which the connection to the database will be made.
     */
    String username();

    /**
     * Returns a password for the connected user
     */
    String password();

    static Credentials credentialsOf(String host, String database, String username, String password) {
        return new CredentialsImpl(host, database, username, password);
    }
}
