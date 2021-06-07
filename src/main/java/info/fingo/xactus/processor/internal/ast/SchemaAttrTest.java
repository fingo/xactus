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

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.StaticContext;
import info.fingo.xactus.processor.internal.types.*;

/**
 * Support for Schema Attribute test.
 */
public class SchemaAttrTest extends KindTest {
	private QName _arg;

	/**
	 * Constructor for SchemaAttrTest.
	 *
	 * @param arg
	 *            QName argument.
	 */
	public SchemaAttrTest(QName arg) {
		_arg = arg;
	}

	/**
	 * Support for Visitor interface.
	 *
	 * @return Result of Visitor operation.
	 */
	public Object accept(XPathVisitor v) {
		return v.visit(this);
	}

	/**
	 * Support for QName interface.
	 *
	 * @return Result of QName operation.
	 */
	public QName arg() {
		return _arg;
	}

	public AnyType createTestType(ResultSequence rs, StaticContext sc) {
		// TODO Auto-generated method stub
		return null;
	}

	public QName name() {
		return _arg;
	}

	public boolean isWild() {
		return false;
	}

	public Class getXDMClassType() {
		// TODO Auto-generated method stub
		return null;
	}
}
