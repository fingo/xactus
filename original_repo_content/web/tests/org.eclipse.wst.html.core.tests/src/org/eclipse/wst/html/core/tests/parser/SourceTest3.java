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

import org.eclipse.wst.xml.core.internal.document.InvalidCharacterException;
import org.eclipse.wst.xml.core.internal.document.SourceValidator;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SourceTest3 extends ModelTest {
	/**
	 * Constructor for SourceTest3.
	 * 
	 * @param name
	 */
	public SourceTest3(String name) {
		super(name);
	}

	public SourceTest3() {
		super();
	}

	public static void main(java.lang.String[] args) {
		new SourceTest3().testModel();
	}

	public void testModel() {
		IDOMModel model = createXMLModel();
		try {
			Document document = model.getDocument();

			Element a = document.createElement("a");
			Attr b = document.createAttribute("b");
			a.setAttributeNode(b);
			document.appendChild(a);
			SourceValidator validator = new SourceValidator(b);

			String source = null;
			String result = null;

			source = "<% aaa %><%= bbb %>/<%! ccc %>ddd";
			fOutputWriter.writeln("source: " + source);
			try {
				validator.validateSource(source);
			}
			catch (InvalidCharacterException ex) {
				fOutputWriter.writeln(ex.getMessage());
			}
			result = validator.convertSource(source);
			fOutputWriter.writeln("result: " + result);

			source = "<% aaa";
			fOutputWriter.writeln("source: " + source);
			try {
				validator.validateSource(source);
			}
			catch (InvalidCharacterException ex) {
				fOutputWriter.writeln(ex.getMessage());
			}
			result = validator.convertSource(source);
			fOutputWriter.writeln("result: " + result);

			source = ">< % aaa %>";
			fOutputWriter.writeln("source: " + source);
			try {
				validator.validateSource(source);
			}
			catch (InvalidCharacterException ex) {
				fOutputWriter.writeln(ex.getMessage());
			}
			result = validator.convertSource(source);
			fOutputWriter.writeln("result: " + result);

			saveAndCompareTestResults();

		}
		finally {
			model.releaseFromEdit();
		}

	}
}
