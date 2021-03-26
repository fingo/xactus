package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;

public class TestSuite {
    private final Collection<TestGroup> testGroups;

    TestSuite(Collection<TestGroup> testGroups) {
        this.testGroups = testGroups.stream().collect(
            collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public Collection<TestGroup> getTestGroups() {
        return testGroups;
    }
}
