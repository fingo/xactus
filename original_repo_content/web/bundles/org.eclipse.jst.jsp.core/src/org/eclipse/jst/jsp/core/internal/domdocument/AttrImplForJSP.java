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
 *     
 *******************************************************************************/

package org.eclipse.jst.jsp.core.internal.domdocument;

import org.eclipse.jst.jsp.core.internal.regions.DOMJSPRegionContexts;
import org.eclipse.wst.xml.core.internal.document.AttrImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class AttrImplForJSP extends AttrImpl {

	protected AttrImplForJSP() {
	}

	protected AttrImplForJSP(AttrImplForJSP that) {
		super(that);
	}

	protected boolean isNestedLanguageOpening(String regionType) {
		boolean result = regionType == DOMJSPRegionContexts.JSP_SCRIPTLET_OPEN || regionType == DOMJSPRegionContexts.JSP_EXPRESSION_OPEN || regionType == DOMJSPRegionContexts.JSP_DECLARATION_OPEN || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_OPEN;
		return result;
	}
	protected void setOwnerDocument(Document ownerDocument) {
		super.setOwnerDocument(ownerDocument);
	}
	protected void setName(String name) {
		super.setName(name);
	}
	protected void setNamespaceURI(String namespaceURI) {
		super.setNamespaceURI(namespaceURI);
	}

	public Node cloneNode(boolean deep) {
		Node cloned = new AttrImplForJSP(this);
		notifyUserDataHandlers(UserDataHandler.NODE_CLONED, cloned);
		return cloned;
	}
}
