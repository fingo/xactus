/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.jst.jsp.core.tests.dom;

import junit.framework.TestCase;

import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.parser.ContextRegionContainer;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AttrValueTest extends TestCase {

	private static final String[] VALUES = {"<<Previous", "<page>", "Next>>"};

	public void testAttributeValue() {
		IDOMModel model = (IDOMModel) StructuredModelManager.getModelManager().createUnManagedStructuredModelFor("org.eclipse.jst.jsp.core.jspsource");

		assertNotNull(model);
		IStructuredDocument document = model.getStructuredDocument();
		assertNotNull(document);

		document.setText(this, "<button value=\""+ VALUES[0] +"\"></button><button value=\"" + VALUES[1] + "\"></button><button value=\"" + VALUES[2] + "\"></button>");

		IDOMDocument dom = model.getDocument();
		NodeList nodes = dom.getElementsByTagName("button");
		assertTrue("Must be 3 button elements in the document.", nodes.getLength() == 3);

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			Node attr = node.getAttributes().getNamedItem("value");
			assertTrue("Attribute 'value' not present.", attr != null && attr.getNodeValue().length() > 0);
			assertEquals("Attribute values are not equal", VALUES[i], attr.getNodeValue()); 
		}
	}

	public void testEmbeddedTagValue() {
		IDOMModel model = (IDOMModel) StructuredModelManager.getModelManager().createUnManagedStructuredModelFor("org.eclipse.jst.jsp.core.jspsource");

		assertNotNull(model);
		IStructuredDocument document = model.getStructuredDocument();
		assertNotNull(document);

		document.setText(this, "<a href=\"<c:out value='test.html'></c:out>\">Test</a>");

		IDOMDocument dom = model.getDocument();
		NodeList nodes = dom.getElementsByTagName("a");
		assertTrue("Must be 1 anchor element in the document.", nodes.getLength() == 1);
		IStructuredDocumentRegion region = document.getFirstStructuredDocumentRegion();
		ITextRegion embeddedRegion = region.getRegionAtCharacterOffset(9);
		assertTrue("The attribute is not a ContextRegionContainer", embeddedRegion instanceof ContextRegionContainer);
		Node node = nodes.item(0);
		Node attr = node.getAttributes().getNamedItem("href");
		assertEquals("Attribute values are not equal", "<c:out value='test.html'></c:out>", attr.getNodeValue());
	}

}
