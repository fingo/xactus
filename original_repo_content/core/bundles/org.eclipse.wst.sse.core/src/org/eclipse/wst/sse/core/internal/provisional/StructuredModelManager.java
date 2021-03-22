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
 *     
 *******************************************************************************/
package org.eclipse.wst.sse.core.internal.provisional;

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.sse.core.internal.SSECorePlugin;
import org.eclipse.wst.sse.core.internal.model.ModelManagerImpl;
import org.osgi.framework.Bundle;

/**
 * Class to allow access to ModelManager. Not intended to be subclassed.
 * 
 * @deprecated - use {@link org.eclipse.wst.sse.core.StructuredModelManager} instead
 */
final public class StructuredModelManager {
	/**
	 * Do not allow instances to be created.
	 */
	private StructuredModelManager() {
		super();
	}

	/**
	 * Provides access to the instance of IModelManager. Returns null if model
	 * manager can not be created or is not valid (such as, when workbench is
	 * shutting down).
	 * 
	 * @return IModelManager - returns the one model manager for structured
	 *         model
	 * @deprecated - use the one that is in
	 *             org.eclipse.wst.sse.core.StructuredModelManager
	 */
	public static IModelManager getModelManager() {
		boolean isReady = false;
		IModelManager modelManager = null;
		while (!isReady) {
			Bundle localBundle = Platform.getBundle(SSECorePlugin.ID);
			int state = localBundle.getState();
			if (state == Bundle.ACTIVE) {
				isReady = true;
				// getInstance is a synchronized static method.
				modelManager = ModelManagerImpl.getInstance();
			}
			else if (state == Bundle.STARTING) {
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					// ignore, just loop again
				}
			}
			else if (state == Bundle.STOPPING || state == Bundle.UNINSTALLED) {
				isReady = true;
				modelManager = null;
			}
			else {
				// not sure about other states, 'resolved', 'installed'
				isReady = true;
				modelManager = null;
			}
		}
		return modelManager;
	}
}
