package org.eclipse.wst.xml.xpath2.processor.conformancesuite;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.xml.xpath2.processor.DOMLoader;
import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.StaticError;
import org.eclipse.wst.xml.xpath2.processor.XercesLoader;
import org.eclipse.wst.xml.xpath2.processor.ast.XPath;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestCase;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestGroup;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSources;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuite;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuiteParser;
import org.eclipse.wst.xml.xpath2.processor.internal.types.AnyType;
import org.eclipse.wst.xml.xpath2.processor.test.AbstractPsychoPathTest;
import org.eclipse.wst.xml.xpath2.processor.testutil.Bundle;
import org.eclipse.wst.xml.xpath2.processor.testutil.Platform;
import org.eclipse.wst.xml.xpath2.processor.util.ResultSequenceUtil;
import org.junit.jupiter.api.Assertions;
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
        TestSources testSources =
            TestSuiteParser.parseTestSources(getTestsuiteElement());

        return allTestCases(testSuite).stream()
            .map(tc -> Arguments.of(
                tc.getName(),
                tc.getInputFiles().stream()
                    .map(testSources::getSourceFileName)
                    .collect(toList()),
                "/Queries/XQuery/" + tc.getFilePath() + tc.getXqFile() + ".xq",
                tc.getOutputFiles().stream()
                    .map(tc.getFilePath()::concat)
                    .map("/ExpectedTestResults/"::concat)
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
        try (InputStream is = bundle.getEntry(xqtsBundleEntry).openStream()) {
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

    @BeforeAll
    static void beforeAll() {
        bundle = getXQTSBundle();
    }

    @ParameterizedTest
    @MethodSource("testcases")
    void test(String tcName,
              List<String> inputFileNames,
              String xqFile,
              List<String> expectedOutputFiles,
              List<String> expectedErrors) throws Exception {
        List<Document> inputDocuments = getInputDocuments(inputFileNames);
        List<XSModel> inputDocumentSchemas = inputDocuments.stream()
            .map(Document::getDocumentElement)
            .map(ElementPSVI.class::cast)
            .map(ElementPSVI::getSchemaInformation)
            .filter(Objects::nonNull)
            .collect(toList());
        PsychopathTestContext psychopathTestContext = new PsychopathTestContext();
        psychopathTestContext.init();

        List<String> expectedOutputs = expectedOutputFiles.stream()
            .map(psychopathTestContext::getResultFileText)
            .collect(toList());

        if (inputDocumentSchemas.size() > 1) {
            throw new IllegalStateException();
        }

        XSModel schema = inputDocumentSchemas.stream().findFirst().orElse(null);

        if (inputDocuments.size() > 2) {
            throw new IllegalStateException();
        }

        if (inputDocuments.size() == 1) {
            psychopathTestContext.setDocument(inputDocuments.get(0));
        } else if (inputDocuments.size() == 2) {
            psychopathTestContext.setDocuments(
                inputDocuments.get(0),
                inputDocuments.get(1));
        }

        psychopathTestContext.setupContext(schema);
        String xPathExpression = psychopathTestContext.extractXPathExpression(xqFile, "whateva-unused");
        String actual = null;
        String error = null;

        try {
            psychopathTestContext.compile(xPathExpression);
            ResultSequence rs = psychopathTestContext.evaluate();

            actual = buildResult(rs);
        } catch (StaticError ex) {
            error = ex.code();
        } catch (DynamicError ex) {
            error = ex.code();
        }

        if (error != null) {
            Assertions.assertTrue(expectedErrors.contains(error));
            return;
        }

        Assertions.assertTrue(expectedOutputs.contains(actual));
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

    private static class PsychopathTestContext extends AbstractPsychoPathTest {
        public void init() throws Exception {
            setUp();
        }

        public void setDocument(Document document) {
            domDoc = document;
        }

        public void setDocuments(Document document1,
                                 Document document2) {
            domDoc = document1;
            domDoc2 = document2;
        }

        public void setupContext(XSModel schema) {
            setupDynamicContext(schema);
        }

        public XPath compile(String xPath) {
            return compileXPath(xPath);
        }

        public ResultSequence evaluate() {
            Object[] params = Stream.of(domDoc, domDoc2)
                .filter(Objects::nonNull)
                .toArray();

            return ResultSequenceUtil.newToOld(newXPath.evaluate(dynamicContextBuilder, params));
        }

        public String getResultFileText(String resultFile) {
            return getExpectedResult(resultFile);
        }
    }

    private static String buildResult(ResultSequence rs) {
        StringBuilder actual = new StringBuilder();
        Iterator<?> iterator = rs.iterator();

        while (iterator.hasNext()) {
            AnyType anyType = (AnyType) iterator.next();

            actual.append(anyType.getStringValue()).append(" ");
        }

        return actual.toString().trim();
    }
}
