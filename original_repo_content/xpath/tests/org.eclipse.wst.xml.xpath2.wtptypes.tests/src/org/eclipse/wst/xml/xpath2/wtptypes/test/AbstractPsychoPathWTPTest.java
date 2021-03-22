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
 *     David Carver (STAR) - initial API and implementation
 *     Jin Mingjan - bug 262765 -  extractXPathExpression and getExpectedResults
 *     Jesper S Moller - bug 283214 - fix IF THEN ELSE parsing and update grammars
 *     Jesper S Moller - bug 283214 - fix XML result serialization
 *     Jesper S Moller - bug 283404 - fixed locale  
 *     Jesper S Moller - bug 281159 - fix document URIs and also filter XML namespace
 *     Jesper S Moller - bug 275610 - Avoid big time and memory overhead for externals
 *     Jesper Steen Moeller - bug 282096 - make test harness handle all string encoding
 *     Jesper Steen Moller  - bug 280555 - Add pluggable collation support
 *     Mukul Gandhi         - bug 338494 - prohibiting xpath expressions starting with / or // to be parsed.
 *******************************************************************************/
package org.eclipse.wst.xml.xpath2.wtptypes.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.custommonkey.xmlunit.XMLTestCase;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.xpath2.api.typesystem.TypeModel;
import org.eclipse.wst.xml.xpath2.processor.DOMLoader;
import org.eclipse.wst.xml.xpath2.processor.DOMLoaderException;
import org.eclipse.wst.xml.xpath2.processor.DefaultDynamicContext;
import org.eclipse.wst.xml.xpath2.processor.DefaultEvaluator;
import org.eclipse.wst.xml.xpath2.processor.DynamicContext;
import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.Evaluator;
import org.eclipse.wst.xml.xpath2.processor.JFlexCupParser;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.StaticChecker;
import org.eclipse.wst.xml.xpath2.processor.StaticError;
import org.eclipse.wst.xml.xpath2.processor.StaticNameResolver;
import org.eclipse.wst.xml.xpath2.processor.XPathParser;
import org.eclipse.wst.xml.xpath2.processor.XPathParserException;
import org.eclipse.wst.xml.xpath2.processor.XercesLoader;
import org.eclipse.wst.xml.xpath2.processor.ast.XPath;
import org.eclipse.wst.xml.xpath2.processor.function.FnFunctionLibrary;
import org.eclipse.wst.xml.xpath2.processor.function.XSCtrLibrary;
import org.eclipse.wst.xml.xpath2.processor.internal.types.AnyType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.DocType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.NodeType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.QName;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSBoolean;
import org.eclipse.wst.xml.xpath2.processor.internal.types.userdefined.UserDefinedCtrLibrary;
import org.eclipse.wst.xml.xpath2.processor.internal.types.xerces.XercesTypeModel;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class AbstractPsychoPathWTPTest extends XMLTestCase {

	protected Document domDoc = null;
	protected Document domDoc2 = null;
	protected Bundle bundle = null;

	IDOMModel model = null;

	private DynamicContext dynamicContext = null;
	
	private static final String INPUT_CONTEXT = "input-context";
	private static final String INPUT_CONTEXT1 = "input-context1";
	private static final String INPUT_CONTEXT2 = "input-context2";
	// private static final String S_COMMENT1 = "(:";
	private static final String S_COMMENT2 = ":)";
	private static final String DECLARE_NAMESPACE = "declare namespace";
	private static final String IMPORT_SCHEMA_NAMESPACE = "import schema namespace";
	private static final String REGEX_DN = " namespace\\s+(\\w[-_\\w]*)\\s*=\\s*['\"]([^;]*)['\"];";

	private static HashMap inputMap = new HashMap(3);

	protected void setUp() throws Exception {
		super.setUp();
		bundle = Platform
				.getBundle("org.w3c.xqts.testsuite");

		
		if (bundle == null) {
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		}
		System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema","org.apache.xerces.jaxp.validation.XMLSchemaFactory");
	}

	protected void loadDOMDocument(URL fileURL) throws IOException,
			DOMLoaderException {
		InputStream is = testResolve(fileURL);
		DOMLoader domloader = new XercesLoader();
		domloader.set_validating(false);
		domDoc = domloader.load(is);
		domDoc.setDocumentURI(fileURL.toString());
	}

	private InputStream testResolve(URL url) throws IOException {
		if (url.getProtocol().equals("http")) {
			return AbstractPsychoPathWTPTest.class.getResourceAsStream("/org/eclipse/wst/xml/xpath2/processor/test/" + url.getFile());
		} else {
			return url.openStream();
		}
	}
	
	protected InputSource getTestSource(String systemId) {
		if (systemId.startsWith("http://")) {
			try {
				URL u = new URL(systemId);
				InputSource inputSource = new InputSource(testResolve(u));
				inputSource.setSystemId(systemId);
				return inputSource;
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return new InputSource(systemId);
	}
	
	protected EntityResolver makeTestResolver() {
		return new EntityResolver2() {
			
			public InputSource resolveEntity(String publicId, String systemId) {
				if (systemId.startsWith("http://")) {
					URL u;
					try {
						u = new URL(systemId);
						return new InputSource(testResolve(u));
					} catch (MalformedURLException e) {
						throw new RuntimeException(e);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				return new InputSource(systemId);
			}

			public InputSource getExternalSubset(String publicId, String systemId)
					throws SAXException, IOException {
				return resolveEntity(publicId, systemId);
			}

			public InputSource resolveEntity(String name,
                    String publicId,
                    String baseURI,
                    String systemId) throws SAXException, IOException {
				return resolveEntity(publicId, systemId);
			}
			
		};
	}
	
	protected void loadDOMDocument(URL fileURL, Schema schema) throws IOException, DOMLoaderException {
		InputStream is = testResolve(fileURL);
		DOMLoader domloader = new XercesLoader(schema);
		domloader.set_validating(false);
		domDoc = domloader.load(is);
		domDoc.setDocumentURI(fileURL.toString());
		
	}
	
	protected void load2DOMDocument(URL fileURL, URL fileURL2) throws IOException,
			DOMLoaderException {
		InputStream is = testResolve(fileURL);
		InputStream is2 = testResolve(fileURL2);
		
		DOMLoader domloader = new XercesLoader();
		domloader.set_validating(false);
		domDoc = domloader.load(is);
		domDoc.setDocumentURI(fileURL.toString());
		domDoc2 = domloader.load(is2);
		domDoc2.setDocumentURI(fileURL2.toString());
		is.close();
		is2.close();
	}	

	protected void tearDown() throws Exception {
		super.tearDown();
		domDoc = null;
		domDoc2 = null;
		dynamicContext = null;
		if (model != null) {
			model.releaseFromRead();
		}
	}

	protected XSModel getGrammar() {
		ElementPSVI rootPSVI = (ElementPSVI) domDoc.getDocumentElement();
		XSModel schema = rootPSVI.getSchemaInformation();
		return schema;
	}

	protected void loadDOMDocument(URL fileURL, URL schemaURL)
			throws IOException, DOMLoaderException, SAXException {
		InputStream is = testResolve(fileURL);
		InputStream schemaIs = testResolve(schemaURL);
		Schema jaxpSchema = getSchema(schemaIs);
		DOMLoader domloader = new XercesLoader(jaxpSchema);
		domloader.set_validating(false);
		domDoc = domloader.load(is);
	}
	
	

	private Schema getSchema(InputStream schemaIs) throws SAXException {
		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new StreamSource(schemaIs));
		return schema;
	}

	protected XSModel getGrammar(URL schemaURL) throws IOException,
			SAXException {
		InputStream schemaIs = testResolve(schemaURL);
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

	protected DefaultDynamicContext setupDynamicContext(XSModel schema) {
		XercesTypeModel typeModel = schema != null ? new XercesTypeModel(schema) : null;
		DefaultDynamicContext dc = new DefaultDynamicContext(typeModel);
//		DefaultDynamicContext dc = new DefaultDynamicContext(schema, domDoc);
		dynamicContext = dc;
		
		dc.add_namespace("xs", "http://www.w3.org/2001/XMLSchema");
		dc.add_namespace("xsd", "http://www.w3.org/2001/XMLSchema");
		dc.add_namespace("fn", "http://www.w3.org/2005/xpath-functions");
		dc.add_namespace("xml", "http://www.w3.org/XML/1998/namespace");

		dc.add_function_library(new FnFunctionLibrary());
		dc.add_function_library(new XSCtrLibrary());
		setupVariables(dc);
		return dc;
	}

	protected DefaultDynamicContext setupDynamicContext2(TypeModel model) {
		DefaultDynamicContext dc = new DefaultDynamicContext(model);
		dynamicContext = dc;
		
		dc.add_namespace("xs", "http://www.w3.org/2001/XMLSchema");
		dc.add_namespace("xsd", "http://www.w3.org/2001/XMLSchema");
		dc.add_namespace("fn", "http://www.w3.org/2005/xpath-functions");
		dc.add_namespace("xml", "http://www.w3.org/XML/1998/namespace");

		dc.add_function_library(new FnFunctionLibrary());
		dc.add_function_library(new XSCtrLibrary());
		setupVariables(dc);
		return dc;
	}

	protected void addXPathDefaultNamespace(String uri) {
	   dynamicContext.add_namespace(null, uri);	
	}

	protected XPath compileXPath(DynamicContext dc, String xpath) throws XPathParserException, StaticError {
		XPathParser xpp = new JFlexCupParser();
		XPath path = xpp.parse(xpath);

		StaticChecker name_check = new StaticNameResolver(dc);
		name_check.check(path);
		return path;
	}
	
	protected XPath compileXPath(DynamicContext dc, String xpath, boolean isRootlessAccess) throws XPathParserException, StaticError {
       XPathParser xpp = new JFlexCupParser();
       XPath path = null; 
       if (isRootlessAccess) {
          path = xpp.parse(xpath, isRootlessAccess);
       }
       else {
    	   path = xpp.parse(xpath); 
       }
       
       StaticChecker name_check = new StaticNameResolver(dc);
       name_check.check(path);
       return path;
    }

	protected String getExpectedResult(String xqFile) {
		return getExpectedResult(xqFile, true);
	}

	protected String getExpectedResultNoEscape(String xqFile) {
		return getExpectedResult(xqFile, false);
	}

	protected String getExpectedResult(String xqFile, boolean unescape) {
		String resultFile = xqFile;
		//
		if (resultFile.length() < 10) { // <9 enough? like XPST0001
			return resultFile;
		}
		String content = "";
		//
		InputStream isrf;
		try {
			isrf = bundle.getEntry(resultFile).openStream();
			BufferedReader rfreader = new BufferedReader(new InputStreamReader(
					isrf, "UTF-8"));
			// XXX:assume char buffer 2048 is long enough;1024 maybe enough
			// Exception: Axes085, NodeTest003/04/05,...
			int bufferLength = 2048;
			if ((resultFile.indexOf("Axes085") != -1)
					|| (resultFile.indexOf("NodeTest003") != -1)
					|| (resultFile.indexOf("NodeTest004") != -1)
					|| (resultFile.indexOf("NodeTest005") != -1)) {
				bufferLength = 40000;
			} else if (resultFile.indexOf("ForExpr013") != -1) {
				bufferLength = 433500;
			} else if (resultFile.indexOf("ForExpr016") != -1
					|| (resultFile.indexOf("ReturnExpr011") != -1)
					|| (resultFile.indexOf("sgml-queries-results-q1") != -1)
					|| (resultFile.indexOf("sgml-queries-results-q2") != -1)) {
				bufferLength = 10240;
			}
			char[] cbuf = new char[bufferLength];
			int nByte = rfreader.read(cbuf);
			assertTrue(resultFile, nByte < bufferLength);// assert nice buffer
			// length

			content = new String(cbuf).trim();
			rfreader.close();
			isrf.close();

		} catch (IOException e) {
			throw new RuntimeException("Can't load expected result from : " + xqFile, e);
		}
		if (unescape && content.contains("&")) return resolveCharacterReferences(content);
		return content;
	}

	public String unwrapResult(String expectedResult, String elemName) {
		return trimSurrounding(expectedResult, "<"+ elemName + ">", "</" + elemName + ">");
	}
	
	protected String getExpectedResult(String resultFile, String elemName) {
		return unwrapResult(getExpectedResult(resultFile), elemName);
	}

	public String extractXPathExpressionNoEscape(String xqFile, String inputFile) {
		return extractXPathExpression(xqFile, inputFile, false);
	}

	public String extractXPathExpression(String xqFile, String inputFile) {
		return extractXPathExpression(xqFile, inputFile, true);
	}

	public String extractXPathExpression(String xqFile, String inputFile, boolean unescape) {
		// get the xpath2 expr from xq file, first
		char[] cbuf = new char[2048];//
		String content = null;
		String xpath2Expr = null;

		try {
			URL entryUrl = bundle.getEntry(xqFile);
			InputStream isxq = testResolve(entryUrl);
			if (dynamicContext.base_uri().string_value() == null)
				dynamicContext.set_base_uri(entryUrl.toString());
			BufferedReader xqreader = new BufferedReader(new InputStreamReader(
					isxq, Charset.forName("UTF-8")));
			int nByte = xqreader.read(cbuf);
			assertTrue(xqFile, nByte < 2048);
			content = new String(cbuf).trim();
			//
			if (content.indexOf(INPUT_CONTEXT) != -1
					&& content.indexOf(INPUT_CONTEXT1) == -1
					&& content.indexOf(INPUT_CONTEXT2) == -1) {
				inputMap.put(INPUT_CONTEXT, inputFile);
			} else if (content.indexOf(INPUT_CONTEXT1) == -1) {
				inputMap.put(INPUT_CONTEXT1, inputFile);
			} else if (content.indexOf(INPUT_CONTEXT2) != -1) {
				inputMap.put(INPUT_CONTEXT2, inputFile);
			}
			//	        
			if (content.indexOf(DECLARE_NAMESPACE) != -1 || content.indexOf(IMPORT_SCHEMA_NAMESPACE) != -1) {
				setupNamespace(content);
			}
			//
			assertTrue(content.lastIndexOf(S_COMMENT2) != -1);// assert to get
			xpath2Expr = content.substring(content.lastIndexOf(S_COMMENT2) + 2)
					.trim();			
			xqreader.close();
			isxq.close();
		} catch (IOException e) {
			throw new RuntimeException("Can't extract XPath expression from XQuery file : " + xqFile, e);
		}
		if (unescape && xpath2Expr.contains("&")) {
			return resolveCharacterReferences(xpath2Expr);
		} else {
			return xpath2Expr;
		}
	}

	protected String extractXPathExpression(String xqFile, String inputFile,
			String tagName) {
		return unwrapQuery(extractXPathExpression(xqFile, inputFile), tagName);
	}

	protected String unwrapQuery(String xpath2Expr, String tag) {
		String str = "<" + tag + ">";
		String endStr = "</" + tag + ">";
		String withoutTag = trimSurrounding(xpath2Expr, str, endStr);
		if (withoutTag != xpath2Expr) {
			// also trim off the braces { }
			xpath2Expr = trimSurrounding(withoutTag, "{", "}");
		}
		return xpath2Expr;
	}

	protected String trimSurrounding(String xpath2Expr, String str, String endStr) {
		int indexOfStart = xpath2Expr.indexOf(str);
		int indexOfEnd = xpath2Expr.indexOf(endStr);
		if (indexOfStart >= 0 && indexOfEnd >= 0) {
			xpath2Expr = xpath2Expr.substring(indexOfStart + str.length(), indexOfEnd).trim(); 
		}
		return xpath2Expr;
	}

	protected void setupNamespace(String content) {
		if (dynamicContext == null) return; // Can't set it up if nonexistent
		Pattern p = Pattern.compile(REGEX_DN);
		Matcher m = p.matcher(content);
		while (m.find()) {
			assertTrue(m.groupCount() == 2);//
			dynamicContext.add_namespace(m.group(1), m.group(2));
		}

	}

	protected DynamicContext setupVariables(DynamicContext dc) {
		dc.add_variable(new QName("x"));
		dc.add_variable(new QName("var"));
		
		if (domDoc != null) {
			AnyType docType = new DocType(domDoc, dc.getTypeModel(domDoc));
			dc.set_variable(new QName("input-context1"), docType);
			dc.set_variable(new QName("input-context"), docType);
			if (domDoc2 == null) {
				dc.set_variable(new QName("input-context2"), docType);
			} else {
				dc.set_variable(new QName("input-context2"), (AnyType) new DocType(domDoc2, dc.getTypeModel(domDoc2)));
			}
		}
		return dc;
	}

	protected String buildResultString(ResultSequence rs) {
		String actual = new String();
		Iterator iterator = rs.iterator();
		while (iterator.hasNext()) {
			AnyType anyType = (AnyType)iterator.next();
			
			actual = actual + anyType.string_value() + " ";
		}

		return actual.trim();
	}
	
	protected String buildXMLResultString(ResultSequence rs) throws Exception {
        DOMImplementationLS domLS = (DOMImplementationLS) domDoc.getImplementation().getFeature("LS", "3.0");
        LSOutput outputText = domLS.createLSOutput();
        LSSerializer serializer = domLS.createLSSerializer();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputText.setByteStream(outputStream);
        
		String actual = new String();
		Iterator iterator = rs.iterator();
		boolean queueSpace = false;
		while (iterator.hasNext()) {
			AnyType aat = (AnyType)iterator.next();
			if (aat instanceof NodeType) {
				NodeType nodeType = (NodeType) aat;
				Node node = nodeType.node_value();
				serializer.write(node, outputText);
				queueSpace = false;
			} else {
				if (queueSpace) outputText.getByteStream().write(32);
				outputText.getByteStream().write(aat.string_value().getBytes("UTF-8"));
				queueSpace = true;
			}
		}

		actual = outputStream.toString("UTF-8");
		actual = actual.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		actual = actual.replace("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>", "");
		outputStream.close();
		return actual.trim();
	}
	
	protected String formatResultString(String resultFile) throws Exception {
		DOMLoader domloader = new XercesLoader(null);
		domloader.set_validating(false);
		InputStream is = bundle.getEntry(resultFile).openStream();		
		Document resultDoc = domloader.load(is);

        DOMImplementationLS domLS = (DOMImplementationLS) resultDoc.getImplementation().getFeature("LS", "3.0");
        LSSerializer serializer = domLS.createLSSerializer();
        
        String actual = serializer.writeToString(resultDoc.getDocumentElement());

		actual = actual.replace("<?xml version=\"1.0\" encoding=\"UTF-16\"?>", "");
		return actual.trim();
	}

	public Document load(java.net.URL url) throws Exception {
		IModelManager modelManager = StructuredModelManager.getModelManager();
		InputStream inStream = url.openStream();
		if (inStream == null)
			throw new FileNotFoundException("Can't file resource stream "
					+ url.getFile());
		model = (IDOMModel) modelManager.getModelForRead(url.toString(),
				inStream, null);

		return model.getDocument();
	}

	protected String removeIrrelevantNamespaces(String expectedResult) {
		expectedResult = expectedResult.replaceAll(" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"", "");
		expectedResult = expectedResult.replaceAll(" xmlns:foo=\"http://www.example.com/foo\"", "");
	    expectedResult = expectedResult.replaceAll(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
		return expectedResult;
	}

	protected String resolveCharacterReferences(String xpath) {
		String docText = "<doc>" + xpath + "</doc>";
		InputStream is;
		try {
			is = new ByteArrayInputStream(docText.getBytes("UTF-8"));
			DOMLoader domloader = new XercesLoader();
			domloader.set_validating(false);
			Document temp = domloader.load(is);
			return temp.getDocumentElement().getFirstChild().getTextContent();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (DOMLoaderException e) {
			throw new RuntimeException(e);
		}
	}

	protected void assertXPathTrue(String xpath, DynamicContext dc, Document domDoc) {
		XSBoolean result = evaluateBoolXPath(xpath, dc, domDoc);
		assertEquals(true, result.value());
	}

	protected XSBoolean evaluateBoolXPath(String xpath, DynamicContext dc, Document doc) {
		return  (XSBoolean) evaluateSimpleXPath(xpath, dc, doc, XSBoolean.class);
	}
		
	protected AnyType evaluateSimpleXPath(String xpath, DynamicContext dc, Document doc, Class resultClass) {
		XPath path;
		try {
			path = compileXPath(dc, xpath);
		}
		catch (XPathParserException e) {
			throw new RuntimeException("XPath parse: " + e.getMessage(), e);
		}
		catch (StaticError e) {
			throw new RuntimeException("Static error: " + e.getMessage(), e);
		}
	
		Evaluator eval = new DefaultEvaluator(dc, doc);
		ResultSequence rs;
		try {
			rs = eval.evaluate(path);
		}
		catch (DynamicError e) {
			throw new RuntimeException("Evaluation error: " + e.getMessage(), e);
		}
		assertEquals("Expected single result from \'" + xpath + "\'", 1, rs.size());
		
		AnyType result = rs.first();
		assertTrue("Exected XPath result instanceof class " + resultClass.getSimpleName() + " from \'" + xpath + "\', got " + result.getClass(), resultClass.isInstance(result));
		
		return result;
	}


}
