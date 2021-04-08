package org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase;

/**
 * Identifies the comparator to use. The documentation for each enumerator is
 * copied from <a href="https://www.w3.org/XML/xquery/test-suite/Guidelines%20for%20Running%20the%20XML%20Query%20Test%20Suite.html">Guidelines for Running the XML Query Test Suite</a>.
 */
public enum ComparisonType {
    /**
     * The test harness must canonicalize both, the actual result and the
     * expected result according to the “Canonical XML” recommendation [2],
     * which refers to a number of open-source implementations. Byte-comparison
     * can then be applied to the resulting XML documents. If the test harness
     * does this process in a different manner, it must be documented.
     */
    XML,
    /**
     * For XML fragments, the same root node must be created for both,
     * implementation result and test suite result. The resulting XML can be
     * compared using XML comparison.
     */
    FRAGMENT,
    /**
     * Text is compared using byte-comparison.
     */
    TEXT,
    /**
     * A human is required to make the call about correctness of the result
     * according to the description in the test case.
     */
    INSPECT,
    /**
     * No comparison needs to be applied; the result is always true if the
     * implementation successfully executes the test case.
     */
    IGNORE
}
