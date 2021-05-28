package info.fingo.xactus.processor.testutil;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> List<T> unmodifiableCopy(Collection<T> collection) {
        if (collection.isEmpty()) {
            return emptyList();
        }

        return collection.stream().collect(
            collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public static <K, V> UnmodifiableMapBuilder<K, V> unmodifiableMapBuilder() {
        return new UnmodifiableMapBuilder<>();
    }

    public static class UnmodifiableMapBuilder<K, V> {
        private final Map<K, V> map = new HashMap<>();

        private UnmodifiableMapBuilder() {
        }

        public UnmodifiableMapBuilder<K, V> withEntry(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            if (map.isEmpty()) {
                return emptyMap();
            }

            return unmodifiableMap(new HashMap<>(map));
        }
    }
}
