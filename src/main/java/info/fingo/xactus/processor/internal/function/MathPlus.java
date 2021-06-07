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
 *******************************************************************************/

package info.fingo.xactus.processor.internal.function;

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.processor.DynamicError;

/**
 * Support for Mathematical Addition.
 */
public interface MathPlus {
	/**
	 * Addition operation.
	 *
	 * @param arg
	 *            input argument.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of operation.
	 */
	public ResultSequence plus(ResultSequence arg) throws DynamicError;
}
