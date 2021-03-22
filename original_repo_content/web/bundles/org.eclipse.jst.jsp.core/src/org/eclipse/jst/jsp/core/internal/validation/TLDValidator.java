/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.content.IContentTypeSettings;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.jsp.core.internal.Assert;
import org.eclipse.jst.jsp.core.internal.JSPCoreMessages;
import org.eclipse.jst.jsp.core.internal.JSPCorePlugin;
import org.eclipse.jst.jsp.core.internal.Logger;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.JSP11TLDNames;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.JSP12TLDNames;
import org.eclipse.jst.jsp.core.internal.preferences.JSPCorePreferenceNames;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.ValidatorMessage;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.validation.MarkupValidator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A miniature validator for .tld files. Checks for valid class names.
 */
public class TLDValidator extends AbstractValidator {
	private static final String MARKER_TYPE = "org.eclipse.jst.jsp.core.validationMarker"; //$NON-NLS-1$
	private static final String PREFERENCE_NODE_QUALIFIER = JSPCorePlugin.getDefault().getBundle().getSymbolicName();

	private IPreferencesService fPreferencesService = Platform.getPreferencesService();

	private static final String[] classElementNames = new String[]{JSP11TLDNames.TAGCLASS, JSP12TLDNames.TAG_CLASS, JSP11TLDNames.TEICLASS, JSP12TLDNames.TEI_CLASS, JSP12TLDNames.VALIDATOR_CLASS, JSP12TLDNames.VARIABLE_CLASS, JSP12TLDNames.LISTENER_CLASS};
	private static final String[] missingClassMessages = new String[]{JSPCoreMessages.TaglibHelper_3, JSPCoreMessages.TaglibHelper_3, JSPCoreMessages.TaglibHelper_0, JSPCoreMessages.TaglibHelper_0, JSPCoreMessages.TLDValidator_MissingValidator, JSPCoreMessages.TLDValidator_MissingVariable, JSPCoreMessages.TLDValidator_MissingListener};
	private static final String[] missingClassSeverityPreferenceKeys = new String[]{JSPCorePreferenceNames.VALIDATION_TRANSLATION_TAG_HANDLER_CLASS_NOT_FOUND, JSPCorePreferenceNames.VALIDATION_TRANSLATION_TAG_HANDLER_CLASS_NOT_FOUND, JSPCorePreferenceNames.VALIDATION_TRANSLATION_TEI_CLASS_NOT_FOUND, JSPCorePreferenceNames.VALIDATION_TRANSLATION_TEI_CLASS_NOT_FOUND, JSPCorePreferenceNames.VALIDATION_TRANSLATION_TAG_HANDLER_CLASS_NOT_FOUND, JSPCorePreferenceNames.VALIDATION_TRANSLATION_TAG_HANDLER_CLASS_NOT_FOUND, JSPCorePreferenceNames.VALIDATION_TRANSLATION_TAG_HANDLER_CLASS_NOT_FOUND};

	private static final String TAGX_CONTENT_TYPE_ID = "org.eclipse.jst.jsp.core.tagxsource"; //$NON-NLS-1$
	private List fTagXexts = null;
	private List fTagXnames = null;

	public TLDValidator() {
		super();

		Assert.isTrue(classElementNames.length == missingClassMessages.length, "mismanaged arrays"); //$NON-NLS-1$
		Assert.isTrue(classElementNames.length == missingClassSeverityPreferenceKeys.length, "mismanaged arrays"); //$NON-NLS-1$
		Assert.isTrue(missingClassMessages.length == missingClassSeverityPreferenceKeys.length, "mismanaged arrays"); //$NON-NLS-1$
		
		initContentTypes();
	}

	private void initContentTypes() {
		fTagXexts = new ArrayList(Arrays.asList(Platform.getContentTypeManager().getContentType(TAGX_CONTENT_TYPE_ID).getFileSpecs(IContentTypeSettings.FILE_EXTENSION_SPEC)));
		fTagXnames = new ArrayList(Arrays.asList(Platform.getContentTypeManager().getContentType(TAGX_CONTENT_TYPE_ID).getFileSpecs(IContentTypeSettings.FILE_NAME_SPEC)));
	}

	private Map checkClass(IJavaProject javaProject, Node classSpecifier, IScopeContext[] preferenceScopes, String preferenceKey, String errorMessage) {
		String className = getTextContents(classSpecifier);
		if (className != null && className.length() > 2) {
			IType type = null;
			try {
				type = javaProject.findType(className);
			}
			catch (JavaModelException e) {
				return null;
			}

			if (type == null || !type.exists()) {
				Object severity = getMessageSeverity(preferenceScopes, preferenceKey);
				if (severity == null)
					return null;

				IDOMNode classElement = (IDOMNode) classSpecifier;
				Map markerValues = new HashMap();
				markerValues.put(IMarker.SEVERITY, severity);
				int start = classElement.getStartOffset();
				if (classElement.getStartStructuredDocumentRegion() != null && classElement.getEndStructuredDocumentRegion() != null)
					start = classElement.getStartStructuredDocumentRegion().getEndOffset();
				markerValues.put(IMarker.CHAR_START, new Integer(start));
				int end = classElement.getEndOffset();
				if (classElement.getStartStructuredDocumentRegion() != null && classElement.getEndStructuredDocumentRegion() != null)
					end = classElement.getEndStructuredDocumentRegion().getStartOffset();
				markerValues.put(IMarker.CHAR_END, new Integer(end));
				int line = classElement.getStructuredDocument().getLineOfOffset(start);
				markerValues.put(IMarker.LINE_NUMBER, new Integer(line + 1));
				markerValues.put(IMarker.MESSAGE, NLS.bind(errorMessage, (errorMessage.indexOf("{1}") >= 0) ? new String[]{getTagName(classSpecifier), className} : new String[]{className})); //$NON-NLS-1$
				return markerValues;
			}
		}
		return null;
	}

	private Map[] detectProblems(IJavaProject javaProject, IFile tld, IScopeContext[] preferenceScopes) throws CoreException {
		List problems = new ArrayList();

		IStructuredModel m = null;
		try {
			m = StructuredModelManager.getModelManager().getModelForRead(tld);
			if (m != null && m instanceof IDOMModel) {
				IDOMDocument document = ((IDOMModel) m).getDocument();

				for (int i = 0; i < classElementNames.length; i++) {
					NodeList classes = document.getElementsByTagName(classElementNames[i]);
					for (int j = 0; j < classes.getLength(); j++) {
						Map problem = checkClass(javaProject, classes.item(j), preferenceScopes, missingClassSeverityPreferenceKeys[i], missingClassMessages[i]);
						if (problem != null)
							problems.add(problem);
					}
				}

			}
		}
		catch (IOException e) {
			Logger.logException(e);
		}
		finally {
			if (m != null)
				m.releaseFromRead();
		}

		return (Map[]) problems.toArray(new Map[problems.size()]);
	}

	Integer getMessageSeverity(IScopeContext[] preferenceScopes, String key) {
		int sev = fPreferencesService.getInt(PREFERENCE_NODE_QUALIFIER, key, IMessage.NORMAL_SEVERITY, preferenceScopes);
		switch (sev) {
			case ValidationMessage.ERROR :
				return new Integer(IMarker.SEVERITY_ERROR);
			case ValidationMessage.WARNING :
				return new Integer(IMarker.SEVERITY_WARNING);
			case ValidationMessage.INFORMATION :
				return new Integer(IMarker.SEVERITY_INFO);
			case ValidationMessage.IGNORE :
				return null;
		}
		return new Integer(IMarker.SEVERITY_WARNING);
	}

	private String getTagName(Node classSpecifier) {
		Node tagElement = classSpecifier.getParentNode();
		Node child = tagElement.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String name = child.getNodeName();
				if (JSP11TLDNames.NAME.equals(name))
					return getTextContents(child);
			}
			child = child.getNextSibling();
		}
		return "";
	}

	private String getTextContents(Node parent) {
		NodeList children = parent.getChildNodes();
		if (children.getLength() == 1) {
			return children.item(0).getNodeValue().trim();
		}
		StringBuffer s = new StringBuffer();
		Node child = parent.getFirstChild();
		while (child != null) {
			s.append(child.getNodeValue().trim());
			child = child.getNextSibling();
		}
		return s.toString().trim();
	}

	public ValidationResult validate(IResource resource, int kind, ValidationState state, IProgressMonitor monitor) {
		if (resource.getType() != IResource.FILE)
			return null;
		ValidationResult result = new ValidationResult();

		IFile file = (IFile) resource;
		if (file.isAccessible()) {
			// TAGX
			if (fTagXexts.contains(file.getFileExtension()) || fTagXnames.contains(file.getName())) {
				monitor.beginTask("", 4);
				org.eclipse.wst.xml.core.internal.validation.eclipse.Validator xmlValidator = new org.eclipse.wst.xml.core.internal.validation.eclipse.Validator();
				ValidationResult result3 = new MarkupValidator().validate(resource, kind, state, new SubProgressMonitor(monitor, 1));
				if(monitor.isCanceled()) return result;
				ValidationResult result2 = xmlValidator.validate(resource, kind, state, new SubProgressMonitor(monitor, 1));
				if(monitor.isCanceled()) return result;
				ValidationResult result1 = new JSPActionValidator().validate(resource, kind, state, new SubProgressMonitor(monitor, 1));
				List messages = new ArrayList(result1.getReporter(new NullProgressMonitor()).getMessages());
				messages.addAll(result2.getReporter(new NullProgressMonitor()).getMessages());
				messages.addAll(result3.getReporter(new NullProgressMonitor()).getMessages());
				messages.addAll(new JSPDirectiveValidator().validate(resource, kind, state, new SubProgressMonitor(monitor, 1)).getReporter(new NullProgressMonitor()).getMessages());
				for (int i = 0; i < messages.size(); i++) {
					IMessage message = (IMessage) messages.get(i);
					if (message.getText() != null && message.getText().length() > 0) {
						ValidatorMessage vmessage = ValidatorMessage.create(message.getText(), resource);
						if (message.getAttributes() != null) {
							Map attrs = message.getAttributes();
							Iterator it = attrs.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry entry = (Map.Entry) it.next();
								if (!(entry.getValue() instanceof String || entry.getValue() instanceof Integer || entry.getValue() instanceof Boolean)) {
									it.remove();
								}
							}
							vmessage.setAttributes(attrs);
						}
						vmessage.setAttribute(IMarker.LINE_NUMBER, message.getLineNumber());
						vmessage.setAttribute(IMarker.MESSAGE, message.getText());
						if (message.getOffset() > -1) {
							vmessage.setAttribute(IMarker.CHAR_START, message.getOffset());
							vmessage.setAttribute(IMarker.CHAR_END, message.getOffset() + message.getLength());
						}
						int severity = 0;
						switch (message.getSeverity()) {
							case IMessage.HIGH_SEVERITY :
								severity = IMarker.SEVERITY_ERROR;
								break;
							case IMessage.NORMAL_SEVERITY :
								severity = IMarker.SEVERITY_WARNING;
								break;
							case IMessage.LOW_SEVERITY :
								severity = IMarker.SEVERITY_INFO;
								break;
						}
						vmessage.setAttribute(IMarker.SEVERITY, severity);
						vmessage.setType(MARKER_TYPE);
						result.add(vmessage);
					}
				}
				monitor.done();
			}
			// TLD
			else {
				try {
					final IJavaProject javaProject = JavaCore.create(file.getProject());
					if (javaProject.exists()) {
						IScopeContext[] scopes = new IScopeContext[]{new InstanceScope(), new DefaultScope()};
						ProjectScope projectScope = new ProjectScope(file.getProject());
						if (projectScope.getNode(PREFERENCE_NODE_QUALIFIER).getBoolean(JSPCorePreferenceNames.VALIDATION_USE_PROJECT_SETTINGS, false)) {
							scopes = new IScopeContext[]{projectScope, new InstanceScope(), new DefaultScope()};
						}
						Map[] problems = detectProblems(javaProject, file, scopes);
						for (int i = 0; i < problems.length; i++) {
							ValidatorMessage message = ValidatorMessage.create(problems[i].get(IMarker.MESSAGE).toString(), resource);
							message.setType(MARKER_TYPE);
							message.setAttributes(problems[i]);
							result.add(message);
						}
					}
				}
				catch (Exception e) {
					Logger.logException(e);
				}
			}
		}

		return result;
	}
}
