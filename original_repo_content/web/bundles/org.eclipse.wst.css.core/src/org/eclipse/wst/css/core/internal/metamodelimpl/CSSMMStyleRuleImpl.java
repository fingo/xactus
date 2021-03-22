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

import org.eclipse.wst.css.core.internal.metamodel.CSSMMNode;
import org.eclipse.wst.css.core.internal.metamodel.CSSMMStyleRule;

class CSSMMStyleRuleImpl extends CSSMMNodeImpl implements CSSMMStyleRule {


	public CSSMMStyleRuleImpl() {
		super();
	}

	public String getType() {
		return TYPE_STYLE_RULE;
	}

	public String getName() {
		return NAME_NOT_AVAILABLE;
	}

	/*
	 * @see CSSMMNodeImpl#canContain(CSSMMNode)
	 */
	boolean canContain(CSSMMNode child) {
		if (child == null) {
			return false;
		}
		String type = child.getType();
		return (type == TYPE_PROPERTY || type == TYPE_SELECTOR);
	}

	short getError() {
		if (getChildCount() == 0) {
			return MetaModelErrors.ERROR_NO_CHILD;
		}
		else {
			return MetaModelErrors.NO_ERROR;
		}
	}
}
