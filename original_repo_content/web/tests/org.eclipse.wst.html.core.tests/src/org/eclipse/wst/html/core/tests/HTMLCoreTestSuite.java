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
package org.eclipse.wst.html.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.html.core.tests.cleanup.TestHTMLCleanupProcessor;
import org.eclipse.wst.html.core.tests.contenttype.TestHTMLResourceEncodingDetector;
import org.eclipse.wst.html.core.tests.format.TestFormatProcessorHTML;
import org.eclipse.wst.html.core.tests.format.TestFormatUtility;
import org.eclipse.wst.html.core.tests.html5.model.HTML5ContentModelTest;
import org.eclipse.wst.html.core.tests.misc.HTMLCorePreferencesTest;
import org.eclipse.wst.html.core.tests.misc.HTMLTagInfoTest;
import org.eclipse.wst.html.core.tests.model.BUG124835SetStyleAttributeValueTest;
import org.eclipse.wst.html.core.tests.model.GetOverrideStyleTest;
import org.eclipse.wst.html.core.tests.model.ModelModifications;
import org.eclipse.wst.html.core.tests.model.TestCSS;
import org.eclipse.wst.html.core.tests.model.TestCatalogContentModels;
import org.eclipse.wst.html.core.tests.model.TestForNPEInCSSCreation;



public class HTMLCoreTestSuite extends TestSuite {

	/**
	 * to get picked up by BVT
	 * 
	 * @return
	 */
	public static Test suite() {
		return new HTMLCoreTestSuite();
	}

	public HTMLCoreTestSuite() {
		super("HTML Core TestSuite");

		addTest(ModelParserTests.suite());
		addTest(new TestSuite(TestCSS.class));
		addTest(new TestSuite(HTMLCorePreferencesTest.class));
		addTest(new TestSuite(HTMLTagInfoTest.class));
		addTest(new TestSuite(ModelModifications.class));
		addTest(new TestSuite(TestForNPEInCSSCreation.class));
		addTest(new TestSuite(GetOverrideStyleTest.class));
		addTest(new TestSuite(BUG124835SetStyleAttributeValueTest.class));
		addTest(new TestSuite(TestFormatProcessorHTML.class));
		addTest(new TestSuite(TestFormatUtility.class));
		addTest(new TestSuite(TestCatalogContentModels.class));
		addTest(TestHTMLCleanupProcessor.suite());
		addTest(new TestSuite(HTML5ContentModelTest.class));
		addTest(new TestSuite(TestHTMLResourceEncodingDetector.class));
	}
}