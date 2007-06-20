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
/*
 * Created on May 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.wst.jsdt.web.ui.internal.java.refactoring;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.wst.jsdt.core.IMethod;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.web.ui.internal.JsUIMessages;
import org.eclipse.wst.jsdt.web.ui.internal.Logger;
import org.eclipse.wst.jsdt.web.ui.views.contentoutline.IJavaWebNode;

/**
 * @author pavery
 */
public class JSPMethodRenameParticipant extends RenameParticipant {
	private IMethod fMethod = null;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#checkConditions(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
	 */
	
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#createChange(org.eclipse.core.runtime.IProgressMonitor)
	 */
	
	public Change createChange(IProgressMonitor pm) throws CoreException {
		Change[] changes = JSPMethodRenameChange.createChangesFor(this.fMethod, getArguments().getNewName());
		CompositeChange multiChange = null;
		if (changes.length > 0) {
			multiChange = new CompositeChange(JsUIMessages.JSP_changes, changes);
		}
		return multiChange;
	}
	
	/**
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#getName()
	 */
	
	public String getName() {
		String name = ""; //$NON-NLS-1$
		if (this.fMethod != null) {
			try {
				name = this.fMethod.getSource();
			} catch (JavaModelException e) {
				Logger.logException(e);
			}
		}
		return name;
	}
	
	/**
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#initialize(java.lang.Object)
	 */
	
	protected boolean initialize(Object element) {
		if (element instanceof IMethod) {
			this.fMethod = (IMethod) element;
			return true;
		}else if (element instanceof IJavaWebNode) {
			if(((IJavaWebNode)element).getJavaElement() instanceof IMethod) {
				this.fMethod = (IMethod) ((IJavaWebNode)element).getJavaElement();
				return true;
			}
		}
		return false;
	}
}
