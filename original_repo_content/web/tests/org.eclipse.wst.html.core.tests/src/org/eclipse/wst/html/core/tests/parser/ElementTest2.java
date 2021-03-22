/*******************************************************************************
 * Copyright (c) 2004, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.html.core.tests.parser;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ElementTest2 extends ModelTest {
	/**
	 * Constructor for ElementTest2.
	 * 
	 * @param name
	 */
	public ElementTest2(String name) {
		super(name);
	}

	public ElementTest2() {
		super();
	}

	public static void main(java.lang.String[] args) {
		new ElementTest2().testModel();
	}

	public void testModel() {
		IDOMModel model = createXMLModel();
		try {
			Document document = model.getDocument();

			Element a = document.createElement("a");
			Element b = document.createElement("b");
			Text t = document.createTextNode("c");
			document.appendChild(a);
			a.appendChild(b);
			b.appendChild(t);

			printSource(model);
			printTree(model);

			a.setPrefix("x");

			fOutputWriter.writeln("a.tagName=" + a.getTagName());

			printSource(model);
			printTree(model);

			b.setPrefix("y");

			printSource(model);
			printTree(model);

			saveAndCompareTestResults();
		}
		finally {
			model.releaseFromEdit();
		}

	}
}
