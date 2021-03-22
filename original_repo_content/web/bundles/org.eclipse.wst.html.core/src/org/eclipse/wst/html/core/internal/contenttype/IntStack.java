/*******************************************************************************
 * Copyright (c) 2001, 2012 IBM Corporation and others.
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
package org.eclipse.wst.html.core.internal.contenttype;

/*
 * 
 * A non-resizable class implementing the behavior of java.util.Stack, but
 * directly for the <code> integer </code> primitive.
 */
import java.util.EmptyStackException;

public class IntStack {
	private int[] list = null;

	private int size = 0;

	public IntStack() {
		this(100);
	}

	public IntStack(int maxdepth) {
		super();
		list = new int[maxdepth];
		initialize();
	}

	public void clear() {
		initialize();
	}

	public boolean empty() {
		return size == 0;
	}

	public int get(int slot) {
		return list[slot];
	}

	private void initialize() {
		for (int i = 0; i < list.length; i++)
			list[i] = -1;
	}

	/**
	 * Returns the int at the top of the stack without removing it
	 * 
	 * @return int at the top of this stack.
	 * @exception EmptyStackException
	 *                when empty.
	 */
	public int peek() {
		if (size == 0)
			throw new EmptyStackException();
		return list[size - 1];
	}

	/**
	 * Removes and returns the int at the top of the stack
	 * 
	 * @return int at the top of this stack.
	 * @exception EmptyStackException
	 *                when empty.
	 */
	public int pop() {
		int value = peek();
		list[size - 1] = -1;
		size--;
		return value;
	}

	/**
	 * Pushes an item onto the top of this stack.
	 * 
	 * @param newValue -
	 *            the int to be pushed onto this stack.
	 * @return the <code>newValue</code> argument.
	 */
	public int push(int newValue) {
		if (size == list.length) {
			throw new StackFullException(newValue);
		}
		list[size++] = newValue;
		return newValue;
	}

	public class StackFullException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public StackFullException(int value) {
			super("Tried to push " + value + " on to a full stack[" + list.length + "].");
		}
	}

	public int size() {
		return size;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer s = new StringBuffer(getClass().getName() + ":" +size + " [");
		for (int i = 0; i < size; i++) {
			s.append(list[i]);
			if(i < size - 1) {
				s.append(", ");
			}
		}
		s.append("]");
		return s.toString();
	}
}
