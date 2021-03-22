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
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.css.core.internal.formatter.StyleSheetFormatter
 *                                           modified in order to process JSON Objects.
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.format;

import org.eclipse.jface.text.IRegion;
import org.eclipse.wst.json.core.document.IJSONNode;

public class JSONDocumentFormatter extends AbstractJSONSourceFormatter {

	private static JSONDocumentFormatter instance;

	JSONDocumentFormatter() {
		super();
	}

	public synchronized static JSONDocumentFormatter getInstance() {
		if (instance == null)
			instance = new JSONDocumentFormatter();
		return instance;
	}


	@Override
	protected void formatPost(IJSONNode node, StringBuilder source) {
	}


	@Override
	protected void formatPost(IJSONNode node, IRegion region, StringBuilder source) {
	}


	@Override
	protected void formatPre(IJSONNode node, StringBuilder source) {
	}


	@Override
	protected void formatPre(IJSONNode node, IRegion region, StringBuilder source) {
	}

}
