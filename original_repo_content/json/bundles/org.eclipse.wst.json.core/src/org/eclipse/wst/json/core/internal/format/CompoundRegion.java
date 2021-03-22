/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.css.core.internal.formatter.CompoundRegion
 *                                           modified in order to process JSON Objects.
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.format;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

class CompoundRegion {

	CompoundRegion(IStructuredDocumentRegion documentRegion, ITextRegion textRegion) {
		super();
		this.fDocumentRegion = documentRegion;
		this.fTextRegion = textRegion;
	}

	IStructuredDocumentRegion getDocumentRegion() {
		return fDocumentRegion;
	}

	ITextRegion getTextRegion() {
		return fTextRegion;
	}

	String getType() {
		return fTextRegion.getType();
	}

	String getText() {
		return fDocumentRegion.getText(fTextRegion);
	}

	// Bug 218993: Added to get text with whitespace for cleanup
	// without formatting
	String getFullText() {
		return fDocumentRegion.getFullText(fTextRegion);
	}

	int getStartOffset() {
		return fDocumentRegion.getStartOffset(fTextRegion);
	}

	int getEndOffset() {
		return fDocumentRegion.getEndOffset(fTextRegion);
	}


	private IStructuredDocumentRegion fDocumentRegion;
	private ITextRegion fTextRegion;

}
