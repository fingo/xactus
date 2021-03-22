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
 * SSI:CONFIG.
 */
final class HedSSIConfig extends HedSSIBase {

	/**
	 */
	public HedSSIConfig(ElementCollection collection) {
		super(CHTMLNamespace.ElementName.SSI_CONFIG, collection);
	}

	/**
	 * SSI:CONFIG.
	 * (errmsg CDATA #IMPLIED)
	 * (sizefmt CDATA #IMPLIED)
	 * (timefmt CDATA #IMPLIED)
	 */
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		String[] names = {CHTMLNamespace.ATTR_NAME_ERRMSG, CHTMLNamespace.ATTR_NAME_SIZEFMT, CHTMLNamespace.ATTR_NAME_TIMEFMT};
		attributeCollection.getDeclarations(attributes, Arrays.asList(names).iterator());
	}
}
