/*******************************************************************************
 * Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     David Carver (STAR) - bug 262046 - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.xsl.launching.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.core.IConfigurationElementConstants;
import org.eclipse.wst.xsl.internal.debug.ui.XSLLaunchShortcut;
import org.eclipse.wst.xsl.launching.XSLLaunchConfigurationConstants;

public class MockXSLLaunchShortCut extends XSLLaunchShortcut {
	
	public ILaunchManager testGetLaunchManager()
	{
		return DebugPlugin.getDefault().getLaunchManager();
	}

	public ILaunchConfigurationType testGetConfigurationType()
	{
		return getLaunchManager().getLaunchConfigurationType(XSLLaunchConfigurationConstants.ID_LAUNCH_CONFIG_TYPE);
	}
	

}
