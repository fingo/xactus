/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/

package org.eclipse.wst.html.ui.internal.search;

import org.eclipse.wst.html.core.text.IHTMLPartitions;
import org.eclipse.wst.sse.ui.internal.search.FindOccurrencesProcessor;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.core.text.IXMLPartitions;

/**
 * Configures a FindOccurrencesProcessor with HTML partitions and regions
 */
public class HTMLFindOccurrencesProcessor extends FindOccurrencesProcessor {

	protected String[] getPartitionTypes() {
		return new String[]{IHTMLPartitions.HTML_DEFAULT, IXMLPartitions.XML_DEFAULT};
	}

	protected String[] getRegionTypes() {
		return new String[]{DOMRegionContext.XML_TAG_NAME, DOMRegionContext.XML_TAG_ATTRIBUTE_NAME, DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE};
	}
}
