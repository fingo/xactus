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

import java.util.*;

import info.fingo.xactus.api.Function;
import info.fingo.xactus.processor.internal.types.*;

/**
 * Class for Function Call support.
 */
public class FunctionCall extends PrimaryExpr implements Iterable<Expr> {
	
	private final QName name;
	private final Collection<Expr> args;
	
	private Function function;

	/**
	 * Constructor for FunctionCall.
	 *
	 * @param name
	 *            QName.
	 * @param args
	 *            Collection of arguments.
	 */
	public FunctionCall(QName name, Collection<Expr> args) {
		
		this.name = name;
		this.args = args;
	}

	public Function function() {
		return function;
	}

	public void set_function(Function function) {
		this.function = function;
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
	 * Support for QName interface.
	 *
	 * @return Result of QName operation.
	 */
	public QName name() {
		return name;
	}

	/**
	 * Support for Iterator interface.
	 *
	 * @return Result of Iterator operation.
	 */
	@Override
	public Iterator<Expr> iterator() {
		return args.iterator();
	}

	/**
	 * Support for Arity interface.
	 *
	 * @return Result of Arity operation.
	 */
	public int arity() {
		return args.size();
	}
	
}
