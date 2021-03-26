package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;

final class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> Collection<T> unmodifiableCopy(Collection<T> collection) {
        return collection.stream().collect(
            collectingAndThen(toList(), Collections::unmodifiableList));
    }
}
