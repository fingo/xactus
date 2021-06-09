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


public class HoursFromTimeFuncTest extends AbstractPsychoPathTest {

   //Evaluates the "hours-from-time" function with the arguments set as follows: $arg = xs:time(lower bound).
   public void test_fn_hours_from_time1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time1args-1.txt";
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

   //Evaluates the "hours-from-time" function with the arguments set as follows: $arg = xs:time(mid range).
   public void test_fn_hours_from_time1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time1args-2.txt";
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

   //Evaluates the "hours-from-time" function with the arguments set as follows: $arg = xs:time(upper bound).
   public void test_fn_hours_from_time1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time1args-3.txt";
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

   //Evaluates the "hours-from-time" function as per example 1 (of this function) of the Functions Operators spec.
   public void test_fn_hours_from_time_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-1.txt";
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

   //Evaluates the "hours-from-time" function as per example 2 (of this function) of the Functions Operators spec.
   public void test_fn_hours_from_time_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-2.txt";
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

   //Evaluates the "hours-from-time" function as per example 3 (of this function) of the Functions Operators spec.
   public void test_fn_hours_from_time_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-3.txt";
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

   //Evaluates the "hours-from-time" function as per example 4 (of this function) of the Functions Operators spec.
   public void test_fn_hours_from_time_4() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-4.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-4.txt";
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

   //Evaluates the "hours-from-time" function using empty sequence as argument. Use of fn:count to avoid empty file.
   public void test_fn_hours_from_time_5() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-5.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-5.txt";
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

   //Evaluates the "hours-from-time" function with argument set so the function returns 0.
   public void test_fn_hours_from_time_6() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-6.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-6.txt";
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

   //Evaluates the "hours-from-time" function with argument set so the function returns 23.
   public void test_fn_hours_from_time_7() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-7.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-7.txt";
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

   //Evaluates the "hours-from-time" function, which is part of an addition expression.
   public void test_fn_hours_from_time_8() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-8.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-8.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a substraction expression.
   public void test_fn_hours_from_time_9() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-9.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-9.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a multiplication expression.
   public void test_fn_hours_from_time_10() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-10.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-10.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a div expression.
   public void test_fn_hours_from_time_11() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-11.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-11.txt";
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

   //Evaluates the "hours-from-time" function, which is part of an idiv expression.
   public void test_fn_hours_from_time_12() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-12.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-12.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a mod expression.
   public void test_fn_hours_from_time_13() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-13.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-13.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a numeric-unary-plus expression.
   public void test_fn_hours_from_time_14() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-14.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-14.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a numeric-unary-minus expression.
   public void test_fn_hours_from_time_15() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-15.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-15.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a numeric-equal expression (eq operator).
   public void test_fn_hours_from_time_16() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-16.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-16.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a numeric-equal expression (ne operator).
   public void test_fn_hours_from_time_17() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-17.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-17.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a numeric-equal expression (le operator).
   public void test_fn_hours_from_time_18() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-18.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-18.txt";
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

   //Evaluates the "hours-from-time" function, which is part of a numeric-equal expression (ge operator).
   public void test_fn_hours_from_time_19() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-19.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/HoursFromTimeFunc/fn-hours-from-time-19.txt";
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
