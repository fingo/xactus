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
package org.eclipse.wst.dtd.core.internal.encoding;

import org.eclipse.wst.sse.core.internal.encoding.EncodingMemento;
import org.eclipse.wst.sse.core.internal.encoding.NonContentBasedEncodingRules;



/**
 * This class can be used in place of an EncodingMemento (its super class),
 * when there is not in fact ANY encoding information. For example, when a
 * structuredDocument is created directly from a String
 */
public class NullMemento extends EncodingMemento {
	/**
	 * 
	 */
	public NullMemento() {
		super();
		String defaultCharset = NonContentBasedEncodingRules.useDefaultNameRules(null);
		setJavaCharsetName(defaultCharset);
		setAppropriateDefault(defaultCharset);
		setDetectedCharsetName(null);
	}
}
