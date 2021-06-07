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


public class DayFromDateTimeFuncTest extends AbstractPsychoPathTest {

   //Evaluates the "day-from-dateTime" function with the arguments set as follows: $arg = xs:dateTime(lower bound).
   public void test_fn_day_from_dateTime1args_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime1args-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime1args-1.txt";
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

   //Evaluates the "day-from-dateTime" function with the arguments set as follows: $arg = xs:dateTime(mid range).
   public void test_fn_day_from_dateTime1args_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime1args-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime1args-2.txt";
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

   //Evaluates the "day-from-dateTime" function with the arguments set as follows: $arg = xs:dateTime(upper bound).
   public void test_fn_day_from_dateTime1args_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime1args-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime1args-3.txt";
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

   //Evaluates the "day-from-dateTime" function as per example 1 (of this function) of the Functions Operators spec.
   public void test_fn_day_from_dateTime_1() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-1.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-1.txt";
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

   //Evaluates the "day-from-dateTime" function as per example 2(of this function) of the Functions Operators spec.
   public void test_fn_day_from_dateTime_2() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-2.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-2.txt";
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

   //Evaluates the "day-from-dateTime" function as per example 3 (of this function) of the Functions Operators spec.
   public void test_fn_day_from_dateTime_3() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-3.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-3.txt";
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

   //Evaluates the "day-from-dateTime" function used as an argument of an avg function.
   public void test_fn_day_from_dateTime_4() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-4.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-4.txt";
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

   //Evaluates the "day-from-dateTime" function using empty sequence as argument. Use of fn:count to avoid empty file.
   public void test_fn_day_from_dateTime_5() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-5.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-5.txt";
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

   //Evaluates the "day-from-dateTime" function with argument set so that function returns 1.
   public void test_fn_day_from_dateTime_6() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-6.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-6.txt";
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

   //Evaluates the "day-from-dateTime" function with argument set so the function returns 31. Use Zulu.
   public void test_fn_day_from_dateTime_7() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-7.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-7.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of an addition expression.
   public void test_fn_day_from_dateTime_8() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-8.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-8.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a subtraction expression.
   public void test_fn_day_from_dateTime_9() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-9.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-9.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a multiplication expression.
   public void test_fn_day_from_dateTimeNew_10() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTimeNew-10.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTimeNew-10.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a div expression.
   public void test_fn_day_from_dateTime_11() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-11.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-11.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of an idiv expression.
   public void test_fn_day_from_dateTime_12() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-12.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-12.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a mod expression.
   public void test_fn_day_from_dateTime_13() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-13.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-13.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a numeric-unary-plus expression.
   public void test_fn_day_from_dateTime_14() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-14.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-14.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a numeric-unary-minus expression.
   public void test_fn_day_from_dateTime_15() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-15.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-15.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a numeric-equal expression (eq operator).
   public void test_fn_day_from_dateTime_16() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-16.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-16.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a numeric-equal expression (ne operator).
   public void test_fn_day_from_dateTime_17() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-17.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-17.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a numeric-equal expression (le operator).
   public void test_fn_day_from_dateTime_18() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-18.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-18.txt";
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

   //Evaluates the "day-from-dateTime" function, which is part of a numeric-equal expression (ge operator).
   public void test_fn_day_from_dateTime_19() throws Exception {
      String inputFile = "/TestSources/emptydoc.xml";
      String xqFile = "/Queries/XQuery/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-19.xq";
      String resultFile = "/ExpectedTestResults/Functions/DurationDateTimeFunc/DayFromDateTimeFunc/fn-day-from-dateTime-19.txt";
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
