/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
package org.eclipse.wst.json.ui.internal.contentassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.json.ui.internal.templates.TemplateContextTypeIdsJSON;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;

/**
 * <p>Completion computer for JSON templates</p>
 */
public class JSONPackageTemplatesCompletionProposalComputer implements ICompletionProposalComputer {
	/** <p>The template processor used to create the proposals</p> */
	private JSONTemplateCompletionProcessor fTemplateProcessor = null;

	/**
	 * <p>Create the computer</p>
	 */
	public JSONPackageTemplatesCompletionProposalComputer() {
		fTemplateProcessor = new JSONTemplateCompletionProcessor();
	}

	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer#computeCompletionProposals(org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List computeCompletionProposals(
			CompletionProposalInvocationContext context,
			IProgressMonitor monitor) {
		List proposals = new ArrayList();
		proposals.addAll(getTemplates(TemplateContextTypeIdsJSON.PACKAGE, context));
		return proposals;
	}

	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer#computeContextInformation(org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public List computeContextInformation(
			CompletionProposalInvocationContext context,
			IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer#getErrorMessage()
	 */
	public String getErrorMessage() {
		return null;
	}

	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer#sessionStarted()
	 */
	public void sessionStarted() {
		//default is to do nothing
	}

	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer#sessionEnded()
	 */
	public void sessionEnded() {
		//default is to do nothing
	}

	/**
	 * <p>Gets template proposals for the given template and proposal contexts</p>
	 * 
	 * @param templateContext the template context
	 * @param context the proposal context
	 * @return {@link List} of template proposals for the given contexts
	 */
	private List getTemplates(String templateContext,
			CompletionProposalInvocationContext context) {
		fTemplateProcessor.setContextType(templateContext);
		ICompletionProposal[] proposals = fTemplateProcessor.computeCompletionProposals(
				context.getViewer(), context.getInvocationOffset());
		return Arrays.asList(proposals);
	}

}
