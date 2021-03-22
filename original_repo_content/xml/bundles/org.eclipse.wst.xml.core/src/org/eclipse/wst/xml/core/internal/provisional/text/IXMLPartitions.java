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
 *******************************************************************************/
package org.eclipse.wst.xml.core.internal.provisional.text;



/**
 * This interface is not intended to be implemented.
 * It defines the partition types for XML.
 * Clients should reference the partition type Strings defined here directly.
 * 
 * @deprecated use org.eclipse.wst.xml.core.text
 */
public interface IXMLPartitions {
	
	String XML_DEFAULT = "org.eclipse.wst.xml.XML_DEFAULT"; //$NON-NLS-1$
	String XML_CDATA = "org.eclipse.wst.xml.XML_CDATA"; //$NON-NLS-1$
	String XML_PI = "org.eclipse.wst.xml.XML_PI"; //$NON-NLS-1$
	String XML_DECLARATION = "org.eclipse.wst.xml.XML_DECL"; //$NON-NLS-1$
	String XML_COMMENT = "org.eclipse.wst.xml.XML_COMMENT"; //$NON-NLS-1$
	
	/*
	 * This value is used as a prefix to any unknown processing instructions
	 * we find. The processor target name is converted to uppercase and
	 * appended to the prefix to create a unique partition type.
	 */
	String PROCESSING_INSTRUCTION_PREFIX = "org.eclipse.wst.xml.PROCESSING_INSTRUCTION:"; //$NON-NLS-1$

	/**
	 * Should match
	 * org.eclipse.wst.sse.core.dtd.partitioning.StructuredTextPartitionerForDTD.ST_DTD_SUBSET
	 */
	String DTD_SUBSET = "org.eclipse.wst.xml.dtd.internal_subset"; //$NON-NLS-1$
}
