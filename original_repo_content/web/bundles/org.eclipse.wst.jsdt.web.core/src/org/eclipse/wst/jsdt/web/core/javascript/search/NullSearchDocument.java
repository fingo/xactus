/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.web.core.javascript.search;

import org.eclipse.wst.jsdt.core.search.SearchDocument;


/**
*

* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*
 * An empty servlet, safe for Java search participation
 * 
 * @author pavery
 */
public class NullSearchDocument extends SearchDocument {
	
	StringBuffer fEmptyServletBuffer = null;
	
	public NullSearchDocument(String documentPath) {
		super(documentPath, new JsSearchParticipant()); 
		this.fEmptyServletBuffer = new StringBuffer();
	}
	
	public byte[] getByteContents() {
		return this.fEmptyServletBuffer.toString().getBytes();
	}
	
	public char[] getCharContents() {
		return this.fEmptyServletBuffer.toString().toCharArray();
	}
	
	public String getEncoding() {
		return null;
	}

}
