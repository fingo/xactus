package org.eclipse.wst.xml.xpath2.processor.conformancesuite.util;

import static java.util.stream.Collectors.toList;
import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.XMLUtil.getChildElements;
import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.XMLUtil.getMandatoryOnlyChildElement;

import java.util.stream.Collectors;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class TestSuiteParser {
    private TestSuiteParser() {
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
        return new TestCase(
            testCaseElement.getAttribute("name"),
            getMandatoryOnlyChildElement(testCaseElement, "description").getTextContent(),
            testCaseElement.getAttribute("scenario"),
            testCaseElement.getAttribute("FilePath"),
            getMandatoryOnlyChildElement(testCaseElement, "query")
                .getAttribute("name"),
            getChildElements(testCaseElement, "input-file")
                .stream()
                .map(Node::getTextContent)
                .collect(Collectors.toList()),
            getChildElements(testCaseElement, "output-file")
                .stream()
                .map(Node::getTextContent)
                .collect(toList()),
            getChildElements(testCaseElement, "expected-error")
                .stream()
                .map(Node::getTextContent)
                .collect(toList()));
    }

    private static String getTextContent(Element element) {
        return element.getTextContent();
    }
}
