package one.leftshift.common.functional;

import one.leftshift.common.util.ObjectUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author benjamin.krenn@leftshift.one - 10/25/18.
 * @since 1.0.0
 */
public final class Functions {

    private Functions() { }

    public static <T1, T2> T2 foldLeft(List<T1> list, T2 identity, Function<T2, Function<T1, T2>> function) {
        T2 result = identity;
        for (T1 t1 : list) {
            result = function.apply(result).apply(t1);
        }

        return result;
    }

    public static <T> T head(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("list can not be empty");
        }
        return list.get(0);
    }

    public static <T> List<T> tail(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("list can not be empty");
        }
        List<T> cloned = new ArrayList<>(list);
        cloned.remove(0);
        return Collections.unmodifiableList(cloned);
    }

    public static <T> List<T> copy(List<T> toCopy) {
        return Collections.unmodifiableList(new ArrayList<T>(toCopy));
    }

    public static <T> List<T> copy(Collection<T> toCopy) {
        return Collections.unmodifiableList(new ArrayList<>(toCopy));
    }

    public static <K, T> Map<K, T> copy(Map<K, T> toCopy) {
        return Collections.unmodifiableMap(new HashMap<>(toCopy));
    }

    public static <T> Collection<List<T>> partition(List<T> list, int partitionSize) {
        if (partitionSize <= 0) {
            return Collections.singletonList(list);
        }
        AtomicInteger counter = new AtomicInteger(0);
        return list.stream().collect(Collectors.groupingBy(obj -> counter.getAndIncrement() / partitionSize)).values();
    }

    @SafeVarargs
    public static <T> List<T> list(T... items) {
        Objects.requireNonNull(items);
        if (ObjectUtil.hasNull(items)) {
            throw new IllegalArgumentException("null values are not allowed");
        }
        return Arrays.asList(items);
    }

    public static <T> Function<T, T> collapse(List<Function<T,T>> functions) {
        return functions.stream().reduce(Function::andThen).orElseThrow(() -> new IllegalArgumentException("can not reduce null values"));
    }
}
