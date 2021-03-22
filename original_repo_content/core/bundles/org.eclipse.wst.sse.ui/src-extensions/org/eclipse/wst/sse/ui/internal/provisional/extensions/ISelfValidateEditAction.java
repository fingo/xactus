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
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *******************************************************************************/
package org.eclipse.wst.sse.ui.internal.provisional.extensions;



/**
 * This is a marker interface to control ValidateEdit call Usually framework
 * calls IExtendedSimpleEditor#validateEdit() before calling
 * IExtendedEditorAction's run() method. However, if the action implements
 * this interface, framework won't call validateEdit() method.
 * 
 * The action should call validateEdit() at their own appropriate timing.
 */
public interface ISelfValidateEditAction {

}
