package info.fingo.xactus.processor.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import info.fingo.xactus.api.DynamicContext;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.XPath2Expression;
import info.fingo.xactus.processor.DOMLoader;
import info.fingo.xactus.processor.Engine;
import info.fingo.xactus.processor.XercesLoader;
import info.fingo.xactus.processor.testutil.ResultSequenceFormatter;
import info.fingo.xactus.processor.testutil.bundle.Bundle;
import info.fingo.xactus.processor.testutil.bundle.Platform;
import info.fingo.xactus.processor.util.DynamicContextBuilder;
import info.fingo.xactus.processor.util.StaticContextBuilder;

public abstract class ConformanceTestAbstarct {
	
	private final Bundle bundle;
	
	public ConformanceTestAbstarct() {
		bundle = Platform
				.getBundle("org.w3c.xqts.testsuite");
	}
	
	protected String getExpectedResult(String resultFile) throws IOException {
		
		try (InputStream isrf = bundle.getEntry(resultFile).openStream();
				 BufferedReader rfreader = new BufferedReader(new InputStreamReader(isrf, "UTF-8"))) {
			
			int bufferLength = 40000;
			char[] cbuf = new char[bufferLength];
			rfreader.read(cbuf);
			
			return new String(cbuf).trim();
		}
	}

	private Document loadInputFile(String inputFile) throws IOException {

		try (InputStream is = bundle.getEntry(inputFile).openStream()) {

			DOMLoader domloader = new XercesLoader();
			domloader.set_validating(false);
			return domloader.load(is);
		}
	}

	protected String buildResultString(ResultSequence rs) {
		return ResultSequenceFormatter.buildResultString(rs);
	}
	
	protected String buildXMLResultString(ResultSequence rs) throws Exception {
		return ResultSequenceFormatter.buildXMLResultString(rs);
	}
	
	protected ResultSequence evaluate(String inputFile, String xpath) {
		return evaluate(inputFile, xpath);
	}

	protected ResultSequence evaluate(String inputFile, String xpath, int context) throws IOException {

		StaticContextBuilder staticContextBuilder = new StaticContextBuilder();
		staticContextBuilder.withNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		staticContextBuilder.withNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
		staticContextBuilder.withNamespace("fn", "http://www.w3.org/2005/xpath-functions");
		staticContextBuilder.withNamespace("xml", "http://www.w3.org/XML/1998/namespace");
		
		XPath2Expression xpathExp = new Engine().parseExpression(xpath, staticContextBuilder);

		DynamicContext dynamicContextBuilder = new DynamicContextBuilder(staticContextBuilder);

		final Document document = loadInputFile(inputFile);
		final Node contextNode;
		if (context > 0) {
			Element elem = document.getDocumentElement();
			for (int i = 1; i < context; i++) {
				elem = findFirstChildElement(elem);
			}
			contextNode = elem;
		} else {
			contextNode = document;
		}
		return xpathExp.evaluate(dynamicContextBuilder, new Object[] { contextNode });
	}

	private static Element findFirstChildElement(Element elem) {

		NodeList childNodes = elem.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i) instanceof Element)
				return (Element) childNodes.item(i);
		}
		return null;
	}

}
