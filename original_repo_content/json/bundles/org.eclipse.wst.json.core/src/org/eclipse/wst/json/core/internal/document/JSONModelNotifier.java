/*******************************************************************************
 * Copyright (c) 2001, 2015 IBM Corporation and others.
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
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.xml.core.internal.document.XMLModelNotifier
 *                                           modified in order to process JSON Objects.
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;
import org.eclipse.wst.json.core.document.IJSONPair;

/**
 * 
 * JSONModelNotifier manages the notification process. Clients should not use
 * extend or reference.
 * 
 * ISSUE: should be internalized.
 */

public interface JSONModelNotifier {

	/**
	 * attrReplaced method
	 * 
	 * @param element
	 *            org.w3c.dom.Element
	 * @param newAttr
	 *            org.w3c.dom.Attr
	 * @param oldAttr
	 *            org.w3c.dom.Attr
	 */
	void pairReplaced(IJSONObject element, IJSONPair newAttr, IJSONPair oldAttr);

	/**
	 * Signal that changing is starting.
	 * 
	 */
	void beginChanging();

	/**
	 * Signal that changing is starting with a brand new model.
	 * 
	 */
	void beginChanging(boolean newModel);

	/**
	 * Cancel pending notifications. This is called in the context of
	 * "reinitialization" so is assumed ALL notifications can be safely
	 * canceled, assuming that once factories and adapters are re-initialized
	 * they will be re-notified as text is set in model, if still appropriate.
	 */
	void cancelPending();

	void childReplaced(IJSONNode parentNode, IJSONNode newChild,
			IJSONNode oldChild);

	/**
	 * Editable state changed for node.
	 * 
	 */
	// void editableChanged(Node node);

	/**
	 * Signal changing is finished.
	 * 
	 */
	void endChanging();

	/**
	 * Signal end tag changed.
	 * 
	 * @param element
	 * 
	 */
	void endTagChanged(IJSONObject element);

	/**
	 * Used to reflect state of model.
	 * 
	 * @return true if model had changed.
	 * 
	 */
	boolean hasChanged();

	/**
	 * Used to reflect state of parsing process.
	 * 
	 * @return true if model is currently changing.
	 */
	boolean isChanging();

	/**
	 * signal property changed
	 * 
	 * @param node
	 */
	void propertyChanged(IJSONNode node);

	/**
	 * signal start tag changed
	 * 
	 * @param element
	 */
	void startTagChanged(IJSONObject element);

	/**
	 * signal structured changed.
	 * 
	 * @param node
	 */
	void structureChanged(IJSONNode node);

	/**
	 * valueChanged method
	 * 
	 * @param node
	 *            org.w3c.dom.Node
	 */
	void valueChanged(IJSONNode node);

}
