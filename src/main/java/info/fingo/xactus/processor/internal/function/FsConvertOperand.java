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
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import java.util.Collection;
import java.util.Iterator;

import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.AnyAtomicType;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.CtrType;
import info.fingo.xactus.processor.internal.types.NumericType;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.processor.internal.types.XSDouble;
import info.fingo.xactus.processor.internal.types.XSString;
import info.fingo.xactus.processor.internal.types.XSUntypedAtomic;

/**
 * Function to convert a sequence of items to a sequence of atomic values.
 */
public class FsConvertOperand extends Function {

	public FsConvertOperand() {
		super(new QName("convert-operand"), 2);
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
		return convert_operand(args);
	}

	/**
	 * Convert-Operand operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fs: operation.
	 */
	public static ResultSequence convert_operand(Collection args)
			throws DynamicError {

		assert args.size() == 2;

		Iterator iter = args.iterator();

		ResultSequence actual = (ResultSequence) iter.next();
		ResultSequence expected = (ResultSequence) iter.next();

		if (expected.size() != 1)
			DynamicError.throw_type_error();

		Item at = expected.first();

		if (!(at instanceof AnyAtomicType))
			DynamicError.throw_type_error();

		AnyAtomicType exp_aat = (AnyAtomicType) at;

		ResultBuffer result = new ResultBuffer();

		// 1
		if (actual.empty())
			return result.getSequence();

		// convert sequence
		for (Iterator i = actual.iterator(); i.hasNext();) {
			AnyType item = (AnyType) i.next();

			// 2
			if (item instanceof XSUntypedAtomic) {
				// a
				if (exp_aat instanceof XSUntypedAtomic)
					result.add(new XSString(item.getStringValue()));
				// b
				else if (exp_aat instanceof NumericType)
					result.add(new XSDouble(item.getStringValue()));
				// c
				else {
					assert exp_aat instanceof CtrType;
					CtrType cons = (CtrType) exp_aat;
					result.concat(cons.constructor(new XSString(item.getStringValue())));
				}
			}
			// 4
			else
				result.add(item);

		}

		return result.getSequence();
	}
}
