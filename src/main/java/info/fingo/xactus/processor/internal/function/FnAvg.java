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
 *    Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *    Mukul Gandhi - bug 273760 - wrong namespace for functions and data types
 *    David Carver - bug 262765 - fix issue with casting items to XSDouble cast
 *                                needed to cast to Numeric so that evaluations
 *                                and formatting occur correctly.
 *                              - fix fn:avg casting issues and divide by zero issues.
 *    Jesper Moller - bug 281028 - fix promotion rules for fn:avg
 *    Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *    Lukasz Wycisk - bug 361060 - Aggregations with nil=�true� throw exceptions.
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;

import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.typesystem.TypeDefinition;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.AnyAtomicType;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.processor.internal.types.XSDouble;
import info.fingo.xactus.processor.internal.types.XSFloat;
import info.fingo.xactus.processor.internal.types.XSInteger;
import info.fingo.xactus.processor.internal.utils.ScalarTypePromoter;
import info.fingo.xactus.processor.internal.utils.TypePromoter;

/**
 * Returns the average of the values in the input sequence $arg, that is, the
 * sum of the values divided by the number of values.
 */
public class FnAvg extends Function {
	/**
	 * Constructor for FnAvg.
	 */
	public FnAvg() {
		super(new QName("avg"), 1);
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
		return avg(args);
	}

	/**
	 * Average value operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:avg operation.
	 */
	public static ResultSequence avg(Collection args) throws DynamicError {

		ResultSequence arg = (ResultSequence)args.iterator().next();

		if (arg.empty())
			return ResultBuffer.EMPTY;

		int elems = 0;

		MathPlus total = null;

		TypePromoter tp = new ScalarTypePromoter();
		tp.considerSequence(arg);

		for (Iterator i = arg.iterator(); i.hasNext();) {
			++elems;
			AnyAtomicType conv = tp.promote((AnyType) i.next());
			if( conv != null ){

				if (conv instanceof XSDouble && ((XSDouble)conv).nan() || conv instanceof XSFloat && ((XSFloat)conv).nan()) {
					return tp.promote( new XSFloat( Float.NaN ) );
				}
				if (total == null) {
					total = (MathPlus)conv;
				} else {
					total = (MathPlus)total.plus( conv ).first();
				}
			}
		}

		if (!(total instanceof MathDiv))
			DynamicError.throw_type_error();

		return ((MathDiv)total).div( new XSInteger( BigInteger.valueOf( elems ) ) );
	}

	@Override
	public TypeDefinition getResultType() {
		// TODO Auto-generated method stub
		return super.getResultType();
	}
}
