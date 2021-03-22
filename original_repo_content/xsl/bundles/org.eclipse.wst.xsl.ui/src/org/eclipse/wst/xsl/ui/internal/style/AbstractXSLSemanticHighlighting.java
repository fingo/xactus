/*******************************************************************************
 * Copyright (c) 2010 Intalio, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver (Intalio) - bug 256339 - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.ui.internal.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Position;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.ui.ISemanticHighlighting;
import org.eclipse.wst.sse.ui.ISemanticHighlightingExtension;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xsl.core.XSLCore;
import org.eclipse.wst.xsl.ui.internal.XSLUIPlugin;

public abstract class AbstractXSLSemanticHighlighting implements
		ISemanticHighlightingExtension, ISemanticHighlighting {

	public abstract String getStylePreferenceKey();

	public String getBoldPreferenceKey() {
		return null;
	}

	public String getUnderlinePreferenceKey() {
		return null;
	}

	public String getStrikethroughPreferenceKey() {
		return null;
	}

	public String getItalicPreferenceKey() {
		return null;
	}

	public String getColorPreferenceKey() {
		return null;
	}

	public IPreferenceStore getPreferenceStore() {
		return XSLUIPlugin.getDefault().getPreferenceStore();
	}

	public String getEnabledPreferenceKey() {
		return null;
	}

	public String getDisplayName() {
		return null;
	}

	public abstract Position[] consumes(IStructuredDocumentRegion region);

	protected List createSemanticPositions(IStructuredDocumentRegion region, String regionType) {
		if (region == null) {
			return Collections.EMPTY_LIST;
		}
		
		if (!region.getType().equals(DOMRegionContext.XML_TAG_NAME)) {
			return Collections.EMPTY_LIST;
		}
	
		ITextRegionList regionList = region.getRegions();
		
		ArrayList arrpos = new ArrayList();
		for (int i = 0; i < regionList.size(); i++) {
			ITextRegion textRegion = regionList.get(i);
			if (textRegion.getType().equals(regionType)) {
				Position pos = new Position(region
						.getStartOffset(textRegion), textRegion.getLength());
				arrpos.add(pos);
			}
		}
		return arrpos;
	}

	public Position[] consumes(IStructuredDocumentRegion documentRegion, IndexedRegion indexedRegion) {
		if (indexedRegion != null && indexedRegion instanceof IDOMNode) {
			IDOMNode node = (IDOMNode)indexedRegion;
			if (XSLCore.isXSLNamespace(node)) {
				return consumes(documentRegion);
			}
		}
		return null;
	}
	
}
