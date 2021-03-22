/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
package org.eclipse.wst.xsd.ui.internal.adt.design.directedit;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.facade.IField;
import org.eclipse.wst.xsd.ui.internal.editor.XSDAttributeReferenceEditManager;

public class AttributeReferenceDirectEditManager extends ReferenceDirectEditManager
{
  public AttributeReferenceDirectEditManager(IField parameter, AbstractGraphicalEditPart source, Label label)
  {
    super(parameter, source, label);
  }

  protected ComponentReferenceEditManager getComponentReferenceEditManager()
  {
    ComponentReferenceEditManager result = null;
    IEditorPart editor = getActiveEditor();
    if (editor != null)
    {
      result = (ComponentReferenceEditManager)editor.getAdapter(XSDAttributeReferenceEditManager.class);
    }  
    return result;
  }
  
}
