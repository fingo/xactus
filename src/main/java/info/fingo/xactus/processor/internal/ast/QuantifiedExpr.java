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

// mite wanna do two separate classes at the end
/**
 * Support for Quantified expressions.
 */
public class QuantifiedExpr extends Expr implements Iterable<VarExprPair> {
	
	/**
	 * Set internal value for SOME.
	 */
	public static final int SOME = 0;
	/**
	 * Set internal value for ALL.
	 */
	public static final int ALL = 1;

	private int type;
	private Collection<VarExprPair> var_expr_pairs;
	private Expr return_exp;

	/**
	 * Constructor for QuantifiedExpr.
	 *
	 * @param type
	 *            Type (0 for SOME, 1 for ALL).
	 * @param varexp
	 *            Expressions.
	 * @param ret
	 *            Returned expression.
	 */
	public QuantifiedExpr(int type, Collection<VarExprPair> varexp, Expr ret) {
		
		this.type = type;
		this.var_expr_pairs = varexp;
		this.return_exp = ret;
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
	 * Support for Integer interface.
	 *
	 * @return Result of Int operation.
	 */
	public int type() {
		return type;
	}

	/**
	 * Support for Iterator inteface.
	 *
	 * @return Result of Iterator operation.
	 */
	@Override
	public Iterator<VarExprPair> iterator() {
		return var_expr_pairs.iterator();
	}

	/**
	 * Support for Expression interface.
	 *
	 * @return Result of Expr operation.
	 */
	public Expr expr() {
		return return_exp;
	}

	/**
	 * Set next expression.
	 *
	 * @param e
	 *            Expression.
	 */
	public void set_expr(Expr e) {
		return_exp = e;
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
			if (!first)
				i.remove();

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
