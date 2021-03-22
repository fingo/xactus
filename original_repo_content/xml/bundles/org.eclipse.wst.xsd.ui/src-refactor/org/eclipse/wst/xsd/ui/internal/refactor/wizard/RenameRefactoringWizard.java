/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.xsd.ui.internal.refactor.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.wst.xsd.ui.internal.refactor.INameUpdating;

public class RenameRefactoringWizard extends RefactoringWizard {
	
	private final String fInputPageDescription;

	private final ImageDescriptor fInputPageImageDescriptor;
	
	public RenameRefactoringWizard(Refactoring refactoring, String defaultPageTitle, String inputPageDescription, 
			ImageDescriptor inputPageImageDescriptor) {
		super(refactoring, DIALOG_BASED_USER_INTERFACE);
		setDefaultPageTitle(defaultPageTitle);
    	fInputPageDescription= inputPageDescription;
		fInputPageImageDescriptor= inputPageImageDescriptor;

	}

	/* non java-doc
	 * @see RefactoringWizard#addUserInputPages
	 */ 
	protected void addUserInputPages() {
		String initialSetting= getProcessor().getCurrentElementName();
		RenameInputWizardPage inputPage= createInputPage(fInputPageDescription, initialSetting);
		inputPage.setImageDescriptor(fInputPageImageDescriptor);
		addPage(inputPage);
	}

	protected INameUpdating getProcessor() {
		
		return (INameUpdating)getRefactoring().getAdapter(INameUpdating.class);	
	}
	
	
	protected RenameInputWizardPage createInputPage(String message, String initialSetting) {
		return new RenameInputWizardPage(message, true, initialSetting) {
			protected RefactoringStatus validateTextField(String text) {
				return validateNewName(text);
			}	
		};
	}
	
	protected RefactoringStatus validateNewName(String newName) {
		INameUpdating ref= getProcessor();
		ref.setNewElementName(newName);
//		try{
			return ref.checkNewElementName(newName);
//		} catch (CoreException e){
//			//XXX: should log the exception
//			String msg= e.getMessage() == null ? "": e.getMessage(); //$NON-NLS-1$
//			return RefactoringStatus.createFatalErrorStatus(RefactoringMessages.getFormattedString("RenameRefactoringWizard.internal_error", msg));//$NON-NLS-1$
//		}	
	}
	
	
}
