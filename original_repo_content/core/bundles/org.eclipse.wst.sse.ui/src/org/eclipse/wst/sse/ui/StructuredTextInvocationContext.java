/*******************************************************************************
 * Copyright (c) 2008, 2020 IBM Corporation and others.
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
package org.eclipse.wst.sse.ui;

import java.util.Map;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.TextInvocationContext;

/**
 * Provisional API: This class/interface is part of an interim API that is
 * still under development and expected to change significantly before
 * reaching stability. It is being made available at this early stage to
 * solicit feedback from pioneering adopters on the understanding that any
 * code that uses this API will almost certainly be broken (repeatedly) as the
 * API evolves.
 * 
 * This class is not intended to be instantiated by clients.
 * 
 * Structured Text quick assist invocation context.
 */
public final class StructuredTextInvocationContext extends TextInvocationContext {
	private Map<String, String> fAttributes;

	/**
	 * @param sourceViewer
	 * @param offset
	 * @param length
	 * @param attributes
	 */
	public StructuredTextInvocationContext(ISourceViewer sourceViewer, int offset, int length, Map<String, String> attributes) {
		super(sourceViewer, offset, length);
		fAttributes = attributes;
	}

	/**
	 * @param attributeName
	 * @return the value of this attribute, or <tt>null</tt> when no such
	 *         attribute is defined or that attribute's value has been set to
	 *         <tt>null</tt>
	 */
	public Object getAttribute(String attributeName) {
		return (fAttributes != null ? fAttributes.get(attributeName) : null);
	}
}
