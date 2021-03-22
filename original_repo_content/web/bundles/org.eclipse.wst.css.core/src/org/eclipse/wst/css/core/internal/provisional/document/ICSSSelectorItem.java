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
package org.eclipse.wst.css.core.internal.provisional.document;



/**
 * 
 */
public interface ICSSSelectorItem {

	public static final int SIMPLE = 1;
	public static final int COMBINATOR = 2;

	/**
	 * @return int
	 */
	int getItemType();

	/**
	 * @return java.lang.String
	 */
	String getString();
}
