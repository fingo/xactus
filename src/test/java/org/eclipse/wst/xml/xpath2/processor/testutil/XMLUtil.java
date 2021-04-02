package org.eclipse.wst.xml.xpath2.processor.testutil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class XMLUtil {
    private XMLUtil() {
    }


    public static Stream<Element> streamElements(NodeList nodeList) {
        return IntStream.range(0, nodeList.getLength())
            .mapToObj(nodeList::item)
            .filter(Element.class::isInstance)
            .map(Element.class::cast);
    }

    public static Optional<Element> getOnlyChildElement(Element element,
                                                        String elementName) {
        List<Element> childElements = getChildElements(element, elementName);

        if (childElements.size() > 1) {
            throw new RuntimeException(
                "Expected exactly one or no child " +
                    "\"" + elementName + "\" elements " +
                    "but got " +
                    "\"" + childElements.size() + "\"" +
                    ".");
        }

        return childElements.isEmpty() ?
            Optional.empty() :
            Optional.of(childElements.get(0));
    }

    public static Element getMandatoryOnlyChildElement(Element element,
                                                       String elementName) {
        return getOnlyChildElement(element, elementName)
            .orElseThrow(() -> new RuntimeException(
                "Expected exactly one " +
                    "\"" + elementName + "\" element " +
                    "but none were found."));
    }

    public static List<Element> getChildElements(Element element,
                                                 String elementName) {
        return streamElements(element.getChildNodes())
            .filter(el -> el.getTagName().equals(elementName))
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                Collections::unmodifiableList));
    }

    public static List<Element> getChildElements(Element element) {
        return streamElements(element.getChildNodes())
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                Collections::unmodifiableList));
    }
}
