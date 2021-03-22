/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
package org.eclipse.wst.css.core.internal.contentmodel;



import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

/**
 * 
 */
public class PropCMURange extends PropCMNode {

	private static java.util.Hashtable instances = null;
	public final static String VAL_URANGE = "urange";//$NON-NLS-1$

	/**
	 * 
	 */
	protected PropCMURange(String name) {
		super(name);
	}

	/**
	 * 
	 */
	void getIdentifiers(Set indents) {
	}

	/**
	 * 
	 */
	public static PropCMURange getInstanceOf(String name) {
		if (name == null)
			return null;

		// initialize
		if (instances == null)
			instances = new Hashtable(10);

		// Initialize of DB
		if (isNeedInitialize())
			PropCMNode.initPropertyCM();

		// query
		Object node = instances.get(name);
		if (node != null)
			return (PropCMURange) node;

		// register
		node = new PropCMURange(name);
		instances.put(name, node);

		return (PropCMURange) node;
	}

	/**
	 * 
	 */
	public short getType() {
		return VAL_UNICODE_RANGE;
	}

	/**
	 * 
	 */
	void getValues(Collection values) {
		if (values != null && !values.contains(this))
			values.add(this);
	}
}