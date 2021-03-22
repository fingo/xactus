/*******************************************************************************
 * Copyright (c) 2008, 2013 IBM Corporation and others.
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
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 *     
 *     
 *******************************************************************************/



package org.eclipse.wst.jsdt.web.core.javascript;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.web.core.internal.project.ModuleCoreSupport;
/**
*

* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*/
public class WebRootFinder {
	public static IPath getServerContextRoot(IProject project) {
		IPath root = ModuleCoreSupport.getWebContentRootPath(project);
		if (root != null)
			return root;
		return Path.ROOT;
	}
	
	public static IPath getWebContentFolder(IProject project) {
		IPath root = ModuleCoreSupport.getWebContentRootPath(project);
		if (root != null)
			return root.removeFirstSegments(1);
		return Path.ROOT;
	}
	
	public static String getWebContext(IProject project) {
		return null;
	}
}
