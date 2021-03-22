/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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
package org.eclipse.jst.jsp.core.internal.java;

import org.eclipse.jdt.core.compiler.IProblem;

/**
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IJSPProblem extends IProblem {

	int F_PROBLEM_ID_LITERAL = 0xA0000000;
	
	int TEIClassNotFound = F_PROBLEM_ID_LITERAL + 1;
	int TEIValidationMessage = F_PROBLEM_ID_LITERAL + 2;
	int TEIClassNotInstantiated = F_PROBLEM_ID_LITERAL + 3;
	int TEIClassMisc = F_PROBLEM_ID_LITERAL + 4;	
	int TagClassNotFound = F_PROBLEM_ID_LITERAL + 5;
	int UseBeanInvalidID = F_PROBLEM_ID_LITERAL + 6;
	int UseBeanMissingTypeInfo = F_PROBLEM_ID_LITERAL + 7;
	int UseBeanAmbiguousType  = F_PROBLEM_ID_LITERAL + 8;
	int StartCustomTagMissing  = F_PROBLEM_ID_LITERAL + 9;
	int EndCustomTagMissing  = F_PROBLEM_ID_LITERAL + 10;
	int UseBeanStartTagMissing  = F_PROBLEM_ID_LITERAL + 11;
	int UseBeanEndTagMissing  = F_PROBLEM_ID_LITERAL + 12;
	int ELProblem = F_PROBLEM_ID_LITERAL + 13;

	/**
	 * @return the ID of this JSP problem
	 */
	int getEID();

}
