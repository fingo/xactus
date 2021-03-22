/*******************************************************************************
 * Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.exslt.ui.internal.contentassist.test;

import java.io.File;
import static org.junit.Assert.*;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.xsl.exslt.ui.internal.contentassist.EXSLTCommonContentAssistProcessor;
import org.eclipse.wst.xsl.exslt.ui.tests.EXSLTUITestsPlugin;
import org.eclipse.wst.xsl.ui.tests.AbstractSourceViewerTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EXSLTCommonContentAssistTest extends AbstractSourceViewerTest {

	@Override
	@Before
	public void setUp() throws Exception {
		if (!fTestProjectInitialized) {
			setupTestProjectFiles(EXSLTUITestsPlugin.PLUGIN_ID);

			fTestProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			fTestProjectInitialized = true;
		}
	}
	
	@After
	@Override
	public void tearDown() {
		
	}

	@Test
	public void testDocumentElementPropsoalAvailable() throws Exception {
		fileName = "commonElements.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		try {
			ICompletionProposal[] proposals = getXMLProposals(5, 9);
			assertNotNull("Did not find proposals.", proposals);

			for (ICompletionProposal proposal : proposals) {
				if (proposal.getDisplayString().equals("common:document")) {
					return;
				}
			}
		} finally {
			model.releaseFromEdit();
		}

		fail("Did not find EXSLT Commond document element in proposal list.");
	}

	@Test
	public void testNodeSetFunctionExists() throws Exception {
		fileName = "commonXpathFunctions.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		try {
			ICompletionProposal[] proposals = getProposals(5, 23);
			assertNotNull("Did not find proposals.", proposals);

			for (ICompletionProposal proposal : proposals) {
				if (proposal.getDisplayString().equals("common:node-set( )")) {
					return;
				}
			}
		} finally {
			model.releaseFromEdit();
		}

		fail("Did not find EXSLT Common node-set function in test attribute proposal list.");

	}

	@Test
	public void testNodeSetFunctionExistsSelect() throws Exception {
		fileName = "commonXpathFunctions.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		try {
			ICompletionProposal[] proposals = getProposals(8, 31);
			assertNotNull("Did not find proposals.", proposals);

			for (ICompletionProposal proposal : proposals) {
				if (proposal.getDisplayString().equals("common:node-set( )")) {
					return;
				}
			}
		} finally {
			model.releaseFromEdit();
		}

		fail("Did not find EXSLT Common node-set function in select attribute proposal list.");

	}

	@Test
	public void testObjectTypeFunctionExistsSelect() throws Exception {
		fileName = "commonXpathFunctions.xsl";
		String xslFilePath = projectName + File.separator + fileName;
		loadFileForTesting(xslFilePath);
		try {
			ICompletionProposal[] proposals = getProposals(8, 31);
			assertNotNull("Did not find proposals.", proposals);

			for (ICompletionProposal proposal : proposals) {
				if (proposal.getDisplayString().equals("common:object-type( )")) {
					return;
				}
			}
		} finally {
			model.releaseFromEdit();
		}

		fail("Did not find EXSLT Common node-set function in select attribute proposal list.");

	}

	@Override
	protected ICompletionProposal[] getProposals(int lineNumber,
			int numberOfCharacters) throws BadLocationException {
		int offset = calculateOffset(lineNumber, numberOfCharacters);
		return new EXSLTCommonContentAssistProcessor()
				.computeCompletionProposals(sourceViewer, offset);
	}

}
