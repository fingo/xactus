package org.eclipse.wst.xml.xpath2.processor.testutil;

import java.net.URL;
import java.text.MessageFormat;

/**
 * This class mimics org.osgi.framework.Bundle class used in the tests.
 */
public class Bundle {
    private final String bundleName;

    public Bundle(String bundleName) {
        this.bundleName = bundleName;
    }

    public URL getEntry(String file) {
        return Bundle.class.getResource(MessageFormat.format("/bundles/{0}{1}", bundleName, file));
    }
}
