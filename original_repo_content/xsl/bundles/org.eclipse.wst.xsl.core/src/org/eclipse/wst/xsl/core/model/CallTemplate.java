/*******************************************************************************
 * Copyright (c) 2007, 2009 Chase Technology Ltd - http://www.chasetechnology.co.uk
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
package org.eclipse.wst.xsl.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>xsl:call-template</code> model element.
 * 
 * @author Doug Satchwell
 * @since 1.0
 */
public class CallTemplate extends XSLElement
{
	final List<Parameter> parameters = new ArrayList<Parameter>();
	
	/**
	 * Create a new instance of this.
	 * 
	 * @param stylesheet the stylesheet that this belongs to
	 */
	public CallTemplate(Stylesheet stylesheet)
	{
		super(stylesheet);
	}
	
	/**
	 * Add a parameter to this.
	 * 
	 * @param parameter the parameter to add
	 */
	public void addParameter(Parameter parameter)
	{
		parameters.add(parameter);
	}

	/**
	 * Get the value of the <code>name</code> attribute if one exists.
	 * 
	 * @return the template name, or null
	 */
	@Override
	public String getName()
	{
		return getAttributeValue("name"); //$NON-NLS-1$
	}
	
	/**
	 * Get the list of parameters associated with this.
	 * 
	 * @return a list of parameters
	 */
	public List<Parameter> getParameters()
	{
		return parameters;
	}
	
	@Override
	public Type getModelType()
	{
		return Type.CALL_TEMPLATE;
	}

}
