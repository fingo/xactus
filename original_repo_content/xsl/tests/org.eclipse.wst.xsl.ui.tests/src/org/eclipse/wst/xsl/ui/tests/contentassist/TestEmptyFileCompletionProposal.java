/*******************************************************************************
 *Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others.
 *All rights reserved. This program and the accompanying materials
 *are made available under the terms of the Eclipse Public License 2.0
 *which accompanies this distribution, and is available at
 https://www.eclipse.org/legal/epl-2.0/
 *
 *SPDX-License-Identifier: EPL-2.0
 *
 *Contributors:
 *    David Carver (STAR) - bug 263843 - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.ui.tests.contentassist;

import java.io.File;
import static org.junit.Assert.*;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.xsl.ui.tests.AbstractSourceViewerTest;
import org.junit.Test;

public class TestEmptyFileCompletionProposal extends AbstractSourceViewerTest {

	@Test
	public void testXSLPropsoalAvailable() throws Exception {
		fileName = "EmptyXSLFile.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		int offset = 0;

		ICompletionProposal[] proposals = getProposals(offset);
		assertNotNull("Did not find proposals.", proposals);
	}

}
