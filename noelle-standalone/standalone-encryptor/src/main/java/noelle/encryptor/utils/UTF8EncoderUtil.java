package noelle.encryptor.utils;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * UTF-8 Charset encoder/decoder.
 * <p>
 * For internal use only.
 *
 * @author Luke Taylor
 */
public final class UTF8EncoderUtil {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private UTF8EncoderUtil() {
    }

    /**
     * Get the bytes of the String in UTF-8 encoded form.
     */
    public static byte[] encode(CharSequence string) {
        try {
            ByteBuffer bytes = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
            byte[] bytesCopy = new byte[bytes.limit()];
            System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());
            return bytesCopy;
        }
        catch (CharacterCodingException ex) {
            throw new IllegalArgumentException("Encoding failed", ex);
        }
    }

    /**
     * Decode the bytes in UTF-8 form into a String.
     */
    public static String decode(byte[] bytes) {
        try {
            return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        }
        catch (CharacterCodingException ex) {
            throw new IllegalArgumentException("Decoding failed", ex);
        }
    }

}
