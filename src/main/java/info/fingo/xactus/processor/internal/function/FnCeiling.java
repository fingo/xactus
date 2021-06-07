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
 *     Jesper Steen Moeller - bug 285145 - implement full arity checking
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import java.util.Collection;

import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.NumericType;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * Returns the smallest (closest to negative infinity) number with no fractional
 * part that is not less than the value of $arg. If type of $arg is one of the
 * four numeric types xs:float, xs:double, xs:decimal or xs:integer the type of
 * the return is the same as the type of $arg. If the type of $arg is a type
 * derived from one of the numeric types, the type of the return is the base
 * numeric type. For xs:float and xs:double arguments, if the argument is
 * positive zero (+0), then positive zero (+0) is returned. If the argument is
 * negative zero (-0), then negative zero (-0) is returned. If the argument is
 * less than zero (0), negative zero (-0) is returned.
 */
public class FnCeiling extends Function {
	/**
	 * Constructor for FnCeiling.
	 */
	public FnCeiling() {
		super(new QName("ceiling"), 1);
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
		// 1 argument only!
		assert args.size() >= min_arity() && args.size() <= max_arity();

		ResultSequence argument = (ResultSequence) args.iterator().next();

		return fn_ceiling(argument);
	}

	/**
	 * Ceiling value operation.
	 *
	 * @param arg
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:ceiling operation.
	 */
	public static ResultSequence fn_ceiling(ResultSequence arg)
			throws DynamicError {

		// sanity chex
		NumericType nt = FnAbs.get_single_numeric_arg(arg);

		// empty arg
		if (nt == null)
			return ResultBuffer.EMPTY;

		return nt.ceiling();
	}
}
