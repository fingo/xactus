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
 *     Mukul Gandhi - bug 274805 - improvements to xs:integer data type
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import java.math.BigInteger;
import java.util.Collection;

import info.fingo.xactus.api.EvaluationContext;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.processor.internal.types.XSInteger;

/**
 * Returns an xs:integer indicating the position of the context item within the
 * sequence of items currently being processed. If the context item is
 * undefined, an error is raised [err:FONC0001].
 */
public class FnPosition extends Function {
	/**
	 * Constructor for FnPosition.
	 */
	public FnPosition() {
		super(new QName("position"), 0);
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
	public ResultSequence evaluate(Collection args, EvaluationContext ec) throws DynamicError {
		return position(args, ec);
	}

	/**
	 * Position operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @param dc
	 *            Result of dynamic context operation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:position operation.
	 */
	public static ResultSequence position(Collection args, EvaluationContext ec)
			throws DynamicError {
		assert args.size() == 0;

		if (ec.getContextItem() == null) {
			throw DynamicError.contextUndefined();
		}

		return new XSInteger( BigInteger.valueOf( ec.getContextPosition() ) );
	}
}
