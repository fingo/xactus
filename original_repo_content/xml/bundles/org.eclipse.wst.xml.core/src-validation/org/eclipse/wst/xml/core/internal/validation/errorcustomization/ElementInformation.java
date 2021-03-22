/*******************************************************************************
 * Copyright (c) 2006, 2014 IBM Corporation and others.
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
package org.eclipse.wst.xml.core.internal.validation.errorcustomization;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple class to store information about an XML element.
 */
public class ElementInformation
{
  protected String localName;
  protected String namespace;
  protected String fileURI;
  protected List children = new ArrayList();

  /**
   * Constructor.
   * 
   * @param uri
   * 		The namespace URI of the element.
   * @param localName
   * 		The local name of the element.
   */
  public ElementInformation(String uri, String localName)
  {
    this.localName = localName;
    this.namespace = uri;
  }
  
  /**
   * Constructor.
   * 
   * @param uri
   * 		The namespace URI of the element.
   * @param localName
   * 		The local name of the element.
   * @param fileURI
   * 		The URI of the file being validated
   */
  public ElementInformation(String uri, String localName, String fileURI) {
	  this(uri, localName);
	  this.fileURI = fileURI;
  }
  /**
   * Get the namespace of this element.
   * 
   * @return
   * 		The namespace of this element.
   */
  public String getNamespace()
  {
	return namespace;
  }
  
  /**
   * Get the local name of this element.
   * 
   * @return
   * 		The local name of this element.
   */
  public String getLocalname()
  {
	return localName;
  }
  
  /**
   * Get the list of children of this element. The list contains
   * ElementInformation objects representing the children of this element.
   * 
   * @return
   * 		The list of children of this element.
   */
  public List getChildren()
  {
	return children;
  }

  /**
   * Get the URI of the file that this element belongs to
   * @return
   * 	The URI of the file that this element belongs to
   */
  public String getFileURI() {
	  return fileURI;
  }
}