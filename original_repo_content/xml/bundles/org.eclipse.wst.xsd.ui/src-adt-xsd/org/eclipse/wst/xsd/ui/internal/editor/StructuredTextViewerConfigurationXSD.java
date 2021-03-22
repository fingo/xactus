/*******************************************************************************
 * Copyright (c) 2004, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *******************************************************************************/
package org.eclipse.wst.xsd.ui.internal.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;

/**
 * Configuration for editing XSD content type
 */
public class StructuredTextViewerConfigurationXSD extends StructuredTextViewerConfigurationXML {
	protected IPreferenceStore[] createPreferenceStores() {
		IPreferenceStore[] defaults = super.createPreferenceStores();
		List preferenceStores = new ArrayList();
		preferenceStores.add(XSDEditorPlugin.getDefault().getPreferenceStore());
		for (int i = 0; i < defaults.length; i++) {
			preferenceStores.add(defaults[i]);
		}
		return (IPreferenceStore[]) preferenceStores.toArray(new IPreferenceStore[preferenceStores.size()]);
	}

	protected Map getHyperlinkDetectorTargets(ISourceViewer sourceViewer) {
		Map targets = super.getHyperlinkDetectorTargets(sourceViewer);
		targets.put("org.eclipse.wst.xsd.core.xsdsource", null);  //$NON-NLS-1$
		return targets;
	}
}
