package one.leftshift.common.util;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Provides common object convenience methods
 *
 * @author benjamin.krenn@leftshift.one - 10/12/18.
 * @since 1.0.0
 */
public final class ObjectUtil {

    public static void assertNotNull(String message, Object... objects) {
        if (hasNull(objects)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean hasNull(Object... objects) {
        if (Objects.isNull(objects)) {
            return true;
        }
        return Stream.of(objects).anyMatch(Objects::isNull);
    }

    public static Object firstNonNull(Object... objects) {
        if (Objects.isNull(objects)) {
            return null;
        }
        return Stream.of(objects)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}
