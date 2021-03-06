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
package org.eclipse.wst.css.core.internal.util.declaration;



/**
 * 
 */
public class ValueData {

	public String value;
	public boolean important = false;

	/**
	 * 
	 */
	public ValueData() {
		super();
	}

	/**
	 * 
	 */
	public ValueData(String val, boolean imp) {
		super();
		value = val;
		important = imp;
	}

	/**
	 * 
	 */
	public String toString() {
		return value;
	}
}
