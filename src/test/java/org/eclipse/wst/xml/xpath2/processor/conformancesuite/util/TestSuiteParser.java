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
                .collect(toList()));
    }

    private static TestGroup parseTestGroup(Element testGroupElement) {
        Element groupInfo = getMandatoryOnlyChildElement(testGroupElement, "GroupInfo");

        return new TestGroup(
            testGroupElement.getAttribute("name"),
            getTextContent(getMandatoryOnlyChildElement(groupInfo, "title")),
            getTextContent(getMandatoryOnlyChildElement(groupInfo, "description")),
            getChildElements(testGroupElement, "test-group")
                .stream()
                .map(TestSuiteParser::parseTestGroup)
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
            getTextContent(getMandatoryOnlyChildElement(testCaseElement, "description")),
            testCaseElement.getAttribute("scenario"),
            testCaseElement.getAttribute("FilePath"),
            getMandatoryOnlyChildElement(testCaseElement, "query")
                .getAttribute("name"), getChildElements(testCaseElement, "input-file")
                .stream()
                .map(TestSuiteParser::getTextContent)
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
