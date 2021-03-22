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
 * Base class for (%inline;)* containers.
 */
abstract class HedInlineContainer extends HTMLElemDeclImpl {

	/**
	 * HedInlineContainer.
	 * @param elementName java.lang.String
	 * @param collection ElementCollection
	 */
	public HedInlineContainer(String elementName, ElementCollection collection) {
		super(elementName, collection);
		typeDefinitionName = ComplexTypeDefinitionFactory.CTYPE_INLINE_CONTAINER;
	}
}
