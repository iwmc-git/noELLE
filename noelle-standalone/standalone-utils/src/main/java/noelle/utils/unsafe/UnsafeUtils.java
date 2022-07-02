package noelle.utils.unsafe;

public class UnsafeUtils {
    private static final sun.misc.Unsafe unsafe;

    static {
        try {
            var unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);

            unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns ${@link sun.misc.Unsafe} instance.
     */
    public static sun.misc.Unsafe unsafe() {
        return unsafe;
    }
}
