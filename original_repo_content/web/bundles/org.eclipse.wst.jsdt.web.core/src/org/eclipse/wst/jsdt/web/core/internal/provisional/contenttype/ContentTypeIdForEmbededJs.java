/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.wst.jsdt.web.core.internal.provisional.contenttype;
/**
*
* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*/
public class ContentTypeIdForEmbededJs {
	/**
	 * The value of the contenttype id field must match what is specified in
	 * plugin.xml file. Note: this value is intentially set with default
	 * protected method so it will not be inlined.
	 */
	public final static String[] ContentTypeIds = ContentTypeIdForEmbededJs. getJsConstantString();

	static String[] getJsConstantString() {
		return new String[] {"org.eclipse.wst.html.core.htmlsource","org.eclipse.jst.jsp.core.jspsource"}; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private ContentTypeIdForEmbededJs() {
		super();
	}
}
