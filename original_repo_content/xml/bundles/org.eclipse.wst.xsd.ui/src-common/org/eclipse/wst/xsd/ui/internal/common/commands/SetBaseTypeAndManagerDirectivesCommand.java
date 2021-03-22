/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
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
package org.eclipse.wst.xsd.ui.internal.common.commands;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.xsd.ui.internal.common.util.XSDDirectivesManager;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDTypeDefinition;

public class SetBaseTypeAndManagerDirectivesCommand extends UpdateTypeReferenceAndManageDirectivesCommand
{

  public SetBaseTypeAndManagerDirectivesCommand(XSDConcreteComponent concreteComponent, String componentName, String componentNamespace, IFile file)
  {
    super(concreteComponent, componentName, componentNamespace, file);
  }

  public void execute()
  {
    try
    {
      beginRecording(concreteComponent.getElement());
      XSDComponent td = computeComponent();
      if (td != null && td instanceof XSDTypeDefinition)
      {
        SetBaseTypeCommand command = new SetBaseTypeCommand(concreteComponent, (XSDTypeDefinition) td);
        command.execute();
        XSDDirectivesManager.removeUnusedXSDImports(concreteComponent.getSchema());
      }
    }
    catch (Exception e)
    {

    }
    finally
    {
      endRecording();
    }
  }
}
