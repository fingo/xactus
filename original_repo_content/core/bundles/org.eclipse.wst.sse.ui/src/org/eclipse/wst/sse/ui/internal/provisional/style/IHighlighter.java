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

package org.eclipse.wst.sse.ui.internal.provisional.style;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;

/**
 * @author davidw
 * 
 */
public interface IHighlighter extends LineStyleListener {
	void addProvider(String partitionType, LineStyleProvider provider);

	void install(ITextViewer viewer);

	void removeProvider(String partitionType);

	void setDocument(IStructuredDocument structuredDocument);
	
	void uninstall();
}
