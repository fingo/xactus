/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.wst.html.ui.internal.text;

// taken from package org.eclipse.jdt.ui.text;

/**
 * Definition of Java partitioning and its partitions.
 * 
 * @see Eclipse 3.1
 */
interface IJavaPartitions {

	/**
	 * The identifier of the Java partitioning.
	 */
	String JAVA_PARTITIONING = "___java_partitioning"; //$NON-NLS-1$

	/**
	 * The identifier of the single-line (JLS2: EndOfLineComment) end comment
	 * partition content type.
	 */
	String JAVA_SINGLE_LINE_COMMENT = "__java_singleline_comment"; //$NON-NLS-1$

	/**
	 * The identifier multi-line (JLS2: TraditionalComment) comment partition
	 * content type.
	 */
	String JAVA_MULTI_LINE_COMMENT = "__java_multiline_comment"; //$NON-NLS-1$

	/**
	 * The identifier of the Javadoc (JLS2: DocumentationComment) partition
	 * content type.
	 */
	String JAVA_DOC = "__java_javadoc"; //$NON-NLS-1$

	/**
	 * The identifier of the Java string partition content type.
	 */
	String JAVA_STRING = "__java_string"; //$NON-NLS-1$

	/**
	 * The identifier of the Java character partition content type.
	 */
	String JAVA_CHARACTER = "__java_character"; //$NON-NLS-1$
}
