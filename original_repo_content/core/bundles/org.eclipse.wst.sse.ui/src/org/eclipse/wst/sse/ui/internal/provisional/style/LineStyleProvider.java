/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
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

import java.util.Collection;

import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;

/**
 * This interface is not intended to be implemented by clients. Clients
 * should instead subclass AbstractLineStyleProvider
 *
 */
public interface LineStyleProvider {

	/**
	 * Initializes this provider for the given Highlighter and document. This
	 * method will be called at least once before prepareRegions is called.
	 * 
	 * @param document
	 * @param highlighter
	 */
	void init(IStructuredDocument document, Highlighter highlighter);

	/**
	 * Optionally appends StyleRanges to the styleRanges Collection, in order,
	 * and only covering the given range within the document. StyleRanges added
	 * to the collection should not expand outside of the range of the currentRegion
	 * ITypedRegion.
	 * 
	 * @param currentRegion -
	 *            the current document partition
	 * @param start
	 * @param length
	 * @param styleRanges
	 * @return whether this LineStyleProvider handled the request; handling
	 *         includes not adding StyleRanges if that is still the correct
	 *         behavior
	 */
	boolean prepareRegions(ITypedRegion currentRegion, int start, int length, Collection styleRanges);

	/**
	 * Instructs this provider to free up any "resources" it might be holding
	 * on to (such as listening for preference changes). It is only called
	 * once in the lifetime of this provider.
	 */
	void release();
}
