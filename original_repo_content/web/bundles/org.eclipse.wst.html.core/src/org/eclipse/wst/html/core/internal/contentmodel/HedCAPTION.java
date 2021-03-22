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
package org.eclipse.wst.html.core.internal.contentmodel;

import org.eclipse.wst.html.core.internal.provisional.HTML40Namespace;



/**
 * CAPTION.
 */
final class HedCAPTION extends HedInlineContainer {

	/**
	 */
	public HedCAPTION(ElementCollection collection) {
		super(HTML40Namespace.ElementName.CAPTION, collection);
		layoutType = LAYOUT_BLOCK;
	}

	/**
	 * %attrs;
	 * (align %CAlign; #IMPLIED) ... should be defined locally.
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		// %attrs;
		attributeCollection.getAttrs(attributes);

		HTMLAttrDeclImpl attr = AttributeCollection.createAlignForCaption();
		if (attr != null)
			attributes.putNamedItem(HTML40Namespace.ATTR_NAME_ALIGN, attr);
	}
}
