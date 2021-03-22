/*******************************************************************************
 * Copyright (c) 2009, 2012 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver - initial API
 *******************************************************************************/
package org.eclipse.wst.xml.xpath.ui.internal.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.xpath.ui.internal.XPathUIPlugin;
import org.eclipse.wst.xml.xpath.ui.internal.views.EditNamespacePrefixDialog;

/**
 * The prefix handler handles commands for editing the namespaces assoicated
 * with XPath quieries in the XPath view.   If an item is set to the default
 * namespace it still needs a prefix for XPath to work with it.
 * 
 * @author dcarver
 *
 */
public class PrefixHandler extends AbstractHandler implements IHandler {
	
	/**
	 * This will use the active editor, and try and retrieve a structured
	 * model from it.  If successful, it setups the namespace edit dialog
	 * to capture the namespace information.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XPathUIPlugin plugin = XPathUIPlugin.getDefault();
		
		IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		// suppress an NPE in the log (shouldn't happen with the enabledWhen rule)
		if (activeEditor == null) return null;
		IFile file = (IFile) activeEditor.getEditorInput().getAdapter(
				IFile.class);
		IModelManager modelManager = StructuredModelManager.getModelManager();
		IDOMModel model = null;
		try {
			model = (IDOMModel) modelManager.getModelForRead(file);
			IDOMDocument document = model.getDocument();

			if (document != null) {
				List<NamespaceInfo> info = plugin.getNamespaceInfo(document);

				IPathEditorInput editorInput = (IPathEditorInput) activeEditor
						.getEditorInput();

				EditNamespacePrefixDialog dlg = new EditNamespacePrefixDialog(
						activeEditor.getSite().getShell(), editorInput
								.getPath());
				dlg.setNamespaceInfoList(info);
				if (SWT.OK == dlg.open()) {
					// Apply changes
				}
			}
		} catch (Exception ex) {

		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
		return null;
	}	
}
