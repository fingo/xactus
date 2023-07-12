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

/**
 * Support for IF expressions.
 */
public class IfExpr extends Expr implements Iterable<Expr> {
	
	private final Collection<Expr> exprs;
	private final Expr then_exp;
	private final Expr else_exp;

	/**
	 * Constructor for IfExpr.
	 *
	 * @param exps
	 *            Condition expressions.
	 * @param t
	 *            If true expressions.
	 * @param e
	 *            If false/else expressions.
	 */
	public IfExpr(Collection<Expr> exps, Expr t, Expr e) {
		
		this.exprs = exps;
		this.then_exp = t;
		this.else_exp = e;
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
	 * Support for Iterator interface.
	 *
	 * @return Result of Iterator operation.
	 */
	@Override
	public Iterator<Expr> iterator() {
		return exprs.iterator();
	}

	/**
	 * Support for Expression interface.
	 *
	 * @return Result of Expr operation.
	 */
	public Expr then_clause() {
		return then_exp;
	}

	/**
	 * Support for Expression interface.
	 *
	 * @return Result of Expr operation.
	 */
	public Expr else_clause() {
		return else_exp;
	}
	
}
