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
 * Class for AxisStep, this generates a sequence of zero or more nodes. These
 * nodes are always returned in Document Order. This can be Forward Step or
 * Reverse Step.
 */
public class AxisStep extends StepExpr {
	
	private Step step;
	private final Collection<Collection<Expr>> exprs;

	/**
	 * Constructor for AxisStep.
	 *
	 * @param step
	 *            Defines forward/reverse step.
	 * @param exprs
	 *            Collection of xpath expressions.
	 */
	public AxisStep(Step step, Collection<Collection<Expr>> exprs) {
		this.step = step;
		this.exprs = exprs;
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
	 * Advances to next step.
	 *
	 * @return Previous step.
	 */
	public Step step() {
		return step;
	}

	/**
	 * Set the step direction.
	 */
	public void set_step(Step s) {
		step = s;
	}

	/**
	 * Interator.
	 *
	 * @return Iterated expressions.
	 */
	@Override
	public Iterator<Collection<Expr>> iterator() {
		return exprs.iterator();
	}

	/**
	 * Determines size of expressions.
	 *
	 * @return Size of expressions.
	 */
	public int predicate_count() {
		return exprs.size();
	}
	
}
