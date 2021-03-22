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
package org.eclipse.wst.xsd.ui.internal.adt.design.figures;

import org.eclipse.draw2d.Label;


public interface IFieldFigure extends IADTFigure
{
  Label getTypeLabel();
  Label getNameLabel();
  Label getNameAnnotationLabel();
  Label getTypeAnnotationLabel();
  void recomputeLayout();
}
