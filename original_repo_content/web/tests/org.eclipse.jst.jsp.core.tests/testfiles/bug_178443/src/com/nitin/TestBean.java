/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package com.nitin;

public class TestBean {

	public TestBean() {
	}
	
	String dummyProperty = null;

	public String getDummyProperty() {
		return dummyProperty;
	}

	public void setDummyProperty(String dummyProperty) {
		this.dummyProperty = dummyProperty;
	}

}
