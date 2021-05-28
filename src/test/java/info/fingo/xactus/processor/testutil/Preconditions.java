package info.fingo.xactus.processor.testutil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public final class Preconditions {
    private Preconditions() {
    }

    public static void requireNonEmptyString(String string) {
        Objects.requireNonNull(string);
        if (string.isEmpty()) {
            throw new IllegalArgumentException(
                "Expecting a non-empty string.");
        }
    }

    public static void requireEmptyString(String string) {
        Objects.requireNonNull(string);
        if (!string.isEmpty()) {
            throw new IllegalArgumentException(
                "Expecting an empty string, but got \"" + string + "\".");
        }
    }

    public static void requireNonEmptyCollection(Collection<?> collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(
                "Expecting a non-empty collection.");
        }
    }

    public static <T> void requireAtLeastOneNonEmptyCollection(Collection<?>... collections) {
        if (Arrays.stream(collections).allMatch(Collection::isEmpty)) {
            throw new IllegalArgumentException(
                "Expecting at least one non-empty collection.");
        }
    }
}
