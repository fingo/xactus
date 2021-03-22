/*******************************************************************************
 * Copyright (c) 2001, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.xml.core.internal.validation.core.ValidationReport
 *                                           modified in order to process JSON Objects.     
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.validation.core;

import java.util.HashMap;

/**
 * An interface for a validation report.
 * 
 * @author Lawrence Mandel, IBM
 */
public interface ValidationReport
{
  /**
   * Returns the URI for the file the report refers to.
   * 
   * @return The URI for the file the report refers to.
   */
  public String getFileURI();
  
  /**
   * Returns whether the file is valid. The file may have warnings associated with it.
   * 
   * @return True if the file is valid, false otherwise.
   */
  public boolean isValid();

  /**
   * Returns an array of validation messages.
   * 
   * @return An array of validation messages.
   */
  public ValidationMessage[] getValidationMessages();
  
  public HashMap getNestedMessages();

}
