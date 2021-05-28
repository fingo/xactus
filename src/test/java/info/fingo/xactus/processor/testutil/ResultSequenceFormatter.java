package info.fingo.xactus.processor.testutil;

import static info.fingo.xactus.processor.util.ResultSequenceUtil.newToOld;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import info.fingo.xactus.processor.ResultSequence;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.NodeType;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public final class ResultSequenceFormatter {
    private ResultSequenceFormatter() {
    }

    private static final DOMImplementationLS DOM_IMPLEMENTATION_LS = getDOMImplementationLS();

    public static String fullDebugString(info.fingo.xactus.api.ResultSequence rs) {
        return fullDebugString(newToOld(rs));
    }

    @SuppressWarnings("deprecation")
    public static String fullDebugString(ResultSequence rs) {
        try {
            return "{{ ResultSequence:\n" +
                "string() -->\n" +
                rs.string() + "\n" +
                "XML -->\n" +
                buildXMLResultString(rs) + "\n" +
                "\n" +
                "TEXT -->\n" +
                buildResultString(rs) + "\n" +
                "}}";
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred.", e);
        }
    }

    public static String buildResultString(info.fingo.xactus.api.ResultSequence rs) {
        return buildResultString(newToOld(rs));
    }

    @SuppressWarnings("deprecation")
    public static String buildResultString(ResultSequence rs) {
        StringBuilder actual = new StringBuilder();
        Iterator<?> iterator = rs.iterator();

        while (iterator.hasNext()) {
            AnyType anyType = (AnyType) iterator.next();
            actual.append(anyType.getStringValue()).append(" ");
        }

        return actual.toString().trim();
    }

    public static String buildXMLResultString(info.fingo.xactus.api.ResultSequence rs) throws Exception {
        return buildXMLResultString(newToOld(rs));
    }

    @SuppressWarnings("deprecation")
    public static String buildXMLResultString(ResultSequence rs) throws Exception {
        LSOutput outputText = DOM_IMPLEMENTATION_LS.createLSOutput();
        LSSerializer serializer = DOM_IMPLEMENTATION_LS.createLSSerializer();
        serializer.getDomConfig().setParameter("xml-declaration", false);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputText.setByteStream(outputStream);

            Iterator<?> iterator = rs.iterator();
            boolean queueSpace = false;
            while (iterator.hasNext()) {
                AnyType aat = (AnyType) iterator.next();
                if (aat instanceof NodeType) {
                    NodeType nodeType = (NodeType) aat;
                    Node node = nodeType.node_value();
                    if (!serializer.write(node, outputText)) {
                        throw new RuntimeException(
                            "Failed to serialize an element of ResultSequence to XML: \"" + node + "\"");
                    }
                    queueSpace = false;
                } else {
                    if (queueSpace) outputText.getByteStream().write(32);
                    outputText.getByteStream().write(aat.getStringValue().getBytes(StandardCharsets.UTF_8));
                    queueSpace = true;
                }
            }

            return outputStream.toString("UTF-8").trim();
        }
    }

    private static DOMImplementationLS getDOMImplementationLS() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return (DOMImplementationLS) documentBuilder.getDOMImplementation().getFeature("LS", "3.0");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unexpected exception occurred.", e);
        }
    }
}
