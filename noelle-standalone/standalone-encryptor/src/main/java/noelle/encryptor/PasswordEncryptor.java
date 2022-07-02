package noelle.encryptor;

import noelle.encryptor.algorithms.*;

public interface PasswordEncryptor {

    /**
     * Encode the raw password.
     *
     * @param rawPassword password for encryption.
     * @return encrypted password.
     */
    String encode(CharSequence rawPassword);

    /**
     * Verify the encoded password obtained from storage matches the submitted raw
     * password after it too is encoded.
     *
     * @param rawPassword the raw password to encode and match.
     * @param encodedPassword the encoded password from storage to compare with.
     *
     * @return true if the raw password, after encoding, matches the encoded password from storage.
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);

    /**
     * Preparing for encryption.
     *
     * @param algorithm encryption algorithm, see ${@link Algorithm}
     * @return selected encryption instance.
     */
    static PasswordEncryptor encryptionType(Algorithm algorithm) {
        return switch (algorithm) {
            case ARGON -> new ArgonPasswordEncryptor();
            case SCRYPT -> new SCryptPasswordEncryptor();
            case BCRYPT -> new BCryptPasswordEncryptor();
            case SHA512 -> new SHA512PasswordEncryptor();
            case SHA256 -> new SHA256PasswordEncryptor();
        };
    }

    /**
     * Preparing for encryption.
     *
     * @param algorithm encryption algorithm, see ${@link Algorithm}
     * @return selected encryption instance.
     */
    static PasswordEncryptor encryptionType(String algorithm) {
        return encryptionType(Algorithm.valueOf(algorithm));
    }

}
