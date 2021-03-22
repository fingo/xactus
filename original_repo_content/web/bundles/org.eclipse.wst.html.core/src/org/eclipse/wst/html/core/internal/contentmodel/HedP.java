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



import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.wst.html.core.internal.provisional.HTML40Namespace;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;

/**
 * P.
 */
final class HedP extends HedInlineContainer {

	private static Collection terminators = null;

	/**
	 */
	public HedP(ElementCollection collection) {
		super(HTML40Namespace.ElementName.P, collection);
		correctionType = CORRECT_EMPTY;
		layoutType = LAYOUT_BLOCK;
		omitType = OMIT_END;
	}

	/**
	 * Create all attribute declarations.
	 * This method is called once in the constructor of the super class.
	 * The <code>P</code> element may have the following attributes:
	 * <table>
	 * <tbody>
	 *   <tr>
	 *     <th>NAME</th><th>TYPE</th><th>USAGE</th><th>DEFAULT (INITIAL) VALUE</th><th>MEMO</th>
	 *   </tr>
	 *   <tr>
	 *     <td>%attrs;</td><td>-</td><td>-</td><td>-</td><td>-</td>
	 *   </tr>
	 *   <tr>
	 *     <td>%align;</td><td>-</td><td>-</td><td>-</td><td>-</td>
	 *   </tr>
	 * </tbody>
	 * </table>
	 * <p><b>%align;</b> means <code>align (left|center|right|justify) #IMPLIED</code>.
	 * Unfortunately, this <code>align</code> is different from one in
	 * <code>IMG</code> or <code>TABLE</code>.  So, the attribute declaration
	 * of <code>align</code> should be localy created and it shouldn't be registered
	 * in a <code>HCMDocImpl</code> instance.</p>
	 * <p>However, %align is used in sevaral times.  I wouldn't write same code
	 * in many times.  So, I add a new utility method into <code>CMUtil</code>
	 * to create the attribute declaration.</p>
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal
		attributes = new CMNamedNodeMapImpl();
		// %attrs;
		attributeCollection.getAttrs(attributes);
		// align
		HTMLAttrDeclImpl adec = AttributeCollection.createAlignForParagraph();
		attributes.putNamedItem(HTML40Namespace.ATTR_NAME_ALIGN, adec);
	}

	/**
	 */
	public CMNamedNodeMap getProhibitedAncestors() {
		if (prohibitedAncestors != null)
			return prohibitedAncestors;
		String[] names = {HTML40Namespace.ElementName.DIR, HTML40Namespace.ElementName.MENU};
		prohibitedAncestors = elementCollection.getDeclarations(names);
		return prohibitedAncestors;
	}

	/**
	 * Return names of terminators.
	 * <code>P</code> has terminators.
	 * @return java.util.Iterator
	 */
	protected Iterator getTerminators() {
		if (terminators != null)
			return terminators.iterator();
		//<<D217982
		terminators = new Vector();
		terminators.addAll(elementCollection.getNamesOfBlock());
		terminators.add(HTML40Namespace.ElementName.LI);
		terminators.add(HTML40Namespace.ElementName.DT);
		terminators.add(HTML40Namespace.ElementName.DD);
		//D217982
		return terminators.iterator();
	}
}
