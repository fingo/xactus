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
 *     David Carver - STAR - bug 244978 - intial API and implementation
 *******************************************************************************/

package org.eclipse.wst.xsl.ui.tests.contentassist;

import java.io.File;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.xsl.ui.tests.AbstractSourceViewerTest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests everything about code completion and code assistance.
 * 
 */
public class TestXPathXMLCompletionProposal extends AbstractSourceViewerTest {

	@Test
	public void testProposalsIncludeXSD() throws Exception {
		fileName = "TestXPathXMLProposals.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		ICompletionProposal[] proposals = getProposals(5, 29);
		assertNotNull("Did not find proposals.", proposals);

		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i].getDisplayString().contains("xsd:")) {
				return;
			}
		}
		fail("Did not find XSD proposals.");
	}

	@Test
	public void testProposalsIncludeAfterColon() throws Exception {
		fileName = "TestXPathXMLProposals.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		ICompletionProposal[] proposals = getProposals(11, 44);
		assertNotNull("Did not find proposals.", proposals);

		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i].getDisplayString().contains("xsd:")) {
				return;
			}
		}
		fail("Did not find XSD proposals.");
	}

	@Test
	public void testProposalsIncludeXSDAfterForwardSlash() throws Exception {
		fileName = "TestXPathXMLProposals.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		ICompletionProposal[] proposals = getProposals(8, 41);
		assertNotNull("Did not find proposals.", proposals);

		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i].getDisplayString().contains("xsd:")) {
				return;
			}
		}
		fail("Did not find XSD proposals.");
	}

	@Test
	public void testBug337649() throws Exception {
		fileName = "TestXPathXMLProposals.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		ICompletionProposal[] proposals = getProposals(17, 20);
		assertNotNull("Did not find proposals.", proposals);
	}

	@Test
	public void testTestAttributeProposal() throws Exception {
		fileName = "TestTestAttributeProposals.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		ICompletionProposal[] proposals = getProposals(5, 25);
		assertNotNull("Did not find proposals.", proposals);

		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i].getDisplayString().contains("document")) {
				return;
			}
		}
		fail("Did not find XPath proposals for the test attribute.");
	}

	@Test
	public void testCurlyBraceProposal() throws Exception {
		fileName = "bug294079.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);

		ICompletionProposal[] proposals = getProposals(5, 16);
		assertNotNull("Did not find proposals.", proposals);

		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i].getDisplayString().contains("document")) {
				return;
			}
		}
		fail("Did not find XPath proposals for the test attribute.");
	}
}