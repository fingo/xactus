package info.fingo.xactus.processor.testutil;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matcher;
import org.xmlunit.matchers.CompareMatcher;

public class XMLComparisonBuilder {
    public static XMLComparisonBuilder xmlComparisonBuilder() {
        return new XMLComparisonBuilder();
    }

    private static Matcher<? super String> isXMLSimilarTo(String expected) {
        return CompareMatcher.isSimilarTo(expected).ignoreWhitespace();
    }

    private static String prepareXMLFragmentForComparison(String xmlFragment) {
        return "<root>" + xmlFragment + "</root>";
    }

    private String message;
    private String actual;
    private String expected;
    private boolean isXmlFragmentComparison;

    private XMLComparisonBuilder() {
    }

    public XMLComparisonBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public XMLComparisonBuilder withActual(String actual) {
        this.actual = actual;
        return this;
    }

    public XMLComparisonBuilder withExpected(String expected) {
        this.expected = expected;
        return this;
    }

    public XMLComparisonBuilder xmlFragmentComparison() {
        this.isXmlFragmentComparison = true;
        return this;
    }

    public void assertEqual() {
        if (actual == null || expected == null) {
            throw new IllegalStateException(
                "Both 'actual' and 'expected' needs to be set " +
                    "to perform comparison.");
        }

        String actualForComparison = isXmlFragmentComparison ?
            prepareXMLFragmentForComparison(actual) :
            actual;
        String expectedForComparison = isXmlFragmentComparison ?
            prepareXMLFragmentForComparison(expected) :
            expected;

        if (message != null) {
            assertThat(
                message,
                actualForComparison,
                isXMLSimilarTo(expectedForComparison));
            return;
        }

        assertThat(
            actualForComparison,
            isXMLSimilarTo(expectedForComparison));
    }
}
