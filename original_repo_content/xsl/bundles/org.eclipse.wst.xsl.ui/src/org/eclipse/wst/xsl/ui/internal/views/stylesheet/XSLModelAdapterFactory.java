/*******************************************************************************
 * Copyright (c) 2008, 2009 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.ui.internal.views.stylesheet;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class XSLModelAdapterFactory implements IAdapterFactory
{
	private static Class[] LIST = new Class[]{IWorkbenchAdapter.class};
	private IWorkbenchAdapter adapter = new XSLWorkbenchAdapter();
	
	public Object getAdapter(Object adaptableObject, Class adapterType)
	{
		if (IWorkbenchAdapter.class.equals(adapterType))
			return adapter;
		return null;
	}
	
	public Class[] getAdapterList()
	{
		return LIST;
	}
	
}
