/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
package org.eclipse.wst.jsdt.web.ui.views.provisional.contentoutline;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.wst.html.ui.views.contentoutline.HTMLContentOutlineConfiguration;

/**
*

* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*/
public class JsContentOutlineConfig extends HTMLContentOutlineConfiguration {
	public static final boolean USE_ADVANCED = false;
	ILabelProvider fLabelProvider = null;
	
	public JsContentOutlineConfig() {}
	
	private ILabelProvider getJavaLabelProvider() {
		if (fLabelProvider == null) {
			fLabelProvider = new JsLabelProvider();
		}
		return fLabelProvider;
	}
	
	
	public ILabelProvider getLabelProvider(TreeViewer viewer) {
		if (!JsContentOutlineConfig.USE_ADVANCED) {
			return super.getLabelProvider(viewer);
		}
		return getJavaLabelProvider();
	}
	
	
	public IMenuListener getMenuListener(TreeViewer treeViewer) {
		// if(!USE_ADVANCED)
		// return super.getMenuListener(treeViewer);
		return new JsMenuListener(treeViewer);
	}
	
	
	public ILabelProvider getStatusLineLabelProvider(TreeViewer treeViewer) {
		if (!JsContentOutlineConfig.USE_ADVANCED) {
			return super.getStatusLineLabelProvider(treeViewer);
		}
		return getJavaLabelProvider();
	}
}
