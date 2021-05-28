/*******************************************************************************
 * Copyright (c) 2009, 2018 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package info.fingo.xactus.processor.testsuite;

import info.fingo.xactus.processor.testsuite.core.AllW3CCoreTests;
import info.fingo.xactus.processor.testsuite.dates.AllW3CDateTests;
import info.fingo.xactus.processor.testsuite.dates.DateEdgeCasesTest;
import info.fingo.xactus.processor.testsuite.functions.AllW3CFunctionTests;
import info.fingo.xactus.processor.testsuite.numeric.AllW3CNumericTests;
import info.fingo.xactus.processor.testsuite.schema.AllW3CSchemaTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllW3CXPath20Tests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"All W3C XPath 2.0 Test Suites");
		//$JUnit-BEGIN$
		suite.addTest(AllW3CCoreTests.suite());
		suite.addTest(AllW3CDateTests.suite());
		suite.addTest(AllW3CFunctionTests.suite());
		suite.addTest(AllW3CNumericTests.suite());
		suite.addTest(AllW3CSchemaTests.suite());
		suite.addTestSuite(DateEdgeCasesTest.class);
		//$JUnit-END$
		return suite;
	}

}
