package info.fingo.xactus.processor.conformancesuite.parser.testcase;

import static java.util.stream.Collectors.toList;
import static info.fingo.xactus.processor.testutil.XMLUtil.getChildElements;
import static info.fingo.xactus.processor.testutil.XMLUtil.getMandatoryOnlyChildElement;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import info.fingo.xactus.processor.testutil.CollectionUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class TestCaseParser {
    private TestCaseParser() {
    }

    private static final Map<String, ComparisonType> STRING_TO_COMPARISON_TYPE =
        CollectionUtil.<String, ComparisonType>unmodifiableMapBuilder()
            .withEntry("XML", ComparisonType.TEXT)
            .withEntry("Fragment", ComparisonType.FRAGMENT)
            .withEntry("Inspect", ComparisonType.INSPECT)
            .withEntry("Ignore", ComparisonType.IGNORE)
            .withEntry("Text", ComparisonType.TEXT)
            .build();
    private static final Map<String, InputType> TAG_TO_INPUT_TYPE =
        CollectionUtil.<String, InputType>unmodifiableMapBuilder()
            .withEntry("input-file", InputType.FILE)
            .withEntry("input-URI", InputType.URI)
            .withEntry("contextItem", InputType.CONTEXT_ITEM)
            .build();

    public static TestCaseRoot parseTestCases(Element testSuiteElement) {
        Objects.requireNonNull(testSuiteElement);

        return new TestCaseRoot(
            getChildElements(testSuiteElement, "test-group")
                .stream()
                .map(TestCaseParser::parseTestGroup)
                .filter(t -> !t.isEmpty())
                .collect(toList()));
    }

    private static TestGroup parseTestGroup(Element testGroupElement) {
        Objects.requireNonNull(testGroupElement);

        Element groupInfo = getMandatoryOnlyChildElement(testGroupElement, "GroupInfo");

        return new TestGroup(
            testGroupElement.getAttribute("name"),
            getMandatoryOnlyChildElement(groupInfo, "title").getTextContent(),
            getMandatoryOnlyChildElement(groupInfo, "description").getTextContent(),
            getChildElements(testGroupElement, "test-group")
                .stream()
                .map(TestCaseParser::parseTestGroup)
                .filter(t -> !t.isEmpty())
                .collect(toList()),
            getChildElements(testGroupElement, "test-case")
                .stream()
                .filter(t -> t.getAttribute("is-XPath2").equals("true"))
                .map(TestCaseParser::parseTestCase)
                .collect(toList()));
    }

    private static TestCase parseTestCase(Element testCaseElement) {
        Objects.requireNonNull(testCaseElement);

        String filePath = testCaseElement.getAttribute("FilePath");
        String query = getMandatoryOnlyChildElement(testCaseElement, "query")
            .getAttribute("name");

        return new TestCase(
            testCaseElement.getAttribute("name"),
            getMandatoryOnlyChildElement(testCaseElement, "description").getTextContent(),
            testCaseElement.getAttribute("scenario"),
            "/Queries/XQuery/" + filePath + query + ".xq",
            Stream.of(
                getChildElements(testCaseElement, "input-file").stream(),
                getChildElements(testCaseElement, "input-URI").stream(),
                getChildElements(testCaseElement, "contextItem").stream())
                .flatMap(s -> s)
                .map(TestCaseParser::parseInputFile)
                .collect(Collectors.toList()),
            getChildElements(testCaseElement, "output-file")
                .stream()
                .map(outputFileElement -> parseOutputFile(filePath, outputFileElement))
                .collect(toList()),
            getChildElements(testCaseElement, "expected-error")
                .stream()
                .map(Node::getTextContent)
                .collect(toList()));
    }

    private static Input parseInputFile(Element inputFileElement) {
        Objects.requireNonNull(inputFileElement);

        return new Input(
            inputFileElement.getTextContent(),
            inputFileElement.getAttribute("variable"),
            getInputFileType(inputFileElement.getTagName()));
    }

    private static OutputFile parseOutputFile(String filePath,
                                              Element outputFileElement) {
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(outputFileElement);

        String fileName = outputFileElement.getTextContent();

        return new OutputFile(
            "/ExpectedTestResults/" + filePath + fileName,
            parseComparisonType(outputFileElement.getAttribute("compare")));
    }

    private static ComparisonType parseComparisonType(String comparisonType) {
        Objects.requireNonNull(comparisonType);

        if (!STRING_TO_COMPARISON_TYPE.containsKey(comparisonType)) {
            throw new IllegalArgumentException(
                "Unknown comparison type: \"" + comparisonType + "\"");
        }

        return STRING_TO_COMPARISON_TYPE.get(comparisonType);
    }

    private static InputType getInputFileType(String elementName) {
        Objects.requireNonNull(elementName);

        if (!TAG_TO_INPUT_TYPE.containsKey(elementName)) {
            throw new IllegalArgumentException(
                "Unknown input element name: \"" + elementName + "\"");
        }

        return TAG_TO_INPUT_TYPE.get(elementName);
    }
}
