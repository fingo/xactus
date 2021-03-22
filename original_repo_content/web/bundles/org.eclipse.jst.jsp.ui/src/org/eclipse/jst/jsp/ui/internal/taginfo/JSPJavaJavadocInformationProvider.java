/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.jst.jsp.ui.internal.taginfo;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension;
import org.eclipse.wst.sse.ui.internal.SSEUIPlugin;

/**
 * Provides javadoc context information for java code inside JSPs (Shows
 * tooltip description)
 * 
 * @deprecated StructuredTextViewerConfiguration creates the appropriate
 *             information provider
 */
public class JSPJavaJavadocInformationProvider implements IInformationProvider, IInformationProviderExtension {
	private ITextHover fTextHover = null;

	public JSPJavaJavadocInformationProvider() {
		fTextHover = SSEUIPlugin.getDefault().getTextHoverManager().createBestMatchHover(new JSPJavaJavadocHoverProcessor());
	}

	public IRegion getSubject(ITextViewer textViewer, int offset) {
		return fTextHover.getHoverRegion(textViewer, offset);
	}

	public String getInformation(ITextViewer textViewer, IRegion subject) {
		return (String) getInformation2(textViewer, subject);
	}

	public Object getInformation2(ITextViewer textViewer, IRegion subject) {
		return fTextHover.getHoverInfo(textViewer, subject);
	}
}
