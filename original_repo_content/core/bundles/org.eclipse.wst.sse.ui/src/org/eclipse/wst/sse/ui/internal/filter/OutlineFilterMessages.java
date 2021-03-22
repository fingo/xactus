/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
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
package org.eclipse.wst.sse.ui.internal.filter;

import org.eclipse.osgi.util.NLS;

public final class OutlineFilterMessages extends NLS {

	private static final String BUNDLE_NAME= "org.eclipse.wst.sse.ui.internal.filter.OutlineFilterMessages";//$NON-NLS-1$

	private OutlineFilterMessages() {
		// Do not instantiate
	}

	public static String CustomFiltersDialog_title;
	public static String CustomFiltersDialog_patternInfo;
	public static String CustomFiltersDialog_enableUserDefinedPattern;
	public static String CustomFiltersDialog_filterList_label;
	public static String CustomFiltersDialog_description_label;
	public static String CustomFiltersDialog_SelectAllButton_label;
	public static String CustomFiltersDialog_DeselectAllButton_label;
	public static String CustomFiltersDialog_name_filter_pattern_description;
	public static String OpenCustomFiltersDialogAction_text;
	public static String FilterDescriptor_filterDescriptionCreationError_message;
	public static String FilterDescriptor_filterCreationError_message;

	static {
		NLS.initializeMessages(BUNDLE_NAME, OutlineFilterMessages.class);
	}
}
