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
package org.eclipse.wst.json.ui.internal.text;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

/**
 * Presenter control creator for JSON.
 *
 */
public class JSONPresenterControlCreator extends
		AbstractReusableInformationControlCreator {

	@Override
	public IInformationControl doCreateInformationControl(Shell parent) {
		if (BrowserInformationControl.isAvailable(parent)) {
			ToolBarManager tbm = new ToolBarManager(SWT.FLAT);
			JSONBrowserInformationControl control = new JSONBrowserInformationControl(
					parent, null, tbm);
			tbm.update(true);
			// addLinkListener(control);
			return control;
		} else {
			return new DefaultInformationControl(parent, true);
		}
	}

	// protected void addLinkListener(TernBrowserInformationControl control) {
	// HoverLocationListener.addLinkListener(control);
	// }

}
