/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.css.core.internal.provisional.text;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredPartitionTypes;

/**
 * This interface is not intended to be implemented. It defines the
 * partitioning for CSS and all its partitions. Clients should reference the
 * partition type Strings defined here directly.
 * 
 * @deprecated org.eclipse.wst.css.core.text.ICSSPartitions
 */
public interface ICSSPartitionTypes extends IStructuredPartitionTypes {

	String STYLE = "org.eclipse.wst.css.STYLE"; //$NON-NLS-1$
}
