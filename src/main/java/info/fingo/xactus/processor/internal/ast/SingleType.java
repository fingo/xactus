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
 * Support for Single types.
 */
public class SingleType extends XPathNode {

	private final QName type;
	private final boolean qmark;

	/**
	 * Constructor for SingleType.
	 *
	 * @param type  QName type.
	 * @param qmark optional type? (true/false).
	 */
	public SingleType(QName type, boolean qmark) {
		this.type = type;
		this.qmark = qmark;
	}

	/**
	 * Default Constructor for SingleType.
	 */
	public SingleType(QName type) {
		this(type, false);
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
	 * Set optional type.
	 *
	 * @return optional type value.
	 */
	public boolean qmark() {
		return qmark;
	}

	/**
	 * Support for QName interface.
	 *
	 * @return Result of QName operation.
	 */
	public QName type() {
		return type;
	}

}
