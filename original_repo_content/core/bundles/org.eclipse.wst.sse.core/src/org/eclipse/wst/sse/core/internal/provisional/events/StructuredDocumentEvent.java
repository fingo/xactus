/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;


/**
 * IStructuredDocument events are generated by the IStructuredDocument, after
 * the IStructuredDocument acts on a request. Not intended to be instantiated,
 * except by subclasses in infrastructure. Not intended to be subclassed by
 * clients.
 * 
 * @plannedfor 1.0
 */
public abstract class StructuredDocumentEvent extends DocumentEvent {
	private String fDeletedText;
	private Object fOriginalRequester;

	/**
	 * There is no public null-arg version of this constructor.
	 */
	private StructuredDocumentEvent() {
		super();
	}

	/**
	 * We assume (and require) that an IStructuredDocument's are always the
	 * source of StructuredDocument events.
	 * 
	 * @param document -
	 *            the document being changed
	 */
	private StructuredDocumentEvent(IStructuredDocument document) {
		this();
		if (document == null)
			throw new IllegalArgumentException("null source"); //$NON-NLS-1$
		fDocument = document;
		fOriginalRequester = document;
	}

	/**
	 * We assume (and require) that an IStructuredDocument's are always the
	 * source of StructuredDocument events.
	 * 
	 * @param document -
	 *            the document being changed.
	 * @param originalRequester -
	 *            the original requester of the change.
	 */
	StructuredDocumentEvent(IStructuredDocument document, Object originalRequester) {
		this(document);
		fOriginalRequester = originalRequester;
	}

	/**
	 * We assume (and require) that an IStructuredDocument's are always the
	 * source of StructuredDocument events.
	 * 
	 * @param document -
	 *            the document being changed.
	 * @param originalRequester -
	 *            the requester of the change.
	 * @param changes -
	 *            the String representing the new text
	 * @param offset -
	 *            the offset of the change.
	 * @param lengthToReplace -
	 *            the length of text to replace.
	 */
	StructuredDocumentEvent(IStructuredDocument document, Object originalRequester, String changes, int offset, int lengthToReplace) {
		this(document);
		fOriginalRequester = originalRequester;
		fText = changes;
		fOffset = offset;
		fLength = lengthToReplace;
	}

	/**
	 * Provides the text that is being deleted.
	 * 
	 * @return the text that is being deleted, or null if none is being
	 *         deleted.
	 */
	public String getDeletedText() {
		return fDeletedText;
	}

	/**
	 * This method returns the object that originally caused the event to
	 * fire. This is typically not the object that created the event (the
	 * IStructuredDocument) but instead the object that made a request to the
	 * IStructuredDocument.
	 * 
	 * @return the object that made the request to the document
	 */
	public Object getOriginalRequester() {
		return fOriginalRequester;
	}

	/**
	 * This method is equivalent to 'getDocument' except it returns an object
	 * of the appropriate type (namely, a IStructuredDocument, instead of
	 * IDocument).
	 * 
	 * @return IStructuredDocumnt - the document being changed
	 */
	public IStructuredDocument getStructuredDocument() {
		// a safe case, since constructor can only be called with a
		// IStructuredDocument
		return (IStructuredDocument) fDocument;
	}

	/**
	 * Not to be called by clients, only parsers and reparsers. (will
	 * eventually be moved to an SPI package).
	 * 
	 * @param newDeletedText -
	 *            the text that has been deleted.
	 */
	public void setDeletedText(String newDeletedText) {
		fDeletedText = newDeletedText;
	}

	/**
	 * for debugging only
	 * 
	 * @deprecated - need to fix unit tests which depend on this exact format,
	 *             then delete this
	 */
	public String toString() {
		// return getClass().getName() + "[source=" + source + "]";
		return getClass().getName();
	}
}
