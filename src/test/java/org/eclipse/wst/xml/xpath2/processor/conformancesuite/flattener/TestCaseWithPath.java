package org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener;

import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestCase;

public class TestCaseWithPath {
    private final TestCasePath testCasePath;
    private final TestCase testCase;

    TestCaseWithPath(TestCasePath testCasePath,
                     TestCase testCase) {
        this.testCasePath = testCasePath;
        this.testCase = testCase;
    }

    public TestCasePath getTestCasePath() {
        return testCasePath;
    }

    public TestCase getTestCase() {
        return testCase;
    }
}
