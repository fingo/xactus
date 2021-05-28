/*******************************************************************************
 * Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver - initial API and implementation
 *     Jesper Steen Moller - bug 283404 - added locale sensitivity test
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.test;

import java.util.Locale;

import info.fingo.xactus.processor.internal.types.XPathDecimalFormat;

import junit.framework.TestCase;

public class XPathDecimalFormatTest extends TestCase {

	public void testDoublePositiveInfinity() {
		Double value = new Double(Double.POSITIVE_INFINITY);
		String result = XPathDecimalFormat.xpathFormat(value);
		assertEquals("Unexpected XPath format String:", "INF", result);
	}

	public void testDoubleNegativeInfinity() {
		Double value = new Double(Double.NEGATIVE_INFINITY);
		String result = XPathDecimalFormat.xpathFormat(value);
		assertEquals("Unexpected XPath format string:", "-INF", result);
	}

	public void testFloatPositiveInfinity() {
		Float value = new Float(Float.POSITIVE_INFINITY);
		String result = XPathDecimalFormat.xpathFormat(value);
		assertEquals("Unexpected XPath format string:", "INF", result);
	}

	public void testFloatNegativeInfinity() {
		Float value = new Float(Float.NEGATIVE_INFINITY);
		String result = XPathDecimalFormat.xpathFormat(value);
		assertEquals("Unexpected XPath format string:", "-INF", result);
	}

	public void testLocaleInsensitivity() {
		Locale.setDefault(Locale.GERMAN);
		Float value = Float.valueOf(1.2f);
		String result = XPathDecimalFormat.xpathFormat(value);
		assertEquals("Unexpected XPath format string:", "1.2", result);
	}

}
