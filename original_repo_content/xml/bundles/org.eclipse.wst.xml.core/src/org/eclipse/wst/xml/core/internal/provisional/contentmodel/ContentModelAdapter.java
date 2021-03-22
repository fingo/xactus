/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
package org.eclipse.wst.xml.core.internal.provisional.contentmodel;



import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;

/**
 * ContentModelAdapter interface
 */
public interface ContentModelAdapter extends org.eclipse.wst.sse.core.internal.provisional.INodeAdapter {

	/**
	 * getCMDocument method
	 * 
	 * @return CMDocument
	 * @param notifer
	 * 
	 * Returns CMDocument associated to the adapting node. For exampl : HTML
	 * CMDocument is associated to HTML Document node, DTD CMDocument is
	 * associated to DocumentType node, DTD/Schema CMDocument is associated to
	 * Element node (sometime with namespace), and taglib CMDocument is
	 * associated to taglib directive Element node.
	 * 
	 * INodeNotifier is passed for stateless (singleton) INodeAdapter
	 * implementation.
	 */
	CMDocument getCMDocument(org.eclipse.wst.sse.core.internal.provisional.INodeNotifier notifier);

	/**
	 * getDeclaration method
	 * 
	 * @return CMNode
	 * @param notifer
	 * 
	 * Returns ElementDefinition or AttributeDefinition for the adapting node.
	 * 
	 * INodeNotifier is passed for stateless (singleton) INodeAdapter
	 * implementation.
	 */
	CMNode getDeclaration(org.eclipse.wst.sse.core.internal.provisional.INodeNotifier notifier);
}
