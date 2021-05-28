package info.fingo.xactus.processor.testutil.bundle;

import java.util.Objects;

/**
 * This class mimics info.fingo.xactus.processor.testutil.Platform used
 * in the tests.
 */
public final class Platform {
    private Platform() {
    }

    public static Bundle getBundle(String bundleName) {
        Objects.requireNonNull(bundleName);

        return new Bundle(bundleName);
    }
}
