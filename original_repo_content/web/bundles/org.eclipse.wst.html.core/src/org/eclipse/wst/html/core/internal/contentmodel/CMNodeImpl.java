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

import org.eclipse.wst.html.core.internal.provisional.HTMLCMProperties;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;



/**
 */
abstract class CMNodeImpl implements CMNode {

	private java.lang.String name = null;

	/**
	 * CMNodeImpl constructor comment.
	 */
	public CMNodeImpl(String nm) {
		super();
		name = nm;
	}

	/**
	 * getNodeName method
	 * @return java.lang.String
	 */
	public String getNodeName() {
		return name;
	}

	/**
	 * getProperty method
	 * @return java.lang.Object
	 *
	 * Returns the object property desciped by the propertyName
	 *
	 */
	public Object getProperty(String propertyName) {
		if (propertyName.equals(HTMLCMProperties.IS_XHTML))
			return new Boolean(false);
		return null;
	}

	/**
	 * supports method
	 * @return boolean
	 *
	 * Returns true if the CMNode supports a specified property
	 *
	 */
	public boolean supports(String propertyName) {
		if (propertyName.equals(HTMLCMProperties.IS_XHTML))
			return true;
		return false;
	}
}
