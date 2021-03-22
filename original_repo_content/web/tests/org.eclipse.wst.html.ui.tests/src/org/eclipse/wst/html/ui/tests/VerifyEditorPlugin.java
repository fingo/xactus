/*******************************************************************************
 * Copyright (c) 2004, 2017 IBM Corporation and others.
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
package org.eclipse.wst.html.ui.tests;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.html.ui.internal.HTMLUIPlugin;


public class VerifyEditorPlugin extends TestCase {

	public void testPluginExists() {
		// TODO: see https://bugs.eclipse.org/bugs/show_bug.cgi?id=81527
		assertTrue(true);
		Plugin plugin = null;
		try {
			plugin = HTMLUIPlugin.getInstance();
		}
		catch (Exception e) {
			plugin = null;
		}
		assertNotNull("editor plugin could not be instantiated", plugin);
	}
}
