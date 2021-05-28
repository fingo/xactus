package info.fingo.xactus.processor.conformancesuite.assertion;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import org.assertj.core.matcher.AssertionMatcher;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.testutil.legacytestsuiteadapter.PsychopathTestContext;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.ComparisonType;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.OutputFile;
import info.fingo.xactus.processor.testutil.XMLComparisonBuilder;
import org.hamcrest.Description;

class OutputFileMatcher extends AssertionMatcher<ResultSequence> {
    private final OutputFile expected;
    private final ContentProvider contentProvider;
    private final PsychopathTestContext context;

    public OutputFileMatcher(OutputFile expected,
                             ContentProvider contentProvider,
                             PsychopathTestContext context) {
        Objects.requireNonNull(expected);
        Objects.requireNonNull(contentProvider);
        Objects.requireNonNull(context);

        this.expected = expected;
        this.contentProvider = contentProvider;
        this.context = context;
    }

    @Override
    public void describeTo(Description description) {
        Objects.requireNonNull(description);

        description.appendText(String.format(
            "A string equal to the content of file \"%s\" " +
                "using \"%s\" comparison type.",
            expected.getFile(),
            expected.getComparisonType()));
    }

    @Override
    public void assertion(ResultSequence actual) throws AssertionError {
        Objects.requireNonNull(actual);

        try {
            String expectedOutput = contentProvider.getContent(
                expected.getFile());
            ComparisonType comparisonType = expected.getComparisonType();
            String processedActual = context.buildResult(actual, comparisonType);

            switch (comparisonType) {
                case XML:
                    XMLComparisonBuilder.xmlComparisonBuilder()
                        .withActual(processedActual)
                        .withExpected(expectedOutput)
                        .assertEqual();
                    return;
                case FRAGMENT:
                    XMLComparisonBuilder.xmlComparisonBuilder()
                        .withActual(processedActual)
                        .withExpected(expectedOutput)
                        .xmlFragmentComparison()
                        .assertEqual();
                    return;
                case INSPECT:
                case TEXT:
                    assertThat(processedActual).isEqualToNormalizingNewlines(expectedOutput);
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
