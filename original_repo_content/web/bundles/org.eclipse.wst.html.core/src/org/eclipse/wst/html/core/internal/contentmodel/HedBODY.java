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
package org.eclipse.wst.html.core.internal.contentmodel;



import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.wst.html.core.internal.provisional.HTML40Namespace;
import org.eclipse.wst.xml.core.internal.contentmodel.CMContent;
import org.eclipse.wst.xml.core.internal.contentmodel.CMGroup;

/**
 * BODY.
 */
final class HedBODY extends HedFlowContainer {

	private static String[] terminators = {HTML40Namespace.ElementName.HEAD, HTML40Namespace.ElementName.BODY, HTML40Namespace.ElementName.FRAMESET, HTML40Namespace.ElementName.HTML};

	/**
	 */
	public HedBODY(ElementCollection collection) {
		super(HTML40Namespace.ElementName.BODY, collection);
		layoutType = LAYOUT_BLOCK;
		omitType = OMIT_BOTH;
	}

	/**
	 * %attrs;
	 * %bodycolors;
	 * (onload %Script; #IMPLIED)
	 * (onunload %Script; #IMPLIED)
	 * (background %URI; #IMPLIED)
	 * (marginwidth %Pixels; #IMPLIED) ... D205514
	 * (marginheight %Pixels; #IMPLIED) .. D205514
	 * (topmargin, CDATA, #IMPLIED) ...... D205514
	 * (bottommargin, CDATA, #IMPLIED) ... D205514
	 * (leftmargin, CDATA, #IMPLIED) ..... D205514
	 * (rightmargin, CDATA, #IMPLIED) .... D205514
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		// %attrs;
		attributeCollection.getAttrs(attributes);
		// %bodycolors;
		attributeCollection.getBodycolors(attributes);

		String[] names = {HTML40Namespace.ATTR_NAME_ONLOAD, HTML40Namespace.ATTR_NAME_ONUNLOAD, HTML40Namespace.ATTR_NAME_BACKGROUND,
		// <<D205514
					HTML40Namespace.ATTR_NAME_MARGINWIDTH, HTML40Namespace.ATTR_NAME_MARGINHEIGHT, HTML40Namespace.ATTR_NAME_TOPMARGIN, HTML40Namespace.ATTR_NAME_BOTTOMMARGIN, HTML40Namespace.ATTR_NAME_LEFTMARGIN, HTML40Namespace.ATTR_NAME_RIGHTMARGIN
		// D205514
		};
		attributeCollection.getDeclarations(attributes, Arrays.asList(names).iterator());
		
		//different sets of attributes for html 4 & 5
		attributeCollection.createAttributeDeclarations(HTML40Namespace.ElementName.BODY, attributes);

	}

	/**
	 * Inclusion.
	 * "Lazy eval."<br>
	 * @return org.eclipse.wst.xml.core.internal.contentmodel.CMContent
	 */
	public CMContent getInclusion() {
		if (inclusion != null)
			return inclusion; // already created.
		if (elementCollection == null)
			return null;

		// (INS|DEL)
		inclusion = new CMGroupImpl(CMGroup.CHOICE, 1, 1);
		String[] names = {HTML40Namespace.ElementName.INS, HTML40Namespace.ElementName.DEL};
		elementCollection.getDeclarations(inclusion, Arrays.asList(names).iterator());
		return inclusion;
	}

	/**
	 * BODY has terminators.
	 * @return java.util.Iterator
	 */
	protected Iterator getTerminators() {
		return Arrays.asList(terminators).iterator();
	}
}
