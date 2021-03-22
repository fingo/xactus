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
package org.eclipse.wst.sse.core.internal.exceptions;


public class MalformedOutputExceptionWithDetail extends MalformedInputExceptionWithDetail {

	/**
	 * Default <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor for MalformedOutputExceptionWithDetail. If attemptedEncoding and charPostion can
	 * not be provided, use sun.io.MalformedException.
	 * 
	 * @param attemptedJavaEncoding
	 * @param attemptedIANAEncoding
	 * @param charPostion
	 */
	public MalformedOutputExceptionWithDetail(String attemptedJavaEncoding, String attemptedIANAEncoding, int charPostion) {
		super(attemptedJavaEncoding, attemptedIANAEncoding, charPostion);
	}

}
