package one.leftshift.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author benjamin.krenn@leftshift.one - 11/16/18.
 * @since 1.0.0
 */
public final class Maps {

    private Maps() {
        throw new UnsupportedOperationException("Can not instantiate");
    }

    public static <K,V> Map<K,V> single(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return Collections.unmodifiableMap(map);
    }
}
