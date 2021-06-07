
/*******************************************************************************
 * Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver - STAR - initial api and implementation bug 262765
 *******************************************************************************/
package info.fingo.xactus.processor.testsuite.core;

import java.net.URL;

import org.apache.xerces.xs.XSModel;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.ResultSequence;
import info.fingo.xactus.processor.StaticError;
import info.fingo.xactus.processor.XPathParserException;
import info.fingo.xactus.processor.test.AbstractPsychoPathTest;


public class SeqExceptTest extends AbstractPsychoPathTest {

   //Arg: node.
   public void test_fn_except_node_args_001() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-001.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-001.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: incorrect nodes.
   public void test_fn_except_node_args_002() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-002.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-002.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertXMLFragmentEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: node.
   public void test_fn_except_node_args_003() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-003.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-003.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertXMLEqual("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: text node and node.
   public void test_fn_except_node_args_004() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-004.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-004.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: processing-instruction node and node.
   public void test_fn_except_node_args_005() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-005.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-005.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: processing-instruction node and node.
   public void test_fn_except_node_args_006() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-006.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-006.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertXMLFragmentEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: comment node and node.
   public void test_fn_except_node_args_007() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-007.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-007.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: processing-instruction node and node.
   public void test_fn_except_node_args_009() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-009.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-009.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertXMLFragmentEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: processing-instruction node and node.
   public void test_fn_except_node_args_010() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-010.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-010.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertXMLFragmentEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: comment node and node.
   public void test_fn_except_node_args_011() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-011.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-011.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: node and non existing node.
   public void test_fn_except_node_args_012() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-012.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-012.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertXMLFragmentEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: node and empty sequence.
   public void test_fn_except_node_args_013() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-013.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-013.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertXMLFragmentEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: empty sequence and empty sequence.
   public void test_fn_except_node_args_014() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-014.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-014.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: node and node.
   public void test_fn_except_node_args_015() throws Exception {
      String inputFile = "/TestSources/atomic.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-015.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-015.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);
      addNamespace("atomic", "http://www.w3.org/XQueryTest");

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: node and node.
   public void test_fn_except_node_args_016() throws Exception {
      String inputFile = "/TestSources/atomic.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-016.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-016.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);
      addNamespace("atomic", "http://www.w3.org/XQueryTest");


      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: node and node.
   public void test_fn_except_node_args_017() throws Exception {
      String inputFile = "/TestSources/atomic.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-017.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-017.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);
      addNamespace("atomic", "http://www.w3.org/XQueryTest");

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildXMLResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      expectedResult = removeIrrelevantNamespaces(expectedResult);

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: text node and text node.
   public void test_fn_except_node_args_018() throws Exception {
      String inputFile = "/TestSources/atomic.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-018.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-018.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);
      addNamespace("atomic", "http://www.w3.org/XQueryTest");

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: text node and text node.
   public void test_fn_except_node_args_019() throws Exception {
      String inputFile = "/TestSources/atomic.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-019.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-019.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);
      addNamespace("atomic", "http://www.w3.org/XQueryTest");


      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: text node and text node.
   public void test_fn_except_node_args_020() throws Exception {
      String inputFile = "/TestSources/atomic.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-020.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-020.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);
      addNamespace("atomic", "http://www.w3.org/XQueryTest");

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: text node and text node.
   public void test_fn_except_node_args_021() throws Exception {
      String inputFile = "/TestSources/atomic.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-021.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-021.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);
      addNamespace("atomic", "http://www.w3.org/XQueryTest");

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

   //Arg: node and node.
   public void test_fn_except_node_args_023() throws Exception {
      String inputFile = "/TestSources/bib2.xml";
      String xqFile = "/Queries/XQuery/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-023.xq";
      String resultFile = "/ExpectedTestResults/Expressions/Operators/SeqOp/SeqExcept/fn-except-node-args-023.txt";
      String expectedResult = getExpectedResult(resultFile);
      URL fileURL = bundle.getEntry(inputFile);
      loadDOMDocument(fileURL);

      // Get XML Schema Information for the Document
      XSModel schema = getGrammar();

      setupDynamicContext(schema);

      String xpath = extractXPathExpression(xqFile, inputFile);
      String actual = null;
      try {
          compileXPath(xpath);
          ResultSequence rs = evaluate(domDoc);


          actual = buildResultString(rs);

      } catch (XPathParserException ex) {
    	 actual = ex.code();
      } catch (StaticError ex) {
         actual = ex.code();
      } catch (DynamicError ex) {
         actual = ex.code();
      }

      assertEquals("XPath Result Error " + xqFile + ":", expectedResult, actual);


   }

}
