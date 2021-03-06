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
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextTest2 extends ModelTest {
	/**
	 * Constructor for TextTest2.
	 * 
	 * @param name
	 */
	public TextTest2(String name) {
		super(name);
	}

	public TextTest2() {
		super();
	}

	public static void main(java.lang.String[] args) {
		new TextTest2().testModel();
	}

	public void testModel() {
		IDOMModel model = createXMLModel();
		try {
			Document document = model.getDocument();

			Element a = document.createElement("a");
			document.appendChild(a);
			CharacterData text = document.createTextNode("text");
			a.appendChild(text);

			text.setNodeValue("hello &lt;");

			printSource(model);
			printTree(model);

			fOutputWriter.writeln(text.getNodeValue());

			saveAndCompareTestResults();
		}
		finally {
			model.releaseFromEdit();
		}


	}
}
