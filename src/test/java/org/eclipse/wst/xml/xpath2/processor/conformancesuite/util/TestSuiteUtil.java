package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public final class TestSuiteUtil {
    private TestSuiteUtil() {
    }

    public static List<TestCase> allTestCases(TestSuite testSuite) {
        return testSuite.getTestGroups().stream()
            .map(TestSuiteUtil::allTestCases)
            .flatMap(Collection::stream)
            .collect(toList());
    }

    private static List<TestCase> allTestCases(TestGroup testGroup) {
        return Stream.concat(
            testGroup.getTestCases().stream(),
            testGroup.getTestGroups().stream()
                .map(TestSuiteUtil::allTestCases)
                .flatMap(Collection::stream))
            .collect(toList());
    }
}
