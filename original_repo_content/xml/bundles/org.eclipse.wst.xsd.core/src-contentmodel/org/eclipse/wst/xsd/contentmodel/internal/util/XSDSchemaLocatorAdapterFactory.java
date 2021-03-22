/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
package org.eclipse.wst.xsd.contentmodel.internal.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.xsd.util.XSDSchemaLocator;

public class XSDSchemaLocatorAdapterFactory extends AdapterFactoryImpl
{
    protected XSDSchemaLocatorImpl schemaLocator = new XSDSchemaLocatorImpl();

    public boolean isFactoryForType(Object type)
    {
      return type == XSDSchemaLocator.class;
    }

    public Adapter adaptNew(Notifier target, Object type)
    {
      return schemaLocator;
    }
}
