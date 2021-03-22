/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.xml.core.internal.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;

public class XMLComponentReferencePattern extends XMLComponentSearchPattern{

	public XMLComponentReferencePattern(IFile file, QualifiedName elementQName, QualifiedName typeQName, int matchRule) {
		super(file, elementQName, typeQName, matchRule);
		
	}

	public XMLComponentReferencePattern(IFile file, QualifiedName elementQName, QualifiedName typeQName) {
		super(file, elementQName, typeQName);
		
	}
	
	public XMLComponentReferencePattern(QualifiedName elementQName, QualifiedName typeQName) {
		super(null, elementQName, typeQName);
		
	}
	
	
	
	
}
