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

package info.fingo.xactus.processor.testsuite.schema;

import java.net.URL;

import org.apache.xerces.xs.XSModel;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.ResultSequence;
import info.fingo.xactus.processor.StaticError;
import info.fingo.xactus.processor.XPathParserException;
import info.fingo.xactus.processor.test.AbstractPsychoPathTest;


public class ancestorAxisTest extends AbstractPsychoPathTest {

   //Evaluates unabbreviated syntax - ancestor::employee - Selects all the "employee" ancestors of the context node.
   public void test_unabbreviatedSyntax_10() throws Exception {
      String inputFile = "/TestSources/works-mod.xml";
      String xqFile = "/Queries/XQuery/FullAxis/ancestorAxis/unabbreviatedSyntax-10.xq";
      String resultFile = "/ExpectedTestResults/FullAxis/ancestorAxis/unabbreviatedSyntax-10.txt";
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

}
