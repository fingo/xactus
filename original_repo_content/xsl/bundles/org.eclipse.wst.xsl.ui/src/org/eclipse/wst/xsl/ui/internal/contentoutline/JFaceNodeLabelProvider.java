/*******************************************************************************
 * Copyright (c) 2009, 2010 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     David Carver (STAR) - based on work for org.eclipse.wst.xml.ui
 *     
 *******************************************************************************/
package org.eclipse.wst.xsl.ui.internal.contentoutline;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;

/**
 * A (column) label provider backed by JFaceNodeAdapters.
 */
public class JFaceNodeLabelProvider extends ColumnLabelProvider {
	/**
	 * JFaceNodeLabelProvider constructor comment.
	 */
	public JFaceNodeLabelProvider() {
		super();
	}

	/**
	 * Returns the JFace adapter for the specified object.
	 * 
	 * @param adaptable
	 *            java.lang.Object The object to get the adapter for
	 */
	protected IJFaceNodeAdapter getAdapter(Object adaptable) {
		if (adaptable instanceof INodeNotifier) {
			INodeAdapter adapter = ((INodeNotifier) adaptable).getAdapterFor(IJFaceNodeAdapter.class);
			if (adapter instanceof IJFaceNodeAdapter) {
				return (IJFaceNodeAdapter) adapter;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		IJFaceNodeAdapter adapter = getAdapter(element);
		if (adapter != null)
			return adapter.getLabelImage(element);
		return super.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		IJFaceNodeAdapter adapter = getAdapter(element);
		if (adapter != null)
			return adapter.getLabelText(element);
		return super.getText(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
}
