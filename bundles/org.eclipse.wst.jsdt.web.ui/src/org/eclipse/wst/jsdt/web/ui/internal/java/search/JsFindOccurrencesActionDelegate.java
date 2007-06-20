/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.jsdt.web.ui.internal.java.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.html.ui.internal.search.HTMLFindOccurrencesProcessor;
import org.eclipse.wst.sse.ui.internal.search.FindOccurrencesActionDelegate;

/**
 * Sets up FindOccurrencesActionDelegate for jsp find occurrences processors
 */
public class JsFindOccurrencesActionDelegate extends FindOccurrencesActionDelegate {
	private List fProcessors;
	
	
	protected List getProcessors() {
		if (fProcessors == null) {
			fProcessors = new ArrayList();
			HTMLFindOccurrencesProcessor htmlProcessor = new HTMLFindOccurrencesProcessor();
			fProcessors.add(htmlProcessor);
			// temporary, workaround to disable function, since using the
			// function
			// can easily cause deadlock to occur.
			// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=103662
// JSPFindOccurrencesProcessor jspProcessor = new
// JSPFindOccurrencesProcessor();
// fProcessors.add(jspProcessor);
		}
		return fProcessors;
	}
}
