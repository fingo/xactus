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

package info.fingo.xactus.processor.internal.ast;

import info.fingo.xactus.processor.internal.types.*;

/**
 * The value of a numeric literal containing an e or E character is an atomic
 * value of type xs:double
 *
 */
public class DoubleLiteral extends NumericLiteral {

	private final XSDouble value;

	/**
	 * Constructor for Doubleiteral
	 *
	 * @param value double value
	 */
	public DoubleLiteral(double value) {
		this.value = new XSDouble(value);
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

	/**
	 * @return xs:double value
	 */
	public XSDouble value() {
		return value;
	}

}
