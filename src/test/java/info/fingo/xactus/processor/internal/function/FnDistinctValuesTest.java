package info.fingo.xactus.processor.internal.function;

import static org.assertj.core.api.Assertions.assertThat;

import info.fingo.xactus.processor.conformancesuite.parser.testcase.ComparisonType;
import info.fingo.xactus.processor.internal.XPathExpressionTestBase;
import java.net.URL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FnDistinctValuesTest extends XPathExpressionTestBase {
    private static URL contextFileURL;

    @BeforeAll
    static void setupDocument() {
        contextFileURL = FnIndexOfTest.class.getResource("distinct-values_1.xml");
    }

    @ParameterizedTest(name = "Expression \"{0}\" returns \"{1}\".")
    @CsvSource(delimiterString = "|",
        value = {
            "(//P1[1])|V01-left",
            "(//P1[1],//P1[1])|V01-left",
            "(//P1[1],//P1[2])|V01-left V01-right",
            "(node-name(//P1[1]), node-name(//P3[1]))|P1 P3",
            "(node-name(//P1[1]), node-name(//P1[2]))|P1",
        })
    void test(String args, String expectedResult) throws Exception {
        // given

        // when
        String resultString = executeXPathExpression(
            contextFileURL,
            String.format("fn:distinct-values(%s)", args),
            ComparisonType.TEXT);

        // then
        assertThat(resultString).isEqualTo(expectedResult);
    }
}