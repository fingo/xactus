/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
package org.eclipse.text.tests;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Test Suite for org.eclipse.text.
 *
 * @since 3.0
 */
public class EclipseTextTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("org.eclipse.text Test Suite using JSP BasicStructuredDocument"); //$NON-NLS-1$
		//$JUnit-BEGIN$
//		suite.addTest(LineTrackerTest4.suite());
//		suite.addTest(DocumentExtensionTest.suite());
//		suite.addTest(LineTrackerTest3.suite());
//		suite.addTest(DocumentTest.suite());
//		suite.addTest(FindReplaceDocumentAdapterTest.suite());
		suite.addTest(PositionUpdatingCornerCasesTest.suite());
		suite.addTest(ExclusivePositionUpdaterTest.suite());
//		suite.addTest(TextEditTests.suite());
//		suite.addTest(GapTextTest.suite());
//		suite.addTest(AdaptiveGapTextTest.suite());
//		suite.addTest(GapTextStoreTest.suite());
//		suite.addTest(ChildDocumentTest.suite());
//		suite.addTest(ProjectionTestSuite.suite());
//		suite.addTest(LinkTestSuite.suite());
//		suite.addTest(CopyOnWriteTextStoreTest.suite());
//		suite.addTest(TextUtilitiesTest.suite());
//		suite.addTest(AnnotationModelStressTest.suite());
//		suite.addTest(AnnotationModelExtension2Test.suite());
//		suite.addTest(TemplatesTestSuite.suite());
		//$JUnit-END$

		return suite;
	}
}
