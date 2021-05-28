package info.fingo.xactus.processor.internal.function;

import static org.assertj.core.api.Assertions.assertThat;

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.internal.XPathExpressionTestBase;
import info.fingo.xactus.processor.internal.types.XSDateTime;
import info.fingo.xactus.processor.testutil.bundle.Platform;
import java.net.URL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FnDateTimeTest extends XPathExpressionTestBase {
    private static URL emptyDoc;

    @BeforeAll
    static void setupDocument() {
        emptyDoc = Platform.getBundle("org.w3c.xqts.testsuite")
            .getEntry("/TestSources/emptydoc.xml");
    }

    @ParameterizedTest(name = "Expression \"fn:dateTime(xs:date(''{0}''),xs:time(''{1}''))\" returns xs:dateTime with \"{1}\" string representation.")
    @CsvSource(delimiter = '|', value = {
        "2008-02-29|00:00:00|2008-02-29T00:00:00",
        "-20000-04-01|00:00:00|-20000-04-01T00:00:00",
        "-0001-04-01|00:00:00|-0001-04-01T00:00:00",
        "2001-10-26|21:32:52|2001-10-26T21:32:52",
        "2001-10-26+02:00|21:32:52+02:00|2001-10-26T21:32:52+02:00",
        "2001-10-26|19:32:52Z|2001-10-26T19:32:52Z",
        "2001-10-26|19:32:52+00:00|2001-10-26T19:32:52Z",
        "-2001-10-26|21:32:52|-2001-10-26T21:32:52",
        "2001-10-26|21:32:52.12679|2001-10-26T21:32:52",
    })
    void testCorrectStrings(String date,
                            String time,
                            String expectedXSDateTimeStringValue) throws Exception {
        // given

        // when
        ResultSequence result = executeXPathExpression(
            emptyDoc,
            String.format("fn:dateTime(xs:date('%s'),xs:time('%s'))", date, time));

        // then
        assertThat(result)
            .satisfies(r -> assertThat(r.size()).isOne())
            .isInstanceOfSatisfying(
                XSDateTime.class,
                xsDateTime -> assertThat(xsDateTime.getStringValue())
                    .isEqualTo(expectedXSDateTimeStringValue));
    }
}