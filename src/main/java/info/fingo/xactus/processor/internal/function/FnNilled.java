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

import java.util.ArrayList;
import java.util.Collection;

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.SeqType;
import info.fingo.xactus.processor.internal.types.NodeType;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * Returns an xs:boolean indicating whether the argument node is "nilled". If
 * the argument is not an element node, returns the empty sequence.
 */
public class FnNilled extends Function {
	private static Collection _expected_args = null;

	/**
	 * Constructor for FnNilled.
	 */
	public FnNilled() {
		super(new QName("nilled"), 0, 1);
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
		return nilled(args);
	}

	/**
	 * Nilled operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:nilled operation.
	 */
	public static ResultSequence nilled(Collection args) throws DynamicError {

		Collection cargs = Function.convert_arguments(args, expected_args());


		ResultSequence arg1 = (ResultSequence) cargs.iterator().next();
		if (arg1.empty()) {
			return arg1;
		}
		NodeType nt = (NodeType) arg1.first();

		return nt.nilled();
	}

	/**
	 * Obtain a list of expected arguments.
	 *
	 * @return Result of operation.
	 */
	public synchronized static Collection expected_args() {
		if (_expected_args == null) {
			_expected_args = new ArrayList();
			_expected_args.add(new SeqType(SeqType.OCC_QMARK));
		}

		return _expected_args;
	}
}
