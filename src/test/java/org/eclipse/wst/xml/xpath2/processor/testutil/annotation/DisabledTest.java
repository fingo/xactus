package org.eclipse.wst.xml.xpath2.processor.testutil.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation provides information about why a JUnit 3 test is disabled.
 * NOTE THAT ADDING THIS ANNOTATION ALONE DOES NOT DISABLE THE TEST!
 * It's there for information purposes ONLY.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DisabledTest {
    String reason();
}
