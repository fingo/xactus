/*******************************************************************************
 * Copyright (c) 2001, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     Angelo Zerr <angelo.zerr@gmail.com> - co.pied from org.eclipse.wst.xml.ui.internal.contentassist.ProposalComparator
 *                                           modified in order to process JSON Objects.                         
 *******************************************************************************/
package org.eclipse.wst.json.ui.internal.contentassist;



import java.util.Comparator;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.sse.ui.internal.contentassist.IRelevanceCompletionProposal;


public class ProposalComparator implements Comparator {


	public int compare(Object o1, Object o2) {
		int relevance = 0;
		if ((o1 instanceof IRelevanceCompletionProposal) && (o2 instanceof IRelevanceCompletionProposal)) {
			// sort based on relevance
			IRelevanceCompletionProposal cp1 = (IRelevanceCompletionProposal) o1;
			IRelevanceCompletionProposal cp2 = (IRelevanceCompletionProposal) o2;

			relevance = cp2.getRelevance() - cp1.getRelevance();

			// if same relevance, secondary sort (lexigraphically)
			if ((relevance == 0) && (o1 instanceof ICompletionProposal) && (o2 instanceof ICompletionProposal)) {
				String displayString1 = ((ICompletionProposal) o1).getDisplayString();
				String displayString2 = ((ICompletionProposal) o2).getDisplayString();
				if ((displayString1 != null) && (displayString2 != null)) {
					// relevance = displayString1.compareTo(displayString2);
					// // this didn't mix caps w/ lowercase
					relevance = com.ibm.icu.text.Collator.getInstance().compare(displayString1, displayString2);
				}
			}
		}
		// otherwise if it's not ISEDRelevanceCompletionProposal, don't sort
		return relevance;
	}
}
