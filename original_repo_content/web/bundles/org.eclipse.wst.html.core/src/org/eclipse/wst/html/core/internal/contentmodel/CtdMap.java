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
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMGroup;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;

/**
 * for MAP.
 */
final class CtdMap extends ComplexTypeDefinition {

	/**
	 * @param elementCollection ElementCollection
	 */
	public CtdMap(ElementCollection elementCollection) {
		super(elementCollection);
		primaryCandidateName = HTML40Namespace.ElementName.AREA;
	}

	/**
	 * ((%block;) | AREA)+.
	 */
	protected void createContent() {
		if (content != null)
			return; // already created.
		if (collection == null)
			return;

		// ( | )+
		content = new CMGroupImpl(CMGroup.CHOICE, 1, CMContentImpl.UNBOUNDED);
		// (%block;)
		CMGroupImpl blocks = new CMGroupImpl(CMGroup.CHOICE, 1, 1);
		if (blocks == null)
			return;
		collection.getBlock(blocks);
		content.appendChild(blocks);
		// AREA
		CMNode area = collection.getNamedItem(HTML40Namespace.ElementName.AREA);
		if (area != null)
			content.appendChild(area);
	}

	/**
	 * ((%block;) | AREA)+.
	 * Because %block; consists of elements only, the type is ELEMENT.
	 * @return int; Should be one of ANY, EMPTY, ELEMENT, MIXED, PCDATA, CDATA,
	 * those are defined in CMElementDeclaration.
	 */
	public int getContentType() {
		return CMElementDeclaration.ELEMENT;
	}

	/**
	 * @return java.lang.String
	 */
	public String getTypeName() {
		return ComplexTypeDefinitionFactory.CTYPE_MAP;
	}
}
