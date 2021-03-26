package org.eclipse.wst.xml.xpath2.processor.conformancesuite;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuiteParser;
import org.eclipse.wst.xml.xpath2.processor.conformancesuite.util.TestSuite;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConformanceSuite {

    @Test
    void test() throws Exception {
        Document doc;
        try (InputStream is = getClass().getResourceAsStream("/catalog/XQTSCatalog.xml")) {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        }

        Element testsuite = doc.getDocumentElement();
        testsuite.normalize();

        TestSuite testSuite = TestSuiteParser.parseTestSuite(testsuite);

        System.out.println(testSuite);
    }
}
