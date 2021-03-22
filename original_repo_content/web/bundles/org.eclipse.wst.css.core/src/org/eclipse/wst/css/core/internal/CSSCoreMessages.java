/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.css.core.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Strings used by CSS Core
 * 
 * @plannedfor 1.0
 */
public class CSSCoreMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.css.core.internal.CSSCorePluginResources";//$NON-NLS-1$

	public static String You_cannot_use_CSSStyleShe_UI_;
	public static String CSSContentPropertiesManager_Updating;
	public static String CSSContentPropertiesManager_Problems_Updating;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, CSSCoreMessages.class);
	}
}
