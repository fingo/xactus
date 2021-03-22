/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.xml.core.internal.document;

import org.eclipse.osgi.util.NLS;

class DOMMessages extends NLS {
	private static String BUNDLE_NAME = "org.eclipse.wst.xml.core.internal.document.DOMMessages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, DOMMessages.class);
	}

	private DOMMessages() {
	}

	public static String DOMSTRING_SIZE_ERR;
	public static String HIERARCHY_REQUEST_ERR;
	public static String HIERARCHY_REQUEST_ERR_WITH_DETAILS;
	public static String INDEX_SIZE_ERR;
	public static String INUSE_ATTRIBUTE_ERR;
	public static String INVALID_ACCESS_ERR;
	public static String INVALID_CHARACTER_ERR;
	public static String INVALID_MODIFICATION_ERR;
	public static String INVALID_STATE_ERR;
	public static String NAMESPACE_ERR;
	public static String NO_DATA_ALLOWED_ERR;
	public static String NO_MODIFICATION_ALLOWED_ERR;
	public static String NOT_FOUND_ERR;
	public static String NOT_SUPPORTED_ERR;
	public static String SYNTAX_ERR;
	public static String TYPE_MISMATCH_ERR;
	public static String VALIDATION_ERR;
	public static String WRONG_DOCUMENT_ERR;
}
