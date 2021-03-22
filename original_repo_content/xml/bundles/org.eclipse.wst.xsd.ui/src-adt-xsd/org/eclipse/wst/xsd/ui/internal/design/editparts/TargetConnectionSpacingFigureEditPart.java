/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
package org.eclipse.wst.xsd.ui.internal.design.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.BaseEditPart;
import org.eclipse.wst.xsd.ui.internal.design.figures.SpacingFigure;

public class TargetConnectionSpacingFigureEditPart extends BaseEditPart
{
  public TargetConnectionSpacingFigureEditPart()
  {
    super();
  }

  SpacingFigure figure;

  protected IFigure createFigure()
  {
    figure = new SpacingFigure();
    return figure;
  }

  public IFigure getConnectionFigure()
  {
    return figure;
  }

  protected void createEditPolicies()
  {

  }
  
  public boolean isSelectable()
  {
    return false;
  }
}
