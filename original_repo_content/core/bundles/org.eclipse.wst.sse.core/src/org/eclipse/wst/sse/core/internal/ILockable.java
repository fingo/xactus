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

package org.eclipse.wst.sse.core.internal;

import org.eclipse.core.runtime.jobs.ILock;

/**
 * 
 * Not API: not to be used or implemented by clients. This is a special
 * purpose interface to help guard some threading issues betweeen model and
 * document. Will be changed soon.
 *  
 */

public interface ILockable {

	ILock getLockObject();

}
