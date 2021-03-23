package org.eclipse.wst.xml.xpath2.processor.testutil;

/**
 * This class mimics org.eclipse.wst.xml.xpath2.processor.testutil.Platform used
 * in the tests.
 */
public final class Platform {
    private Platform() {
    }

    public static Bundle getBundle(String bundleName) {
        return new Bundle(bundleName);
    }
}
