/*******************************************************************************
 * Copyright (c) 2008, 2019 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *     Jesper S Moller - bug 406262 - StackOverflow when checking for circular includes on next level import
 *******************************************************************************/

package org.eclipse.wst.xsl.internal.model.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.xsl.core.XSLCore;
import org.eclipse.wst.xsl.core.internal.model.StylesheetBuilder;
import org.eclipse.wst.xsl.core.model.StylesheetModel;
import org.eclipse.wst.xsl.core.model.Template;
import org.eclipse.wst.xsl.core.model.XSLAttribute;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestStylesheetModel extends AbstractModelTest {
    private StylesheetModel model = null;
	public TestStylesheetModel() {
		
	}
	
	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		StylesheetBuilder builder = StylesheetBuilder.getInstance();
		builder.release();
		model = null;
	}

	@Test
	public void testStyleSheetModel() {
		
		model = XSLCore.getInstance().getStylesheet(getFile("globalVariablesTest.xsl"));
		assertNotNull("Failed to load stylesheet 'globalVariablesTest.xsl'.", model);
		model = XSLCore.getInstance().getStylesheet(getFile("style1.xsl"));
		assertNotNull("Failed to load stylesheet 'style1.xsl'.", model);
		model = XSLCore.getInstance().getStylesheet(getFile("XSLT20Test.xsl"));
		assertNotNull("Failed to load stylesheet 'XSLT20Test.xsl'.", model);
		model = XSLCore.getInstance().getStylesheet(getFile("circularref.xsl"));
		assertNotNull("Failed to load stylesheet 'circularref.xsl'.", model);
		model = XSLCore.getInstance().getStylesheet(getFile("modeTest.xsl"));
		assertNotNull("Failed to load stylesheet 'modeTest.xsl'.", model);
		
	}
	
	@Test
	public void testFindAvailableTemplateModes() {
		ArrayList<String> modes = new ArrayList();
		model = XSLCore.getInstance().getStylesheet(getFile("modeTest.xsl"));
		List<Template> templates = model.getTemplates();
		assertTrue("No templates returned.", templates.size() > 0);
		
		for (Template template : templates) {
			XSLAttribute attribute = template.getAttribute("mode");
			if (attribute != null) {
				if (modes.indexOf(attribute.getValue()) == -1 ) {
					modes.add(attribute.getValue());
				}
			}
		}
		assertEquals("Wrong number of mode templates returned.", 3, modes.size());
	}
	
	@Test
	public void testCircularReference() {
		model = XSLCore.getInstance().getStylesheet(getFile("circularref.xsl"));
		assertTrue("Undetected circular reference", model.hasCircularReference());
	}

	@Test
	public void testStackOverflow() {
		model = XSLCore.getInstance().getStylesheet(getFile("stackOverflowInclude.xsl"));
		assertTrue("Undetected circular reference", model.hasCircularReference());
	}

	@Test
	public void testNoCircularReference() {
		model = XSLCore.getInstance().getStylesheet(getFile("modeTest.xsl"));
		assertFalse("Undetected circular reference", model.hasCircularReference());
	}	
	
	@Test
	public void testLoadStylesheetWithEXSLTFuncs() throws Exception {
		model = XSLCore.getInstance().getStylesheet(getFile("exsltfunctionparm.xsl"));
	}
	
	@Test
	public void testNPEOnParmParsing() throws Exception {
		model = XSLCore.getInstance().getStylesheet(getFile("exsltfunctionparm.xsl"));
	}

}
