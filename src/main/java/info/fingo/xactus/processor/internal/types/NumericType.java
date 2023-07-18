/*******************************************************************************
 * Copyright (c) 2005, 2011 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *******************************************************************************/

package info.fingo.xactus.processor.internal.types;

import java.util.Iterator;

import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.function.CmpEq;
import info.fingo.xactus.processor.internal.function.CmpGt;
import info.fingo.xactus.processor.internal.function.CmpLt;
import info.fingo.xactus.processor.internal.function.MathDiv;
import info.fingo.xactus.processor.internal.function.MathIDiv;
import info.fingo.xactus.processor.internal.function.MathMinus;
import info.fingo.xactus.processor.internal.function.MathMod;
import info.fingo.xactus.processor.internal.function.MathPlus;
import info.fingo.xactus.processor.internal.function.MathTimes;

/**
 * A representation of the NumericType datatype
 */
public abstract class NumericType extends CtrType

implements CmpEq, CmpGt, CmpLt,

MathPlus, MathMinus, MathTimes, MathDiv, MathIDiv, MathMod {

	// XXX needed for fn:boolean
	/**
	 * Check whether node represnts 0
	 *
	 * @return True if node represnts 0. False otherwise
	 */
	public abstract boolean zero();

	/**
	 * Creates a new ResultSequence representing the negation of the number
	 * stored
	 *
	 * @return New ResultSequence representing the negation of the number stored
	 */
	public abstract ResultSequence unary_minus();

	// numeric functions
	/**
	 * Absolutes the number stored
	 *
	 * @return New NumericType representing the absolute of the number stored
	 */
	public abstract NumericType abs();

	/**
	 * Returns the smallest integer greater than the number stored
	 *
	 * @return A NumericType representing the smallest integer greater than the
	 *         number stored
	 */
	public abstract NumericType ceiling();

	/**
	 * Returns the largest integer smaller than the number stored
	 *
	 * @return A NumericType representing the largest integer smaller than the
	 *         number stored
	 */
	public abstract NumericType floor();

	/**
	 * Returns the closest integer of the number stored.
	 *
	 * @return A NumericType representing the closest long of the number stored.
	 */
	public abstract NumericType round();

	/**
	 * Returns the closest integer of the number stored.
	 *
	 * @return A NumericType representing the closest long of the number stored.
	 */
	public abstract NumericType round_half_to_even();

	public abstract NumericType round_half_to_even(int precision);

	protected Item get_single_arg(ResultSequence rs) throws DynamicError {
		if (rs.size() != 1)
			DynamicError.throw_type_error();

		return rs.first();
	}

	protected ResultSequence convertResultSequence(ResultSequence arg)
			throws DynamicError {
		
		ResultSequence carg = arg;
		Iterator<Item> it = carg.iterator();
		while (it.hasNext()) {
			AnyType type = (AnyType) it.next();
			if (type.string_type().equals("xs:untypedAtomic") ||
				type.string_type().equals("xs:string")) {
				throw DynamicError.throw_type_error();
			}
		}

		carg = constructor(carg);
		return carg;
	}

	/***
	 * Check whether the supplied node is of the supplied type
	 *
	 * @param at
	 *            The node being tested
	 * @param type
	 *            The type expected
	 * @return The node being tested
	 * @throws DynamicError
	 *             If node being tested is not of expected type
	 */
	public static Item get_single_type(Item at, Class type)
			throws DynamicError {

		if (!type.isInstance(at))
			DynamicError.throw_type_error();

		return at;
	}

	public static Item get_single_type(AnyType at, Class type)
			throws DynamicError {
		
		return get_single_type((Item)at, type);
	}

	/***
	 * Check whether first node in supplied ResultSequence is of the supplied
	 * type
	 *
	 * @param rs
	 *            The node being tested
	 * @param type
	 *            The type expected
	 * @return The node being tested
	 * @throws DynamicError
	 *             If node being tested is not of expected type
	 */
	public static AnyType get_single_type(ResultSequence rs, Class type)
			throws DynamicError {
		
		if (rs.size() != 1) {
			DynamicError.throw_type_error();
		}

		Item at = rs.first();
		if (!type.isInstance(at)) {
			DynamicError.throw_type_error();
		}

		return (AnyType) at;
	}
	
}
