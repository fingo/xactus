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
package org.eclipse.jst.jsp.ui.internal.registry;

import java.util.Iterator;

import org.eclipse.jst.jsp.core.internal.document.PageDirectiveAdapter;
import org.eclipse.jst.jsp.core.internal.java.IJSPTranslation;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslationAdapterFactory;
import org.eclipse.jst.jsp.core.internal.modelhandler.ModelHandlerForJSP;
import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.wst.html.ui.internal.contentoutline.JFaceNodeAdapterFactoryForHTML;
import org.eclipse.wst.sse.core.internal.PropagatingAdapter;
import org.eclipse.wst.sse.core.internal.ltk.modelhandler.IDocumentTypeHandler;
import org.eclipse.wst.sse.core.internal.model.FactoryRegistry;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.sse.ui.internal.provisional.registry.AdapterFactoryProvider;
import org.eclipse.wst.sse.ui.internal.provisional.registry.AdapterFactoryRegistry;
import org.eclipse.wst.sse.ui.internal.provisional.registry.embedded.EmbeddedAdapterFactoryProvider;
import org.eclipse.wst.sse.ui.internal.util.Assert;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.properties.XMLPropertySourceAdapterFactory;

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
		// -------
		// Must update/add to "editor side" embedded adapters here too
		addEmbeddedContentFactories(structuredModel);

	}

	protected void addContentBasedFactories(IStructuredModel structuredModel) {
		FactoryRegistry factoryRegistry = structuredModel.getFactoryRegistry();
		Assert.isNotNull(factoryRegistry, "Program Error: client caller must ensure model has factory registry"); //$NON-NLS-1$
		INodeAdapterFactory factory = null;
		factory = factoryRegistry.getFactoryFor(IPropertySource.class);
		if (factory == null) {
			factory = new XMLPropertySourceAdapterFactory();
			factoryRegistry.addFactory(factory);
		}
		factory = factoryRegistry.getFactoryFor(IJFaceNodeAdapter.class);
		if (factory == null) {
			factory = new JFaceNodeAdapterFactoryForHTML(IJFaceNodeAdapter.class, true);
			factoryRegistry.addFactory(factory);
		}

		factory = factoryRegistry.getFactoryFor(IJSPTranslation.class);
		if (factory == null) {
			factory = new JSPTranslationAdapterFactory();
			factoryRegistry.addFactory(factory);
		}
	}

	protected void addEmbeddedContentFactories(IStructuredModel structuredModel) {

		if (structuredModel instanceof IDOMModel) {
			IDOMModel xmlModel = (IDOMModel) structuredModel;
			IDOMDocument document = xmlModel.getDocument();
			PageDirectiveAdapter pageDirectiveAdapter = (PageDirectiveAdapter) document.getAdapterFor(PageDirectiveAdapter.class);
			if (pageDirectiveAdapter != null) {
				// made into registry mechanism
				AdapterFactoryRegistry adapterRegistry = JSPUIPlugin.getDefault().getEmbeddedAdapterFactoryRegistry();
				Iterator adapterList = adapterRegistry.getAdapterFactories();
				// And all those appropriate for this particular type of
				// content
				while (adapterList.hasNext()) {
					EmbeddedAdapterFactoryProvider provider = (EmbeddedAdapterFactoryProvider) adapterList.next();
					if (provider.isFor(pageDirectiveAdapter.getEmbeddedType())) {
						provider.addAdapterFactories(structuredModel);
					}
				}
			}
		}

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
		// assuming the original ones have been removed already
		// from the page directives registry. The original ones
		// are removed when the embeddedContentType is set.
		addEmbeddedContentFactories(structuredModel);
	}

}
