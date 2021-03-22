/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.jsdt.web.ui.internal.java.search;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.Match;
import org.eclipse.wst.jsdt.ui.search.ISearchRequestor;
import org.eclipse.wst.jsdt.web.ui.internal.Logger;
/**
*

* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*
 * @author pavery
 */
public class JsSearchRequestor extends BasicJsSearchRequestor {
	private ISearchRequestor fJavaRequestor = null;
	
	public JsSearchRequestor() {
		super();
	}
	
	public JsSearchRequestor(ISearchRequestor javaRequestor) {
		// need to report matches to javaRequestor
		this.fJavaRequestor = javaRequestor;
	}
	
	
	protected void addSearchMatch(IDocument jspDocument, IFile jspFile, int jspStart, int jspEnd, String jspText) {
		if (!jspFile.exists()) {
			return;
		}
		int lineNumber = -1;
		try {
			lineNumber = jspDocument.getLineOfOffset(jspStart);
		} catch (BadLocationException e) {
			Logger.logException("offset: " + Integer.toString(jspStart), e); //$NON-NLS-1$
		}
		createSearchMarker(jspFile, jspStart, jspEnd, lineNumber);
		if (this.fJavaRequestor != null) {
			Match match = new Match(jspFile, jspStart, jspEnd - jspStart);
			this.fJavaRequestor.reportMatch(match);
		}
	}
	
	/**
	 * @param jspFile
	 * @param jspStart
	 * @param jspEnd
	 */
	private void createSearchMarker(IFile jspFile, int jspStart, int jspEnd, int lineNumber) {
		try {
			IMarker marker = jspFile.createMarker(NewSearchUI.SEARCH_MARKER);
			HashMap attributes = new HashMap(4);
			attributes.put(IMarker.CHAR_START, new Integer(jspStart));
			attributes.put(IMarker.CHAR_END, new Integer(jspEnd));
			attributes.put(IMarker.LINE_NUMBER, new Integer(lineNumber));
			marker.setAttributes(attributes);
		} catch (CoreException e) {
			Logger.logException(e);
		}
	}
}
