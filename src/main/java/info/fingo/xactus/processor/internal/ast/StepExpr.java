/*******************************************************************************
 * Copyright (c) 2005, 2009 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *******************************************************************************/

package info.fingo.xactus.processor.internal.ast;

import java.util.Collection;
import java.util.Iterator;

/**
 * Support for Step expressions.
 */
public abstract class StepExpr extends Expr implements Iterable<Collection<Expr>> {
	
	@Override
	public abstract Iterator<Collection<Expr>> iterator();
	
}
