package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;
import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.XMLUtil.getChildElements;
import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.XMLUtil.getMandatoryOnlyChildElement;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class TestSuiteParser {
    private TestSuiteParser() {
    }

    private static final Map<String, ComparisonType> STRING_TO_COMPARISON_TYPE;

    static {
        Map<String, ComparisonType> stringToComparisonType = new HashMap<>();
        stringToComparisonType.put("XML", ComparisonType.TEXT);
        stringToComparisonType.put("Fragment", ComparisonType.FRAGMENT);
        stringToComparisonType.put("Inspect", ComparisonType.INSPECT);
        stringToComparisonType.put("Ignore", ComparisonType.IGNORE);
        stringToComparisonType.put("Text", ComparisonType.TEXT);
        STRING_TO_COMPARISON_TYPE = unmodifiableMap(stringToComparisonType);
    }

    public static TestSuite parseTestSuite(Element testSuiteElement) {
        return new TestSuite(
            getChildElements(testSuiteElement, "test-group")
                .stream()
                .map(TestSuiteParser::parseTestGroup)
                .filter(t -> !t.isEmpty())
                .collect(toList()));
    }

    public static TestSources parseTestSources(Element testsuiteElement) {
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

    private static TestGroup parseTestGroup(Element testGroupElement) {
        Element groupInfo = getMandatoryOnlyChildElement(testGroupElement, "GroupInfo");

        return new TestGroup(
            testGroupElement.getAttribute("name"),
            getMandatoryOnlyChildElement(groupInfo, "title").getTextContent(),
            getMandatoryOnlyChildElement(groupInfo, "description").getTextContent(),
            getChildElements(testGroupElement, "test-group")
                .stream()
                .map(TestSuiteParser::parseTestGroup)
                .filter(t -> !t.isEmpty())
                .collect(toList()),
            getChildElements(testGroupElement, "test-case")
                .stream()
                .filter(t -> t.getAttribute("is-XPath2").equals("true"))
                .map(TestSuiteParser::parseTestCase)
                .collect(toList()));
    }

    private static TestCase parseTestCase(Element testCaseElement) {
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
                .map(TestSuiteParser::parseInputFile)
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

    private static InputFile parseInputFile(Element inputFileElement) {
        return new InputFile(
            inputFileElement.getTextContent(),
            inputFileElement.getAttribute("variable"));
    }

    private static OutputFile parseOutputFile(String filePath,
                                              Element outputFileElement) {
        String fileName = outputFileElement.getTextContent();

        return new OutputFile(
            "/ExpectedTestResults/" + filePath + fileName,
            parseComparisonType(outputFileElement.getAttribute("compare")));
    }

    private static ComparisonType parseComparisonType(String comparisonType) {
        if (!STRING_TO_COMPARISON_TYPE.containsKey(comparisonType)) {
            throw new IllegalArgumentException(
                "Unknown comparison type: \"" + comparisonType + "\"");
        }

        return STRING_TO_COMPARISON_TYPE.get(comparisonType);
    }
}
