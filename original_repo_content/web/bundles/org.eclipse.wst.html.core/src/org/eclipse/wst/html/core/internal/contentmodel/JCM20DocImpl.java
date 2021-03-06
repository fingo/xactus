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
package org.eclipse.wst.html.core.internal.contentmodel;




/**
 * Implementation of CMDocument for JSP 2.0.
 */
class JCM20DocImpl extends JCMDocImpl {
	/**
	 * JCM20DocImpl constructor comment.
	 */
	public JCM20DocImpl(String docTypeName, CMNamespaceImpl targetNamespace) {
		super(docTypeName, targetNamespace, new JSP20ElementCollection());
	}
}
