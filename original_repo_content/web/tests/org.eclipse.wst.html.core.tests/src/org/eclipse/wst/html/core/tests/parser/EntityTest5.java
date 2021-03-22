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

public class EntityTest5 extends ModelTest {
	/**
	 * Constructor for EntityTest5.
	 * 
	 * @param name
	 */
	public EntityTest5(String name) {
		super(name);
	}

	public EntityTest5() {
		super();
	}

	public static void main(java.lang.String[] args) {
		new EntityTest5().testModel();
	}

	public void testModel() {
		IDOMModel model = createHTMLModel();
		try {
			Document document = model.getDocument();

			Element p = document.createElement("P");
			document.appendChild(p);
			Text text = document.createTextNode("a&b");
			p.appendChild(text);

			printSource(model);
			printTree(model);

			Text text2 = document.createTextNode("a&b");
			p.insertBefore(text2, text);

			printSource(model);
			printTree(model);

			saveAndCompareTestResults();
		}
		finally {
			model.releaseFromEdit();
		}


	}
}
