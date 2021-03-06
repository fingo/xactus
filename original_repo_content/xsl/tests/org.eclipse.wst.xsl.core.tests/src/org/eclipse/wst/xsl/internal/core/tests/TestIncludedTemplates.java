/*******************************************************************************
 * Copyright (c) 2008, 2017 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.internal.core.tests;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

public class TestIncludedTemplates extends AbstractValidationTest {
	
	@Test
	public void test1() throws Exception {
		validate(getFile("style1.xsl"));
	}
	
	@Test
	public void testUnknownInclude() throws XPathExpressionException, CoreException, IOException {
		validate(getFile("missingInclude.xsl"));
	}

}
