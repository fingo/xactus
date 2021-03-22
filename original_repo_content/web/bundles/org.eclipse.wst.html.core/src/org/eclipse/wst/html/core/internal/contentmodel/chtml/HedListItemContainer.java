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
package org.eclipse.wst.html.core.internal.contentmodel.chtml;



/**
 * Base class for list item container declarations.
 * - OL, UL, MENU, DIR.
 */
abstract class HedListItemContainer extends HTMLElemDeclImpl {

	/**
	 */
	public HedListItemContainer(String elementName, ElementCollection collection) {
		super(elementName, collection);
		typeDefinitionName = ComplexTypeDefinitionFactory.CTYPE_LI_CONTAINER;
		correctionType = CORRECT_EMPTY;
		layoutType = LAYOUT_BLOCK;
		indentChild = true;
	}
}
