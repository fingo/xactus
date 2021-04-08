package org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener;

import java.util.Objects;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestCase;

public class TestCaseWithPath {
    private final TestCasePath testCasePath;
    private final TestCase testCase;

    TestCaseWithPath(TestCasePath testGroupPath,
                     TestCase testCase) {
        Objects.requireNonNull(testGroupPath);
        Objects.requireNonNull(testCase);

        this.testCasePath = testGroupPath.append(testCase.getName());
        this.testCase = testCase;
    }

    public TestCasePath getTestCasePath() {
        return testCasePath;
    }

    public TestCase getTestCase() {
        return testCase;
    }
}
