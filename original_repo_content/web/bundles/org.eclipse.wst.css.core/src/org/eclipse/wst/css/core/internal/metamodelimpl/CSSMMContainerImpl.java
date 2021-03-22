/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.wst.css.core.internal.metamodelimpl;

import org.eclipse.wst.css.core.internal.metamodel.CSSMMContainer;
import org.eclipse.wst.css.core.internal.metamodel.CSSMMNode;

class CSSMMContainerImpl extends CSSMMNodeImpl implements CSSMMContainer {


	public CSSMMContainerImpl() {
		super();
	}

	public String getType() {
		return TYPE_CONTAINER;
	}

	public String getName() {
		String name = getAttribute(ATTR_NAME);
		if (name != null) {
			return name.toLowerCase();
		}
		else {
			return null;
		}
	}

	/*
	 * @see CSSMMNodeImpl#canContain(CSSMMNode)
	 */
	boolean canContain(CSSMMNode child) {
		if (child == null) {
			return false;
		}
		String type = child.getType();
		return (type == TYPE_NUMBER || type == TYPE_KEYWORD || type == TYPE_FUNCTION || type == TYPE_CONTAINER);
	}

	/*
	 * @see CSSMMNodeImpl#getError()
	 */
	short getError() {
		if (getName() == null) {
			return MetaModelErrors.ERROR_NOT_DEFINED;
		}
		else if (getChildCount() == 0) {
			return MetaModelErrors.ERROR_NO_CHILD;
		}
		else {
			return MetaModelErrors.NO_ERROR;
		}
	}

}
