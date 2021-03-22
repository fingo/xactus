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

public class SplitTextTest8 extends ModelTest {
	/**
	 * Constructor for SplitTextTest8.
	 * 
	 * @param name
	 */
	public SplitTextTest8(String name) {
		super(name);
	}

	public SplitTextTest8() {
		super();
	}

	public static void main(java.lang.String[] args) {
		new SplitTextTest8().testModel();
	}

	public void testModel() {
		IDOMModel model = createHTMLModel();
		try {
			Document document = model.getDocument();

			Element p = document.createElement("P");
			document.appendChild(p);
			Text text = document.createTextNode("aaaa");
			Text text2 = document.createTextNode("bbbb");
			p.appendChild(text);
			p.appendChild(text2);

			printSource(model);
			printTree(model);


			// delete accross node boundry
			model.getStructuredDocument().replaceText(this, 4, 6, "");

			printSource(model);
			printTree(model);

			saveAndCompareTestResults();

		}
		finally {
			model.releaseFromEdit();
		}

	}
}
