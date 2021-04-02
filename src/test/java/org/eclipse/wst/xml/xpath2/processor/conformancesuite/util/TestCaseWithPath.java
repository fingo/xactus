package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

public class TestCaseWithPath {
    private final TestCasePath testCasePath;
    private final TestCase testCase;

    public TestCaseWithPath(TestCasePath testCasePath,
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
