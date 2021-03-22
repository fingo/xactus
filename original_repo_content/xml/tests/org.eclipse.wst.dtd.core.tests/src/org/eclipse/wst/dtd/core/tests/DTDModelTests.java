/*******************************************************************************
 * Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     David Carver (STAR) - bug 245216 - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.dtd.core.tests;

import org.eclipse.wst.dtd.core.internal.document.DTDModelImpl;
import org.eclipse.wst.dtd.core.internal.provisional.contenttype.ContentTypeIdForDTD;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

import junit.framework.TestCase;

public class DTDModelTests  extends TestCase {

	public void testcreateUnmanagedDTDModel() {
		IModelManager modelManager = StructuredModelManager.getModelManager();
		IStructuredModel model = modelManager.createUnManagedStructuredModelFor(ContentTypeIdForDTD.ContentTypeID_DTD);
		assertTrue("Did not get DTD Model", model instanceof DTDModelImpl );
	}
	
}
