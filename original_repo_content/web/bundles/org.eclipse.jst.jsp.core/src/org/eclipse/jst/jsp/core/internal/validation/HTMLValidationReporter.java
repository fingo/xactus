/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
package org.eclipse.jst.jsp.core.internal.validation;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.html.core.internal.validate.MessageFactory;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.validate.ErrorInfo;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.sse.core.internal.validate.ValidationReporter;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;

/*
 * Copied from html.ui's HTMLValidationReporter with some minor cleanup
 * modifications
 */
class HTMLValidationReporter implements ValidationReporter {

	private IValidator owner = null;
	private IReporter reporter = null;
	private IFile file = null;
	private IStructuredModel model = null;
	private MessageFactory fFactory = null;
	
	/**
	 */
	public HTMLValidationReporter(IValidator owner, IReporter reporter, IFile file, IStructuredModel model) {
		super();
		this.owner = owner;
		this.reporter = reporter;
		this.file = file;
		this.model = model;
		fFactory = new MessageFactory(file != null ? file.getProject() : null);
	}

	/**
	 */
	public void clear() {
		if (this.file == null)
			return;


		if (this.reporter != null) {
			this.reporter.removeAllMessages(this.owner, this.file);
		}
	}



	/**
	 */
	public void report(ValidationMessage message) {
		if (message == null || this.file == null || message.getSeverity() == ValidationMessage.IGNORE)
			return;
		IMessage mes = translateMessage(message);

		if (this.reporter != null) {
			this.reporter.addMessage(this.owner, mes);
		}
	}

	/**
	 * Translate ValidationMessage to IMessage and generate result log
	 */
	private IMessage translateMessage(ValidationMessage message) {
		int severity = IMessage.LOW_SEVERITY;
		switch (message.getSeverity()) {
			case ValidationMessage.ERROR :
				severity = IMessage.HIGH_SEVERITY;
				break;
			case ValidationMessage.WARNING :
				severity = IMessage.NORMAL_SEVERITY;
				break;
			case ValidationMessage.INFORMATION :
				break;
			default :
				break;
		}

		IMessage mes = new LocalizedMessage(severity, message.getMessage(), this.file);
		mes.setOffset(message.getOffset());
		mes.setLength(message.getLength());
		if (this.model != null) {
			IStructuredDocument flatModel = this.model.getStructuredDocument();
			if (flatModel != null) {
				int line = flatModel.getLineOfOffset(message.getOffset());
				mes.setLineNo(line + 1);
			}
		}

		return mes;
	}

	public void report(ErrorInfo info) {
		report(fFactory.createMessage(info));
	}
}
