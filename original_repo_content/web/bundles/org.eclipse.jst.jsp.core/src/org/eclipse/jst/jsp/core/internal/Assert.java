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
package org.eclipse.jst.jsp.core.internal;



/**
 * <code>Assert</code> is useful for for embedding runtime sanity checks in
 * code. The predicate methods all test a condition and throw some type of
 * unchecked exception if the condition does not hold.
 * <p>
 * Assertion failure exceptions, like most runtime exceptions, are thrown when
 * something is misbehaving. Assertion failures are invariably unspecified
 * behavior; consequently, clients should never rely on these being thrown
 * (and certainly should not being catching them specifically).
 * </p>
 */
public final class Assert {

	/**
	 * <code>AssertionFailedException</code> is a runtime exception thrown
	 * by some of the methods in <code>Assert</code>.
	 * <p>
	 * This class is not declared public to prevent some misuses; programs
	 * that catch or otherwise depend on assertion failures are susceptible to
	 * unexpected breakage when assertions in the code are added or removed.
	 * </p>
	 */
	class AssertionFailedException extends RuntimeException {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a new exception.
		 */
		public AssertionFailedException() {
			super();
		}

		/**
		 * Constructs a new exception with the given message.
		 */
		public AssertionFailedException(String detail) {
			super(detail);
		}
	}

	/**
	 * Asserts that an argument is legal. If the given boolean is not
	 * <code>true</code>, an <code>IllegalArgumentException</code> is
	 * thrown. The given message is included in that exception, to aid
	 * debugging.
	 * 
	 * @param expression
	 *            the outcode of the check
	 * @param message
	 *            the message to include in the exception
	 * @return <code>true</code> if the check passes (does not return if the
	 *         check fails)
	 * @exception IllegalArgumentException
	 *                if the legality test failed
	 */
	public static boolean isLegal(boolean expression, String message) {
		if (!expression)
			throw new IllegalArgumentException(message);
		return expression;
	}

	/**
	 * Asserts that the given object is not <code>null</code>. If this is
	 * not the case, some kind of unchecked exception is thrown. The given
	 * message is included in that exception, to aid debugging.
	 * 
	 * @param object
	 *            the value to test
	 * @param message
	 *            the message to include in the exception
	 * @exception IllegalArgumentException
	 *                if the object is <code>null</code>
	 */
	public static void isNotNull(Object object, String message) {
		if (object == null) {
			throw new Assert().new AssertionFailedException(message);
		}
	}

	/**
	 * Asserts that the given boolean is <code>true</code>. If this is not
	 * the case, some kind of unchecked exception is thrown. The given message
	 * is included in that exception, to aid debugging.
	 * 
	 * @param expression
	 *            the outcode of the check
	 * @param message
	 *            the message to include in the exception
	 * @return <code>true</code> if the check passes (does not return if the
	 *         check fails)
	 */
	public static boolean isTrue(boolean expression, String message) {
		if (!expression) {
			throw new Assert().new AssertionFailedException(message);
		}
		return expression;
	}

	/* This class is not intended to be instantiated. */
	private Assert() {
	}
}
