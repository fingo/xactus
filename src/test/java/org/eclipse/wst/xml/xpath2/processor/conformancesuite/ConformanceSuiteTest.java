package org.eclipse.wst.xml.xpath2.processor.conformancesuite;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.xml.xpath2.processor.DOMLoader;
import org.eclipse.wst.xml.xpath2.processor.XercesLoader;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestCase;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestGroup;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSources;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuite;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuiteParser;
import org.eclipse.wst.xml.xpath2.processor.testutil.Bundle;
import org.eclipse.wst.xml.xpath2.processor.testutil.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

class ConformanceSuiteTest {
    static Stream<Arguments> testcases() throws Exception {
        Element testsuite = getTestsuiteElement();
        TestSuite testSuite = TestSuiteParser.parseTestSuite(testsuite);
        Bundle bundle = getXQTSBundle();
        TestSources testSources =
            TestSuiteParser.parseTestSources(getTestsuiteElement());

        return allTestCases(testSuite).stream()
            .map(tc -> Arguments.of(
                tc.getName(),
                tc.getInputFiles().stream()
                    .map(testSources::getSourceFileName)
                    .collect(toList()),
                "/Queries/XQuery/" + tc.getFilePath() + tc.getXqFile(),
                tc.getOutputFiles().stream()
                    .map(tc.getFilePath()::concat)
                    .map("/ExpectedTestResults/"::concat)
                    .map(o -> bundleToString(bundle, o))
                    .collect(toList()),
                tc.getExpectedErrors()));
    }

    private static Element getTestsuiteElement() throws IOException, ParserConfigurationException, SAXException {
        Bundle xqtsBundle = getXQTSBundle();
        Document doc;
        try (InputStream is = xqtsBundle.getEntry("/XQTSCatalog.xml").openStream()) {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        }

        Element testsuite = doc.getDocumentElement();
        testsuite.normalize();

        return testsuite;
    }

    private static List<TestCase> allTestCases(TestSuite testSuite) {
        return testSuite.getTestGroups().stream()
            .map(ConformanceSuiteTest::allTestCases)
            .flatMap(Collection::stream)
            .collect(toList());
    }

    private static List<TestCase> allTestCases(TestGroup testGroup) {
        return Stream.concat(
            testGroup.getTestCases().stream(),
            testGroup.getTestGroups().stream()
                .map(ConformanceSuiteTest::allTestCases)
                .flatMap(Collection::stream))
            .collect(toList());
    }

    private static Bundle getXQTSBundle() {
        return Platform.getBundle("org.w3c.xqts.testsuite");
    }

    private static String bundleToString(Bundle bundle,
                                  String xqtsBundleEntry) {
        try(InputStream is = bundle.getEntry(xqtsBundleEntry).openStream()) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static InputStream testResolve(URL url) throws IOException {
        if (url.getProtocol().equals("http")) {
            return ConformanceSuiteTest.class.getResourceAsStream(
                "/org/eclipse/wst/xml/xpath2/processor/test/" + url.getFile());
        } else {
            return url.openStream();
        }
    }

    private static Bundle bundle;
    private static TestSources testSources;

    @BeforeAll
    static void beforeAll() throws Exception {
        bundle = getXQTSBundle();
        testSources = TestSuiteParser.parseTestSources(getTestsuiteElement());
    }

    @ParameterizedTest
    @MethodSource("testcases")
    void test(String tcName,
              List<String> inputFileNames,
              String xqFile,
              List<String> expectedOutputs,
              List<String> expectedErrors) {
        DOMLoader domloader = new XercesLoader();
        domloader.set_validating(false);

        List<Document> inputDocuments = getInputDocuments(inputFileNames);
        List<XSModel> inputDocumentSchemas = inputDocuments.stream()
            .map(Document::getDocumentElement)
            .map(ElementPSVI.class::cast)
            .map(ElementPSVI::getSchemaInformation)
            .filter(Objects::nonNull)
            .collect(toList());

        System.out.println(inputDocumentSchemas);

//        setupDynamicContext(schema);
//
//        String xpath = extractXPathExpression(xqFile, inputFile);
//        String actual = null;
//        try {
//            compileXPath(xpath);
//            ResultSequence rs = evaluate(domDoc);
//
//
//            actual = buildResultString(rs);
//
//        } catch (XPathParserException ex) {
//            actual = ex.code();
//        } catch (StaticError ex) {
//            actual = ex.code();
//        } catch (DynamicError ex) {
//            actual = ex.code();
//        }
//
//        assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);
    }

    private List<Document> getInputDocuments(Collection<String> inputFileNames) {
        DOMLoader domloader = new XercesLoader();
        domloader.set_validating(false);

        return inputFileNames.stream()
            .map(bundle::getEntry)
            .map(url -> {
                try (InputStream is = testResolve(url)) {
                    Document doc = domloader.load(is);
                    doc.setDocumentURI(url.toString());
                    return doc;
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            })
            .collect(toList());
    }
}
