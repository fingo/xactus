/*******************************************************************************
 * Copyright (c) 2007, 2020 IBM Corporation and others.
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
package org.eclipse.wst.sse.ui.internal.correction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.quickassist.IQuickAssistInvocationContext;
import org.eclipse.jface.text.quickassist.IQuickAssistProcessor;
import org.eclipse.jface.text.quickassist.IQuickFixableAnnotation;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension2;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.ui.StructuredTextInvocationContext;
import org.eclipse.wst.sse.ui.internal.reconcile.TemporaryAnnotation;

/**
 * This quick assist processor will provide quick fixes for source validation
 * errors (Temporary Annotation)
 */
public class SourceValidationQuickAssistProcessor implements IQuickAssistProcessor {
	public boolean canAssist(IQuickAssistInvocationContext invocationContext) {
		return false;
	}

	public boolean canFix(Annotation annotation) {
		if (annotation instanceof IQuickFixableAnnotation) {
			if (((IQuickFixableAnnotation) annotation).isQuickFixableStateSet()) {
				return ((IQuickFixableAnnotation) annotation).isQuickFixable();
			}
		}
		return false;
	}

	public ICompletionProposal[] computeQuickAssistProposals(IQuickAssistInvocationContext quickAssistContext) {
		ISourceViewer viewer = quickAssistContext.getSourceViewer();
		int documentOffset = quickAssistContext.getOffset();
		int length = viewer != null ? viewer.getSelectedRange().y : 0;

		IAnnotationModel model = viewer != null ? viewer.getAnnotationModel() : null;
		if (model == null)
			return null;

		List<ICompletionProposal> allProposals = new ArrayList<>();
		if (model instanceof IAnnotationModelExtension2) {
			Iterator<Annotation> iter = ((IAnnotationModelExtension2) model).getAnnotationIterator(documentOffset, length, true, true);
			while (iter.hasNext()) {
				List<IQuickAssistProcessor> processors = new ArrayList<>();
				Annotation anno = iter.next();
				if (canFix(anno)) {
					// first check to see if annotation already has a quick
					// fix processor attached to it
					if (anno instanceof TemporaryAnnotation) {
						Object o = ((TemporaryAnnotation) anno).getAdditionalFixInfo();
						if (o instanceof IQuickAssistProcessor) {
							processors.add((IQuickAssistProcessor) o);
						}
					}

					// get all relevant quick fixes for this annotation
					QuickFixRegistry registry = QuickFixRegistry.getInstance();
					processors.addAll(Arrays.asList(registry.getQuickFixProcessors(anno)));

					// set up context
					Map<String, String> attributes = null;
					if (anno instanceof TemporaryAnnotation) {
						attributes = ((TemporaryAnnotation) anno).getAttributes();
					}
					Position pos = model.getPosition(anno);
					StructuredTextInvocationContext sseContext = new StructuredTextInvocationContext(viewer, pos.getOffset(), pos.getLength(), attributes);

					// call each processor
					for (int i = 0; i < processors.size(); ++i) {
						List<ICompletionProposal> proposals = new ArrayList<>();
						collectProposals(processors.get(i), anno, sseContext, proposals);

						if (proposals.size() > 0) {
							allProposals.addAll(proposals);
						}
					}

				}
			}
		}

		if (allProposals.isEmpty())
			return null;

		return allProposals.toArray(new ICompletionProposal[allProposals.size()]);
	}

	private void collectProposals(IQuickAssistProcessor processor, Annotation annotation, IQuickAssistInvocationContext invocationContext, List<ICompletionProposal> proposalsList) {
		ICompletionProposal[] proposals = processor.computeQuickAssistProposals(invocationContext);
		if (proposals != null && proposals.length > 0) {
			proposalsList.addAll(Arrays.asList(proposals));
		}
	}


	public String getErrorMessage() {
		return null;
	}

}
