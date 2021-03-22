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
package org.eclipse.wst.xml.core.internal.document;



/**
 * Thrown an invalid character is specified in : XMLNode#setSource(String)
 */
public class InvalidCharacterException extends Exception {

	/**
	 * Default <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	private char invalidChar = 0;
	private int offset = -1;

	/**
	 */
	public InvalidCharacterException() {
		super();
	}

	/**
	 */
	public InvalidCharacterException(String s) {
		super(s);
	}

	/**
	 */
	public InvalidCharacterException(String s, char c) {
		super(s);
		this.invalidChar = c;
	}

	/**
	 */
	public InvalidCharacterException(String s, char c, int offset) {
		super(s);
		this.invalidChar = c;
		this.offset = offset;
	}

	/**
	 */
	public char getInvalidChar() {
		return this.invalidChar;
	}

	/**
	 */
	public int getOffset() {
		return this.offset;
	}
}
