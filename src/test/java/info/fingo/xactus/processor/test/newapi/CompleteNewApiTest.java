/*******************************************************************************
 * Copyright (c) 2011, 2017 Jesper Steen Moller and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jesper Steen Moller  - initial API and implementation
 *******************************************************************************/

package info.fingo.xactus.processor.test.newapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;
import org.apache.xerces.jaxp.validation.XSGrammarPoolContainer;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSModel;
import info.fingo.xactus.processor.testutil.bundle.Platform;
import info.fingo.xactus.api.DynamicContext;
import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.StaticContext;
import info.fingo.xactus.api.XPath2Expression;
import info.fingo.xactus.processor.DOMLoader;
import info.fingo.xactus.processor.Engine;
import info.fingo.xactus.processor.XercesLoader;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.xerces.XercesTypeModel;
import info.fingo.xactus.processor.util.DynamicContextBuilder;
import info.fingo.xactus.processor.util.StaticContextBuilder;
import info.fingo.xactus.processor.testutil.bundle.Bundle;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CompleteNewApiTest extends TestCase {

	protected Document domDoc = null;
	protected Bundle bundle = null;

	protected void setUp() throws Exception {
		super.setUp();
		bundle = Platform
				.getBundle("org.w3c.xqts.testsuite");

		if (bundle == null) {
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		}
		System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema","org.apache.xerces.jaxp.validation.XMLSchemaFactory");
	}

	protected void loadDOMDocument(URL fileURL) throws IOException {
		InputStream is = fileURL.openStream();
		DOMLoader domloader = new XercesLoader();
		domloader.set_validating(false);
		domDoc = domloader.load(is);
		domDoc.setDocumentURI(fileURL.toString());
	}

	protected XSModel getGrammar() {
		ElementPSVI rootPSVI = (ElementPSVI) domDoc.getDocumentElement();
		XSModel schema = rootPSVI.getSchemaInformation();
		return schema;
	}

//	private Schema getSchema(InputStream schemaIs) throws SAXException {
//		SchemaFactory sf = SchemaFactory
//				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		Schema schema = sf.newSchema(new StreamSource(schemaIs));
//		return schema;
//	}

	protected XSModel getGrammar(URL schemaURL) throws IOException,
			SAXException {
		InputStream schemaIs = schemaURL.openStream();
		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new StreamSource(schemaIs));
		XSGrammarPoolContainer poolContainer = (XSGrammarPoolContainer) schema;
		XMLGrammarPool pool = poolContainer.getGrammarPool();
		Grammar[] grammars = pool
				.retrieveInitialGrammarSet(XMLGrammarDescription.XML_SCHEMA);

		XSGrammar[] xsGrammars = new XSGrammar[grammars.length];
		System.arraycopy(grammars, 0, xsGrammars, 0, grammars.length);

		return xsGrammars[0].toXSModel(xsGrammars);
	}

	protected String buildResultString(ResultSequence rs) {
		
		String actual = new String();
		Iterator<Item> iterator = rs.iterator();
		while (iterator.hasNext()) {
			AnyType anyType = (AnyType)iterator.next();
			actual = actual + anyType.getStringValue() + " ";
		}

		return actual.trim();
	}

	protected Object evaluateSimpleXPath(String xpath, StaticContext sc, Document doc, Class<?> resultClass) {
		
		XPath2Expression path = new Engine().parseExpression(xpath, sc);

		DynamicContext dynamicContext = new DynamicContextBuilder(sc);

		info.fingo.xactus.api.ResultSequence rs = path.evaluate(dynamicContext, doc != null ? new Object[] { doc } : new Object[0]);
		assertEquals("Expected single result from \'" + xpath + "\'", 1, rs.size());
		Object result = rs.value(0);
		assertTrue("Exected XPath result instanceof class " + resultClass.getSimpleName() + " from \'" + xpath + "\', got " + result.getClass(), resultClass.isInstance(result));
		return result;
	}

	public void testSimpleMath() throws Exception {
//		String xpath = "($input-context/atomic:root/atomic:integer) union ($input-context/atomic:root/atomic:integer)";
		String xpath = "2+2 = 4";

		Boolean b = (Boolean)evaluateSimpleXPath(xpath, new StaticContextBuilder(), null, Boolean.class);

		assertEquals(Boolean.TRUE, b);
	}

	public void testNamesWhichAreKeywords() throws Exception {
		// Bug 273719
		bundle = Platform
		.getBundle("info.fingo.xactus.processor.tests");

		URL fileURL = bundle.getEntry("/bugTestFiles/bug311480.xml");
		loadDOMDocument(fileURL);

		// Get XML Schema Information for the Document
		XSModel schema = getGrammar();

//		String xpath = "($input-context/atomic:root/atomic:integer) union ($input-context/atomic:root/atomic:integer)";
		String xpath = "((/element/eq eq 'eq') or //child::xs:*) and false";

		Boolean b = (Boolean)evaluateSimpleXPath(xpath, new StaticContextBuilder().withNamespace("xs", "urn:joe").withTypeModel(new XercesTypeModel(schema)), domDoc, Boolean.class);

		assertEquals(Boolean.FALSE, b);
	}


}
