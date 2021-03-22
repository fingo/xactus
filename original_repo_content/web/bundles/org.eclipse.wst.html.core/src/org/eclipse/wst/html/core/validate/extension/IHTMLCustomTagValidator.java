/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.html.core.validate.extension;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

/**
 * @since 1.2
 */
public interface IHTMLCustomTagValidator {
	/**
	 * Validator initialization. This method is called only once during validator loading.
	 * 
	 * @param doc validated document
	 */
	public void init(IStructuredDocument doc);
	
	/**
	 * 
	 * @param target tag to be validated
	 * @return <code>true</code> if validator can validate tag
	 */
	public boolean canValidate(IDOMElement target);
	
	/**Validate specified tag
	 * 
	 * @param target tag to be validated
	 * @return <code>null</code> if no error happens or {@link ValidationMessage} with error message and error region
	 */
	public ValidationMessage validateTag(IDOMElement target);
}
