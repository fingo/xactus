/*******************************************************************************
 * Copyright (c) 2016, 2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Red Hat, Inc. - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.web.ui.internal.validation;

import java.util.Locale;

import org.eclipse.core.resources.IResource;
import org.eclipse.wst.validation.internal.core.Message;

/**
 * copied from org.eclipse.wst.validation.internal.operations.LocalizedMessage
 * 
 * This class is provided for validators which run only in Eclipse and whose messages, because they
 * come from another tool, are already localized. LocalizedMessage cannot be used by any validator
 * which needs to run in both WebSphere and Eclipse.
 */
public class LocalizedMessage extends Message {
	private String _message = null;

	public LocalizedMessage(int severity, String messageText) {
		this(severity, messageText, null);
	}

	public LocalizedMessage(int severity, String messageText, IResource targetObject) {
		this(severity, messageText, (Object) targetObject);
	}

	public LocalizedMessage(int severity, String messageText, Object targetObject) {
		super(null, severity, null);
		setLocalizedMessage(messageText);
		setTargetObject(targetObject);
	}

	public void setLocalizedMessage(String message) {
		_message = message;
	}

	public String getLocalizedMessage() {
		return _message;
	}

	public String getText() {
		return getLocalizedMessage();
	}

	public String getText(ClassLoader cl) {
		return getLocalizedMessage();
	}

	public String getText(Locale l) {
		return getLocalizedMessage();
	}

	public String getText(Locale l, ClassLoader cl) {
		return getLocalizedMessage();
	}
}
