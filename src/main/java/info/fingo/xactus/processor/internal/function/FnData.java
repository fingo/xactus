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
 *     Mukul Gandhi - bug 276134 - improvements to schema aware primitive type support
 *                                 for attribute/element nodes
 *     Jesper Steen Moeller - bug 285145 - implement full arity checking
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import java.util.Collection;

import info.fingo.xactus.api.EvaluationContext;
import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.internal.types.AnyAtomicType;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.NodeType;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * fn:data takes a sequence of items and returns a sequence of atomic values.
 * The result of fn:data is the sequence of atomic values produced by applying
 * the following rules to each item in $arg: - If the item is an atomic value,
 * it is returned. - If the item is a node, fn:data() returns the typed value of
 * the node as defined by the accessor function dm:typed-value in Section 5.6
 * typed-value Accessor in the specification.
 */
public class FnData extends Function {
	/**
	 * Constructor for FnData.
	 */
	public FnData() {
		super(new QName("data"), 1);
	}

	/**
	 * Evaluate arguments.
	 *
	 * @param args
	 *            argument expressions.
	 * @return Result of evaluation.
	 */
	public ResultSequence evaluate(Collection args, EvaluationContext ec) {
		// 1 argument only!
		assert args.size() >= min_arity() && args.size() <= max_arity();

		ResultSequence argument = (ResultSequence) args.iterator().next();

		return atomize(argument);
	}

	/**
	 * Atomize a ResultSequnce argument expression.
	 *
	 * @param arg
	 *            input expression.
	 * @return Result of operation.
	 */
	public static ResultSequence atomize(ResultSequence arg) {

		ResultBuffer rs = new ResultBuffer();

		for (Item next : arg) {

			AnyType at = (AnyType) next;
			if (at instanceof AnyAtomicType) {
				rs.add(at);
			}
			else if (at instanceof NodeType) {
				NodeType nt = (NodeType) at;
				rs.concat(nt.typed_value());
			} else {
				throw new RuntimeException("XPath implementation error: " + at);
			}
		}

		return rs.getSequence();
	}

	/**
	 * Atomize argument expression of any type.
	 *
	 * @param arg
	 *            input expression.
	 * @return Result of operation.
	 */
	public static AnyAtomicType atomize(Item arg) {
		if (arg instanceof AnyAtomicType)
			return (AnyAtomicType)arg;
		else if (arg instanceof NodeType) {
			ResultSequence nodeValues = ((NodeType)arg).typed_value();
			if(nodeValues.empty()){
			return null;
		}
			return (AnyAtomicType)nodeValues.first();
		} else {
			throw new RuntimeException("XPath implemenatation error: "+arg);
		}
	}
}
