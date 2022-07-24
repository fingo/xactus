package info.fingo.xactus.processor.internal;

import info.fingo.xactus.processor.XPathParserException;
import info.fingo.xactus.processor.ast.XPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class InternalXPathParserTest {
    private InternalXPathParser sut;

    @BeforeEach
    final void setup() {
        sut = new InternalXPathParser();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "\uDE4A\uD83Daa",
            "ss\uDE4A\uD83Daa",
            "ss\uDE4A\uD83D",
            "\uDE4A\uD83D",

            "\uD83Daa",
            "ss\uD83Daa",
            "ss\uD83D",
            "\uD83D",

            "\uDE4Aaa",
            "ss\uDE4Aaa",
            "ss\uDE4A",
            "\uDE4A",
    })
    void throwsExceptionForInvalidSurrogatePair(String invalidSurrogateString) {
        Throwable thrownWhenXPathContainsInvalidSurrogatePair = catchThrowable(() -> sut.parse(invalidSurrogateString, false));

        assertThat(thrownWhenXPathContainsInvalidSurrogatePair)
                .isInstanceOf(XPathParserException.class)
                .hasMessageContaining("Invalid surrogate pair");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "\uD83D\uDE4Aaa",
            "ss\uD83D\uDE4Aaa",
            "ss\uD83D\uDE4A",
            "\uD83D\uDE4A",
    })
    void handlesSupplementaryCharacters(String stringContainingSupplementaryCharacters) {
        Throwable thrownWhenXPathContainsSupplementaryCharacters = catchThrowable(() -> sut.parse(stringContainingSupplementaryCharacters, false));

        assertThat(thrownWhenXPathContainsSupplementaryCharacters).isNull();
    }
}
