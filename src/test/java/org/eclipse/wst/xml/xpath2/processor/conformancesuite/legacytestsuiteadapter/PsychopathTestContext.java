package org.eclipse.wst.xml.xpath2.processor.conformancesuite.legacytestsuiteadapter;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.ast.XPath;
import org.eclipse.wst.xml.xpath2.processor.test.AbstractPsychoPathTest;
import org.eclipse.wst.xml.xpath2.processor.util.ResultSequenceUtil;
import org.w3c.dom.Document;

public class PsychopathTestContext extends AbstractPsychoPathTest {
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

    public String buildResult(ResultSequence rs) {
        return buildResultString(rs);
    }

    public void loadInput(Collection<String> inputFiles) throws IOException {
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
