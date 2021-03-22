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
package org.eclipse.wst.dtd.core.internal.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.dtd.core.internal.DTDNode;


public final class NodesEvent {
	private ArrayList changedNodes = new ArrayList();

	public void add(DTDNode changedNode) {
		changedNodes.add(changedNode);
	}

	public List getNodes() {
		return changedNodes;
	}

}
