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
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.xml.core.internal.document.StructuredDocumentRegionUtil
 *                                           modified in order to process JSON Objects.          
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.document;



import org.eclipse.wst.json.core.regions.JSONRegionContexts;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;



/**
 * Provides convenient functions to handle IStructuredDocumentRegion and
 * ITextRegion.
 */
class StructuredDocumentRegionUtil {

	/**
	 * Extracts contents enclosed with quotes. Quotes may be double or single.
	 */
	static String getAttrValue(IStructuredDocumentRegion flatNode, ITextRegion region) {
		if (region == null)
			return null;
		if (flatNode == null)
			return null;
		String value = flatNode.getText(region);
		if (value == null)
			return null;
		int length = value.length();
		if (length == 0)
			return value;
		char firstChar = value.charAt(0);
		if (firstChar == '"' || firstChar == '\'') {
			if (length == 1)
				return ""; //$NON-NLS-1$
			if (value.charAt(length - 1) == firstChar)
				length--;
			return value.substring(1, length);
		}
		return value;
	}

	/**
	 * Extracts the name without heading '&' and tailing ';'.
	 */
	static String getEntityRefName(IStructuredDocumentRegion flatNode, ITextRegion region) {
		if (region == null)
			return null;
		String ref = flatNode.getText(region);
		int length = ref.length();
		if (length == 0)
			return ref;
		int offset = 0;
		if (ref.charAt(0) == '&')
			offset = 1;
		if (ref.charAt(length - 1) == ';')
			length--;
		if (offset >= length)
			return null;
		return ref.substring(offset, length);
	}

	/**
	 * Returns the first region.
	 */
	static ITextRegion getFirstRegion(IStructuredDocumentRegion flatNode) {
		if(flatNode instanceof StructuredDocumentRegionProxy) {
			flatNode = ((StructuredDocumentRegionProxy)flatNode).getStructuredDocumentRegion();
		}
		if (flatNode == null)
			return null;
		ITextRegionList regions = flatNode.getRegions();
		if (regions == null || regions.size() == 0)
			return null;
		return regions.get(0);
	}

	/**
	 * Returns the type of the first region.
	 */
	static String getFirstRegionType(IStructuredDocumentRegion flatNode) {
		if(flatNode instanceof StructuredDocumentRegionProxy) {
			flatNode = ((StructuredDocumentRegionProxy)flatNode).getStructuredDocumentRegion();
		}
		if (flatNode == null)
			return JSONRegionContexts.UNDEFINED;
		ITextRegionList regions = flatNode.getRegions();
		if (regions == null || regions.size() == 0)
			return JSONRegionContexts.UNDEFINED;
		ITextRegion region = regions.get(0);
		return region.getType();
	}

	/**
	 */
	static IStructuredDocumentRegion getFirstStructuredDocumentRegion(IStructuredDocumentRegion flatNode) {
		if (flatNode == null)
			return null;
		if (flatNode instanceof StructuredDocumentRegionContainer) {
			flatNode = ((StructuredDocumentRegionContainer) flatNode).getFirstStructuredDocumentRegion();
		}
		if (flatNode instanceof StructuredDocumentRegionProxy) {
			flatNode = ((StructuredDocumentRegionProxy) flatNode).getStructuredDocumentRegion();
		}
		return flatNode;
	}

	/**
	 * Returns the last region.
	 */
	static ITextRegion getLastRegion(IStructuredDocumentRegion flatNode) {
		if(flatNode instanceof StructuredDocumentRegionProxy) {
			flatNode = ((StructuredDocumentRegionProxy)flatNode).getStructuredDocumentRegion();
		}
		if (flatNode == null)
			return null;
		ITextRegionList regions = flatNode.getRegions();
		if (regions == null || regions.size() == 0)
			return null;
		return regions.get(regions.size() - 1);
	}

	/**
	 * Returns the type of the first region.
	 */
	static String getLastRegionType(IStructuredDocumentRegion flatNode) {
		if(flatNode instanceof StructuredDocumentRegionProxy) {
			flatNode = ((StructuredDocumentRegionProxy)flatNode).getStructuredDocumentRegion();
		}
		if (flatNode == null)
			return JSONRegionContexts.UNDEFINED;
		ITextRegionList regions = flatNode.getRegions();
		if (regions == null || regions.size() == 0)
			return JSONRegionContexts.UNDEFINED;
		ITextRegion region = regions.get(regions.size() - 1);
		return region.getType();
	}

	/**
	 */
	static IStructuredDocumentRegion getLastStructuredDocumentRegion(IStructuredDocumentRegion flatNode) {
		if (flatNode == null)
			return null;
		if (flatNode instanceof StructuredDocumentRegionContainer) {
			flatNode = ((StructuredDocumentRegionContainer) flatNode).getLastStructuredDocumentRegion();
		}
		if (flatNode instanceof StructuredDocumentRegionProxy) {
			flatNode = ((StructuredDocumentRegionProxy) flatNode).getStructuredDocumentRegion();
		}
		return flatNode;
	}

	/**
	 */
	static IStructuredDocumentRegion getStructuredDocumentRegion(IStructuredDocumentRegion flatNode) {
		if (flatNode == null)
			return null;
		if (flatNode instanceof StructuredDocumentRegionProxy) {
			flatNode = ((StructuredDocumentRegionProxy) flatNode).getStructuredDocumentRegion();
		}
		return flatNode;
	}

	StructuredDocumentRegionUtil() {
		super();
	}
}
