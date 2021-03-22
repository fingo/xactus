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
 *******************************************************************************/
package org.eclipse.wst.html.core.internal.htmlcss;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.wst.css.core.internal.document.CSSModelImpl;
import org.eclipse.wst.css.core.internal.provisional.adapters.ICSSModelAdapter;
import org.eclipse.wst.css.core.internal.provisional.contenttype.ContentTypeIdForCSS;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.html.core.internal.document.ElementStyleImpl;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Element;

/**
 */
public abstract class AbstractCSSModelAdapter implements ICSSModelAdapter {
	private final static String CSS_ID = ContentTypeIdForCSS.ContentTypeID_CSS;

	private Element element = null;
	private ICSSModel model = null;

	/**
	 */
	AbstractCSSModelAdapter() {
		super();
	}

	/**
	 */
	protected ICSSModel createModel() {
		// create embedded CSS model (not for external CSS)
		Element element = getElement();
		if (element == null)
			return null;
		String contentType = null;
		if (element instanceof ElementStyleImpl) {
			contentType = ((ElementStyleImpl) element).getEmbeddedStyleType();
		}
		IStructuredModel baseModel = ((IDOMNode) element).getModel();
		ICSSModel newModel = (ICSSModel) baseModel.getModelManager().createUnManagedStructuredModelFor(contentType != null ? contentType : CSS_ID);
		((CSSModelImpl) newModel).setOwnerDOMNode(element);
		return newModel;
	}

	/**
	 */
	public Element getElement() {
		return this.element;
	}

	/**
	 */
	protected ICSSModel getExistingModel() {
		return this.model;
	}

	/**
	 */
	protected void notifyStyleChanged(Element target) {
		INodeNotifier notifier = (INodeNotifier) target;
		if (notifier == null)
			return;
		Collection adapters = notifier.getAdapters();
		if (adapters == null)
			return;
		Iterator it = adapters.iterator();
		if (it == null)
			return;
		while (it.hasNext()) {
			INodeAdapter adapter = (INodeAdapter) it.next();
			if (adapter instanceof StyleListener) {
				StyleListener listener = (StyleListener) adapter;
				listener.styleChanged();
			}
		}
	}

	/**
	 */
	void setElement(Element element) {
		this.element = element;
	}

	/**
	 * check 
	 * 1. If attributes of element is valid (type,rel ...)
	 * 2. If content model supports this element / attribute (future ?)
	 */
	protected boolean isValidAttribute() {
		return (getElement() != null);
	}

	/**
	 */
	protected void setModel(ICSSModel model) {
		this.model = model;
	}
}
