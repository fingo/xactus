/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
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
package org.eclipse.wst.xml.ui.internal.preferences;

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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.eclipse.wst.xml.ui.internal.XMLUIPlugin;
import org.eclipse.wst.xml.ui.internal.editor.IHelpContextIds;


/**
 * Preference page for XML templates
 */
public class XMLTemplatePreferencePage extends TemplatePreferencePage {

	class XMLEditTemplateDialog extends EditTemplateDialog {
		public XMLEditTemplateDialog(Shell parent, Template template, boolean edit, boolean isNameModifiable, ContextTypeRegistry registry) {
			super(parent, template, edit, isNameModifiable, registry);
		}

		protected SourceViewer createViewer(Composite parent) {
			SourceViewerConfiguration sourceViewerConfiguration = new StructuredTextViewerConfiguration() {
				StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationXML();

				public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
					return baseConfiguration.getConfiguredContentTypes(sourceViewer);
				}

				public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
					return baseConfiguration.getLineStyleProviders(sourceViewer, partitionType);
				}

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

	public XMLTemplatePreferencePage() {
		XMLUIPlugin xmlEditorPlugin = XMLUIPlugin.getDefault();

		setPreferenceStore(xmlEditorPlugin.getPreferenceStore());
		setTemplateStore(xmlEditorPlugin.getTemplateStore());
		setContextTypeRegistry(xmlEditorPlugin.getTemplateContextRegistry());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite ancestor) {
		Control c = super.createContents(ancestor);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(c, IHelpContextIds.XML_PREFWEBX_TEMPLATES_HELPID);
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.templates.TemplatePreferencePage#createViewer(org.eclipse.swt.widgets.Composite)
	 */
	protected SourceViewer createViewer(Composite parent) {
		SourceViewerConfiguration sourceViewerConfiguration = new StructuredTextViewerConfiguration() {
			StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationXML();

			public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
				return baseConfiguration.getConfiguredContentTypes(sourceViewer);
			}

			public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
				return baseConfiguration.getLineStyleProviders(sourceViewer, partitionType);
			}
		};
		return doCreateViewer(parent, sourceViewerConfiguration);
	}

	SourceViewer doCreateViewer(Composite parent, SourceViewerConfiguration viewerConfiguration) {
		SourceViewer viewer = null;
		String contentTypeID = ContentTypeIdForXML.ContentTypeID_XML;
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
	protected Template editTemplate(Template template, boolean edit, boolean isNameModifiable) {
		EditTemplateDialog dialog = new XMLEditTemplateDialog(getShell(), template, edit, isNameModifiable, getContextTypeRegistry());
		if (dialog.open() == Window.OK) {
			return dialog.getTemplate();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.templates.TemplatePreferencePage#isShowFormatterSetting()
	 */
	protected boolean isShowFormatterSetting() {
		// template formatting has not been implemented
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean ok = super.performOk();
		XMLUIPlugin.getDefault().savePluginPreferences();
		return ok;
	}
}
