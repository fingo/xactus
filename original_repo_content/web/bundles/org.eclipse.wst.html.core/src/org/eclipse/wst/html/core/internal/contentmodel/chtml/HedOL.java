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



import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;

/**
 * OL.
 */
final class HedOL extends HedListItemContainer {

	/**
	 */
	public HedOL(ElementCollection collection) {
		super(CHTMLNamespace.ElementName.OL, collection);
	}

	/**
	 * OL.
	 * (type %OLStyle; #IMPLIED) ... should be defined locally.
	 * (compact (compact) #IMPLIED)
	 * (start NUMBER #IMPLIED)
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		// %attrs;
		attributeCollection.getAttrs(attributes);

	}

	/**
	 */
	public CMNamedNodeMap getProhibitedAncestors() {
		if (prohibitedAncestors != null)
			return prohibitedAncestors;

		String[] names = {CHTMLNamespace.ElementName.DIR, CHTMLNamespace.ElementName.MENU};
		prohibitedAncestors = elementCollection.getDeclarations(names);

		return prohibitedAncestors;
	}
}
