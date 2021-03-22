/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
package org.eclipse.jst.jsp.ui.internal.registry;

import org.eclipse.jst.jsp.core.internal.modelhandler.ModelHandlerForJSP;
import org.eclipse.wst.html.ui.internal.contentoutline.JFaceNodeAdapterFactoryForHTML;
import org.eclipse.wst.sse.core.internal.PropagatingAdapter;
import org.eclipse.wst.sse.core.internal.ltk.modelhandler.IDocumentTypeHandler;
import org.eclipse.wst.sse.core.internal.model.FactoryRegistry;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.sse.ui.internal.provisional.registry.AdapterFactoryProvider;
import org.eclipse.wst.sse.ui.internal.util.Assert;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

public class AdapterFactoryProviderForJSP implements AdapterFactoryProvider {

	/*
	 * @see AdapterFactoryProvider#addAdapterFactories(IStructuredModel)
	 */
	public void addAdapterFactories(IStructuredModel structuredModel) {
		// these are the main factories, on model's factory registry
		addContentBasedFactories(structuredModel);
		// -------
		// Must update/add to propagating adapters here too
		addPropagatingAdapters(structuredModel);
	}

	protected void addContentBasedFactories(IStructuredModel structuredModel) {
		FactoryRegistry factoryRegistry = structuredModel.getFactoryRegistry();
		Assert.isNotNull(factoryRegistry, "Program Error: client caller must ensure model has factory registry"); //$NON-NLS-1$
		INodeAdapterFactory factory = null;
		factory = factoryRegistry.getFactoryFor(IJFaceNodeAdapter.class);
		if (factory == null) {
			factory = new JFaceNodeAdapterFactoryForHTML(IJFaceNodeAdapter.class, true);
			factoryRegistry.addFactory(factory);
		}

		ModelHandlerForJSP.ensureTranslationAdapterFactory(structuredModel);
	}

	protected void addPropagatingAdapters(IStructuredModel structuredModel) {

		if (structuredModel instanceof IDOMModel) {
			IDOMModel xmlModel = (IDOMModel) structuredModel;
			IDOMDocument document = xmlModel.getDocument();
			PropagatingAdapter propagatingAdapter = (PropagatingAdapter) document.getAdapterFor(PropagatingAdapter.class);
			if (propagatingAdapter != null) {
				// what to do?
			}
		}
	}

	/*
	 * @see AdapterFactoryProvider#isFor(ContentTypeDescription)
	 */
	public boolean isFor(IDocumentTypeHandler contentTypeDescription) {
		return (contentTypeDescription instanceof ModelHandlerForJSP);
	}

	public void reinitializeFactories(IStructuredModel structuredModel) {
	}
}
