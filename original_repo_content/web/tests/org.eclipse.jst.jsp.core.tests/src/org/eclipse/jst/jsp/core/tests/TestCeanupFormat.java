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
package org.eclipse.jst.jsp.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.jsp.core.tests.cleanupformat.CleanupTester;
import org.eclipse.jst.jsp.core.tests.cleanupformat.FormatTester;


public class TestCeanupFormat extends TestSuite {
	public static Test suite() {
		return new TestCeanupFormat();
	}

	public TestCeanupFormat() {
		super("TestCeanupFormat");

		addTest(new TestSuite(CleanupTester.class, "CleanupTester"));
		addTest(new TestSuite(FormatTester.class, "FormatTester"));
	}
}
