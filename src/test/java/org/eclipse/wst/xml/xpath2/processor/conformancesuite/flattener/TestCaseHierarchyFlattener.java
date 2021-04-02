package org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestGroup;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestCaseRoot;

public final class TestCaseHierarchyFlattener {
    private TestCaseHierarchyFlattener() {
    }

    public static List<TestCaseWithPath> flatten(TestCaseRoot testCaseRoot) {
        return testCaseRoot.getTestGroups().stream()
            .map(testGroup -> flatten(
                TestCasePath.empty(),
                testGroup))
            .flatMap(Collection::stream)
            .collect(toList());
    }

    private static List<TestCaseWithPath> flatten(TestCasePath testCasePath,
                                                  TestGroup testGroup) {
        TestCasePath currentTestCasePath = testCasePath.add(testGroup);

        return Stream.concat(
            testGroup.getTestCases().stream()
                .map(tc -> new TestCaseWithPath(currentTestCasePath, tc)),
            testGroup.getTestGroups().stream()
                .map(tg -> TestCaseHierarchyFlattener.flatten(
                    currentTestCasePath,
                    tg))
                .flatMap(Collection::stream))
            .collect(toList());
    }
}
