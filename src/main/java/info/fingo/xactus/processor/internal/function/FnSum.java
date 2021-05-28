/*******************************************************************************
 * Copyright (c) 2005, 2012 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *     Mukul Gandhi - bug 274805 - improvements to xs:integer data type
 *     Jesper Moller - bug 281028 - fix promotion rules for fn:sum
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *    Lukasz Wycisk - bug 361060 - Aggregations with nil=�true� throw exceptions.
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.TypeError;
import info.fingo.xactus.processor.internal.types.AnyAtomicType;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.processor.internal.types.XSDouble;
import info.fingo.xactus.processor.internal.types.XSFloat;
import info.fingo.xactus.processor.internal.types.XSInteger;
import info.fingo.xactus.processor.internal.utils.ScalarTypePromoter;
import info.fingo.xactus.processor.internal.utils.TypePromoter;

/**
 * Returns a value obtained by adding together the values in $arg. If the
 * single-argument form of the function is used, then the value returned for an
 * empty sequence is the xs:integer value 0. If the two-argument form is used,
 * then the value returned for an empty sequence is the value of the $zero
 * argument.
 */
public class FnSum extends Function {

	static private XSInteger ZERO = new XSInteger(BigInteger.ZERO);

	/**
	 * Constructor for FnSum.
	 */
	public FnSum() {
		super(new QName("sum"), 1, 2);
	}

	/**
	 * Evaluate arguments.
	 *
	 * @param args
	 *            argument expressions.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of evaluation.
	 */
	public ResultSequence evaluate(Collection args, info.fingo.xactus.api.EvaluationContext ec) throws DynamicError {
		Iterator argIterator = args.iterator();
		ResultSequence argSequence = (ResultSequence)argIterator.next();
		AnyAtomicType zero = ZERO;
		if (argIterator.hasNext()) {
			ResultSequence zeroSequence = (ResultSequence)argIterator.next();
			if (zeroSequence.size() != 1)
				throw new DynamicError(TypeError.invalid_type(null));
			if (! (zeroSequence.first() instanceof AnyAtomicType))
				throw new DynamicError(TypeError.invalid_type(zeroSequence.first().getStringValue()));
			zero = (AnyAtomicType)zeroSequence.first();
		}
		return sum(argSequence, zero);
	}

	/**
	 * Sum operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:sum operation.
	 */
	public static ResultSequence sum(ResultSequence arg, AnyAtomicType zero) throws DynamicError {


		if (arg.empty())
			return zero;

		MathPlus total = null;

		TypePromoter tp = new ScalarTypePromoter();
		tp.considerSequence(arg);

		for (Iterator i = arg.iterator(); i.hasNext();) {
			AnyAtomicType conv = tp.promote((AnyType) i.next());

			if(conv == null){
				conv = zero;
			}

			if (conv instanceof XSDouble && ((XSDouble)conv).nan() || conv instanceof XSFloat && ((XSFloat)conv).nan()) {
				return tp.promote( new XSFloat( Float.NaN ) );
			}
			if (total == null) {
				total = (MathPlus)conv;
			} else {
				total = (MathPlus)total.plus( conv ).first();
			}
		}

		return (AnyType)total;
	}
}
