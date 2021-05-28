package info.fingo.xactus.processor.internal.function;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.ComparisonType;
import info.fingo.xactus.processor.testutil.legacytestsuiteadapter.PsychopathTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FnIndexOfTest {
    private PsychopathTestContext context;

    @BeforeEach
    final void beforeEach() throws Exception {
        context = new PsychopathTestContext();
        context.init();
    }

    @ParameterizedTest(name = "Expression \"{0}\" returns \"{1}\".")
    @CsvSource(delimiterString = "|",
        value = {
        "fn:index-of( (//P1[1],//P1[2]), 'x' )|''",
        "fn:index-of( (//P1[1],//P1[2]), //P3[1] )|''",
        "fn:index-of( (//P1[1],//P1[2]), //P1[2] )|2",
        "fn:index-of( ( node-name(//P1[1]), node-name(//P3[1]) ), node-name(//P1[1]) )|1",
        "fn:index-of( ( node-name(//P1[1]), node-name(//P1[2]), node-name(//P3[1]) ), node-name(//P1[1]) )|1 2",
        "fn:index-of( (), 1 )|''",
        "fn:index-of( (1,2,3), 4 )|''",
        "fn:index-of( (1,2,3), 2 )|2",
        "fn:index-of( (1,2,3,2), 2 )|2 4",
    })
    void test(String xPath, String expectedResult) throws Exception {
        URL fileURL = getClass().getResource("index-of_1.xml");

        context.loadInput(fileURL);

        context.setupContext(null);

        context.compile(xPath);
        ResultSequence rs = context.evaluate();

        String resultString = context.buildResult(rs, ComparisonType.TEXT);

        assertThat(resultString).isEqualTo(expectedResult);
    }
}
