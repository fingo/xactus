/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.jst.jsp.css.core.internal.document;

import org.eclipse.wst.css.core.internal.document.CSSModelImpl;
import org.eclipse.wst.css.core.internal.document.CSSModelParser;

public class JSPedCSSModelImpl extends CSSModelImpl {
	private JSPedCSSModelParser fParser;
	
	protected CSSModelParser getParser() {
		if (fParser == null) {
			if (getDocument() != null) {
				fParser = new JSPedCSSModelParser(getDocument());
			}
		}
		return fParser;
	}

}
