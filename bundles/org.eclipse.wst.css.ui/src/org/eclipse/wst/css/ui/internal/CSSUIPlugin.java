/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.css.ui.internal;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.sse.ui.internal.SSEUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class CSSUIPlugin extends AbstractUIPlugin {
	public final static String ID = "org.eclipse.wst.css.ui"; //$NON-NLS-1$
	//The shared instance.
	private static CSSUIPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	private static final String KEY_PREFIX = "%"; //$NON-NLS-1$
	private static final String KEY_DOUBLE_PREFIX = "%%"; //$NON-NLS-1$	

	/**
	 * The constructor.
	 */
	public CSSUIPlugin() {
		super();
		plugin = this;

		// Force a call to initialize default preferences since
		// initializeDefaultPreferences is only called if *this* plugin's
		// preference store is accessed
		initializeDefaultCSSPreferences(SSEUIPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * Returns the shared instance.
	 */
	public static CSSUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * @deprecated using CSSUIPreferenceInitializer instead
	 */
	private void initializeDefaultCSSPreferences(IPreferenceStore store) {
//		String ctId = IContentTypeIdentifier.ContentTypeID_CSS;
//
//		// CSS Style Preferences
//		String NOBACKGROUNDBOLD = " | null | false"; //$NON-NLS-1$
//		String styleValue = "null" + NOBACKGROUNDBOLD; //$NON-NLS-1$
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.NORMAL, ctId), styleValue);
//
//		styleValue = ColorHelper.getColorString(63, 127, 127) + NOBACKGROUNDBOLD;
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.ATMARK_RULE, ctId), styleValue);
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.SELECTOR, ctId), styleValue);
//
//		styleValue = ColorHelper.getColorString(42, 0, 225) + NOBACKGROUNDBOLD;
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.MEDIA, ctId), styleValue);
//
//		styleValue = ColorHelper.getColorString(63, 95, 191) + NOBACKGROUNDBOLD;
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.COMMENT, ctId), styleValue);
//
//		styleValue = ColorHelper.getColorString(127, 0, 127) + NOBACKGROUNDBOLD;
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.PROPERTY_NAME, ctId), styleValue);
//
//		styleValue = ColorHelper.getColorString(42, 0, 225) + NOBACKGROUNDBOLD;
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.PROPERTY_VALUE, ctId), styleValue);
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.URI, ctId), styleValue);
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.STRING, ctId), styleValue);
//
//		styleValue = "null" + NOBACKGROUNDBOLD; //$NON-NLS-1$
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.COLON, ctId), styleValue);
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.SEMI_COLON, ctId), styleValue);
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.CURLY_BRACE, ctId), styleValue);
//
//		styleValue = ColorHelper.getColorString(191, 63, 63) + NOBACKGROUNDBOLD;
//		store.setDefault(PreferenceKeyGenerator.generateKey(IStyleConstantsCSS.ERROR, ctId), styleValue);
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String value) {
		String s = value.trim();
		if (!s.startsWith(KEY_PREFIX, 0))
			return s;
		if (s.startsWith(KEY_DOUBLE_PREFIX, 0))
			return s.substring(1);

		int ix = s.indexOf(' ');
		String key = ix == -1 ? s : s.substring(0, ix);

		ResourceBundle bundle = getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key.substring(1)) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public static String getResourceString(String key, Object[] args) {

		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (IllegalArgumentException e) {
			return getResourceString(key);
		}

	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("org.eclipse.wst.css.ui.internal.CSSUIPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
}
