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
package org.eclipse.wst.sse.ui.internal;



import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.ui.IEditorPart;

/**
 * TextDropAction
 */
public class TextDropAction extends AbstractDropAction {
	public boolean run(DropTargetEvent event, IEditorPart targetEditor) {
		return insert((String) event.data, targetEditor);
	}
}
