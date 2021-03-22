/*******************************************************************************
 * Copyright (c) 2007, 2008 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.jaxp.launching;

/**
 * An XSLT debugger. Typically this will have been 
 * contributed via the <code>org.eclipse.wst.xsl.launching.debugger</code> extension point.
 * 
 * @author Doug Satchwell
 */
public interface IDebugger
{
	/**
	 * Get the unique id for this debugger.
	 * @return debugger id
	 */
	String getId();

	/**
	 * Get the set of bundle-relative jar files to add to the classpath. 
	 * @return array of bundle-relative jars
	 */
	String[] getClassPath();

	/**
	 * Get a unique name for this debugger
	 * @return the name for the debugger
	 */
	String getName();

	/**
	 * Get the processor type that this debugger is associated with
	 * @return the processor type
	 */
	IProcessorType getProcessorType();

	/**
	 * Get the class name for this debugger
	 * @return the class name
	 */
	String getClassName();
	
	/**
	 * Get the transformer factory this debugger is associated with
	 * @return the transformer factory
	 */
	String getTransformerFactory();
}
