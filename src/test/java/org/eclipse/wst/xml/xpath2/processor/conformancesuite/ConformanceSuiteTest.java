package org.eclipse.wst.xml.xpath2.processor.conformancesuite;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.eclipse.wst.xml.xpath2.processor.conformancesuite.assertion.ConformanceSuiteMatchers.matchesAnyOfExpected;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.xml.xpath2.api.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.StaticError;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.assertion.ContentProvider;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.flattener.TestCaseHierarchyFlattener;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.legacytestsuiteadapter.PsychopathTestContext;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.Input;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.OutputFile;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestCase;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestCaseParser;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.TestCaseRoot;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testsources.TestSources;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testsources.TestSourcesParser;
import org.eclipse.wst.xml.xpath2.processor.testutil.bundle.Bundle;
import org.eclipse.wst.xml.xpath2.processor.testutil.bundle.Platform;
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
        return TestCaseHierarchyFlattener.flatten(testCaseRoot).stream()
            .filter(TestCaseFilter::isTestcaseEnabled)
            .map(testCaseWithGroup -> {
                TestCase tc = testCaseWithGroup.getTestCase();

                return Arguments.of(
                    testCaseWithGroup.getTestCasePath().pathString(),
                    tc.getInputFiles(),
                    tc.getXqFile(),
                    tc.getOutputFiles(),
                    tc.getExpectedErrors());
            });
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

    private static ContentProvider contentProvider(Bundle bundle) {
        Objects.requireNonNull(bundle);

        return entryId -> {
            Path bundlePath = Paths.get(bundle.getEntry(entryId).toURI());
            List<String> lines = Files.readAllLines(bundlePath);
            return String.join("\n", lines);
        };
    }

    private static TestCaseRoot testCaseRoot;
    private static TestSources testSources;
    private static ContentProvider contentProvider;

    @BeforeAll
    static void beforeAll() throws ParserConfigurationException, SAXException, IOException {
        Bundle bundle = getXQTSBundle();
        Element testSuiteElement = getTestsuiteElement(bundle);
        testSources = TestSourcesParser.parseTestSources(testSuiteElement);
        testCaseRoot = TestCaseParser.parseTestCases(testSuiteElement);
        contentProvider = contentProvider(bundle);
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
    @SuppressWarnings("unused")
    void test(String testCaseId,
              List<Input> inputs,
              String xqFile,
              List<OutputFile> expectedOutputFiles,
              List<String> expectedErrors) throws Exception {
        psychopathTestContext.loadInput(inputs.stream()
            .map(Input::getName)
            .map(testSources::getSourceFileName)
            .collect(toList()));

        XSModel schema = psychopathTestContext.loadGrammar();

        psychopathTestContext.setupContext(schema);
        String xPathExpression = psychopathTestContext.extractXPathExpression(xqFile, "whateva-unused");
        ResultSequence actual = null;
        String error = null;

        try {
            psychopathTestContext.compile(xPathExpression);
            actual = psychopathTestContext.evaluate();
        } catch (StaticError ex) {
            error = ex.code();
        } catch (DynamicError ex) {
            error = ex.code();
        }

        if (error != null) {
            if (expectedErrors.isEmpty()) {
                fail("An error occurred " +
                    "although no error was expected: " +
                    "\"" + error + "\".");
                return;
            }

            assertThat(error).isIn(expectedErrors);
            return;
        }

        assertThat(actual)
            .is(matchesAnyOfExpected(expectedOutputFiles, contentProvider, psychopathTestContext));
    }
}
