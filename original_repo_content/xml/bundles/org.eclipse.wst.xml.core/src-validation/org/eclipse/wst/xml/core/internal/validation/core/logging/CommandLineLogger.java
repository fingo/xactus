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

package org.eclipse.wst.xml.core.internal.validation.core.logging;

/**
 * A logger that will print log info to System.out.
 * 
 * @author Lawrence Mandel, IBM
 */
public class CommandLineLogger implements ILogger
{
  public void logError(String error, Throwable exception)
  {
    System.out.println(error);
    System.out.println(exception.toString());
  }

  public void logWarning(String warning, Throwable exception)
  {
    System.out.println(warning);
    System.out.println(exception.toString());
  }
}
