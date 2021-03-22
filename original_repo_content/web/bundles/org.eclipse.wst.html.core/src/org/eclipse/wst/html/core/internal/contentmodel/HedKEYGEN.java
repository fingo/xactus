/*******************************************************************************
 * Copyright (c) 2010, 2020 IBM Corporation and others.
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

import org.eclipse.wst.html.core.internal.provisional.HTML40Namespace;
import org.eclipse.wst.html.core.internal.provisional.HTML50Namespace;

public class HedKEYGEN extends HedEmpty {

	public HedKEYGEN(ElementCollection collection) {
		super(HTML50Namespace.ElementName.KEYGEN, collection);
		layoutType = LAYOUT_OBJECT;
	}
	protected void createAttributeDeclarations() {
		if (attributes != null)
			return; // already created.
		if (attributeCollection == null)
			return; // fatal

		attributes = new CMNamedNodeMapImpl();

		attributeCollection.getAttrs(attributes);

		String[] names = { HTML50Namespace.ATTR_NAME_CHALLENGE, HTML40Namespace.ATTR_NAME_DISABLED, HTML50Namespace.ATTR_NAME_FORM, HTML50Namespace.ATTR_NAME_KEYTYPE, HTML40Namespace.ATTR_NAME_NAME};
		attributeCollection.getDeclarations(attributes, Arrays.asList(names).iterator());
	}

}
