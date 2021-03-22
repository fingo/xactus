/*******************************************************************************
 * Copyright (c) 2008, 2018 IBM Corporation and others.
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
/*******************************************************************************
 * Copyright (c) Standards for Technology in Automotive Retail and others
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     David Carver - STAR - bug 230136 - intial API and implementation
 *******************************************************************************/

package org.eclipse.wst.xsl.ui.tests.contentassist;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.sse.core.internal.provisional.exceptions.ResourceAlreadyExists;
import org.eclipse.wst.sse.core.internal.provisional.exceptions.ResourceInUse;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xsl.ui.tests.AbstractSourceViewerTest;
import org.junit.Test;

/**
 * Tests everything about code completion and code assistance.
 * 
 */
public class TestExcludeResultPrefixesCompletionProposal extends
		AbstractSourceViewerTest {

	private ICompletionProposal[] setupProposals(int column, int line)
			throws ResourceAlreadyExists, ResourceInUse, IOException,
			CoreException, BadLocationException, Exception {
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		IStructuredDocument document = (IStructuredDocument) sourceViewer
				.getDocument();
		// Column is off by one when calculating for the offset position

		int offset = document.getLineOffset(line) + column;

		ICompletionProposal[] proposals = getProposals(offset);
		return proposals;
	}

	@Test
	public void testAllDefaultValueNoProposals() throws Exception {
		fileName = "TestResultPrefixes.xsl";
		int column = 29;
		int line = 2;
		ICompletionProposal[] proposals = setupProposals(column, line);
		assertEquals("Found proposals when #all already in result value.", 0,
				proposals.length);
	}

	@Test
	public void testXHTMLNamespacePropsoalAvailable() throws Exception {
		fileName = "TestResultPrefixesEmpty.xsl";
		int column = 29;
		int line = 3;
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		IStructuredDocument document = (IStructuredDocument) sourceViewer
				.getDocument();

		ICompletionProposal[] proposals = getProposals(line, column);
		assertNotNull("Did not find proposals.", proposals);
		assertEquals("Proposal length not 2.", 2, proposals.length);
		assertEquals("Proposal did not find xhtml as proposal value.", "xhtml",
				proposals[1].getDisplayString());
	}

	@Test
	public void testAllPropsoalAvailable() throws Exception {
		fileName = "TestResultPrefixesEmpty.xsl";
		int column = 29;
		int line = 3;
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		IStructuredDocument document = (IStructuredDocument) sourceViewer
				.getDocument();

		ICompletionProposal[] proposals = getProposals(line, column);
		assertNotNull("Did not find proposals.", proposals);
		assertEquals("Proposal length not 2.", 2, proposals.length);
		assertEquals("Proposal did not find xhtml as proposal value.", "#all",
				proposals[0].getDisplayString());
	}

	@Test
	public void testExcludeXHTMLProposal() throws Exception {
		fileName = "TestResultPrefixesWithXhtml.xsl";
		int column = 35;
		int line = 2;

		ICompletionProposal[] proposals = setupProposals(column, line);
		assertNotNull("Did not find proposals.", proposals);

		for (int cnt = 0; cnt < proposals.length; cnt++) {
			if (proposals[cnt].getDisplayString().equals("xhtml")) {
				fail("XHTML Proposal found, when it should not have been!");
			}
		}
	}

	@Test
	public void testTestProposal() throws Exception {
		fileName = "TestResultPrefixesWithXhtml.xsl";
		int column = 35;
		int line = 3;
		ICompletionProposal[] proposals = setupProposals(column, line);
		assertNotNull("Did not find proposals.", proposals);
		assertFalse("Proposals returned more than one.", proposals.length > 1);
		assertEquals("Did not find test in proposal list", "test", proposals[0]
				.getDisplayString());
	}
}