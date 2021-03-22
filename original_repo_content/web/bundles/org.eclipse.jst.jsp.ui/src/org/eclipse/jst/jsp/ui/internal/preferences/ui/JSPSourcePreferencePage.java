/*******************************************************************************
 * Copyright (c) 2005, 2012 IBM Corporation and others.
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
package org.eclipse.jst.jsp.ui.internal.preferences.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jst.jsp.ui.internal.JSPUIMessages;
import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;
import org.eclipse.jst.jsp.ui.internal.preferences.JSPUIPreferenceNames;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class JSPSourcePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Button fFormatterButton;

	public void init(IWorkbench workbench) {
		// do nothing
	}

	protected Control createContents(Composite parent) {
		Composite composite = createComposite(parent, 1);

		new PreferenceLinkArea(composite, SWT.WRAP | SWT.MULTI, "org.eclipse.wst.sse.ui.preferences.editor", JSPUIMessages._UI_STRUCTURED_TEXT_EDITOR_PREFS_LINK,//$NON-NLS-1$
					(IWorkbenchPreferenceContainer) getContainer(), null).getControl().setLayoutData(GridDataFactory.fillDefaults().indent(5, 0).hint(150, SWT.DEFAULT).create());
		new Label(composite, SWT.NONE).setLayoutData(GridDataFactory.swtDefaults().create());

		Text label = new Text(composite, SWT.READ_ONLY);
		label.setText(JSPUIMessages.JSPSourcePreferencePage_0);
		GridData data = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
		data.horizontalIndent = 0;
		label.setLayoutData(data);
		
		PreferenceLinkArea fileEditorsArea = new PreferenceLinkArea(composite, SWT.NONE, "org.eclipse.wst.html.ui.preferences.source", JSPUIMessages.JSPSourcePreferencePage_1,//$NON-NLS-1$
					(IWorkbenchPreferenceContainer) getContainer(), null);

		data = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
		data.horizontalIndent = 5;
		fileEditorsArea.getControl().setLayoutData(data);

		PreferenceLinkArea contentTypeArea = new PreferenceLinkArea(composite, SWT.NONE, "org.eclipse.wst.sse.ui.preferences.xml.source", JSPUIMessages.JSPSourcePreferencePage_2,//$NON-NLS-1$
					(IWorkbenchPreferenceContainer) getContainer(), null);

		data = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
		data.horizontalIndent = 5;
		contentTypeArea.getControl().setLayoutData(data);
		fFormatterButton = new Button(composite, SWT.CHECK);
		fFormatterButton.setText(JSPUIMessages.JSPSourcePreferencePage_format);
		fFormatterButton.setSelection(getPreferenceStore().getBoolean(JSPUIPreferenceNames.USE_HTML_FORMATTER));

		return composite;
	}

	private Composite createComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NULL);

		//GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		composite.setLayout(layout);

		//GridData
		GridData data = new GridData(GridData.FILL);
		data.horizontalIndent = 0;
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected IPreferenceStore doGetPreferenceStore() {
		return JSPUIPlugin.getInstance().getPreferenceStore();
	}

	protected void performDefaults() {
		if (fFormatterButton != null) {
			fFormatterButton.setSelection(getPreferenceStore().getDefaultBoolean(JSPUIPreferenceNames.USE_HTML_FORMATTER));
		}
		super.performDefaults();
	}

	public boolean performOk() {
		if (fFormatterButton != null) {
			getPreferenceStore().setValue(JSPUIPreferenceNames.USE_HTML_FORMATTER, fFormatterButton.getSelection());
		}
		return super.performOk();
	}
}
