/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
package org.eclipse.wst.jsdt.web.ui.internal.java.refactoring;

import java.text.MessageFormat;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.web.core.javascript.search.JSDTSearchDocumentDelegate;
import org.eclipse.wst.jsdt.web.ui.internal.JsUIMessages;
/**
*

* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*
 * @author pavery
 */
public class JSPMethodRenameRequestor extends BasicRefactorSearchRequestor {
	public JSPMethodRenameRequestor(IJavaScriptElement element, String newName) {
		super(element, newName);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.web.ui.internal.java.refactoring.BasicRefactorSearchRequestor#getDescription()
	 */
	
	protected String getDescription() {
		String methodName = getElement().getElementName();
		String newName = getNewName();
		String description = MessageFormat.format(JsUIMessages.BasicRefactorSearchRequestor_3, new String[] { methodName, newName });
		return description;
	}
	
	
	protected String getRenameText(JSDTSearchDocumentDelegate searchDoc, SearchMatch javaMatch) {
		String javaText = searchDoc.getJspTranslation().getJsText();
		String methodText = javaText.substring(javaMatch.getOffset(), javaMatch.getOffset() + javaMatch.getLength());
		String methodSuffix = methodText.substring(methodText.indexOf("(")); //$NON-NLS-1$
		return getNewName() + methodSuffix;
	}
}
