/**
 *  Copyright (c) 2013-2015 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.ui.views.contentoutline;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;

class MyDelegatingStyledCellLabelProvider extends DelegatingStyledCellLabelProvider implements ILabelProvider {

	public MyDelegatingStyledCellLabelProvider(
			IStyledLabelProvider labelProvider) {
		super(labelProvider);
	}

	@Override
	public String getText(Object element) {
		return getStyledText(element).getString();
	}

}
