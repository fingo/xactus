/*******************************************************************************
 * Copyright (c) 2004, 2014 IBM Corporation and others.
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
import org.eclipse.wst.css.core.internal.provisional.contenttype.ContentTypeIdForCSS;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.w3c.dom.Document;

public class ElementImplForJSP extends ElementStyleImpl {

	/**
	 * 
	 */
	public ElementImplForJSP() {
		super();
	}

	/**
	 * @param that
	 */
	public ElementImplForJSP(ElementImpl that) {
		super(that);
	}

	protected boolean isNestedEndTag(String regionType) {
		boolean result = regionType == DOMJSPRegionContexts.JSP_ROOT_TAG_NAME || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_NAME;
		return result;
	}

	protected boolean isNestedClosed(String regionType) {
		boolean result = (regionType == DOMJSPRegionContexts.JSP_CLOSE || regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_CLOSE);
		return result;
	}

	protected boolean isNestedClosedComment(String regionType) {
		boolean result = regionType == DOMJSPRegionContexts.JSP_COMMENT_CLOSE;
		return result;
	}

	protected boolean isClosedNestedDirective(String regionType) {
		boolean result = regionType == DOMJSPRegionContexts.JSP_DIRECTIVE_CLOSE;
		return result;
	}

	protected void setOwnerDocument(Document ownerDocument) {
		super.setOwnerDocument(ownerDocument);
	}

	protected void setTagName(String tagName) {
		super.setTagName(tagName);
	}

	protected ElementImpl newInstance() {
		return new ElementImplForJSP(this);
	}

	public String getEmbeddedStyleType() {
		return ContentTypeIdForCSS.ContentTypeID_CSS;
	}
}
