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



import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMGroup;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;

/**
 * OPTION container.
 * (OPTION)+
 */
final class CtdOptionContainer extends ComplexTypeDefinition {

	/**
	 * @param elementCollection ElementCollection
	 */
	public CtdOptionContainer(ElementCollection elementCollection) {
		super(elementCollection);
		primaryCandidateName = CHTMLNamespace.ElementName.OPTION;
	}

	/**
	 * (OPTION)+.
	 */
	protected void createContent() {
		if (content != null)
			return; // already created.
		if (collection == null)
			return;

		// ( )+
		content = new CMGroupImpl(CMGroup.SEQUENCE, 1, CMContentImpl.UNBOUNDED);
		// OPTION
		CMNode dec = collection.getNamedItem(CHTMLNamespace.ElementName.OPTION);
		if (dec != null)
			content.appendChild(dec);
	}

	/**
	 * (OPTION)+
	 * @return int; Should be one of ANY, EMPTY, ELEMENT, MIXED, PCDATA, CDATA,
	 * those are defined in CMElementDeclaration.
	 */
	public int getContentType() {
		return CMElementDeclaration.ELEMENT;
	}

	/**
	 * Name of complex type definition.
	 * Each singleton must know its own name.
	 * All names should be defined in
	 * {@link <code>ComplexTypeDefinitionFactory</code>} as constants.<br>
	 * @return java.lang.String
	 */
	public String getTypeName() {
		return ComplexTypeDefinitionFactory.CTYPE_OPTION_CONTAINER;
	}
}
