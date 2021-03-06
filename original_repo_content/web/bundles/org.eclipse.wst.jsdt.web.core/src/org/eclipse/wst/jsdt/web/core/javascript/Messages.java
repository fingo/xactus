/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
/**
 * 
 */
package org.eclipse.wst.jsdt.web.core.javascript;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * 
 * Provisional API: This class/interface is part of an interim API that is
 * still under development and expected to change significantly before
 * reaching stability. It is being made available at this early stage to
 * solicit feedback from pioneering adopters on the understanding that any
 * code that uses this API will almost certainly be broken (repeatedly) as the
 * API evolves.
 * 
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.web.core.internal.java.messages"; //$NON-NLS-1$

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(Messages.BUNDLE_NAME, Messages.class);
	}

	public static String NodeHelper00;
	public static String NodeHelper01;
	public static String NodeHelper03;
	public static String NodeHelper11;
	public static String NodeHelper12;

}
