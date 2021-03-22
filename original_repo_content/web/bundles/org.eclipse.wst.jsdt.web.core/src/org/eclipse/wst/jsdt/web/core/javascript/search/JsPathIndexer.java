/*******************************************************************************
 * Copyright (c) 2004, 2018 IBM Corporation and others.
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
package org.eclipse.wst.jsdt.web.core.javascript.search;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.web.core.internal.validation.Util;

/**
*

* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*(copied from JSP)
 * pa_TODO Still need to take into consideration:
 * 	- focus in workspace
 *  - search pattern
 * 
 * @author pavery
 */
public class JsPathIndexer {

	// for debugging
	static final boolean DEBUG;
	static {
		
		String value = Platform.getDebugOption("org.eclipse.wst.jsdt.web.core/debug/jssearch"); //$NON-NLS-1$
 		DEBUG = value != null && value.equalsIgnoreCase("true"); //$NON-NLS-1$

	}
	
	// visitor that retrieves jsp project paths for all jsp files in the workspace
	class JSPFileVisitor implements IResourceProxyVisitor {
		// hash map forces only one of each file
		private Set<IPath> fPaths = new HashSet<>();
		IJavaScriptSearchScope fScope = null;
		SearchPattern fPattern = null;

		public JSPFileVisitor(SearchPattern pattern, IJavaScriptSearchScope scope) {
			this.fPattern = pattern;
			this.fScope = scope;
		}

		public boolean visit(IResourceProxy proxy) throws CoreException {
			if (JsSearchSupport.getInstance().isCanceled() || proxy.isDerived()) {
				return false;
			}
			
			if (proxy.getType() == IResource.FILE) {
				if(Util.isJsType(proxy.getName())){	
					if (this.fScope.encloses(proxy.requestFullPath().toString())) {
						IPath folderPath = proxy.requestFullPath().removeLastSegments(1);
						if (DEBUG) {
							System.out.println("adding selected index path:" + folderPath); //$NON-NLS-1$
						}
						fPaths.add(JsSearchSupport.getInstance().computeIndexLocation(folderPath));
					}
				}
				// don't search deeper for files
				return false;
			}
			return true;
		}

		public IPath[] getPaths() {
			return fPaths.toArray(new IPath[fPaths.size()]);
		}
	}

	public IPath[] getVisibleJspPaths(SearchPattern pattern, IJavaScriptSearchScope scope) {

		JSPFileVisitor jspFileVisitor = new JSPFileVisitor(pattern, scope);
		try {
			ResourcesPlugin.getWorkspace().getRoot().accept(jspFileVisitor, 0);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		return jspFileVisitor.getPaths();
	}
}

