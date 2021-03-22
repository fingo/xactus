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
package org.eclipse.wst.xsd.ui.internal.common.commands;

import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDSchema;

public class AddXSDIncludeCommand extends AddXSDSchemaDirectiveCommand
{
  public AddXSDIncludeCommand(String label, XSDSchema schema)
  {
    super(label);
    this.xsdSchema = schema;
  }

  public void execute()
  {
    try
    {
      super.execute();
      // Add this after if we don't have a DOM Node yet
      beginRecording(xsdSchema.getElement());
      XSDInclude xsdInclude = XSDFactory.eINSTANCE.createXSDInclude();
      xsdInclude.setSchemaLocation(""); //$NON-NLS-1$
      xsdSchema.getContents().add(findNextPositionToInsert(), xsdInclude);
      addedXSDConcreteComponent = xsdInclude;
      formatChild(xsdSchema.getElement());
    }
    finally
    {
      endRecording();
    }
  }
}
