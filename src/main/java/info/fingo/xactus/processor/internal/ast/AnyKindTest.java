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
 ******************************************************************************/

package info.fingo.xactus.processor.internal.ast;

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.StaticContext;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.NodeType;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * Class to test a type of any kind. This matches any node in the expression.
 */
public class AnyKindTest extends KindTest {

	/**
	 * Support for Visitor interface.
	 *
	 * @return Result of Visitor operation.
	 */
	public Object accept(XPathVisitor v) {
		return v.visit(this);
	}

	public AnyType createTestType(ResultSequence rs, StaticContext sc) {
		return null;
	}

	public QName name() {
		return null;
	}

	public boolean isWild() {
		return false;
	}

	public Class getXDMClassType() {
		return NodeType.class;
	}




}
