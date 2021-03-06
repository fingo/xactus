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
 *     Jesper Moller - bug 280555 - Add pluggable collation support
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import info.fingo.xactus.api.DynamicContext;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.AnyType;

/**
 * Class for compare for greater than operation.
 */
public interface CmpGt {
	/**
	 * Constructor for CmpGt
	 *
	 * @param arg
	 *            argument of any type.
	 * @param context TODO
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of operation, true/false.
	 */
	public boolean gt(AnyType arg, DynamicContext context) throws DynamicError;
}
