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
import java.util.Iterator;

import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.SeqType;
import info.fingo.xactus.processor.internal.types.NodeType;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * Support for Intersect operation.
 */
public class OpIntersect extends Function {
	private static Collection _expected_args = null;

	/**
	 * Constructor for OpIntersect.
	 */
	public OpIntersect() {
		super(new QName("intersect"), 2);
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
		assert args.size() >= min_arity() && args.size() <= max_arity();

		return op_intersect(args);
	}

	/**
	 * Op-Intersect operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of operation.
	 */
	public static ResultSequence op_intersect(Collection args)
			throws DynamicError {
		ResultBuffer rs = new ResultBuffer();

		// convert arguments
		Collection cargs = Function.convert_arguments(args, expected_args());

		// get arguments
		Iterator iter = cargs.iterator();
		ResultSequence one = (ResultSequence) iter.next();
		ResultSequence two = (ResultSequence) iter.next();

		// XXX lame
		for (Iterator i = one.iterator(); i.hasNext();) {
			NodeType node = (NodeType) i.next();
			boolean found = false;

			// death
			for (Iterator j = two.iterator(); j.hasNext();) {
				NodeType node2 = (NodeType) j.next();

				if (node.node_value() == node2.node_value()) {
					found = true;
					break;
				}

			}
			if (found)
				rs.add(node);
		}
		rs = NodeType.linarize(rs);

		return rs.getSequence();
	}

	/**
	 * Obtain a list of expected arguments.
	 *
	 * @return Result of operation.
	 */
	public synchronized static Collection expected_args() {
		if (_expected_args == null) {
			_expected_args = new ArrayList();

			SeqType st = new SeqType(SeqType.OCC_STAR);

			_expected_args.add(st);
			_expected_args.add(st);
		}
		return _expected_args;
	}
}
