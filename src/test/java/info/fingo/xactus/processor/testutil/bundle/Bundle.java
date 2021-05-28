package info.fingo.xactus.processor.testutil.bundle;

import java.net.URL;
import java.text.MessageFormat;
import java.util.NoSuchElementException;
import java.util.Objects;
import info.fingo.xactus.processor.testutil.Preconditions;

/**
 * This class mimics org.osgi.framework.Bundle class used in the tests.
 */
public class Bundle {
    private final String bundleName;

    Bundle(String bundleName) {
        Preconditions.requireNonEmptyString(bundleName);

        this.bundleName = bundleName;
    }

    public URL getEntry(String entryName) {
        Objects.requireNonNull(entryName);

        URL resourceURL = Bundle.class.getResource(
            MessageFormat.format(
                "/bundles/{0}{1}",
                bundleName,
                entryName));

        if (resourceURL == null) {
            throw new NoSuchElementException(
                "Entry \"" + entryName + "\" " +
                    "not found in bundle \"" + bundleName + "\".");
        }

        return resourceURL;
    }
}
