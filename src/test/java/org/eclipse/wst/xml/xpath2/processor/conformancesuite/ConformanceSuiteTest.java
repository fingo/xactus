package org.eclipse.wst.xml.xpath2.processor.conformancesuite;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.xml.xpath2.processor.DOMLoader;
import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.StaticError;
import org.eclipse.wst.xml.xpath2.processor.XercesLoader;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.InputFile;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.OutputFile;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.PsychopathTestContext;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSources;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuite;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuiteParser;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuiteUtil;
import org.eclipse.wst.xml.xpath2.processor.testutil.Bundle;
import org.eclipse.wst.xml.xpath2.processor.testutil.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

class ConformanceSuiteTest {
    static Stream<Arguments> testcases() {
        return TestSuiteUtil.allTestCases(testSuite).stream()
            .map(tc -> Arguments.of(
                tc.getName(),
                tc.getInputFiles(),
                tc.getXqFile(),
                tc.getOutputFiles(),
                tc.getExpectedErrors()));
    }

    private static Element getTestsuiteElement(Bundle xqtsBundle) throws IOException, ParserConfigurationException, SAXException {
        Document doc;
        try (InputStream is = xqtsBundle.getEntry("/XQTSCatalog.xml").openStream()) {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        }

        Element testsuite = doc.getDocumentElement();
        testsuite.normalize();

        return testsuite;
    }

    private static Bundle getXQTSBundle() {
        return Platform.getBundle("org.w3c.xqts.testsuite");
    }

    private static InputStream testResolve(URL url) throws IOException {
        if (url.getProtocol().equals("http")) {
            return ConformanceSuiteTest.class.getResourceAsStream(
                "/org/eclipse/wst/xml/xpath2/processor/test/" + url.getFile());
        } else {
            return url.openStream();
        }
    }

    private static TestSuite testSuite;
    private static Bundle bundle;
    private static TestSources testSources;

    @BeforeAll
    static void beforeAll() throws ParserConfigurationException, SAXException, IOException {
        bundle = getXQTSBundle();
        Element testSuiteElement = getTestsuiteElement(bundle);
        testSources = TestSuiteParser.parseTestSources(testSuiteElement);
        testSuite = TestSuiteParser.parseTestSuite(testSuiteElement);
    }

    private PsychopathTestContext psychopathTestContext;

    @BeforeEach
    void beforeEach() throws Exception {
        PsychopathTestContext psychopathTestContext = new PsychopathTestContext();
        psychopathTestContext.init();
        this.psychopathTestContext = psychopathTestContext;
    }

    @ParameterizedTest
    @MethodSource("testcases")
    void test(String tcName,
              List<InputFile> inputFiles,
              String xqFile,
              List<OutputFile> expectedOutputFiles,
              List<String> expectedErrors) throws Exception {
        List<String> expectedOutputs = expectedOutputFiles.stream()
            .map(OutputFile::getFile)
            .map(psychopathTestContext::getResultFileText)
            .collect(toList());

        psychopathTestContext.loadInput(inputFiles.stream()
            .map(InputFile::getFile)
            .map(testSources::getSourceFileName)
            .collect(toList()));

        XSModel schema = psychopathTestContext.loadGrammar();

        psychopathTestContext.setupContext(schema);
        String xPathExpression = psychopathTestContext.extractXPathExpression(xqFile, "whateva-unused");
        String actual = null;
        String error = null;

        try {
            psychopathTestContext.compile(xPathExpression);
            ResultSequence rs = psychopathTestContext.evaluate();

            actual = psychopathTestContext.buildResult(rs);
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
}
