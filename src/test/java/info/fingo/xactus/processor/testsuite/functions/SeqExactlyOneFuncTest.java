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

package info.fingo.xactus.processor.testsuite.functions;

import java.net.URL;

import org.apache.xerces.xs.XSModel;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.ResultSequence;
import info.fingo.xactus.processor.StaticError;
import info.fingo.xactus.processor.XPathParserException;
import info.fingo.xactus.processor.test.AbstractPsychoPathTest;


public class SeqExactlyOneFuncTest extends AbstractPsychoPathTest {

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:int(lower bound).
   public void test_fn_exactly_oneint1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneint1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneint1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:int(mid range).
   public void test_fn_exactly_oneint1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneint1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneint1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:int(upper bound).
   public void test_fn_exactly_oneint1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneint1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneint1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:integer(lower bound).
   public void test_fn_exactly_oneintg1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneintg1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneintg1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:integer(mid range).
   public void test_fn_exactly_oneintg1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneintg1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneintg1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:integer(upper bound).
   public void test_fn_exactly_oneintg1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneintg1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneintg1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:decimal(lower bound).
   public void test_fn_exactly_onedec1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedec1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedec1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:decimal(mid range).
   public void test_fn_exactly_onedec1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedec1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedec1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:decimal(upper bound).
   public void test_fn_exactly_onedec1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedec1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedec1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:double(lower bound).
   public void test_fn_exactly_onedbl1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedbl1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedbl1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:double(mid range).
   public void test_fn_exactly_onedbl1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedbl1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedbl1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:double(upper bound).
   public void test_fn_exactly_onedbl1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedbl1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onedbl1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:float(lower bound).
   public void test_fn_exactly_oneflt1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneflt1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneflt1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:float(mid range).
   public void test_fn_exactly_oneflt1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneflt1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneflt1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:float(upper bound).
   public void test_fn_exactly_oneflt1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneflt1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneflt1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:long(lower bound).
   public void test_fn_exactly_onelng1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onelng1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onelng1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:long(mid range).
   public void test_fn_exactly_onelng1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onelng1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onelng1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:long(upper bound).
   public void test_fn_exactly_onelng1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onelng1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onelng1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:unsignedShort(lower bound).
   public void test_fn_exactly_oneusht1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneusht1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneusht1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:unsignedShort(mid range).
   public void test_fn_exactly_oneusht1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneusht1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneusht1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:unsignedShort(upper bound).
   public void test_fn_exactly_oneusht1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneusht1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneusht1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:negativeInteger(lower bound).
   public void test_fn_exactly_onenint1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenint1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenint1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:negativeInteger(mid range).
   public void test_fn_exactly_onenint1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenint1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenint1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:negativeInteger(upper bound).
   public void test_fn_exactly_onenint1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenint1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenint1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:positiveInteger(lower bound).
   public void test_fn_exactly_onepint1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onepint1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onepint1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:positiveInteger(mid range).
   public void test_fn_exactly_onepint1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onepint1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onepint1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:positiveInteger(upper bound).
   public void test_fn_exactly_onepint1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onepint1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onepint1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:unsignedLong(lower bound).
   public void test_fn_exactly_oneulng1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneulng1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneulng1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:unsignedLong(mid range).
   public void test_fn_exactly_oneulng1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneulng1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneulng1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:unsignedLong(upper bound).
   public void test_fn_exactly_oneulng1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneulng1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-oneulng1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:nonPositiveInteger(lower bound).
   public void test_fn_exactly_onenpi1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenpi1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenpi1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:nonPositiveInteger(mid range).
   public void test_fn_exactly_onenpi1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenpi1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenpi1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:nonPositiveInteger(upper bound).
   public void test_fn_exactly_onenpi1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenpi1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenpi1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:nonNegativeInteger(lower bound).
   public void test_fn_exactly_onenni1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenni1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenni1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:nonNegativeInteger(mid range).
   public void test_fn_exactly_onenni1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenni1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenni1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:nonNegativeInteger(upper bound).
   public void test_fn_exactly_onenni1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenni1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onenni1args-3.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:short(lower bound).
   public void test_fn_exactly_onesht1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onesht1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onesht1args-1.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:short(mid range).
   public void test_fn_exactly_onesht1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onesht1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onesht1args-2.txt";
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

   //Evaluates the "exactly-one" function with the arguments set as follows: $arg = xs:short(upper bound).
   public void test_fn_exactly_onesht1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onesht1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-onesht1args-3.txt";
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

   //Evaluates the "exactly-one" function with the argument set to a sequence with more than one item.
   public void test_fn_exactly_one_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/SeqFunc/CardinalitySeqFunc/SeqExactlyOneFunc/fn-exactly-one-1.xq";
      String expectedResult = "FORG0005";
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
