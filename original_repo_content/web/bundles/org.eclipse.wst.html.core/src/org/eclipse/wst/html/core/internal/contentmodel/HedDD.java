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
 * DD.
 */
final class HedDD extends HedFlowContainer {

	private static String[] terminators = {HTML40Namespace.ElementName.DT, HTML40Namespace.ElementName.DD};

	/**
	 */
	public HedDD(ElementCollection collection) {
		super(HTML40Namespace.ElementName.DD, collection);
		correctionType = CORRECT_EMPTY;
		layoutType = LAYOUT_BLOCK;
		omitType = OMIT_END_DEFAULT;
	}

	/**
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
	}

	/**
	 * DD has terminators.
	 * @return java.util.Iterator
	 */
	protected Iterator getTerminators() {
		return Arrays.asList(terminators).iterator();
	}
}
