/*******************************************************************************
 * Copyright (c) 2004, 2011 IBM Corporation and others.
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
package org.eclipse.jst.jsp.ui.internal.java.search;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.SearchDocument;
import org.eclipse.jst.jsp.core.internal.java.search.JSPSearchScope;
import org.eclipse.jst.jsp.core.internal.java.search.JSPSearchSupport;
import org.eclipse.jst.jsp.ui.internal.JSPUIMessages;
import org.eclipse.wst.sse.ui.internal.search.BasicSearchQuery;

/**
 * Implementation of <code>ISearchQuery</code> for <code>IJavaElement</code>s in JSP files.
 * 
 * @author pavery
 */
public class JSPSearchQuery extends BasicSearchQuery {
	
	/** the IJavaElement we are searching for in the file **/
	private IJavaElement fElement = null;
	
	public JSPSearchQuery(IFile file, IJavaElement element) {
		super(file);
		super.setResult(new JSPOccurrencesSearchResult(this));
		this.fElement = element;
	}
	
	public IJavaElement getJavaElement() {
		return this.fElement;
	}
	
	// for access by inner class
	public JSPSearchQuery getInstance() {
		return this;
	}
	
	protected IStatus doQuery(IProgressMonitor monitor) {
		IStatus status = Status.OK_STATUS;
		try {
			JSPSearchSupport support = JSPSearchSupport.getInstance();
			// index the file
			SearchDocument delegate =  support.addJspFile(getFile());
			
			String scopePath = delegate.getPath();
			JSPSearchScope singleFileScope = new JSPSearchScope(new String[]{getFile().getFullPath().toString(), scopePath});
			
			// perform a searchs
			// by passing in this jsp search query, requstor can add matches
			support.searchRunnable(getJavaElement(), singleFileScope, new JSPSingleFileSearchRequestor(getInstance()), monitor);
		}
		catch (Exception e){
			status = new Status(IStatus.ERROR, "org.eclipse.wst.sse.ui", IStatus.OK, "", null); //$NON-NLS-1$	//$NON-NLS-2$
		}
		return status;
	}
	
	/**
	 * @see org.eclipse.search.ui.ISearchQuery#getLabel()
	 */
	public String getLabel() {
		String label = JSPUIMessages.OccurrencesSearchQuery_0; //$NON-NLS-1$
		String[] args = {getSearchText(), "" + super.getMatchCount(), getFilename()};
		return MessageFormat.format(label, args);
	}

	private String getFilename() {
		String filename = JSPUIMessages.OccurrencesSearchQuery_2;
		if(getFile() != null)
			filename = getFile().getName();
		return filename;
	}

	protected String getSearchText() {
		if(fElement != null)
			return fElement.getElementName();
		return "";
	}
	
	public boolean canRerun() {
		
		return false;
	}

	/**
	 * @see org.eclipse.search.ui.ISearchQuery#canRunInBackground()
	 */
	public boolean canRunInBackground() {
		return true;
	}
}
