/*******************************************************************************
 * Copyright (c) Standards for Technology in Automotive Retail and others
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     David Carver - STAR - intial API and implementation
 *******************************************************************************/

package org.eclipse.wst.xsl.ui.tests.contentassist;

import java.io.File;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsl.ui.tests.AbstractSourceViewerTest;

/**
 * Tests everything about code completion and code assistance.
 * 
 */
public class XSLCompletionTest extends AbstractSourceViewerTest {

	public XSLCompletionTest() {
	}

	public void testGetNodeAtLine15() throws Exception {
	
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		
		try {
		IDOMNode node = (IDOMNode) ContentAssistUtils.getNodeAt(sourceViewer,
				sourceViewer.getDocument().getLineOffset(14)+1);
		assertEquals("Wrong node name returned:", "xsl:stylesheet", node
				.getNodeName());
		} finally {
			model.releaseFromEdit();
		}

	}

	public void testGetNodeAtLine16() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		
		try {

		IDOMNode node = (IDOMNode) ContentAssistUtils.getNodeAt(sourceViewer,
				sourceViewer.getDocument().getLineOffset(15)+11);
		assertEquals("Wrong node name returned:", "xsl:template", node
				.getNodeName());
		} finally {
			model.releaseFromEdit();
		}
	}

	public void testGetNodeAtLine17() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		try {
		IDOMNode node = (IDOMNode) ContentAssistUtils.getNodeAt(sourceViewer,
				sourceViewer.getDocument().getLineOffset(16)+14);
		assertEquals("Wrong node name returned:", "xsl:param", node
				.getNodeName());
		} finally {
			model.releaseFromEdit();
		}
	}

	public void testAttributeNotValueAvailable() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		try {
			ICompletionProposal[] proposals = getProposals(838);

			assertTrue(proposals.length > 1);
			ICompletionProposal proposal = proposals[0];
			assertFalse("Found \"number(substring($date, 6, 2))\".", proposal
					.getDisplayString().equals(
							"\"number(substring($date, 6, 2))\""));
		} finally {
			model.releaseFromEdit();
		}
	}

	public void testSelectAttributeProposalsAvailable() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		try {
			int offset = sourceViewer.getDocument().getLineOffset(18) + 44;
			String s = sourceViewer.getDocument().get(offset - 1, 6);
			assertEquals("number", s);

			ICompletionProposal[] proposals = getProposals(18,43);

			assertTrue(proposals.length > 1);
			ICompletionProposal proposal = proposals[3];
			assertEquals("Wrong select item returned: ", "..", proposal
					.getDisplayString());
		} finally {
			model.releaseFromEdit();
		}
	}

	/**
	 * Bug 240170
	 * 
	 * @throws Exception
	 */
	public void testSelectAttributeProposalsNarrow() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		try {
			int offset = sourceViewer.getDocument().getLineOffset(18) + 44;
			String s = sourceViewer.getDocument().get(offset - 9, 9);
			assertEquals("select=\"n", s);

			ICompletionProposal[] proposals = getProposals(offset);
			assertEquals("Wrong xpath item returned: ", "name(node-set)",
					proposals[0].getDisplayString());
			assertEquals("Wrong Number of items returned: ", 6,
					proposals.length);
		} finally {
			model.releaseFromEdit();
		}
	}

	public void testTestAttributeProposalsAvailable() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		try {
			ICompletionProposal[] proposals = getProposals(37,18);
			assertTrue(proposals.length >= 1);
			ICompletionProposal proposal = proposals[0];
			assertTrue("Wrong attribute proposal returned:", proposal
					.getDisplayString().contains("disable-output-escaping"));
		} finally {
			model.releaseFromEdit();
		}
	}

	public void testXSLElementProposalsAvailable() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		try {
			ICompletionProposal[] proposals = getProposals(31,58);
			assertTrue(proposals.length >= 2);

			ICompletionProposal proposal = proposals[1];
			assertTrue("Can't find XSL element proposals.", proposal
					.getDisplayString().equals("xsl:otherwise"));
		} finally {
			model.releaseFromEdit();
		}
	}
	
	/*
	 * Bug 259575
	 */
	public void testXPathProposalAvaialbleAfterComma() throws Exception {
		fileName = "utils.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		try {
			ICompletionProposal[] proposals = getProposals(861);
			assertTrue(proposals.length > 0);

		} finally {
			model.releaseFromEdit();
		}
		
	}

}