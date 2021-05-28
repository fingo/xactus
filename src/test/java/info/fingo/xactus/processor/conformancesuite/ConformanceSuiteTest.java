package info.fingo.xactus.processor.conformancesuite;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static info.fingo.xactus.processor.conformancesuite.assertion.ConformanceSuiteMatchers.matchesAnyOfExpected;

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
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.StaticError;
import info.fingo.xactus.processor.conformancesuite.assertion.ContentProvider;
import info.fingo.xactus.processor.conformancesuite.flattener.TestCaseHierarchyFlattener;
import info.fingo.xactus.processor.testutil.legacytestsuiteadapter.PsychopathTestContext;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.Input;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.OutputFile;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.TestCase;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.TestCaseParser;
import info.fingo.xactus.processor.conformancesuite.parser.testcase.TestCaseRoot;
import info.fingo.xactus.processor.conformancesuite.parser.testsources.TestSources;
import info.fingo.xactus.processor.conformancesuite.parser.testsources.TestSourcesParser;
import info.fingo.xactus.processor.testutil.ResultSequenceFormatter;
import info.fingo.xactus.processor.testutil.bundle.Bundle;
import info.fingo.xactus.processor.testutil.bundle.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

class ConformanceSuiteTest {
    private static final Representation REPRESENTATION = new StandardRepresentation() {
        @Override
        public String toStringOf(Object object) {
            if (object instanceof ResultSequence) {
                return ResultSequenceFormatter.fullDebugString((ResultSequence) object);
            }

            return super.toStringOf(object);
        }
    };

    @SuppressWarnings("unused")
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
            Objects.requireNonNull(entryId);

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

    @Execution(ExecutionMode.CONCURRENT)
    @ParameterizedTest
    @MethodSource("testcases")
    @SuppressWarnings("unused")
    void test(String testCaseId,
              List<Input> inputs,
              String xqFile,
              List<OutputFile> expectedOutputFiles,
              List<String> expectedErrors) throws Exception {
        PsychopathTestContext psychopathTestContext = new PsychopathTestContext();
        psychopathTestContext.init();

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
            .withRepresentation(REPRESENTATION)
            .is(matchesAnyOfExpected(expectedOutputFiles, contentProvider, psychopathTestContext));
    }
}
