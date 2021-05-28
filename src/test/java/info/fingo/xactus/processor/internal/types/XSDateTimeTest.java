package info.fingo.xactus.processor.internal.types;

import static org.assertj.core.api.Assertions.assertThat;

import info.fingo.xactus.processor.conformancesuite.parser.testcase.ComparisonType;
import info.fingo.xactus.processor.internal.XPathExpressionTestBase;
import info.fingo.xactus.processor.testutil.bundle.Platform;
import java.net.URL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class XSDateTimeTest extends XPathExpressionTestBase {
    private static URL emptyDoc;

    @BeforeAll
    static void setupDocument() {
        emptyDoc = Platform.getBundle("org.w3c.xqts.testsuite")
            .getEntry("/TestSources/emptydoc.xml");
    }

    @ParameterizedTest(name = "\"{0}\" is parsed correctly as xs:dateTime (with \"{1}\" string representation).")
    @CsvSource(delimiter = '|', value = {
        "-20000-04-01T00:00:00|-20000-04-01T00:00:00",
        "-0001-04-01T00:00:00|-0001-04-01T00:00:00",
        "2001-10-26T21:32:52|2001-10-26T21:32:52",
        "2001-10-26T21:32:52+02:00|2001-10-26T21:32:52+02:00",
        "2001-10-26T19:32:52Z|2001-10-26T19:32:52Z",
        "2001-10-26T19:32:52+00:00|2001-10-26T19:32:52Z",
        "-2001-10-26T21:32:52|-2001-10-26T21:32:52",
        "2001-10-26T21:32:52.12679|2001-10-26T21:32:52.126",
    })
    void testCorrectStrings(String correctXSDateTimeValue, String expectedStringValue) throws Exception {
        // given

        // when
        String resultString = executeXPathExpression(
            emptyDoc,
            String.format("xs:dateTime('%s')", correctXSDateTimeValue),
            ComparisonType.TEXT);

        // then
        assertThat(resultString).isEqualTo(expectedStringValue);
    }
}