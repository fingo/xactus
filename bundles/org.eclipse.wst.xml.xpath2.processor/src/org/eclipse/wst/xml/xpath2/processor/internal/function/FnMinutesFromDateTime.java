/*******************************************************************************
 * Copyright (c) 2005, 2009 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0 
 *******************************************************************************/

package org.eclipse.wst.xml.xpath2.processor.internal.function;

import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.ResultSequenceFactory;
import org.eclipse.wst.xml.xpath2.processor.internal.*;
import org.eclipse.wst.xml.xpath2.processor.internal.types.*;

import java.math.BigInteger;
import java.util.*;

/**
 * Returns an xs:integer value between 0 and 59, both inclusive, representing
 * the minute component in the localized value of $arg. If $arg is the empty
 * sequence, returns the empty sequence.
 */
public class FnMinutesFromDateTime extends Function {
	private static Collection _expected_args = null;

	/**
	 * Constructor for FnMinutesFromDateTime.
	 */
	public FnMinutesFromDateTime() {
		super(new QName("minutes-from-dateTime"), 1);
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
	@Override
	public ResultSequence evaluate(Collection args) throws DynamicError {
		return minutes_from_date_time(args);
	}

	/**
	 * Minutes-from-DateTime operation.
	 * 
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:minutes-from-dateTime operation.
	 */
	public static ResultSequence minutes_from_date_time(Collection args)
			throws DynamicError {
		Collection cargs = Function.convert_arguments(args, expected_args());

		ResultSequence arg1 = (ResultSequence) cargs.iterator().next();

		ResultSequence rs = ResultSequenceFactory.create_new();

		if (arg1.empty()) {
			return rs;
		}

		XSDateTime dt = (XSDateTime) arg1.first();

		int res = dt.minute();

		rs.add(new XSInteger(BigInteger.valueOf(res)));

		return rs;
	}

	/**
	 * Obtain a list of expected arguments.
	 * 
	 * @return Result of operation.
	 */
	public static Collection expected_args() {
		if (_expected_args == null) {
			_expected_args = new ArrayList();
			_expected_args
					.add(new SeqType(new XSDateTime(), SeqType.OCC_QMARK));
		}

		return _expected_args;
	}
}
