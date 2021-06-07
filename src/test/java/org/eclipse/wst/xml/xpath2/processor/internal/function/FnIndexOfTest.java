package org.eclipse.wst.xml.xpath2.processor.internal.function;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.ComparisonType;
import org.eclipse.wst.xml.xpath2.processor.internal.XPathExpressionTestBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FnIndexOfTest extends XPathExpressionTestBase {
    @ParameterizedTest(name = "Expression \"fn:index-of(''{0}'')\" returns \"{1}\".")
    @CsvSource(delimiterString = "|",
        value = {
        "(//P1[1],//P1[2]), 'x'|''",
        "(//P1[1],//P1[2]), //P3[1]|''",
        "(//P1[1],//P1[2]), //P1[2]|2",
        "( node-name(//P1[1]), node-name(//P3[1]) ), node-name(//P1[1])|1",
        "( node-name(//P1[1]), node-name(//P1[2]), node-name(//P3[1]) ), node-name(//P1[1])|1 2",
        "(), 1|''",
        "(1,2,3), 4|''",
        "(1,2,3), 2|2",
        "(1,2,3,2), 2|2 4",
    })
    void test(String parameters, String expectedResult) throws Exception {
        String resultString = executeXPathExpression(
            getClass().getResource("index-of_1.xml"),
            String.format("fn:index-of(%s)", parameters),
            ComparisonType.TEXT);

        assertThat(resultString).isEqualTo(expectedResult);
    }
}
