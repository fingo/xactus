/*******************************************************************************
 * Copyright (c) 2009, 2017 IBM Corporation and others.
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
package org.eclipse.wst.sse.core.tests.document;

import junit.framework.TestCase;

import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocument;
import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocumentRegion;

public class TestObjects extends TestCase {

	/**
	 *  
	 */
	public TestObjects() {
		super();

	}

	/**
	 * @param name
	 */
	public TestObjects(String name) {
		super(name);

	}

	public void testEndedFlag() {
		BasicStructuredDocumentRegion region = new BasicStructuredDocumentRegion();
		region.setParentDocument(new BasicStructuredDocument());
		boolean deleted = region.isDeleted();
		boolean ended = region.isEnded();
		region.setEnded(!ended);
		assertEquals(!ended, region.isEnded());
		assertEquals(deleted, region.isDeleted());
	}

	public void testDeletedFlag() {
		BasicStructuredDocumentRegion region = new BasicStructuredDocumentRegion();
		region.setParentDocument(new BasicStructuredDocument());
		boolean deleted = region.isDeleted();
		boolean ended = region.isEnded();
		region.setDeleted(!deleted);
		assertEquals(!deleted, region.isDeleted());
		assertEquals(ended, region.isEnded());
	}
}