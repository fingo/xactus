package org.eclipse.wst.xml.xpath2.processor.internal;

import java.io.IOException;
import java.net.URL;
import org.eclipse.wst.xml.xpath2.api.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.parser.testcase.ComparisonType;
import org.eclipse.wst.xml.xpath2.processor.testutil.legacytestsuiteadapter.PsychopathTestContext;
import org.junit.jupiter.api.BeforeEach;

public class XPathExpressionTestBase {
    private PsychopathTestContext context;

    @BeforeEach
    final void beforeEach() throws Exception {
        context = new PsychopathTestContext();
        context.init();
    }

    protected String executeXPathExpression(URL fileURL,
                                            String xPath,
                                            ComparisonType comparisonType) throws IOException {
        return context.buildResult(
            executeXPathExpression(fileURL, xPath),
            comparisonType);
    }

    protected ResultSequence executeXPathExpression(URL fileURL,
                                                    String xPath) throws IOException {
        context.loadInput(fileURL);
        context.setupContext(null);
        context.compile(xPath);

        return context.evaluate();
    }
}
