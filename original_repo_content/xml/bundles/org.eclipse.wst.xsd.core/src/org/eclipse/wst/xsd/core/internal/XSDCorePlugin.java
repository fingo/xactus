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
*     IBM Corporation - Initial API and implementation
*******************************************************************************/
package org.eclipse.wst.xsd.core.internal;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.xsd.contentmodel.internal.XSDCMManager;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class XSDCorePlugin extends Plugin {
	//The shared instance.
	private static XSDCorePlugin plugin;
	
	/**
	 * The constructor.
	 */
	public XSDCorePlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
    XSDCMManager.getInstance().startup();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static XSDCorePlugin getDefault() {
		return plugin;
	}


}
