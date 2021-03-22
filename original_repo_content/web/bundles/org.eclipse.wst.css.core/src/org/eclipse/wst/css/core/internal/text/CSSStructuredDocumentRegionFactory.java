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
package org.eclipse.wst.css.core.internal.text;



import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocumentRegion;

/**
 * 
 */
public class CSSStructuredDocumentRegionFactory {

	public static final int CSS_GENERIC = 101109;

	/**
	 * 
	 */
	public static IStructuredDocumentRegion createStructuredDocumentRegion(int type) {
		IStructuredDocumentRegion instance = null;
		switch (type) {
			case CSS_GENERIC :
				instance = new BasicStructuredDocumentRegion();
				break;
			default :
				break;
		}
		return instance;
	}
}
