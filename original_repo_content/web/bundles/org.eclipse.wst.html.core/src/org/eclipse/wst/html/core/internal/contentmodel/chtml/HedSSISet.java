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



import java.util.Arrays;

/**
 * SSI:SET.
 */
final class HedSSISet extends HedSSIBase {

	/**
	 */
	public HedSSISet(ElementCollection collection) {
		super(CHTMLNamespace.ElementName.SSI_SET, collection);
	}

	/**
	 * SSI:SET
	 * (var CDATA #IMPLIED)
	 * (value CDATA #IMPLIED)
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		String[] names = {CHTMLNamespace.ATTR_NAME_VAR, CHTMLNamespace.ATTR_NAME_VALUE};
		attributeCollection.getDeclarations(attributes, Arrays.asList(names).iterator());
	}
}
