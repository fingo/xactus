/*******************************************************************************
 * Copyright (c) 2001, 2015 IBM Corporation and others.
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
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.xml.ui.internal.preferences.XMLFilesPreferencePage
 *                                           modified in order to process JSON Objects.                    
 *******************************************************************************/
package org.eclipse.wst.json.ui.internal.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.json.core.JSONCorePlugin;
import org.eclipse.wst.json.core.contenttype.ContentTypeIdForJSON;
import org.eclipse.wst.json.core.preferences.JSONCorePreferenceNames;
import org.eclipse.wst.json.ui.internal.JSONUIMessages;
//import org.eclipse.wst.json.ui.internal.editor.IHelpContextIds;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;
import org.eclipse.wst.sse.ui.internal.editor.IHelpContextIds;
import org.eclipse.wst.sse.ui.internal.preferences.ui.AbstractPreferencePage;

public class JSONFilesPreferencePage extends AbstractPreferencePage {
	protected EncodingSettings fEncodingSettings = null;

	private Combo fDefaultSuffix;
	private List fValidExtensions;

	protected Control createContents(Composite parent) {
		Composite composite = (Composite) super.createContents(parent);
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IHelpContextIds.JSON_PREFWEBX_FILES_HELPID);
		createContentsForCreatingGroup(composite);

		setSize(composite);
		loadPreferences();

		return composite;
	}

	protected void createContentsForCreatingGroup(Composite parent) {
		Group creatingGroup = createGroup(parent, 2);
		creatingGroup.setText(JSONUIMessages.Creating_files);

		// Default extension for New file Wizard
		createLabel(creatingGroup, JSONUIMessages.JSONFilesPreferencePage_ExtensionLabel);
		fDefaultSuffix = createDropDownBox(creatingGroup);
		String[] validExtensions = (String[]) getValidExtensions().toArray(new String[0]);
		Arrays.sort(validExtensions);
		fDefaultSuffix.setItems(validExtensions);
		fDefaultSuffix.addSelectionListener(this);

		Label label = createLabel(creatingGroup, JSONUIMessages.Encoding_desc);
		((GridData) label.getLayoutData()).horizontalSpan = 2;
		fEncodingSettings = new EncodingSettings(creatingGroup, JSONUIMessages.Encoding);
		((GridData) fEncodingSettings.getLayoutData()).horizontalSpan = 2;
	}

	public void dispose() {
		fDefaultSuffix.removeModifyListener(this);
		super.dispose();
	}

	protected void doSavePreferenceStore() {
		JSONCorePlugin.getDefault().savePluginPreferences(); // model
	}

	/**
	 * Get content type associated with this new file wizard
	 * 
	 * @return IContentType
	 */
	protected IContentType getContentType() {
		return Platform.getContentTypeManager().getContentType(ContentTypeIdForJSON.ContentTypeID_JSON);
	}

	/**
	 * Get list of valid extensions
	 * 
	 * @return List
	 */
	private List getValidExtensions() {
		if (fValidExtensions == null) {
			IContentType type = getContentType();
			fValidExtensions = new ArrayList(Arrays.asList(type.getFileSpecs(IContentType.FILE_EXTENSION_SPEC)));
		}
		return fValidExtensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.preferences.ui.AbstractPreferencePage#getModelPreferences()
	 */
	protected Preferences getModelPreferences() {
		return JSONCorePlugin.getDefault().getPluginPreferences();
	}

	protected void initializeValues() {
		initializeValuesForCreatingGroup();
	}

	protected void initializeValuesForCreatingGroup() {
		String suffix = getModelPreferences().getString(JSONCorePreferenceNames.DEFAULT_EXTENSION);
		fDefaultSuffix.setText(suffix);

		String encoding = getModelPreferences().getString(CommonEncodingPreferenceNames.OUTPUT_CODESET);

		fEncodingSettings.setIANATag(encoding);
	}

	protected void performDefaults() {
		performDefaultsForCreatingGroup();

		super.performDefaults();
	}

	protected void performDefaultsForCreatingGroup() {
		String suffix = getModelPreferences().getDefaultString(JSONCorePreferenceNames.DEFAULT_EXTENSION);
		fDefaultSuffix.setText(suffix);

		String encoding = getModelPreferences().getDefaultString(CommonEncodingPreferenceNames.OUTPUT_CODESET);

		fEncodingSettings.setIANATag(encoding);
		// fEncodingSettings.resetToDefaultEncoding();
	}

	public boolean performOk() {
		boolean result = super.performOk();

		doSavePreferenceStore();

		return result;
	}

	protected void storeValues() {
		storeValuesForCreatingGroup();
	}

	protected void storeValuesForCreatingGroup() {
		String suffix = fDefaultSuffix.getText();
		getModelPreferences().setValue(JSONCorePreferenceNames.DEFAULT_EXTENSION, suffix);

		getModelPreferences().setValue(CommonEncodingPreferenceNames.OUTPUT_CODESET, fEncodingSettings.getIANATag());
	}

	protected void validateValues() {
		boolean isValid = false;
		Iterator i = getValidExtensions().iterator();
		while (i.hasNext() && !isValid) {
			String extension = (String) i.next();
			isValid = extension.equalsIgnoreCase(fDefaultSuffix.getText());
		}

		if (!isValid) {
			setErrorMessage(NLS.bind(JSONUIMessages.JSONFilesPreferencePage_ExtensionError, getValidExtensions().toString()));
			setValid(false);
		}
		else {
			setErrorMessage(null);
			setValid(true);
		}
	}
}
