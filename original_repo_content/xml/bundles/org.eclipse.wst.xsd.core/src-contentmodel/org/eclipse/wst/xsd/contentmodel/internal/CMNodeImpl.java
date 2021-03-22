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
package org.eclipse.wst.xsd.contentmodel.internal;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;

public abstract class CMNodeImpl extends AdapterImpl implements CMNode
{
  protected static final String PROPERTY_DOCUMENTATION = "documentation";
  protected static final String PROPERTY_DOCUMENTATION_SOURCE = "documentationSource";
  protected static final String PROPERTY_DOCUMENTATION_LANGUAGE = "documentationLanguage";
  protected static final String PROPERTY_MOF_NOTIFIER = "key";
  protected static final String PROPERTY_DEFINITION_INFO = "http://org.eclipse.wst/cm/properties/definitionInfo";
  protected static final String PROPERTY_DEFINITION = "http://org.eclipse.wst/cm/properties/definition";

  public abstract Object getKey();

  public boolean supports(String propertyName)
  {
    return propertyName.equals(PROPERTY_MOF_NOTIFIER);
  }

  public Object getProperty(String propertyName)
  {
    return null;
  }

  public void setProperty(String propertyName, Object object)
  {
	  //no propertyes supported? 
  }
}
