/*******************************************************************************
 * Copyright (c) 2008, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Carver (STAR) - bug 262046 - refactored launching suite.  
 *******************************************************************************/

package org.eclipse.wst.xsl.launching.tests;

import org.eclipse.wst.xsl.launching.tests.testcase.LaunchShortcutTest;
import org.eclipse.wst.xsl.launching.tests.testcase.ResultRunnableTest;
import org.eclipse.wst.xsl.launching.tests.testcase.InputFileBlockTest;
import org.eclipse.wst.xsl.launching.tests.testcase.XSLLaunchingTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This class does specifies all the classes in this bundle that provide tests.
 * It is primarily for the convenience of the AllTestsSuite.
 * 
 * @since 1.2
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ XSLLaunchingTest.class, InputFileBlockTest.class,
		LaunchShortcutTest.class, ResultRunnableTest.class })
public class LaunchingSuite {

}
