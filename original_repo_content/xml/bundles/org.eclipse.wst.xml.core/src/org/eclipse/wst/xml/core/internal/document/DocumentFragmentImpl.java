/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *     Balazs Banfai: Bug 154737 getUserData/setUserData support for Node
 *     https://bugs.eclipse.org/bugs/show_bug.cgi?id=154737
 *     
 *******************************************************************************/
package org.eclipse.wst.xml.core.internal.document;



import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;


/**
 * DocumentFragmentImpl class
 */
public class DocumentFragmentImpl extends NodeContainer implements DocumentFragment {

	/**
	 * DocumentFragmentImpl constructor
	 */
	protected DocumentFragmentImpl() {
		super();
	}

	/**
	 * DocumentFragmentImpl constructor
	 * 
	 * @param that
	 *            DocumentFragmentImpl
	 */
	protected DocumentFragmentImpl(DocumentFragmentImpl that) {
		super(that);
	}

	/**
	 * cloneNode method
	 * 
	 * @return org.w3c.dom.Node
	 * @param deep
	 *            boolean
	 */
	public Node cloneNode(boolean deep) {
		DocumentFragmentImpl cloned = new DocumentFragmentImpl(this);
		if (deep)
			cloneChildNodes(cloned, deep);
		
		notifyUserDataHandlers(UserDataHandler.NODE_CLONED, cloned);
		return cloned;
	}

	/**
	 * getNodeName method
	 * 
	 * @return java.lang.String
	 */
	public String getNodeName() {
		return "#document-fragment";//$NON-NLS-1$
	}

	/**
	 * getNodeType method
	 * 
	 * @return short
	 */
	public short getNodeType() {
		return DOCUMENT_FRAGMENT_NODE;
	}
}
