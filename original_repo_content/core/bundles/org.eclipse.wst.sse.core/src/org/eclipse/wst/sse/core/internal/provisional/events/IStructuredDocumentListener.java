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
package org.eclipse.wst.sse.core.internal.provisional.events;

/**
 * @deprecated will be removed since we now subclass DocumentEvent.
 */

public interface IStructuredDocumentListener {

	public void newModel(NewDocumentEvent structuredDocumentEvent);

	public void noChange(NoChangeEvent structuredDocumentEvent);

	public void nodesReplaced(StructuredDocumentRegionsReplacedEvent structuredDocumentEvent);

	public void regionChanged(RegionChangedEvent structuredDocumentEvent);

	public void regionsReplaced(RegionsReplacedEvent structuredDocumentEvent);
}
