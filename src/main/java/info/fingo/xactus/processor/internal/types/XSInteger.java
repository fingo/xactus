/*******************************************************************************
 * Copyright (c) 2005, 2013 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *     Mukul Gandhi - bug 274805 - improvements to xs:integer data type. Using Java
 *                      BigInteger class to enhance numerical range to -INF -> +INF.
 *     David Carver (STAR) - bug 262765 - fix comparision to zero.
 *     David Carver (STAR) - bug 282223 - fix casting issues.
 *     Jesper Steen Moller - bug 262765 - fix type tests
 *     Jesper Steen Moller - bug 281028 - Added constructor from string
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import info.fingo.xactus.api.DynamicContext;
import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.typesystem.TypeDefinition;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.builtin.BuiltinTypeLibrary;

/**
 * A representation of the Integer datatype
 */
public class XSInteger extends XSDecimal {

	private static final String XS_INTEGER = "xs:integer";
	
	private BigInteger _value;

	/**
	 * Initializes a representation of 0
	 */
	public XSInteger() {
		this(BigInteger.valueOf(0));
	}

	/**
	 * Initializes a representation of the supplied integer
	 *
	 * @param x
	 *            Integer to be stored
	 */
	public XSInteger(BigInteger x) {
		super(new BigDecimal(x));
		_value = x;
	}

	/**
	 * Initializes a representation of the supplied integer
	 *
	 * @param x
	 *            Integer to be stored
	 */
	public XSInteger(String x) {
		super(new BigDecimal(x));
		_value = new BigInteger(x);
	}

	/**
	 * Retrieves the datatype's full pathname
	 *
	 * @return "xs:integer" which is the datatype's full pathname
	 */
	public String string_type() {
		return XS_INTEGER;
	}

	/**
	 * Retrieves the datatype's name
	 *
	 * @return "integer" which is the datatype's name
	 */
	public String type_name() {
		return "integer";
	}

	/**
	 * Retrieves a String representation of the integer stored
	 *
	 * @return String representation of the integer stored
	 */
	public String getStringValue() {
		return _value.toString();
	}

	@Override
	public Number getNativeValue() {
		return _value;
	}

	/**
	 * Check whether the integer represented is 0
	 *
	 * @return True is the integer represented is 0. False otherwise
	 */
	public boolean zero() {
		return (_value.compareTo(BigInteger.ZERO) == 0);
	}

	/**
	 * Creates a new ResultSequence consisting of the extractable integer in the
	 * supplied ResultSequence
	 *
	 * @param arg
	 *            The ResultSequence from which the integer is to be extracted
	 * @return New ResultSequence consisting of the integer supplied
	 * @throws DynamicError
	 */
	@Override
	public ResultSequence constructor(ResultSequence arg) throws DynamicError {
		if (arg.empty())
			return ResultBuffer.EMPTY;

		// the function conversion rules apply here too. Get the argument
		// and convert it's string value to an integer.
		Item aat = arg.first();

		if (aat instanceof XSDuration || aat instanceof CalendarType ||
			aat instanceof XSBase64Binary || aat instanceof XSHexBinary ||
			aat instanceof XSAnyURI) {
			throw DynamicError.invalidType();
		}

		if (!isCastable(aat)) {
			throw DynamicError.cant_cast(null);
		}

		try {
			BigInteger bigInt = castInteger(aat);
			return new XSInteger(bigInt);
		} catch (NumberFormatException e) {
			throw DynamicError.invalidLexicalValue();
		}

	}

	private BigInteger castInteger(Item aat) {
		if (aat instanceof XSBoolean) {
			if (aat.getStringValue().equals("true")) {
				return BigInteger.ONE;
			} else {
				return BigInteger.ZERO;
			}
		}

		if (aat instanceof XSDecimal || aat instanceof XSFloat ||
				aat instanceof XSDouble) {
				BigDecimal bigDec =  new BigDecimal(aat.getStringValue());
				return bigDec.toBigInteger();
		}

		return new BigInteger(aat.getStringValue());
	}

	private boolean isCastable(Item aat) throws DynamicError {
		if (aat instanceof XSString || aat instanceof XSUntypedAtomic ||
			aat instanceof NodeType) {
			if (isLexicalValue(aat.getStringValue())) {
				return true;
			} else {
				return false;
			}
		}
		if (aat instanceof XSBoolean || aat instanceof NumericType) {
			return true;
		}
		return false;
	}

	protected boolean isLexicalValue(String value) {

		try {
			new BigInteger(value);
		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}

	/**
	 * Retrieves the actual integer value stored
	 *
	 * @return The actual integer value stored
	 */
	public BigInteger int_value() {
		return _value;
	}

	/**
	 * Sets the integer stored to that supplied
	 *
	 * @param x
	 *            Integer to be stored
	 */
	public void set_int(BigInteger x) {
		_value = x;
		set_double(x.intValue());
	}

	/**
	 * Mathematical addition operator between this XSInteger and the supplied
	 * ResultSequence.
	 *
	 * @param arg
	 *            The ResultSequence to perform an addition with
	 * @return A XSInteger consisting of the result of the mathematical
	 *         addition.
	 */
	public ResultSequence plus(ResultSequence arg) throws DynamicError {
		ResultSequence carg = convertResultSequence(arg);
		XSDecimal val = (XSDecimal)get_single_type( carg, XSDecimal.class );

		BigDecimal result = val.getValue().add( new BigDecimal( int_value() ) );
		return createResult( result );
	}

	protected ResultSequence convertResultSequence(ResultSequence arg)
			throws DynamicError {
		
		ResultSequence carg = arg;
		Iterator it = carg.iterator();
		while (it.hasNext()) {
			AnyType type = (AnyType) it.next();
			if (type.string_type().equals("xs:untypedAtomic") ||
				type.string_type().equals("xs:string")) {
				throw DynamicError.invalidType();
			}
		}
		carg = constructorSafe( carg );
		return carg;
	}

	/**
	 * Mathematical subtraction operator between this XSInteger and the supplied
	 * ResultSequence.
	 *
	 * @param arg
	 *            The ResultSequence to perform a subtraction with
	 * @return A XSInteger consisting of the result of the mathematical
	 *         subtraction.
	 */
	public ResultSequence minus(ResultSequence arg) throws DynamicError {
		ResultSequence carg = convertResultSequence(arg);
		XSDecimal val = (XSDecimal)get_single_type( carg, XSDecimal.class );

		BigDecimal result = new BigDecimal( int_value() ).subtract( val.getValue() );
		return createResult( result );
	}

	/**
	 * Mathematical multiplication operator between this XSInteger and the
	 * supplied ResultSequence.
	 *
	 * @param arg
	 *            The ResultSequence to perform a multiplication with
	 * @return A XSInteger consisting of the result of the mathematical
	 *         multiplication.
	 */
	public ResultSequence times(ResultSequence arg) throws DynamicError {
		ResultSequence carg = convertResultSequence(arg);

		XSDecimal val = (XSDecimal)get_single_type( carg, XSDecimal.class );

		return createResult( val.getValue().multiply( new BigDecimal( int_value() ) ) );
	}

	/**
	 * Mathematical modulus operator between this XSInteger and the supplied
	 * ResultSequence.
	 *
	 * @param arg
	 *            The ResultSequence to perform a modulus with
	 * @return A XSInteger consisting of the result of the mathematical modulus.
	 */
	public ResultSequence mod(ResultSequence arg) throws DynamicError {
		ResultSequence carg = convertResultSequence(arg);

		XSDecimal val = (XSDecimal) get_single_type(carg, XSDecimal.class);

		if( val instanceof XSInteger )
		{
			BigInteger result = int_value().remainder(((XSInteger)val).int_value());
			return new XSInteger( result );
		}

		return super.mod(arg);
	}

	/**
	 * Negates the integer stored
	 *
	 * @return New XSInteger representing the negation of the integer stored
	 */
	public ResultSequence unary_minus() {
		return new XSInteger( int_value().multiply( BigInteger.valueOf( -1 ) ) );
	}

	/**
	 * Absolutes the integer stored
	 *
	 * @return New XSInteger representing the absolute of the integer stored
	 */
	public NumericType abs() {
		return new XSInteger(int_value().abs());
	}

	/*
	 * (non-Javadoc)
	 * @see info.fingo.xactus.processor.internal.types.XSDecimal#gt(info.fingo.xactus.processor.internal.types.AnyType)
	 */
	public boolean gt(AnyType arg, DynamicContext context) throws DynamicError {
		ResultSequence rs = constructorSafe( arg );
		XSDecimal val = (XSDecimal)get_single_type( rs, XSDecimal.class );

		int compareResult = getValue().compareTo( val.getValue() );

		return compareResult > 0;
	}

	protected Item convertArg(AnyType arg) throws DynamicError {
		ResultSequence rs = arg;
		rs = constructor(rs);
		Item carg = rs.first();
		return carg;
	}

	/*
	 * (non-Javadoc)
	 * @see info.fingo.xactus.processor.internal.types.XSDecimal#lt(info.fingo.xactus.processor.internal.types.AnyType)
	 */
	public boolean lt(AnyType arg, DynamicContext context) throws DynamicError {
		ResultSequence rs = constructorSafe( arg );
		XSDecimal val = (XSDecimal)get_single_type( rs, XSDecimal.class );

		int compareResult = this.getValue().compareTo( val.getValue() );

		return compareResult < 0;
	}

	public boolean eq( AnyType arg, DynamicContext dynamicContext ) throws DynamicError
	{
		ResultSequence rs = constructorSafe( arg );
		XSDecimal val = (XSDecimal)get_single_type( rs, XSDecimal.class );

		int compareResult = this.getValue().compareTo( val.getValue() );

		return compareResult == 0;
	}

	public ResultSequence div(ResultSequence arg) throws DynamicError {
		ResultSequence carg = convertResultSequence(arg);

		XSDecimal val = (XSDecimal) get_single_type(carg, XSDecimal.class);

		if (val.zero()) {
			throw DynamicError.div_zero(null);
		}

		BigDecimal result = getValue().divide(val.getValue(), 18, BigDecimal.ROUND_HALF_EVEN);
		return new XSDecimal( result );
	}

	public TypeDefinition getTypeDefinition() {
		return BuiltinTypeLibrary.XS_INTEGER;
	}

	/**
	 * Creates a new result sequence consisting of the retrievable decimal number in the supplied result sequence
	 *
	 * @param arg
	 *            The result sequence from which to extract the decimal number.
	 * @throws DynamicError
	 * @return A new result sequence consisting of the decimal number supplied.
	 */
	private ResultSequence constructorSafe( ResultSequence arg ) throws DynamicError
	{
		if( arg.empty() )
			return ResultBuffer.EMPTY;

		Item aat = arg.first();

		if( aat instanceof XSDuration || aat instanceof CalendarType || aat instanceof XSBase64Binary
			|| aat instanceof XSHexBinary || aat instanceof XSAnyURI )
		{
			throw DynamicError.invalidType();
		}

		if( aat.getStringValue().indexOf( "-INF" ) != -1 )
		{
			throw DynamicError.cant_cast( null );
		}

		if( !super.isLexicalValue( aat.getStringValue() ) )
		{
			throw DynamicError.invalidLexicalValue();
		}

		if( !isCastable( aat ) )
		{
			throw DynamicError.cant_cast( null );
		}

		try
		{
			// XPath doesn't allow for converting Exponents to Decimal values.

			return castDecimal( aat );
		}
		catch( NumberFormatException e )
		{
			throw DynamicError.cant_cast( null );
		}

	}

	private ResultSequence createResult( BigDecimal result )
	{
		if( result.signum() == 0 || result.scale() <= 0 || result.stripTrailingZeros().scale() <= 0 )
		{
			return new XSInteger( result.toBigInteger() );
		}
		return new XSDecimal( result );
	}
	
}
