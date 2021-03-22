/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - Initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.xml.core.internal.search.matching;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;

public class SAXSearchElement{
	Attributes attributes;
	String elementName;
	String elementNamespace;
	Map namespaceMap = new HashMap(); // Map of string prefix keys and namespace
	String targetNamespace = ""; //$NON-NLS-1$
	String parentName;
    int depth = -1;
	
	public SAXSearchElement() {
		super();
	}
	public Attributes getAttributes() {
		return attributes;
	}
	public String getElementName() {
		return elementName;
	}
	public String getElementNamespace() {
		return elementNamespace;
	}
	public Map getNamespaceMap() {
		return namespaceMap;
	}
	public String getTargetNamespace() {
		return targetNamespace;
	}
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public void setElementNamespace(String elementNamespace) {
		this.elementNamespace = elementNamespace;
	}
	public void setNamespaceMap(Map namespaceMap) {
		this.namespaceMap = namespaceMap;
	}
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}
  public int getDepth()
  {
    return depth;
  }
  public void setDepth(int depth)
  {
    this.depth = depth;
  }
public String getParentName() {
	return parentName;
}
public void setParentName(String parentName) {
	this.parentName = parentName;
}
	
	
	
	
}