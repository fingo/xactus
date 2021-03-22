/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.jface.text.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TextHoverPopupTest extends TestCase {


	public TextHoverPopupTest(String name) {
		super(name);
	}

	protected void setUp() {
	}

	public static Test suite() {
		return new TestSuite(TextHoverPopupTest.class);
	}

	protected void tearDown() {
	}


	protected int search(int[] array, int x) {
		int low= 0;
		int high= array.length -1;

		while (high > low) {
			int offset= (low + high) / 2;
			int lookup= array[offset];
			if (lookup > x)
				high= offset - 1;
			else if (lookup < x)
				low= offset + 1;
			else
				low= high= offset;
		}

		return high;
	}

	public void testSearch() {
		int[] values= { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		for (int i= 0; i < 10; i++) {
			int result= search(values, i);
			assertTrue(i == result);
		}

		int[] values2= { 0, 3, 6, 9, 12, 15, 18, 21, 24, 27 };
		for (int i= 0; i < 10; i++) {
			int result= search(values2, i * 3);
			assertTrue(i == result);
		}
	}
}
