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
 *     Jesper S Moller - bug 286452 - always return the stable date/time from dynamic context
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import java.util.Collection;

import javax.xml.datatype.Duration;

import info.fingo.xactus.api.DynamicContext;
import info.fingo.xactus.api.EvaluationContext;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.processor.internal.types.XSDateTime;
import info.fingo.xactus.processor.internal.types.XSDayTimeDuration;

/**
 * Returns the xs:dateTime (with timezone) from the dynamic context. (See
 * Section C.2 Dynamic Context ComponentsXP.) This is a xs:dateTime that is
 * current at some time during the evaluation of a query or transformation in
 * which fn:current-dateTime() is executed. This function is stable. The precise
 * instant during the query or transformation represented by the value of
 * fn:current-dateTime() is implementation dependent.
 */
public class FnCurrentDateTime extends Function {
	/**
	 * Constructor for FnCurrentDateTime.
	 */
	public FnCurrentDateTime() {
		super(new QName("current-dateTime"), 0);
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
		return current_dateTime(args, ec.getDynamicContext());
	}

	/**
	 * Current-Date-Time operation.
	 *
	 * @param args
	 *            Result from the expressions evaluation.
	 * @param dc
	 *            Result of dynamic context operation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:current-dateTime operation.
	 */
	public static ResultSequence current_dateTime(Collection args,
			DynamicContext dc) throws DynamicError {
		assert args.size() == 0;

		Duration d = dc.getTimezoneOffset();
		XSDayTimeDuration tz = new XSDayTimeDuration(0, d.getHours(), d.getMinutes(), 0.0, d.getSign() == -1);

		AnyType res = new XSDateTime(dc.getCurrentDateTime(), tz);

		return res;
	}
}
