package org.eclipse.core.runtime;

import org.osgi.framework.Bundle;

public class Platform {
    public static Bundle getBundle(String bundleName) {
        return new Bundle(bundleName);
    }
}
