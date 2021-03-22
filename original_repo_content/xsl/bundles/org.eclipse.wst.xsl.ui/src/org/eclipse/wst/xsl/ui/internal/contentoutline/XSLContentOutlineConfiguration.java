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
 *     IBM Corporation - based on DecoratedXMLContentOutlineConfiguration initial API and implementation
 *     David Carver (STAR) - initial XSL implementation.
 *     
 *******************************************************************************/
package org.eclipse.wst.xsl.ui.internal.contentoutline;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.wst.sse.ui.internal.contentoutline.PropertyChangeUpdateAction;
import org.eclipse.wst.sse.ui.internal.contentoutline.PropertyChangeUpdateActionContributionItem;
import org.eclipse.wst.sse.ui.internal.editor.EditorPluginImageHelper;
import org.eclipse.wst.sse.ui.internal.editor.EditorPluginImages;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;
import org.eclipse.wst.xsl.ui.internal.contentoutline.JFaceNodeContentProvider;
import org.eclipse.wst.xml.ui.internal.preferences.XMLUIPreferenceNames;
import org.eclipse.wst.xml.ui.views.contentoutline.AbstractXMLContentOutlineConfiguration;
import org.eclipse.wst.xml.ui.views.contentoutline.DecoratedXMLContentOutlineConfiguration;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * More advanced Outline Configuration for XML support.  Expects that the viewer's
 * input will be the DOM Model.
 * 
 * @see AbstractXMLContentOutlineConfiguration
 * @since 1.0
 */
public class XSLContentOutlineConfiguration extends DecoratedXMLContentOutlineConfiguration {
	static final String ATTR_NAME = "name"; //$NON-NLS-1$
	static final String ATTR_ID = "id"; //$NON-NLS-1$


	/**
	 * Toggle action for whether or not to display element's first attribute
	 */
	private class ToggleShowAttributeAction extends PropertyChangeUpdateAction {
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=88444
		private TreeViewer fTreeViewer;

		public ToggleShowAttributeAction(IPreferenceStore store, String preference, TreeViewer treeViewer) {
			super(XMLUIMessages.XMLContentOutlineConfiguration_0, store, preference, true);
			setToolTipText(getText());

			setImageDescriptor(EditorPluginImageHelper.getInstance().getImageDescriptor(EditorPluginImages.IMG_OBJ_PROP_PS));
			fTreeViewer = treeViewer;
			update();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.texteditor.IUpdate#update()
		 */
		@Override
		public void update() {
			super.update();
			fShowAttributes = isChecked();

			// notify the configuration of the change
			enableShowAttributes(fShowAttributes, fTreeViewer);

			// refresh the outline view
			fTreeViewer.refresh(true);
		}
	}
	
	private AttributeShowingLabelProvider fAttributeShowingLabelProvider;
	private IContentProvider fContentProvider = null;

	boolean fShowAttributes = false;

	/*
	 * Preference key for Show Attributes
	 */
	private static final String OUTLINE_SHOW_ATTRIBUTE_PREF = "outline-show-attribute"; //$NON-NLS-1$

	/**
	 * Create new instance of DecoratedXMLContentOutlineConfiguration
	 */
	public XSLContentOutlineConfiguration() {
		// Must have empty constructor to createExecutableExtension
		super();

		/**
		 * Set up our preference store here. This is done so that subclasses
		 * aren't required to set their own values, although if they have,
		 * those will be used instead.
		 */
		IPreferenceStore store = getPreferenceStore();
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.DOCUMENT_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.DOCUMENT_NODE, "1, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.PROCESSING_INSTRUCTION_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.PROCESSING_INSTRUCTION_NODE, "2, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.DOCUMENT_TYPE_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.DOCUMENT_TYPE_NODE, "3, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.DOCUMENT_FRAGMENT_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.DOCUMENT_FRAGMENT_NODE, "4, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.COMMENT_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.COMMENT_NODE, "5, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ATTRIBUTE_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ATTRIBUTE_NODE, "6, false"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ELEMENT_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ELEMENT_NODE, "7, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ENTITY_REFERENCE_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ENTITY_REFERENCE_NODE, "8, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.CDATA_SECTION_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.CDATA_SECTION_NODE, "9, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ENTITY_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.ENTITY_NODE, "10, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.NOTATION_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.NOTATION_NODE, "11, true"); //$NON-NLS-1$
		if (store.getDefaultString(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.TEXT_NODE).length() == 0)
			store.setDefault(XMLUIPreferenceNames.OUTLINE_BEHAVIOR.TEXT_NODE, "12, false"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration#createMenuContributions(org.eclipse.jface.viewers.TreeViewer)
	 */
	@Override
	protected IContributionItem[] createMenuContributions(TreeViewer viewer) {
		IContributionItem[] items;
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=88444
		IContributionItem showAttributeItem = new PropertyChangeUpdateActionContributionItem(new ToggleShowAttributeAction(getPreferenceStore(), OUTLINE_SHOW_ATTRIBUTE_PREF, viewer));

		items = super.createMenuContributions(viewer);
		if (items == null) {
			items = new IContributionItem[]{showAttributeItem};
		}
		else {
			IContributionItem[] combinedItems = new IContributionItem[items.length + 1];
			System.arraycopy(items, 0, combinedItems, 0, items.length);
			combinedItems[items.length] = showAttributeItem;
			items = combinedItems;
		}
		return items;
	}

	/**
	 * Notifies this configuration that the flag that indicates whether or not
	 * to show attribute values in the tree viewer has changed. The tree
	 * viewer is automatically refreshed afterwards to update the labels.
	 * 
	 * Clients should not call this method, but rather should react to it.
	 * 
	 * @param showAttributes
	 *            flag indicating whether or not to show attribute values in
	 *            the tree viewer
	 * @param treeViewer
	 *            the TreeViewer associated with this configuration
	 */
	@Override
	protected void enableShowAttributes(boolean showAttributes, TreeViewer treeViewer) {
		if (fAttributeShowingLabelProvider != null) {
			fAttributeShowingLabelProvider.setShowAttributes(showAttributes);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration#getContentProvider(org.eclipse.jface.viewers.TreeViewer)
	 */
	@Override
	public IContentProvider getContentProvider(TreeViewer viewer) {
		if (fContentProvider == null) {
			fContentProvider = new JFaceNodeContentProvider();
		}
		return fContentProvider;
	}

	private Object getFilteredNode(Object object) {
		if (object instanceof Node) {
			Node node = (Node) object;
			short nodeType = node.getNodeType();
			// replace attribute node in selection with its parent
			if (nodeType == Node.ATTRIBUTE_NODE) {
				node = ((Attr) node).getOwnerElement();
			}
			// anything else not visible, replace with parent node
			else if (nodeType == Node.TEXT_NODE || nodeType == Node.COMMENT_NODE) {
				node = node.getParentNode();
			}
			return node;
		}
		return object;
	}

	private Object[] getFilteredNodes(Object[] filteredNodes) {
		for (int i = 0; i < filteredNodes.length; i++) {
			filteredNodes[i] = getFilteredNode(filteredNodes[i]);
		}
		return filteredNodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration#getLabelProvider(org.eclipse.jface.viewers.TreeViewer)
	 */
	@Override
	public ILabelProvider getLabelProvider(TreeViewer viewer) {
		if (fAttributeShowingLabelProvider == null) {
			fAttributeShowingLabelProvider = new AttributeShowingLabelProvider(fShowAttributes);
		}
		return fAttributeShowingLabelProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration#getSelection(org.eclipse.jface.viewers.TreeViewer,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public ISelection getSelection(TreeViewer viewer, ISelection selection) {
		ISelection filteredSelection = selection;
		if (selection instanceof IStructuredSelection) {
			Object[] filteredNodes = getFilteredNodes(((IStructuredSelection) selection).toArray());
			filteredSelection = new StructuredSelection(filteredNodes);
		}
		return filteredSelection;
	}
}
