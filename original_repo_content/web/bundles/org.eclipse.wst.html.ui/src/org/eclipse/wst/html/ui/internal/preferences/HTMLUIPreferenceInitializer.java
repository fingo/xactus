/*******************************************************************************
 * Copyright (c) 2006, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Benjamin Muskalla, b.muskalla@gmx.net - [158660] character entities should have their own syntax highlighting preference      
 *     
 *******************************************************************************/
package org.eclipse.wst.html.ui.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.html.ui.internal.HTMLUIPlugin;
import org.eclipse.wst.html.ui.internal.style.IStyleConstantsHTML;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ColorHelper;
import org.eclipse.wst.sse.ui.preferences.AppearancePreferenceNames;
import org.eclipse.wst.xml.ui.internal.style.IStyleConstantsXML;

/**
 * Sets default values for HTML UI preferences
 */
public class HTMLUIPreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = HTMLUIPlugin.getDefault().getPreferenceStore();
		ColorRegistry registry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry();
		
		store.setDefault(HTMLUIPreferenceNames.AUTO_PROPOSE, true);
		store.setDefault(HTMLUIPreferenceNames.AUTO_PROPOSE_CODE, "<=");//$NON-NLS-1$
		store.setDefault(HTMLUIPreferenceNames.AUTO_PROPOSE_DELAY, 500);
		store.setDefault(HTMLUIPreferenceNames.INSERT_SINGLE_SUGGESTION, true);
		// HTML Style Preferences
		String NOBACKGROUNDBOLD = " | null | false"; //$NON-NLS-1$
		String JUSTITALIC = " | null | false | true"; //$NON-NLS-1$
		String styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.TAG_ATTRIBUTE_NAME, 127, 0, 127) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.TAG_ATTRIBUTE_NAME, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.TAG_ATTRIBUTE_VALUE, 42, 0, 255) + JUSTITALIC;
		store.setDefault(IStyleConstantsXML.TAG_ATTRIBUTE_VALUE, styleValue);

		styleValue = "null" + NOBACKGROUNDBOLD; //$NON-NLS-1$
		store.setDefault(IStyleConstantsXML.TAG_ATTRIBUTE_EQUALS, styleValue); // specified value is black; leaving as widget default

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.COMMENT_BORDER, 63, 95, 191) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.COMMENT_BORDER, styleValue);
		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.COMMENT_TEXT, 63, 95, 191) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.COMMENT_TEXT, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.DECL_BORDER, 0, 128, 128) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.DECL_BORDER, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.DOCTYPE_NAME, 0, 0, 128) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.DOCTYPE_NAME, styleValue);
		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.DOCTYPE_EXTERNAL_ID_PUBREF, 0, 0, 128) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.DOCTYPE_EXTERNAL_ID_PUBREF, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.DOCTYPE_EXTERNAL_ID, 128, 128, 128) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.DOCTYPE_EXTERNAL_ID, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.DOCTYPE_EXTERNAL_ID_SYSREF, 63, 127, 95) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.DOCTYPE_EXTERNAL_ID_SYSREF, styleValue);

		styleValue = "null" + NOBACKGROUNDBOLD; //$NON-NLS-1$
		store.setDefault(IStyleConstantsXML.XML_CONTENT, styleValue);	// specified value is black; leaving as widget default

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.TAG_BORDER, 0, 128, 128) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.TAG_BORDER, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.TAG_NAME, 63, 127, 127) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.TAG_NAME, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.PI_BORDER, 0, 128, 128) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.PI_BORDER, styleValue);

		styleValue = "null" + NOBACKGROUNDBOLD; //$NON-NLS-1$
		store.setDefault(IStyleConstantsXML.PI_CONTENT, styleValue);	// specified value is black; leaving as widget default

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.CDATA_BORDER, 0, 128, 128) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.CDATA_BORDER, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.CDATA_TEXT, 0, 0, 0) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.CDATA_TEXT, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsHTML.SCRIPT_AREA_BORDER, 191, 95, 63) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsHTML.SCRIPT_AREA_BORDER, styleValue);

		styleValue = ColorHelper.findRGBString(registry, IStyleConstantsXML.ENTITY_REFERENCE, 42, 0, 255) + NOBACKGROUNDBOLD;
		store.setDefault(IStyleConstantsXML.ENTITY_REFERENCE, styleValue);

		// set default new html 5 file template to use in new file wizard
		/*
		 * Need to find template name that goes with default template id (name
		 * may change for different language)
		 */
		store.setDefault(HTMLUIPreferenceNames.NEW_FILE_TEMPLATE_ID, "org.eclipse.wst.html.ui.templates.html5"); //$NON-NLS-1$
		
		// Defaults for the Typing preference page
		store.setDefault(HTMLUIPreferenceNames.TYPING_COMPLETE_COMMENTS, true);
		store.setDefault(HTMLUIPreferenceNames.TYPING_COMPLETE_END_TAGS, true);
		store.setDefault(HTMLUIPreferenceNames.TYPING_COMPLETE_ELEMENTS, true);
		store.setDefault(HTMLUIPreferenceNames.TYPING_REMOVE_END_TAGS, true);
		store.setDefault(HTMLUIPreferenceNames.TYPING_CLOSE_STRINGS, true);
		store.setDefault(HTMLUIPreferenceNames.TYPING_CLOSE_BRACKETS, true);
		
		// Defaults for Content Assist preference page
		store.setDefault(HTMLUIPreferenceNames.CONTENT_ASSIST_DO_NOT_DISPLAY_ON_DEFAULT_PAGE, "org.eclipse.wst.html.ui.proposalCategory.generic");
		store.setDefault(HTMLUIPreferenceNames.CONTENT_ASSIST_DO_NOT_DISPLAY_ON_OWN_PAGE, "");
		store.setDefault(HTMLUIPreferenceNames.CONTENT_ASSIST_DEFAULT_PAGE_SORT_ORDER,
				"org.eclipse.wst.html.ui.proposalCategory.htmlTags\0" +
				"org.eclipse.wst.html.ui.proposalCategory.generic\0" +
				"org.eclipse.wst.css.ui.proposalCategory.css\0" +
				"org.eclipse.wst.html.ui.proposalCategory.htmlTemplates\0" +
				"org.eclipse.wst.css.ui.proposalCategory.cssTemplates");
		store.setDefault(HTMLUIPreferenceNames.CONTENT_ASSIST_OWN_PAGE_SORT_ORDER,
				"org.eclipse.wst.html.ui.proposalCategory.htmlTemplates\0"+
				"org.eclipse.wst.css.ui.proposalCategory.cssTemplates\0" +
				"org.eclipse.wst.html.ui.proposalCategory.htmlTags\0" +
				"org.eclipse.wst.css.ui.proposalCategory.css\0" +
				"org.eclipse.wst.html.ui.proposalCategory.generic");

		initAppearancePreferences(store, registry);
	}

	private void initAppearancePreferences(IPreferenceStore store, ColorRegistry registry) {
		/* these annotation preferences are not part of base text editor
		 preference */
		store.setDefault(AppearancePreferenceNames.EVALUATE_TEMPORARY_PROBLEMS, true);
		// matching brackets is not part of base text editor preference
		// set default enable folding value
		store.setDefault(AppearancePreferenceNames.FOLDING_ENABLED, true);
		
		// set default for show message dialog when unknown content type in editor
		store.setDefault(AppearancePreferenceNames.SHOW_UNKNOWN_CONTENT_TYPE_MSG, true);

		store.setDefault(AppearancePreferenceNames.SEMANTIC_HIGHLIGHTING, true);

		// matching brackets enablement and color
		store.setDefault(AppearancePreferenceNames.MATCHING_BRACKETS, true);
		PreferenceConverter.setDefault(store, AppearancePreferenceNames.MATCHING_BRACKETS_COLOR, ColorHelper.findRGB(registry, AppearancePreferenceNames.MATCHING_BRACKETS_COLOR, new RGB(192, 192, 192)));

		// set content assist defaults
		PreferenceConverter.setDefault(store, AppearancePreferenceNames.CODEASSIST_PROPOSALS_BACKGROUND, ColorHelper.findRGB(registry, AppearancePreferenceNames.CODEASSIST_PROPOSALS_BACKGROUND, new RGB(255, 255, 255)));
		PreferenceConverter.setDefault(store, AppearancePreferenceNames.CODEASSIST_PROPOSALS_FOREGROUND, ColorHelper.findRGB(registry, AppearancePreferenceNames.CODEASSIST_PROPOSALS_FOREGROUND, new RGB(0, 0, 0)));
		PreferenceConverter.setDefault(store, AppearancePreferenceNames.CODEASSIST_PARAMETERS_BACKGROUND, ColorHelper.findRGB(registry, AppearancePreferenceNames.CODEASSIST_PARAMETERS_BACKGROUND, new RGB(255, 255, 255)));
		PreferenceConverter.setDefault(store, AppearancePreferenceNames.CODEASSIST_PARAMETERS_FOREGROUND, ColorHelper.findRGB(registry, AppearancePreferenceNames.CODEASSIST_PARAMETERS_FOREGROUND, new RGB(0, 0, 0)));

		// hover help preferences are not part of base text editor preference
		String mod2Name = Action.findModifierString(SWT.MOD2);
		/*
		 * SWT.MOD2 is currently SWT.COMMAND on Mac; SWT.CONTROL elsewhere
		 */
		store.setDefault(AppearancePreferenceNames.EDITOR_TEXT_HOVER_MODIFIERS, "combinationHover|true|0;problemHover|false|0;documentationHover|false|0;annotationHover|true|" + mod2Name); //$NON-NLS-1$
	}
}
