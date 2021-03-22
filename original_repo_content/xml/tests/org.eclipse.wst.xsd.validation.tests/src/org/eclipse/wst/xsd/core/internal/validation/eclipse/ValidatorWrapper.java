/*******************************************************************************
 * Copyright (c) 2001, 2017 IBM Corporation and others.
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
package org.eclipse.wst.xsd.core.internal.validation.eclipse;

import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage;

/**
 * A wrapper class to allow for testing the Validator class.
 */
public class ValidatorWrapper extends Validator 
{
  /* (non-Javadoc)
   * @see org.eclipse.wst.xsd.core.internal.validation.eclipse.Validator#addInfoToMessage(org.eclipse.wst.xml.core.internal.validation.core.ValidationMessage, org.eclipse.wst.validation.internal.provisional.core.IMessage)
   */
  public void addInfoToMessage(ValidationMessage validationMessage, IMessage message) 
  {
	super.addInfoToMessage(validationMessage, message);
  }
}
