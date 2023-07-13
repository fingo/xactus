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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * Returns the items of $sourceSeq in a non-deterministic order.
 */
public class FnUnordered extends Function {
	/**
	 * Constructor for FnUnordered.
	 */
	public FnUnordered() {
		super(new QName("unordered"), 1);
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
		return unordered(args);
	}

	/**
	 * Unordered operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:unordered operation.
	 */
	public static ResultSequence unordered(Collection args) throws DynamicError {

		assert args.size() == 1;

		// get args
		Iterator citer = args.iterator();
		ResultSequence arg = (ResultSequence) citer.next();

		if (arg.empty())
			return ResultBuffer.EMPTY;

		// XXX lame
		List<Item> tmp = new ArrayList<>();
		for (Item next : arg) {
			tmp.add(next);
		}

		Collections.shuffle(tmp);

		ResultBuffer rb = new ResultBuffer();
		for (Item next : tmp) {
			rb.add(next);
		}

		return rb.getSequence();
	}
	
}
