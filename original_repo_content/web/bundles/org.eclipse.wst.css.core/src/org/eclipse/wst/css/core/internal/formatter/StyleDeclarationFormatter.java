/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
package org.eclipse.wst.css.core.internal.formatter;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.text.IRegion;
import org.eclipse.wst.css.core.internal.CSSCorePlugin;
import org.eclipse.wst.css.core.internal.cleanup.CSSCleanupStrategy;
import org.eclipse.wst.css.core.internal.parserz.CSSRegionContexts;
import org.eclipse.wst.css.core.internal.preferences.CSSCorePreferenceNames;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

/**
 * 
 */
public class StyleDeclarationFormatter extends DefaultCSSSourceFormatter {

	private static StyleDeclarationFormatter instance;

	/**
	 * 
	 */
	StyleDeclarationFormatter() {
		super();
	}

	/**
	 * 
	 */
	protected void formatBefore(ICSSNode node, ICSSNode child, String toAppend, StringBuffer source, IRegion exceptFor) {
		CSSCleanupStrategy stgy = getCleanupStrategy(node);

		ICSSNode prev = (child != null) ? child.getPreviousSibling() : node.getLastChild();
		int start = (prev != null) ? ((IndexedRegion) prev).getEndOffset() : 0;
		int end = (child != null) ? ((IndexedRegion) child).getStartOffset() : 0;

		// check no child
		if (child == null && prev == null)
			return;

		if (start > 0 && start < end) { // format source
			ICSSModel cssModel = node.getOwnerDocument().getModel();
			// BUG202615 - it is possible to have a style declaration with no
			// model associated with it
			if (cssModel != null) {
				IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
				if (structuredDocument != null) {
					// get meaning regions
					CompoundRegion[] regions = null;
					if (exceptFor == null)
						regions = getRegionsWithoutWhiteSpaces(structuredDocument, new FormatRegion(start, end - start), stgy);
					else {
						String pickupType = CSSRegionContexts.CSS_DECLARATION_DELIMITER;
						if (prev == null || child == null)
							pickupType = null;
						regions = getRegions(structuredDocument, new FormatRegion(start, end - start), exceptFor, pickupType);
					}
					// extract source
					for (int i = 0; i < regions.length; i++) {
						appendSpaceBefore(node, regions[i], source);
						source.append(decoratedRegion(regions[i], 0, stgy)); // must
						// be comments
					}
				}
			}
		} else if (prev != null && child != null) { // generate source :
			// between two declarations
			// BUG93037-properties view adds extra ; when add new property
			boolean semicolonFound = false;

			ICSSModel cssModel = node.getOwnerDocument().getModel();
			// BUG202615 - it is possible to have a style declaration with no
			// model associated with it
			if (cssModel != null) {
				IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
				if (structuredDocument != null) {
					int prevStart = (prev != null) ? ((IndexedRegion) prev).getStartOffset() : 0;
					int prevEnd = (prev != null) ? ((IndexedRegion) prev).getEndOffset() : 0;
					CompoundRegion[] regions = getRegionsWithoutWhiteSpaces(structuredDocument, new FormatRegion(prevStart, prevEnd - prevStart), stgy);
					int i = regions.length - 1;
					while (i >= 0 && !semicolonFound) {
						if (regions[i].getType() == CSSRegionContexts.CSS_DECLARATION_DELIMITER)
							semicolonFound = true;
						--i;
					}
				}
			}
			if (!semicolonFound)
				source.append(";");//$NON-NLS-1$
		} else if (prev == null) { // generate source : before the first
			// declaration
			org.eclipse.wst.css.core.internal.util.RegionIterator it = null;
			if (end > 0) {
				ICSSModel cssModel = node.getOwnerDocument().getModel();
				// BUG202615 - it is possible to have a style declaration with
				// no model associated with it
				if (cssModel != null) {
					IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
					if (structuredDocument != null) {
						it = new org.eclipse.wst.css.core.internal.util.RegionIterator(structuredDocument, end - 1);
					}
				}
			} else {
				int pos = getChildInsertPos(node);
				if (pos >= 0) {
					ICSSModel cssModel = node.getOwnerDocument().getModel();
					// BUG202615 - it is possible to have a style declaration
					// with no model associated with it
					if (cssModel != null) {
						IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
						if (structuredDocument != null) {
							it = new org.eclipse.wst.css.core.internal.util.RegionIterator(structuredDocument, pos - 1);
						}
					}
				}
			}
			if (it != null) {
				int limit = ((IndexedRegion) ((node.getParentNode() != null) ? node.getParentNode() : node)).getStartOffset();
				while (it.hasPrev()) {
					ITextRegion curReg = it.prev();
					if (curReg.getType() == CSSRegionContexts.CSS_LBRACE || curReg.getType() == CSSRegionContexts.CSS_DECLARATION_DELIMITER)
						break;
					if (curReg.getType() != CSSRegionContexts.CSS_S && curReg.getType() != CSSRegionContexts.CSS_COMMENT) {
						source.append(";");//$NON-NLS-1$
						break;
					}
					if (it.getStructuredDocumentRegion().getStartOffset(curReg) <= limit)
						break;
				}
			}
		} else if (child == null) { // generate source : after the last
			// declaration
			org.eclipse.wst.css.core.internal.util.RegionIterator it = null;
			if (start > 0) {
				ICSSModel cssModel = node.getOwnerDocument().getModel();
				// BUG202615 - it is possible to have a style declaration with
				// no model associated with it
				if (cssModel != null) {
					IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
					if (structuredDocument != null) {
						it = new org.eclipse.wst.css.core.internal.util.RegionIterator(structuredDocument, start);
					}
				}
			} else {
				int pos = getChildInsertPos(node);
				if (pos >= 0) {
					ICSSModel cssModel = node.getOwnerDocument().getModel();
					// BUG202615 - it is possible to have a style declaration
					// with no model associated with it
					if (cssModel != null) {
						IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
						if (structuredDocument != null) {
							it = new org.eclipse.wst.css.core.internal.util.RegionIterator(structuredDocument, pos);
						}
					}
				}
			}
			if (it != null) {
				int limit = ((IndexedRegion) ((node.getParentNode() != null) ? node.getParentNode() : node)).getEndOffset();
				while (it.hasNext()) {
					ITextRegion curReg = it.next();
					if (curReg.getType() == CSSRegionContexts.CSS_RBRACE || curReg.getType() == CSSRegionContexts.CSS_DECLARATION_DELIMITER)
						break;
					if (curReg.getType() != CSSRegionContexts.CSS_S && curReg.getType() != CSSRegionContexts.CSS_COMMENT) {
						// Bug 219004 - Before appending a ;, make sure that there
						// isn't one already
						boolean semicolonFound = false;
						while(it.hasNext() && !semicolonFound) {
							if(it.next().getType() == CSSRegionContexts.CSS_DECLARATION_DELIMITER)
								semicolonFound = true;
						}
						
						if(!semicolonFound)
							source.append(";");//$NON-NLS-1$
						break;
					}
					if (limit <= it.getStructuredDocumentRegion().getEndOffset(curReg))
						break;
				}
			}
		}
		if (child == null) {
			if (((IndexedRegion) node).getEndOffset() <= 0) {
				// get next region
				int pos = getChildInsertPos(node);
				CompoundRegion toAppendRegion = null;
				if (pos >= 0) {
					ICSSModel cssModel = node.getOwnerDocument().getModel();
					// BUG202615 - it is possible to have a style declaration
					// with no model associated with it
					if (cssModel != null) {
						IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
						if (structuredDocument != null) {
							IStructuredDocumentRegion flatNode = structuredDocument.getRegionAtCharacterOffset(pos);
							toAppendRegion = new CompoundRegion(flatNode, flatNode.getRegionAtCharacterOffset(pos));
						}
					}
				}
				appendDelimBefore(node.getParentNode(), toAppendRegion, source);
			}
		} else if ((prev != null || ((IndexedRegion) node).getEndOffset() <= 0)) {
			Preferences preferences = CSSCorePlugin.getDefault().getPluginPreferences();

			if (preferences.getBoolean(CSSCorePreferenceNames.WRAPPING_ONE_PER_LINE) && (node.getOwnerDocument() != node || !preferences.getBoolean(CSSCorePreferenceNames.WRAPPING_PROHIBIT_WRAP_ON_ATTR)))
				appendDelimBefore(node, null, source);
			else if (prev != null || node.getOwnerDocument() != node)
				appendSpaceBefore(node, toAppend, source);
		}
	}

	/**
	 * 
	 */
	protected void formatBefore(ICSSNode node, ICSSNode child, IRegion region, String toAppend, StringBuffer source) {
		CSSCleanupStrategy stgy = getCleanupStrategy(node);

		ICSSModel cssModel = node.getOwnerDocument().getModel();
		// BUG202615 - it is possible to have a style declaration
		// with no model associated with it
		if (cssModel != null) {
			IStructuredDocument structuredDocument = cssModel.getStructuredDocument();
			if (structuredDocument != null) {
				CompoundRegion[] regions = getRegionsWithoutWhiteSpaces(structuredDocument, region, stgy);
				CompoundRegion[] outside = getOutsideRegions(structuredDocument, region);

				for (int i = 0; i < regions.length; i++) {
					if (i != 0 || needS(outside[0]))
						appendSpaceBefore(node, regions[i], source);
					source.append(decoratedRegion(regions[i], 0, stgy)); // must
																			// be
																			// comments
				}
				Preferences preferences = CSSCorePlugin.getDefault().getPluginPreferences();
				if (needS(outside[1])) {
					if (((IndexedRegion) child).getStartOffset() == region.getOffset() + region.getLength() && preferences.getBoolean(CSSCorePreferenceNames.WRAPPING_ONE_PER_LINE) && (node.getOwnerDocument() != node || !preferences.getBoolean(CSSCorePreferenceNames.WRAPPING_PROHIBIT_WRAP_ON_ATTR))) {
						appendDelimBefore(node, null, source);
					} else
						appendSpaceBefore(node, toAppend, source);
				}
			}
		}
	}

	/**
	 * Insert the method's description here.
	 */
	public int getChildInsertPos(ICSSNode node) {
		if (node == null)
			return -1;
		int pos = super.getChildInsertPos(node);
		if (pos < 0) {
			CSSSourceGenerator formatter = getParentFormatter(node);
			return (formatter != null) ? formatter.getChildInsertPos(node.getParentNode()) : -1;
		}
		return pos;
	}

	/**
	 * 
	 */
	public synchronized static StyleDeclarationFormatter getInstance() {
		if (instance == null)
			instance = new StyleDeclarationFormatter();
		return instance;
	}

	/**
	 * 
	 * @return int
	 * @param node
	 *            org.eclipse.wst.css.core.model.interfaces.ICSSNode
	 * @param insertPos
	 *            int
	 */
	public int getLengthToReformatAfter(ICSSNode node, int insertPos) {
		if (node == null)
			return 0;
		IndexedRegion nnode = (IndexedRegion) node;
		if (insertPos < 0 || !nnode.contains(insertPos)) {
			if (node.getParentNode() != null && nnode.getEndOffset() <= 0) {
				CSSSourceGenerator pntFormatter = getParentFormatter(node);
				if (pntFormatter != null)
					return pntFormatter.getLengthToReformatAfter(node.getParentNode(), insertPos);
			}
			return 0;
		}
		return super.getLengthToReformatAfter(node, insertPos);
	}

	/**
	 * 
	 * @return int
	 * @param node
	 *            org.eclipse.wst.css.core.model.interfaces.ICSSNode
	 * @param insertPos
	 *            int
	 */
	public int getLengthToReformatBefore(ICSSNode node, int insertPos) {
		if (node == null)
			return 0;
		IndexedRegion nnode = (IndexedRegion) node;
		if (insertPos <= 0 || !nnode.contains(insertPos - 1)) {
			if (node.getParentNode() != null && nnode.getEndOffset() <= 0) {
				CSSSourceGenerator pntFormatter = getParentFormatter(node);
				if (pntFormatter != null)
					return pntFormatter.getLengthToReformatBefore(node.getParentNode(), insertPos);
			}
			return 0;
		}
		return super.getLengthToReformatBefore(node, insertPos);
	}
}