package info.fingo.xactus.processor.conformancesuite.parser.testsources;

import static java.util.stream.Collectors.toList;
import static info.fingo.xactus.processor.testutil.XMLUtil.getChildElements;
import static info.fingo.xactus.processor.testutil.XMLUtil.getMandatoryOnlyChildElement;

import java.util.Objects;
import org.w3c.dom.Element;

public final class TestSourcesParser {
    private TestSourcesParser() {
    }

    public static TestSources parseTestSources(Element testsuiteElement) {
        Objects.requireNonNull(testsuiteElement);

        Element sourcesElement = getMandatoryOnlyChildElement(
            testsuiteElement, "sources");

        return new TestSources(
            getChildElements(sourcesElement, "source")
                .stream()
                .map(s -> new TestSource(
                    s.getAttribute("ID"),
                    s.getAttribute("FileName")))
                .collect(toList()));
    }
}
