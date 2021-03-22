/*******************************************************************************
 * Copyright (c) 2004, 2011 IBM Corporation and others.
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
package org.eclipse.jst.jsp.ui.views.contentoutline;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;
import org.eclipse.jst.jsp.ui.internal.actions.JSPNodeActionManager;
import org.eclipse.wst.html.ui.views.contentoutline.HTMLContentOutlineConfiguration;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.ui.internal.contentoutline.XMLNodeActionManager;

/**
 * Configuration for outline view page which shows JSP content.
 * 
 * @see org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration
 * @since 1.0
 */
public class JSPContentOutlineConfiguration extends HTMLContentOutlineConfiguration {

	// private static final String ATT_CLASS = "class"; //$NON-NLS-1$

	// TODO: Automate the loading of a real configuration based on the model
	// type at
	// creation time; clear on unConfigure so that a new embedded
	// configuration can
	// be used
	// private StructuredContentOutlineConfiguration fEmbeddedConfiguration =
	// null;
	private static final String OUTLINE_FILTER_PREF = "org.eclipse.jst.jsp.ui.OutlinePage"; //$NON-NLS-1$
	/**
	 * Create new instance of JSPContentOutlineConfiguration
	 */
	public JSPContentOutlineConfiguration() {
		// Must have empty constructor to createExecutableExtension
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration#getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return JSPUIPlugin.getDefault().getPreferenceStore();
	}
	
	protected XMLNodeActionManager createNodeActionManager(TreeViewer treeViewer) {
		return new JSPNodeActionManager((IStructuredModel) treeViewer.getInput(), treeViewer);
	}
	
	public String getOutlineFilterTarget(){
		return OUTLINE_FILTER_PREF ;
	}
}
