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
package org.eclipse.wst.xsl.ui.tests.contentassist;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xsl.ui.tests.AbstractSourceViewerTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestHrefCompletionProposal extends AbstractSourceViewerTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Ignore
	@Test
	public void testHrefProposalsAtStart() throws Exception {
		fileName = projectName + File.separator + "hrefs" + File.separator
				+ "mainFile.xsl";
		loadFileForTesting(fileName);
		IStructuredDocument document = (IStructuredDocument) sourceViewer
				.getDocument();
		// Column is off by one when calculating for the offset position
		int column = 25;
		int line = 16;

		int offset = document.getLineOffset(line) + column;

		ICompletionProposal[] proposals = getProposals(offset);
		assertTrue("Incorrect number of proposals", proposals.length > 3);
		doCommonTests(proposals);
	}

	private void doCommonTests(ICompletionProposal[] proposals) {
		int currDepth = 1;
		for (ICompletionProposal completionProposal : proposals) {
			System.out.println(completionProposal.getDisplayString());
			IPath p = new Path(completionProposal.getDisplayString());
			assertNotSame("Stylesheet must not include itself", new Path(
					"mainFile.xsl"), p);
			assertTrue(
					"Proposals wrongly ordered - number of segments should increase down the list",
					p.segmentCount() >= currDepth);
			currDepth = p.segmentCount();
		}
	}

}