/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
package org.eclipse.wst.xsd.ui.internal.common.properties.sections.appinfo.custom;

import org.eclipse.jface.viewers.LabelProvider;

public abstract class ListNodeEditorConfiguration extends NodeEditorConfiguration
{
  private LabelProvider labelProvider;
 
  public LabelProvider getLabelProvider()
  {
    return labelProvider;
  }
  
  public int getStyle()
  {
    return STYLE_COMBO;
  }    

  public void setLabelProvider(LabelProvider labelProvider)
  {
    this.labelProvider = labelProvider;
  }

  public abstract Object[] getValues(Object propertyObject);
}
