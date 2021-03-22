/*******************************************************************************
 * Copyright (c) 2007, 2013 IBM Corporation and others.
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
/**
 * 
 */
package org.eclipse.wst.jsdt.web.support.jsp;

import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jst.jsp.core.text.IJSPPartitions;
import org.eclipse.jst.jsp.ui.StructuredTextViewerConfigurationJSP;
import org.eclipse.wst.html.core.text.IHTMLPartitions;
import org.eclipse.wst.jsdt.web.ui.StructuredTextViewerConfigurationJSDT;
import org.eclipse.wst.jsdt.web.ui.internal.autoedit.AutoEditStrategyForJs;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
/**
*
* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*/
public class JSDTStructuredTextViewerConfigurationJSP extends StructuredTextViewerConfigurationJSP{

	private StructuredTextViewerConfiguration fHTMLSourceViewerConfiguration;

	private StructuredTextViewerConfiguration getJSDTHTMLSourceViewerConfiguration() {
		if (fHTMLSourceViewerConfiguration == null) {
			fHTMLSourceViewerConfiguration = new StructuredTextViewerConfigurationJSDT();
		}
		return fHTMLSourceViewerConfiguration;
	}
	
	public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType) {
		String[] indentations = null;
		if (IHTMLPartitions.SCRIPT.equals(contentType) || IJSPPartitions.JSP_CONTENT_JAVASCRIPT.equals(contentType) || IHTMLPartitions.SCRIPT_EVENTHANDLER.equals(contentType))
			indentations = getJSDTHTMLSourceViewerConfiguration().getIndentPrefixes(sourceViewer, contentType);
		else
			indentations = super.getIndentPrefixes(sourceViewer, contentType);
		return indentations;
	}
	
	public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
		LineStyleProvider[] providers = null;
		if (IHTMLPartitions.SCRIPT.equals(partitionType) || IJSPPartitions.JSP_CONTENT_JAVASCRIPT.equals(partitionType) || IHTMLPartitions.SCRIPT_EVENTHANDLER.equals(partitionType)) {
			providers = getJSDTHTMLSourceViewerConfiguration().getLineStyleProviders(sourceViewer, IHTMLPartitions.SCRIPT);
		}
		else{
			providers = super.getLineStyleProviders(sourceViewer, partitionType);
		}

		return providers;
	}
	
	/**
	 * @see org.eclipse.jst.jsp.ui.StructuredTextViewerConfigurationJSP#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		if(contentType.equals(IHTMLPartitions.SCRIPT) || contentType.equals(IHTMLPartitions.SCRIPT_EVENTHANDLER)) {
			IAutoEditStrategy[] strategies = new IAutoEditStrategy[1];
			strategies[0] = new AutoEditStrategyForJs();
			return strategies;
		} else {
			return super.getAutoEditStrategies(sourceViewer, contentType);
		}
	}

	protected IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) {
		return super.getContentAssistProcessors(sourceViewer, partitionType);
	}
}
