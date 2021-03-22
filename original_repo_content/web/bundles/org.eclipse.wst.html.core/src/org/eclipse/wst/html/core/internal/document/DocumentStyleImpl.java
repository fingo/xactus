/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
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
/*
 * http://www.w3.org/TR/DOM-Level-2-Style/stylesheets.html#StyleSheets-StyleSheet-DocumentStyle
 */
package org.eclipse.wst.html.core.internal.document;

import org.eclipse.wst.css.core.internal.provisional.adapters.IStyleSheetListAdapter;
import org.eclipse.wst.html.core.internal.parser.NameValidator;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.xml.core.internal.document.DocumentImpl;
import org.eclipse.wst.xml.core.internal.document.JSPTag;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.DocumentCSS;
import org.w3c.dom.stylesheets.StyleSheetList;

/**
 * @author davidw
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DocumentStyleImpl extends DocumentImpl implements IDOMDocument, DocumentCSS {
	public DocumentStyleImpl() {
		super();
	}

	protected DocumentStyleImpl(DocumentImpl that) {
		super(that);
	}

	public CSSStyleDeclaration getOverrideStyle(Element element, String pseudoName) {
		INodeAdapter adapter = getAdapterFor(IStyleSheetListAdapter.class);
		if (adapter == null)
			return null;
		return ((IStyleSheetListAdapter) adapter).getOverrideStyle(element, pseudoName);
	}

	public StyleSheetList getStyleSheets() {
		INodeAdapter adapter = getAdapterFor(IStyleSheetListAdapter.class);
		if (adapter == null)
			return null;
		return ((IStyleSheetListAdapter) adapter).getStyleSheets();
	}

	protected boolean isValidName(String name) {
		if (name == null || name.length() == 0)
			return false;
		if (NameValidator.isValid(name))
			return true;
		// special for invalid declaration
		if (name.length() == 1 && name.charAt(0) == '!')
			return true;
		// special for JSP tag in tag name
		if (name.startsWith(JSPTag.TAG_OPEN))
			return true;
		return name.indexOf('=') < 0;
	}
	protected void releaseStyleSheets() {
		INodeAdapter adapter = getExistingAdapter(IStyleSheetListAdapter.class);
		if (adapter == null)
			return;
		((IStyleSheetListAdapter) adapter).releaseStyleSheets();
	}

	/**
	 * createElement method
	 * @return org.w3c.dom.Element
	 * @param tagName java.lang.String
	 */
	public Element createElement(String tagName) throws DOMException {
		checkTagNameValidity(tagName);

		ElementStyleImpl element = new ElementStyleImpl();
		element.setOwnerDocument(this);
		element.setTagName(tagName);
		return element;
	}

	/**
	 * cloneNode method
	 * @return org.w3c.dom.Node
	 * @param deep boolean
	 */
	public Node cloneNode(boolean deep) {
		DocumentStyleImpl cloned = new DocumentStyleImpl(this);
		if (deep)
			cloned.importChildNodes(this, true);
		return cloned;
	}

	protected void setModel(IDOMModel model) {
		super.setModel(model);
	}
}
