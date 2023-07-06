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
 *     David Carver - bug 298535 - Attribute instance of improvements
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.ast;

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.StaticContext;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.PIType;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * Class for Processing Instruction support.
 */
public class PITest extends KindTest {
	private String _arg;

	/**
	 * Constructor for PITest.
	 *
	 * @param arg
	 *            instruction argument.
	 */
	public PITest(String arg) {
		_arg = arg;
	}

	/**
	 * Default Constructor for PITest.
	 */
	public PITest() {
		this(null);
	}

	/**
	 * Support for String arguments.
	 *
	 * @return Result of String operation.
	 */
	public String arg() {
		return _arg;
	}

	@Override
	public AnyType createTestType(ResultSequence rs, StaticContext sc) {
		// TODO review
		return null;
	}

	@Override
	public QName name() {
		// TODO review
		return null;
	}

	@Override
	public boolean isWild() {
		return false;
	}

	@Override
	public Class<PIType> getXDMClassType() {
		return PIType.class;
	}

	/**
	 * Support for Visitor interface.
	 *
	 * @return Result of Visitor operation.
	 */
	@Override
	public Object accept(XPathVisitor v) {
		return v.visit(this);
	}

}
