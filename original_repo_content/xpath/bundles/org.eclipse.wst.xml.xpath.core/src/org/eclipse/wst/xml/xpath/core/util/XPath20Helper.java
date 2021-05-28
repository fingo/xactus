/*******************************************************************************
 * Copyright (c) 2009, 2011 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver (STAR) - bug 226245 - initial API and implementation
 *     Jesper Steen Moller - bug 343804 - Updated API information
 *     Jesper Steen Moller - bug 348737 - Hook up default namespace from context
 *******************************************************************************/

package org.eclipse.wst.xml.xpath.core.util;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpressionException;

import info.fingo.xactus.api.XPath2Expression;
import info.fingo.xactus.processor.Engine;
import info.fingo.xactus.processor.JFlexCupParser;
import info.fingo.xactus.processor.StaticError;
import info.fingo.xactus.processor.XPathParser;
import info.fingo.xactus.processor.XPathParserException;
import info.fingo.xactus.processor.util.DynamicContextBuilder;
import info.fingo.xactus.processor.util.StaticContextBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @since 1.0
 */
public class XPath20Helper {

	public XPath20Helper() {
	}

	public static void compile(String xpathExp) throws XPathExpressionException {
		try {
			XPathParser xpathParser = new JFlexCupParser();
			xpathParser.parse(xpathExp);
		} catch (XPathParserException ex) {
			throw new XPathExpressionException(ex.getMessage());
		}
	}
	/**
	 * @since 1.2
	 */
	public static class XPath2Engine implements SimpleXPathEngine {

		private NamespaceContext namespaceContext;
		private StaticContextBuilder staticContextBuilder = new StaticContextBuilder() {
			@Override
			public NamespaceContext getNamespaceContext() {
				return namespaceContext;
			}
			public String getDefaultNamespace() {
				return namespaceContext.getNamespaceURI(XMLConstants.DEFAULT_NS_PREFIX);
			};
		};
		private XPath2Expression xPathExpression;

		public void parse(String expression) throws XPathExpressionException {
			xPathExpression = null;
			try {
				xPathExpression = new Engine().parseExpression(expression, staticContextBuilder);
			} catch (StaticError se) {
				throw new XPathExpressionException(se.getMessage() + " (" + se.code() + ")");
			}
		}

		public boolean isValid() {
			return xPathExpression != null;
		}

		public NodeList execute(Node contextNode) {
			DynamicContextBuilder dynContext = new DynamicContextBuilder(staticContextBuilder);
			 info.fingo.xactus.api.ResultSequence rs = xPathExpression.evaluate(dynContext, new Object[] { contextNode });
			 return new NodeListImpl(rs);
		}

		public void setNamespaceContext(NamespaceContext namespaceContext) {
			this.namespaceContext = namespaceContext;
//			if (namespaceContext != null) {
//				namespaceContext.dc.add_namespace("xs", "http://www.w3.org/2001/XMLSchema");
		}
	}

}
