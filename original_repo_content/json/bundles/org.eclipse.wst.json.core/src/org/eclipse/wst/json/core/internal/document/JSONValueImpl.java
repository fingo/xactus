/**
 *  Copyright (c) 2016 Angelo ZERR and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONPair;
import org.eclipse.wst.json.core.document.IJSONValue;

public abstract class JSONValueImpl extends JSONNodeImpl implements IJSONValue {

	private IJSONPair ownerPairNode;

	protected JSONValueImpl() {
		super();
	}

	/**
	 * NodeImpl constructor
	 * 
	 * @param that
	 *            NodeImpl
	 */
	protected JSONValueImpl(JSONValueImpl that) {
		super(that);
	}

	@Override
	public String getSimpleValue() {
		if (getStartStructuredDocumentRegion() == null) {
			return null;
		}
		return getStartStructuredDocumentRegion().getText();
	}

	@Override
	public String getValueRegionType() {
		if (getStartStructuredDocumentRegion() == null) {
			return null;
		}
		return getStartStructuredDocumentRegion().getType();
	}

	public void setOwnerPairNode(IJSONPair pairNode) {
		this.ownerPairNode = pairNode;
	}

	@Override
	public IJSONNode getParentOrPairNode() {
		if (ownerPairNode != null) {
			return ownerPairNode;
		}
		IJSONNode parent = super.getParentNode();
		return (parent != null && parent.getOwnerPairNode() != null) ? parent
				.getOwnerPairNode() : parent;
	}

	public IJSONPair getOwnerPairNode() {
		return ownerPairNode;
	}
	
	public void updateValue(JSONValueImpl value) {
		notify(CHANGE, this.getParentNode(), this, value, getStartOffset());
	}

}
