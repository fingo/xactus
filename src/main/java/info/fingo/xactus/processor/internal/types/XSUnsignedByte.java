/*******************************************************************************
 * Copyright (c) 2009, 2013 Mukul Gandhi, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mukul Gandhi - bug 277650 - Initial API and implementation, of xs:unsignedByte
 *                                 data type.
 *     David Carver (STAR) - bug 262765 - fixed abs value tests.
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.types;

import java.math.BigInteger;

import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.typesystem.TypeDefinition;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.builtin.BuiltinTypeLibrary;

public class XSUnsignedByte extends XSUnsignedShort {

	private static final String XS_UNSIGNED_BYTE = "xs:unsignedByte";

	/**
	 * Initializes a representation of 0
	 */
	public XSUnsignedByte() {
	  this(BigInteger.valueOf(0));
	}

	/**
	 * Initializes a representation of the supplied unsignedByte value
	 *
	 * @param x
	 *            unsignedByte to be stored
	 */
	public XSUnsignedByte(BigInteger x) {
		super(x);
	}

	/**
	 * Retrieves the datatype's full pathname
	 *
	 * @return "xs:unsignedByte" which is the datatype's full pathname
	 */
	public String string_type() {
		return XS_UNSIGNED_BYTE;
	}

	/**
	 * Retrieves the datatype's name
	 *
	 * @return "unsignedByte" which is the datatype's name
	 */
	public String type_name() {
		return "unsignedByte";
	}

	/**
	 * Creates a new ResultSequence consisting of the extractable unsignedByte
	 * in the supplied ResultSequence
	 *
	 * @param arg
	 *            The ResultSequence from which the unsignedByte is to be extracted
	 * @return New ResultSequence consisting of the 'unsignedByte' supplied
	 * @throws DynamicError
	 */
	public ResultSequence constructor(ResultSequence arg) throws DynamicError {
		if (arg.empty())
			return ResultBuffer.EMPTY;

		// the function conversion rules apply here too. Get the argument
		// and convert it's string value to a unsignedByte.
		Item aat = arg.first();

		try {
			BigInteger bigInt = new BigInteger(aat.getStringValue());

			// doing the range checking
			// min value is 0
			// max value is 255
			BigInteger min = BigInteger.valueOf(0);
			BigInteger max = BigInteger.valueOf(255L);

			if (bigInt.compareTo(min) < 0 || bigInt.compareTo(max) > 0) {
			   // invalid input
			   throw DynamicError.cant_cast(null);
			}

			return new XSUnsignedByte(bigInt);
		} catch (NumberFormatException e) {
			throw DynamicError.cant_cast(null);
		}

	}

	public TypeDefinition getTypeDefinition() {
		return BuiltinTypeLibrary.XS_UNSIGNEDBYTE;
	}

	public Number getNativeValue() {
		return getValue().shortValue();
	}

}
