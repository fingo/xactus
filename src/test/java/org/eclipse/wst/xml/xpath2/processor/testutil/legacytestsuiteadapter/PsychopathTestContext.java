package org.eclipse.wst.xml.xpath2.processor.testutil.legacytestsuiteadapter;

import static java.util.stream.Collectors.toList;
import static org.eclipse.wst.xml.xpath2.processor.util.ResultSequenceUtil.newToOld;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.xml.xpath2.api.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.ComparisonType;
import org.eclipse.wst.xml.xpath2.processor.test.AbstractPsychoPathTest;

public class PsychopathTestContext extends AbstractPsychoPathTest {
    public void init() throws Exception {
        setUp();
    }

    public void setupContext(XSModel schema) {
        setupDynamicContext(schema);
    }

    public void compile(String xPath) {
        Objects.requireNonNull(xPath);
        compileXPath(xPath);
    }

    public ResultSequence evaluate() {
        Object[] params = Stream.of(domDoc, domDoc2)
            .filter(Objects::nonNull)
            .toArray();

        return newXPath.evaluate(dynamicContextBuilder, params);
    }

    public String buildResult(ResultSequence rs,
                              ComparisonType comparisonType) {
        Objects.requireNonNull(rs);
        Objects.requireNonNull(comparisonType);

        switch (comparisonType) {
            case XML:
            case FRAGMENT:
                try {
                    return super.buildXMLResultString(newToOld(rs));
                } catch (Exception e) {
                    throw new RuntimeException("Unexpected error occurred", e);
                }
            case TEXT:
            case INSPECT:
                return super.buildResultString(newToOld(rs));
        }
        throw new UnsupportedOperationException(
            "Comparison type \"" + comparisonType + "\" is unsupported.");
    }

    public void loadInput(URL url) throws IOException {
        loadDOMDocument(url);
    }

    public void loadInput(Collection<String> inputFiles) throws IOException {
        Objects.requireNonNull(inputFiles);

        if (inputFiles.isEmpty()) {
            return;
        }

        List<URL> inputFileURLs = inputFiles.stream()
            .map(bundle::getEntry)
            .collect(toList());

        if (inputFileURLs.size() == 1) {
            loadDOMDocument(inputFileURLs.get(0));
            return;
        }

        if (inputFileURLs.size() == 2) {
            load2DOMDocument(inputFileURLs.get(0), inputFileURLs.get(1));
            return;
        }

        throw new IllegalStateException(
            "No more than 2 input files expected.");
    }

    public XSModel loadGrammar() {
        XSModel grammar = getGrammar();

        if (grammar != null && domDoc2 != null) {
            throw new IllegalArgumentException();
        }

        return grammar;
    }
}
