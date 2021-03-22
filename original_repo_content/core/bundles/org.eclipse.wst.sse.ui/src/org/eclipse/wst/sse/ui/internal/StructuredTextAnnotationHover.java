/*******************************************************************************
 * Copyright (c) 2001, 2018 IBM Corporation and others.
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.internal.text.html.HTMLPrinter;
import org.eclipse.jface.text.source.DefaultAnnotationHover;

/**
 * Determines all messages for the given line and collects, concatenates, and
 * formats them in HTML. Use of the internal HTMLPrinter was observed in
 * org.eclipse.jdt.internal.ui.text.HTMLAnnotationHover .
 */
public class StructuredTextAnnotationHover extends DefaultAnnotationHover {

	public StructuredTextAnnotationHover() {
		super();
	}

	public StructuredTextAnnotationHover(boolean showLineNumber) {
		super(showLineNumber);
	}

	/*
	 * Formats a message as HTML text.
	 */
	protected String formatSingleMessage(String message) {
		StringBuilder buffer = new StringBuilder();
		HTMLPrinter.addPageProlog(buffer);
		HTMLPrinter.addParagraph(buffer, HTMLPrinter.convertToHTMLContent(message));
		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}

	/*
	 * Formats several message as HTML text.
	 */
	protected String formatMultipleMessages(List messages) {
		StringBuilder buffer = new StringBuilder();
		HTMLPrinter.addPageProlog(buffer);
		HTMLPrinter.addParagraph(buffer, HTMLPrinter.convertToHTMLContent(SSEUIMessages.Multiple_errors));

		HTMLPrinter.startBulletList(buffer);
		Set collectedMessages = new HashSet();
		Iterator e = messages.iterator();
		while (e.hasNext()) {
			String converted = HTMLPrinter.convertToHTMLContent((String) e.next());
			collectedMessages.add(converted);
		}
		e = collectedMessages.iterator();
		while (e.hasNext()) {
			HTMLPrinter.addBullet(buffer, e.next().toString());
		}
		HTMLPrinter.endBulletList(buffer);

		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.IReleasable#release()
	 */
	public void release() {
	}
}
