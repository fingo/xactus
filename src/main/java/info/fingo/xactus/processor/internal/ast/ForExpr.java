/*******************************************************************************
 * Copyright (c) 2005, 2013 Andrea Bittau, University College London, and others
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
 * Class for the For expression.
 */
public class ForExpr extends Expr implements Iterable<VarExprPair> {

	private final Collection<VarExprPair> var_expr_pairs;
	private Expr return_expr;

	/**
	 * Constructor for ForExpr.
	 *
	 * @param varexp Expressions.
	 * @param ret    Return expression.
	 */
	public ForExpr(Collection<VarExprPair> varexp, Expr ret) {

		this.var_expr_pairs = varexp;
		this.return_expr = ret;
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
	public Iterator<VarExprPair> iterator() {
		return var_expr_pairs.iterator();
	}

	/**
	 * Support for Expr interface.
	 *
	 * @return Result of Expr operation.
	 */
	public Expr expr() {
		return return_expr;
	}

	/**
	 * Set Expression.
	 *
	 * @param e Expression.
	 */
	public void set_expr(Expr e) {
		return_expr = e;
	}

	// used for normalization... basically just keep a "simple for"... no
	// pairs... collection will always have 1 element
	/**
	 * Normalization of expression pairs.
	 */
	public void truncate_pairs() {
		boolean first = true;

		for (Iterator<VarExprPair> i = var_expr_pairs.iterator(); i.hasNext();) {
			i.next();
			if (!first) {
				i.remove();
			}

			first = false;
		}
	}

	/**
	 * Support for Collection interface.
	 *
	 * @return Expression pairs.
	 */
	public Collection<VarExprPair> ve_pairs() {
		return var_expr_pairs;
	}

}
