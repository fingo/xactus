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


package org.eclipse.wst.dtd.ui.internal.views.contentoutline.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.dtd.core.internal.CMGroupNode;
import org.eclipse.wst.dtd.core.internal.DTDNode;
import org.eclipse.wst.dtd.core.internal.Element;
import org.eclipse.wst.dtd.core.internal.document.DTDModelImpl;


public class AddElementToContentModelAction extends BaseAction {

	public AddElementToContentModelAction(DTDModelImpl model, String label) {
		super(model, label);
	}

	public void run() {
		DTDNode node = getFirstNodeSelected();

		if (node instanceof CMGroupNode) {
			((CMGroupNode) node).addChild();
		}
		else if (node instanceof Element) {
			((Element) node).addChild();
		}
	}

	protected boolean updateSelection(IStructuredSelection selection) {
		boolean rc = super.updateSelection(selection);
		DTDNode node = getFirstNodeSelected(selection);
		if (node instanceof CMGroupNode) {
			setEnabled(true);
		}
		else {
			setEnabled(false);
		}
		return rc;
	}
}
