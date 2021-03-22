/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
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
package org.eclipse.wst.json.ui.internal.preferences;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;
import org.eclipse.wst.json.core.contenttype.ContentTypeIdForJSON;
import org.eclipse.wst.json.ui.StructuredTextViewerConfigurationJSON;
import org.eclipse.wst.json.ui.internal.JSONUIPlugin;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;

/**
 * Preference page for JSON templates
 */
public class JSONTemplatePreferencePage extends TemplatePreferencePage {

	class JSONEditTemplateDialog extends EditTemplateDialog {
		public JSONEditTemplateDialog(Shell parent, Template template, boolean edit, boolean isNameModifiable, ContextTypeRegistry registry) {
			super(parent, template, edit, isNameModifiable, registry);
		}

		@Override
		protected SourceViewer createViewer(Composite parent) {			SourceViewerConfiguration sourceViewerConfiguration = new StructuredTextViewerConfiguration() {
			StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationJSON();

			@Override
			public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
				return baseConfiguration.getConfiguredContentTypes(sourceViewer);
			}

			@Override
			public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
				return baseConfiguration.getLineStyleProviders(sourceViewer, partitionType);
			}

			@Override
			public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
				ContentAssistant assistant = new ContentAssistant();
				assistant.enableAutoActivation(true);
				assistant.enableAutoInsert(true);
				assistant.setContentAssistProcessor(getTemplateProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
				return assistant;
			}
		};
		return doCreateViewer(parent, sourceViewerConfiguration);
}
	}

	public JSONTemplatePreferencePage() {
		JSONUIPlugin jsonUIPlugin = JSONUIPlugin.getDefault();

		setPreferenceStore(jsonUIPlugin.getPreferenceStore());
		setTemplateStore(jsonUIPlugin.getTemplateStore());
		setContextTypeRegistry(jsonUIPlugin.getTemplateContextRegistry());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		boolean ok = super.performOk();
		JSONUIPlugin.getDefault().savePluginPreferences();
		return ok;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.templates.TemplatePreferencePage#isShowFormatterSetting()
	 */
	@Override
	protected boolean isShowFormatterSetting() {
		// template formatting has not been implemented
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite ancestor) {
		Control c = super.createContents(ancestor);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(c,
		// IHelpContextIds.CSS_PREFWEBX_TEMPLATES_HELPID);
		return c;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.texteditor.templates.TemplatePreferencePage#createViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected SourceViewer createViewer(Composite parent) {
		SourceViewerConfiguration sourceViewerConfiguration = new StructuredTextViewerConfiguration() {
			StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationJSON();

			@Override
			public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
				return baseConfiguration.getConfiguredContentTypes(sourceViewer);
			}

			@Override
			public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
				return baseConfiguration.getLineStyleProviders(sourceViewer, partitionType);
			}
		};
		return doCreateViewer(parent, sourceViewerConfiguration);
	}

	SourceViewer doCreateViewer(Composite parent, SourceViewerConfiguration viewerConfiguration) {
		SourceViewer viewer = null;
		String contentTypeID = ContentTypeIdForJSON.ContentTypeID_JSON;
		viewer = new StructuredTextViewer(parent, null, null, false, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		viewer.getTextWidget().setFont(JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$
		IStructuredModel scratchModel = StructuredModelManager.getModelManager().createUnManagedStructuredModelFor(contentTypeID);
		IDocument document = scratchModel.getStructuredDocument();
		viewer.configure(viewerConfiguration);
		viewer.setDocument(document);
		return viewer;
	}

	/**
	 * Creates the edit dialog. Subclasses may override this method to provide
	 * a custom dialog.
	 *
	 * @param template
	 *            the template being edited
	 * @param edit
	 *            whether the dialog should be editable
	 * @param isNameModifiable
	 *            whether the template name may be modified
	 * @return the created or modified template, or <code>null</code> if the
	 *         edition failed
	 * @since 3.1
	 */
	@Override
	protected Template editTemplate(Template template, boolean edit, boolean isNameModifiable) {
		EditTemplateDialog dialog = new JSONEditTemplateDialog(getShell(), template, edit, isNameModifiable, getContextTypeRegistry());
		if (dialog.open() == Window.OK) {
			return dialog.getTemplate();
		}
		return null;
	}
}
