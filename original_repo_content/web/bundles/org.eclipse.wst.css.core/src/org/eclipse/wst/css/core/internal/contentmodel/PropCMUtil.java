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
import java.util.Iterator;

/**
 * 
 */
public class PropCMUtil {

	/**
	 * 
	 */
	public static void minus(Collection src, Collection ref) {
		Iterator it = ref.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (src.contains(obj))
				src.remove(obj);
		}
		return;
	}

	/**
	 * 
	 */
	public static void plus(Collection src, Collection ref) {
		Iterator it = ref.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (!src.contains(obj))
				src.add(obj);
		}
		return;
	}
}