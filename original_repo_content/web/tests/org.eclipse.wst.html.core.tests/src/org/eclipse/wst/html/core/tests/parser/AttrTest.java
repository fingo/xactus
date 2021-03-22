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

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

public class AttrTest extends ModelTest {
	/**
	 * Constructor for AttrTest.
	 * 
	 * @param name
	 */
	public AttrTest(String name) {
		super(name);
	}

	public AttrTest() {
		super();
	}

	public static void main(java.lang.String[] args) {
		new AttrTest().testModel();
	}

	public void testModel() {
		IDOMModel model = createHTMLModel();
		try {
			IStructuredDocument structuredDocument = model.getStructuredDocument();
			//Document document = model.getDocument();

			structuredDocument.setText(this, "<a href=\"<%=c%>\">a</a>");

			printSource(model);
			printTree(model);
			saveAndCompareTestResults();
		}
		finally {
			model.releaseFromEdit();
		}
	}
}
