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
package org.eclipse.wst.dtd.ui.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.dtd.ui.tests.plugin.TestPluginXMLRequirements;
import org.eclipse.wst.dtd.ui.tests.viewer.TestViewerConfigurationDTD;



public class DTDUITestSuite extends TestSuite {
	public static Test suite() {
		return new DTDUITestSuite();
	}

	public DTDUITestSuite() {
		super("DTD UI TestSuite");
		addTest(new TestSuite(VerifyUIPlugin.class));
		addTest(new TestSuite(DTDUIPreferencesTest.class));
		addTest(new TestSuite(TestViewerConfigurationDTD.class));
		addTest(new TestSuite(TestEditorConfigurationDTD.class));
		addTestSuite(TestPluginXMLRequirements.class);
		// [299645] Disabled for failing unit tests on build machine - Problem with test
		// addTest(DTDCodeFoldingTest.suite());
	}
}
