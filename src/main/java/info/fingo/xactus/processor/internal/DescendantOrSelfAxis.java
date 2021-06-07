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

package info.fingo.xactus.processor.internal;

import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.processor.internal.types.NodeType;
import org.w3c.dom.Node;

/**
 * The descendant-or-self axis contains the context node and the descendants of
 * the context node.
 */
// multiple inheretance might be cool here =D
public class DescendantOrSelfAxis extends ForwardAxis {

	/**
	 * Retrieve the the descendants of the context node and the context node
	 * itself.
	 *
	 * @param node
	 *            is the type of node.
	 */
	public void iterate(NodeType node, ResultBuffer rs, Node limitNode) {

		// add self
		rs.add(node);

		// add descendants
		new DescendantAxis().iterate(node, rs, null);
	}

	public String name() {
		return "descendant-or-self";
	}
}
