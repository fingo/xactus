package org.osgi.framework;

import java.net.URL;
import java.text.MessageFormat;

public class Bundle {
    private final String bundleName;

    public Bundle(String bundleName) {
        this.bundleName = bundleName;
    }

    public URL getEntry(String file) {
        return Bundle.class.getResource(MessageFormat.format("/bundles/{0}{1}", bundleName, file));
    }
}
