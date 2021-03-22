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
package org.eclipse.wst.xml.core.internal.encoding;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.document.DocumentReader;
import org.eclipse.wst.sse.core.internal.document.IDocumentCharsetDetector;
import org.eclipse.wst.xml.core.internal.contenttype.XMLResourceEncodingDetector;


/**
 * This class reads and parses first of XML file to get encoding.
 *  
 */
public class XMLDocumentCharsetDetector extends XMLResourceEncodingDetector implements IDocumentCharsetDetector {

	/**
	 * XMLLoader constructor comment.
	 */
	public XMLDocumentCharsetDetector() {
		super();
	}

	public void set(IDocument document) {
		set(new DocumentReader(document, 0));
	}
}
