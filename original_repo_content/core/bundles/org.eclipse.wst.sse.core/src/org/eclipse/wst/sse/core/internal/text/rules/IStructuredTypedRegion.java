/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *******************************************************************************/
package org.eclipse.wst.sse.core.internal.text.rules;

import org.eclipse.jface.text.ITypedRegion;

/**
 * Similar to extended interface, except it allows the length, offset, and
 * type to be set. This is useful when iterating through a number of "small"
 * regions, that all map to the the same partion regions.
 */
public interface IStructuredTypedRegion extends IStructuredRegion, ITypedRegion {
	void setType(String partitionType);
}
