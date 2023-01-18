package info.fingo.xactus.processor.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import info.fingo.xactus.api.ResultSequence;

/**
 * Inspired by NodeTestTest
 * but do not use variables and tests several context values instead
 * 
 * '_B' - run on root as context
 * 
 * @author wojciech.diakowski
 *
 */
public class ConformanceNodeTest extends ConformanceTestAbstarct {

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	public void test_NodeTest001(int context) throws Exception {

		String inputFile = "/TestSources/bib2.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest001.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest001.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "/comment()";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = buildXMLResultString(rs);
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	public void test_NodeTest002(int context) throws Exception {

		String inputFile = "/TestSources/bib2.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest002.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest002.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "/processing-instruction()";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = buildXMLResultString(rs);
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	public void test_NodeTest006(int context) throws Exception {

		String inputFile = "/TestSources/bib.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest006.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest006.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "/bib/book/editor/affiliation/text()";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<result>" + buildXMLResultString(rs) + "</result>";
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 1 })
	public void test_NodeTest006_B(int context) throws Exception {

		String inputFile = "/TestSources/bib.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest006.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest006.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "book/editor/affiliation/text()";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<result>" + buildXMLResultString(rs) + "</result>";
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1 })
	// processing-instruction() finds no descendant PIs of the given name anywhere.
	public void test_NodeTest007_1(int context) throws Exception {

		String inputFile = "/TestSources/TreeEmpty.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest007.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest007-1.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "fn:count(//processing-instruction('a-pi'))";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	// processing-instruction() gets all PIs of the given name, including those off
	// root.
	public void test_NodeTest007_2(int context) throws Exception {

		String inputFile = "/TestSources/TopMany.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest007.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest007-2.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "fn:count(//processing-instruction('a-pi'))";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	// text() gets no descendant text nodes.
	public void test_NodeTest008_1(int context) throws Exception {

		String inputFile = "/TestSources/Tree1Child.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest008.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest008-1.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "fn:count(//center/text())";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 1 })
	// text() gets no descendant text nodes.
	public void test_NodeTest008_1_B(int context) throws Exception {
		
		String inputFile = "/TestSources/Tree1Child.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest008.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest008-1.xml";
		String expectedResult = getExpectedResult(resultFile);
		
		String xpath = "fn:count(north/near-north/center/text())";
		ResultSequence rs = evaluate(inputFile, xpath, context);
		
		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	// text() gets text nodes.
	public void test_NodeTest008_2(int context) throws Exception {

		String inputFile = "/TestSources/TreeCompass.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest008.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest008-2.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "fn:count(//center/text())";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 1 })
	// text() gets text nodes.
	public void test_NodeTest008_2_B(int context) throws Exception {
		
		String inputFile = "/TestSources/TreeCompass.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest008.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest008-2.xml";
		String expectedResult = getExpectedResult(resultFile);
		
		String xpath = "fn:count(north/near-north/center/text())";
		ResultSequence rs = evaluate(inputFile, xpath, context);
		
		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	// comment() gets no descendant comment nodes.
	public void test_NodeTest009_1(int context) throws Exception {

		String inputFile = "/TestSources/Tree1Child.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest009.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest009-1.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "fn:count(//center/comment())";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 1 })
	// comment() gets no descendant comment nodes.
	public void test_NodeTest009_1_B(int context) throws Exception {
		
		String inputFile = "/TestSources/Tree1Child.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest009.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest009-1.xml";
		String expectedResult = getExpectedResult(resultFile);
		
		String xpath = "fn:count(north/near-north/center/comment())";
		ResultSequence rs = evaluate(inputFile, xpath, context);
		
		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2 })
	// comment() gets comment nodes.
	public void test_NodeTest009_2(int context) throws Exception {

		String inputFile = "/TestSources/TreeCompass.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest009.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest009-2.xml";
		String expectedResult = getExpectedResult(resultFile);

		String xpath = "fn:count(//center/comment())";
		ResultSequence rs = evaluate(inputFile, xpath, context);

		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}
	
	@ParameterizedTest
	@ValueSource(ints = { 1 })
	// comment() gets comment nodes.
	public void test_NodeTest009_2_B(int context) throws Exception {
		
		String inputFile = "/TestSources/TreeCompass.xml";
		// String xqFile =
		// "/Queries/XQuery/Expressions/PathExpr/Steps/NodeTest/NodeTest009.xq";
		String resultFile = "/ExpectedTestResults/Expressions/PathExpr/Steps/NodeTest/NodeTest009-2.xml";
		String expectedResult = getExpectedResult(resultFile);
		
		String xpath = "fn:count(north/near-north/center/comment())";
		ResultSequence rs = evaluate(inputFile, xpath, context);
		
		String actual = "<out>" + buildResultString(rs) + "</out>";
		assertEquals(expectedResult, actual);
	}

}
