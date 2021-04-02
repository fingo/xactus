package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public final class TestSuiteUtil {
    private TestSuiteUtil() {
    }

    public static List<TestCaseWithPath> allTestCases(TestSuite testSuite) {
        return testSuite.getTestGroups().stream()
            .map(testGroup -> allTestCases(
                TestCasePath.empty(),
                testGroup))
            .flatMap(Collection::stream)
            .collect(toList());
    }

    private static List<TestCaseWithPath> allTestCases(TestCasePath testCasePath,
                                                       TestGroup testGroup) {
        TestCasePath currentTestCasePath = testCasePath.add(testGroup);

        return Stream.concat(
            testGroup.getTestCases().stream()
                .map(tc -> new TestCaseWithPath(currentTestCasePath, tc)),
            testGroup.getTestGroups().stream()
                .map(tg -> TestSuiteUtil.allTestCases(
                    currentTestCasePath,
                    tg))
                .flatMap(Collection::stream))
            .collect(toList());
    }
}
