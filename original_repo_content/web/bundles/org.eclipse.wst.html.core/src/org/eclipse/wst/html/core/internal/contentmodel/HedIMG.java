/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;

/**
 * IMG.
 */
final class HedIMG extends HedEmpty {

	/**
	 */
	public HedIMG(ElementCollection collection) {
		super(HTML40Namespace.ElementName.IMG, collection);
		layoutType = LAYOUT_OBJECT;
	}

	/**
	 * IMG.
	 * %attrs;
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		// %attrs;
		attributeCollection.getAttrs(attributes);

		//different sets of attributes for html 4 & 5
		attributeCollection.createAttributeDeclarations(HTML40Namespace.ElementName.IMG, attributes);
	
	}

	/**
	 */
	public CMNamedNodeMap getProhibitedAncestors() {
		if (prohibitedAncestors != null)
			return prohibitedAncestors;

		String[] names = {HTML40Namespace.ElementName.PRE};
		prohibitedAncestors = elementCollection.getDeclarations(names);

		return prohibitedAncestors;
	}
}
