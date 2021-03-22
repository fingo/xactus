/*******************************************************************************
 * Copyright (c) 2004, 2012 IBM Corporation and others.
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
package org.eclipse.jst.jsp.ui.internal.format;

import java.util.LinkedList;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy;
import org.eclipse.jface.text.formatter.FormattingContextProperties;
import org.eclipse.jface.text.formatter.IFormattingContext;
import org.eclipse.jst.jsp.core.internal.Logger;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslation;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslationUtil;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;


public class FormattingStrategyJSPJava extends ContextBasedFormattingStrategy {

	/** Documents to be formatted by this strategy */
	private final LinkedList fDocuments = new LinkedList();
	/** Partitions to be formatted by this strategy */
	private final LinkedList fPartitions = new LinkedList();
	JSPTranslation translation = null;

	/**
	 * Creates a new java formatting strategy.
	 */
	public FormattingStrategyJSPJava() {
		super();
	}

	/*
	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#format()
	 */
	public void format() {
		super.format();
		final IDocument document = (IDocument) fDocuments.removeFirst();
		final TypedPosition partition = (TypedPosition) fPartitions.removeFirst();

		if (document != null && partition != null) {
			try {

				JSPTranslationUtil translationUtil = new JSPTranslationUtil(document);
				ICompilationUnit cu = translationUtil.getCompilationUnit();
				if (cu != null) {
					String cuSource = cu.getSource();
					/*
					 * Format the entire compilation unit, but only create
					 * edits for the requested JSP partition's range in the
					 * Java source
					 */
					TextEdit textEdit = formatString(CodeFormatter.K_COMPILATION_UNIT, cuSource, translationUtil.getTranslation().getJavaOffset(partition.getOffset()), partition.getLength(), TextUtilities.getDefaultLineDelimiter(document), getPreferences());

					TextEdit jspEdit = translationUtil.getTranslation().getJspEdit(textEdit);
					if (jspEdit != null && jspEdit.hasChildren())
						jspEdit.apply(document);
				}

			}
			catch (MalformedTreeException exception) {
				Logger.logException(exception);
			}
			catch (BadLocationException exception) {
				// Can only happen on concurrent document modification - log
				// and bail out
				Logger.logException(exception);
			}
			catch (JavaModelException exception) {
				Logger.logException(exception);
			}
		}
	}

	private TextEdit formatString(int kind, String cuSource, int javaOffset, int length, String defaultLineDelimiter, Map preferences) {
		return ToolFactory.createCodeFormatter(preferences).format(kind, cuSource, javaOffset, length, 0, defaultLineDelimiter);
	}

	/*
	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStarts(org.eclipse.jface.text.formatter.IFormattingContext)
	 */
	public void formatterStarts(final IFormattingContext context) {
		super.formatterStarts(context);

		fPartitions.addLast(context.getProperty(FormattingContextProperties.CONTEXT_PARTITION));
		fDocuments.addLast(context.getProperty(FormattingContextProperties.CONTEXT_MEDIUM));
	}

	/*
	 * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStops()
	 */
	public void formatterStops() {
		super.formatterStops();

		fPartitions.clear();
		fDocuments.clear();
	}

	public TextEdit formatString(int kind, String string, int indentationLevel, String lineSeparator, Map options) {
		return ToolFactory.createCodeFormatter(options).format(kind, string, 0, string.length(), indentationLevel, lineSeparator);
	}
}
