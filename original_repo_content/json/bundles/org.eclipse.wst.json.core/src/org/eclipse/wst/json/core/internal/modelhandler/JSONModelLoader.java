/**
 *  Copyright (c) 2013-2015 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.core.internal.modelhandler;

import org.eclipse.wst.json.core.internal.document.JSONModelImpl;
import org.eclipse.wst.json.core.internal.encoding.JSONDocumentLoader;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.model.AbstractModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

/**
 * JSON Model loader.
 */
public class JSONModelLoader extends AbstractModelLoader {

	public JSONModelLoader() {
		super();
	}

	@Override
	public IStructuredModel newModel() {
		return new JSONModelImpl();
	}

	@Override
	public IModelLoader newInstance() {
		return new JSONModelLoader();
	}

	@Override
	public IDocumentLoader getDocumentLoader() {
		if (documentLoaderInstance == null) {
			documentLoaderInstance = new JSONDocumentLoader();
		}
		return documentLoaderInstance;
	}
}
