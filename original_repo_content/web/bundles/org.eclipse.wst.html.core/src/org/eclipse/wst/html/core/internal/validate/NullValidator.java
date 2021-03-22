/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
package org.eclipse.wst.html.core.internal.validate;

import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.validate.ValidationReporter;
import org.eclipse.wst.xml.core.internal.validate.ValidationComponent;

/**
 * NullValidator class is intended to be a replacement of null
 * for ValidationComponent type.
 */
final class NullValidator extends ValidationComponent {

	public NullValidator() {
		super();
	}

	public void validate(IndexedRegion node) {
		return;
	}

	public void setReporter(ValidationReporter reporter) {
		return;
	}
}
