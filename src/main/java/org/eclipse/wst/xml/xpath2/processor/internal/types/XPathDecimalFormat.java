/*******************************************************************************
 * Copyright (c) 2009, 2011 Standards for Technology in Automotive Retail and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    David Carver - initial API and implementation
 *    Jesper Steen Moller - bug 283404 - fixed locale
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package org.eclipse.wst.xml.xpath2.processor.internal.types;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * This is an XPath specific implementation of DecimalFormat to handle
 * some of the xpath specific formatting requirements.   Specifically
 * it allows for E# to be represented to indicate that the exponent value
 * is optional.  Otherwise all existing DecimalFormat patterns are handled
 * as is.
 * @author dcarver
 * @see 1.1
 *
 */
public final class XPathDecimalFormat {
	private XPathDecimalFormat() {
	}

	private static final String NEG_INFINITY = "-INF";
	private static final String POS_INFINITY = "INF";
	private static final DecimalFormatSymbols SYMBOLS_US = new DecimalFormatSymbols(Locale.US);
	private static final String XS_FLOAT_DEFAULT_FORMAT = "0.#######E0";
	private static final String XS_FLOAT_FORMAT_2 = XS_FLOAT_DEFAULT_FORMAT.replaceAll("E0", "");
	private static final String XS_FLOAT_FORMAT_3 = XS_FLOAT_DEFAULT_FORMAT.replaceAll("0\\.#", "0.0" );
	private static final String XS_DOUBLE_DEFAULT_FORMAT = "0.################E0";
	private static final String XS_DOUBLE_FORMAT_2 = XS_DOUBLE_DEFAULT_FORMAT.replaceAll("E0", "");
	private static final String XS_DOUBLE_FORMAT_3 = XS_DOUBLE_DEFAULT_FORMAT.replaceAll("0\\.#", "0.0");
	private static final String XS_DECIMAL_FORMAT = "0.####################";

	private static final ThreadLocal<DecimalFormat> XS_FLOAT_DEFAULT_DECIMAL_FORMAT =
		threadLocalizedDecimalFormat(XS_FLOAT_DEFAULT_FORMAT);
	private static final ThreadLocal<DecimalFormat> XS_FLOAT_DECIMAL_FORMAT_2 =
		threadLocalizedDecimalFormat(XS_FLOAT_FORMAT_2);
	private static final ThreadLocal<DecimalFormat> XS_FLOAT_DECIMAL_FORMAT_3 =
		threadLocalizedDecimalFormat(XS_FLOAT_FORMAT_3);

	private static final ThreadLocal<DecimalFormat> XS_DOUBLE_DECIMAL_FORMAT_2 =
		threadLocalizedDecimalFormat(XS_DOUBLE_FORMAT_2);
	private static final ThreadLocal<DecimalFormat> XS_DOUBLE_DECIMAL_FORMAT_3 =
		threadLocalizedDecimalFormat(XS_DOUBLE_FORMAT_3);

	private static final ThreadLocal<DecimalFormat> XS_DECIMAL_DECIMAL_FORMAT =
		threadLocalizedDecimalFormat(XS_DECIMAL_FORMAT);

	/**
	 * Formats the string dropping a Zero Exponent Value if it exists.
	 * @param obj
	 * @return
	 */
	public static String xpathFormat(Object obj) {
		if (obj instanceof Float) {
            return formatFloatValue((Float)obj);
		}

		if (obj instanceof Double) {
			return formatDoubleValue(obj);
		}

		return XS_DECIMAL_DECIMAL_FORMAT.get().format(obj);
	}

	private static String formatDoubleValue(Object obj) {
		Double doubleValue = (Double) obj;
		if (doubleValue.doubleValue() == Double.NEGATIVE_INFINITY) {
			return NEG_INFINITY;
		}
		if (doubleValue.doubleValue() == Double.POSITIVE_INFINITY) {
			return POS_INFINITY;
		}
		DecimalFormat decimalFormat = doubleXPathPattern(obj);
		return decimalFormat.format(obj);
	}

	private static DecimalFormat doubleXPathPattern(Object obj) {
		BigDecimal doubleValue = BigDecimal.valueOf((((Double) obj)).doubleValue());
		BigDecimal minValue = new BigDecimal("-1E6");
		BigDecimal maxValue = new BigDecimal("1E6");
		if (doubleValue.compareTo(minValue) > 0 && doubleValue.compareTo(maxValue) < 0) {
			return XS_DOUBLE_DECIMAL_FORMAT_2.get();
		} else { //if (doubleValue.compareTo(minValue) < 0) {
			return XS_DOUBLE_DECIMAL_FORMAT_3.get();
		}
	}

	private static String formatFloatValue(Float floatValue) {
		if (floatValue.floatValue() == Float.NEGATIVE_INFINITY) {
			return NEG_INFINITY;
		}
		if (floatValue.floatValue() == Float.POSITIVE_INFINITY) {
			return POS_INFINITY;
		}

		return floatXPathPattern(floatValue).format(floatValue);
	}

	private static DecimalFormat floatXPathPattern(Float floatValue) {
		if (floatValue.floatValue() > -1E6f && floatValue.floatValue() < 1E6f) {
			return XS_FLOAT_DECIMAL_FORMAT_2.get();
		} else if (floatValue.floatValue() <= -1E6f) {
			return XS_FLOAT_DECIMAL_FORMAT_3.get();
		}

		return XS_FLOAT_DEFAULT_DECIMAL_FORMAT.get();
	}

	private static ThreadLocal<DecimalFormat> threadLocalizedDecimalFormat(String pattern) {
		return ThreadLocal.withInitial(
			() -> new DecimalFormat(pattern, SYMBOLS_US));
	}
}
