package org.eclipse.wst.xml.xpath2.processor.conformancesuite.assertion;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.matcher.AssertionMatcher;
import org.custommonkey.xmlunit.XMLAssert;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.ComparisonType;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.OutputFile;
import org.hamcrest.Description;

class OutputFileMatcher extends AssertionMatcher<String> {
    private static String prepareXMLFragment(String xmlFragment) {
        return "<root>" + xmlFragment + "</root>";
    }

    private final OutputFile expected;
    private final ContentProvider contentProvider;

    public OutputFileMatcher(OutputFile expected,
                             ContentProvider contentProvider) {
        this.expected = expected;
        this.contentProvider = contentProvider;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("A string equal to the content of file \"%s\" using \"%s\" comparison type.",
            expected.getFile(),
            expected.getComparisonType()));
    }

    @Override
    public void assertion(String actual) throws AssertionError {
        try {
            String expectedOutput = contentProvider.getContent(
                expected.getFile());
            ComparisonType comparisonType = expected.getComparisonType();

            switch (comparisonType) {
                case XML:
                    XMLAssert.assertXMLEqual(actual, expectedOutput);
                    return;
                case FRAGMENT:
                    XMLAssert.assertXMLEqual(
                        prepareXMLFragment(actual),
                        prepareXMLFragment(expectedOutput));
                    return;
                case INSPECT:
                case TEXT:
                    assertThat(actual).isEqualToNormalizingNewlines(expectedOutput);
                    return;
            }

            throw new UnsupportedOperationException(
                "Comparison type \"" + comparisonType + "\" is unsupported.");
        } catch (Exception e) {
            throw new RuntimeException(
                "An unexpected processing error occurred.", e);
        }
    }
}
