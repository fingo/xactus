/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.web.core.internal.java;

import java.util.List;

import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IJavaElement;

/**
 * An object that holds a translated JSP String along with position mapping from
 * Java to JSP, and JSP to Java.
 * 
 * @author pavery
 * 
 */
public interface IJSPTranslation {
	
	/**
	 * The corresponding CompilationUnit for the translated JSP document
	 * 
	 * @return an ICompilationUnit of the translation
	 */
	public ICompilationUnit getCompilationUnit();
	
	/**
	 * Returns the IJavaElements corresponding to the JSP range in the JSP
	 * StructuredDocument
	 * 
	 * @param jspStart
	 *            staring offset in the JSP document
	 * @param jspEnd
	 *            ending offset in the JSP document
	 * @return IJavaElements corresponding to the JSP selection
	 */
	public IJavaElement[] getElementsFromJspRange(int jspStart, int jspEnd);
	
	/**
	 * The corresponding java offset in the translated document for a given jsp
	 * offset.
	 * 
	 * @param jspPosition
	 * @return the java offset that maps to jspOffset, -1 if the position has no
	 *         mapping.
	 */
	public int getJavaOffset(int jspOffset);
	
	/**
	 * The string contents of the translated document.
	 * 
	 * @return the string contents of the translated document.
	 */
	public String getJavaText();
	
	/**
	 * The corresponding jsp offset in the source document for a given jsp
	 * offset in the translated document.
	 * 
	 * @param javaPosition
	 * @return the jsp offset that maps to javaOffset, -1 if the position has no
	 *         mapping.
	 */
	public int getJspOffset(int javaOffset);
	
	/**
	 * @return the List of problems collected during reconcile of the
	 *         compilation unit
	 */
	public List getProblems();
	
	/**
	 * Reconciles the compilation unit for this JSPTranslation
	 */
	public void reconcileCompilationUnit();
	
	/**
	 * Must be set true in order for problems to be collected during reconcile.
	 * If set false, problems will be ignored during reconcile.
	 * 
	 * @param collect
	 */
	public void setProblemCollectingActive(boolean collect);
	
	// add these API once finalized
	// getJspEdits(TextEdit javaEdit)
	// getJavaRanges()
	// getJavaDocument()
}