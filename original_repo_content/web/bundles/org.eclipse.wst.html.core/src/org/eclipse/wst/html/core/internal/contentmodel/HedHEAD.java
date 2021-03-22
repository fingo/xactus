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



import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.wst.html.core.internal.provisional.HTML40Namespace;


/**
 * HTML.
 */
final class HedHEAD extends HTMLElemDeclImpl {

	private static String[] terminators = {HTML40Namespace.ElementName.HEAD, HTML40Namespace.ElementName.BODY, HTML40Namespace.ElementName.FRAMESET, HTML40Namespace.ElementName.HTML};

	/**
	 */
	public HedHEAD(ElementCollection collection) {
		super(HTML40Namespace.ElementName.HEAD, collection);
		typeDefinitionName = ComplexTypeDefinitionFactory.CTYPE_HEAD;
		layoutType = LAYOUT_HIDDEN;
		omitType = OMIT_BOTH;
	}

	/**
	 * Create all attribute declarations.
	 * This method is called once in the constructor of the super class.
	 * The <code>HEAD</code> element may have the following attributes:
	 * <table>
	 * <tbody>
	 *   <tr>
	 *     <th>NAME</th><th>TYPE</th><th>USAGE</th><th>DEFAULT (INITIAL) VALUE</th><th>MEMO</th>
	 *   </tr>
	 *   <tr>
	 *     <td>%i18n;</td><td>-</td><td>-</td><td>-</td><td>-</td>
	 *   </tr>
	 *   <tr>
	 *     <td>profile</td><td>URI</td><td>#IMPLIED</td><td>N/A</td><td>-</td>
	 *   </tr>
	 * </tbody>
	 * </table>
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		// %i18n;
		attributeCollection.getI18n(attributes);
		// profile
		HTMLAttributeDeclaration adec = attributeCollection.getDeclaration(HTML40Namespace.ATTR_NAME_PROFILE);
		if (adec != null)
			attributes.putNamedItem(HTML40Namespace.ATTR_NAME_PROFILE, adec);
	}

	/**
	 * HEAD has terminators.
	 * @return java.util.Iterator
	 */
	protected Iterator getTerminators() {
		return Arrays.asList(terminators).iterator();
	}
}
