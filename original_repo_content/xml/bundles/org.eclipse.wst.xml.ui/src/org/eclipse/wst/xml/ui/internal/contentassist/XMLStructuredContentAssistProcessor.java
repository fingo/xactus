/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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
package org.eclipse.wst.xml.ui.internal.contentassist;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.wst.sse.ui.contentassist.StructuredContentAssistProcessor;
import org.eclipse.wst.xml.ui.internal.XMLUIPlugin;
import org.eclipse.wst.xml.ui.internal.preferences.XMLUIPreferenceNames;

/**
 * <p>Implementation of {@link StructuredContentAssistProcessor} for XML documents</p>
 * 
 * <p>This implementation will react to user preference changes for auto activation
 * characters for XML pages</p>
 * 
 * @see StructuredContentAssistProcessor
 */
public class XMLStructuredContentAssistProcessor extends StructuredContentAssistProcessor {
	/** the auto activation characters for this processor, set by user preference */
	private char[] fCompletionProposalAutoActivationCharacters;
	
	/** the context information validator for this processor */
	private IContextInformationValidator fContextInformationValidator;
	
	/**
	 * <p>Constructor</p>
	 * 
	 * @param assistant {@link ContentAssistant} to use
	 * @param partitionTypeID the partition type this processor is for
	 * @param viewer {@link ITextViewer} this processor is acting in
	 */
	public XMLStructuredContentAssistProcessor(ContentAssistant assistant,
			String partitionTypeID, ITextViewer viewer) {
		
		super(assistant, partitionTypeID, viewer, XMLUIPlugin.getDefault().getPreferenceStore());
		getAutoActivationCharacterPreferences();
		updateAutoActivationDelay();
	}

	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.StructuredContentAssistProcessor#getContextInformationValidator()
	 */
	public IContextInformationValidator getContextInformationValidator() {
		if (this.fContextInformationValidator == null) {
			this.fContextInformationValidator = new AttributeContextInformationPresenter();
		}
		return this.fContextInformationValidator;
	}

	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.StructuredContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return super.getCompletionProposalAutoActivationCharacters() != null ? super.getCompletionProposalAutoActivationCharacters() : this.fCompletionProposalAutoActivationCharacters;
	}
	
	/**
	 * @see org.eclipse.wst.sse.ui.contentassist.StructuredContentAssistProcessor#propertyChange(
	 * 	org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().equals(XMLUIPreferenceNames.AUTO_PROPOSE) ||
				event.getProperty().equals(XMLUIPreferenceNames.AUTO_PROPOSE_CODE)) {
			getAutoActivationCharacterPreferences();
		}else if (event.getProperty().equals(XMLUIPreferenceNames.AUTO_PROPOSE_DELAY)) {
			updateAutoActivationDelay();
		}
	}

	
	
	/**
	 * <p>Sets the auto activation delay in Content Assist</p>
	 */
	private void updateAutoActivationDelay() {
		IPreferenceStore store = getPreferenceStore();		
		boolean doAuto = store.getBoolean(XMLUIPreferenceNames.AUTO_PROPOSE);
		if (doAuto) {
			setAutoActivationDelay(store.getInt(XMLUIPreferenceNames.AUTO_PROPOSE_DELAY));
		} 
		
	}
	

	protected void getAutoActivationCharacterPreferences() {
		String key = XMLUIPreferenceNames.AUTO_PROPOSE;
		boolean doAuto = getPreferenceStore().getBoolean(key);
		if (doAuto) {
			key = XMLUIPreferenceNames.AUTO_PROPOSE_CODE;
			fCompletionProposalAutoActivationCharacters = getPreferenceStore().getString(key).toCharArray();
		}
		else {
			fCompletionProposalAutoActivationCharacters = null;
		}
	}
}
