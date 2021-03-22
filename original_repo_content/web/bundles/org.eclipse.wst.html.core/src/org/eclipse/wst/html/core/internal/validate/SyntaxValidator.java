/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - external validator extension
 *******************************************************************************/
package org.eclipse.wst.html.core.internal.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.wst.html.core.internal.HTMLCorePlugin;
import org.eclipse.wst.html.core.internal.Logger;
import org.eclipse.wst.html.core.internal.contentmodel.HTMLElementDeclaration;
import org.eclipse.wst.html.core.internal.contentmodel.HTMLPropertyDeclaration;
import org.eclipse.wst.html.core.internal.document.HTMLDocumentTypeEntry;
import org.eclipse.wst.html.core.internal.document.HTMLDocumentTypeRegistry;
import org.eclipse.wst.html.core.internal.preferences.HTMLCorePreferenceNames;
import org.eclipse.wst.html.core.internal.provisional.HTML50Namespace;
import org.eclipse.wst.html.core.validate.extension.IHTMLCustomTagValidator;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.core.internal.validate.ErrorInfo;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.document.InvalidCharacterException;
import org.eclipse.wst.xml.core.internal.document.SourceValidator;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

class SyntaxValidator extends PrimeValidator implements ErrorState {
	private IPreferencesService fPreferenceService;
	private static Map fIgnorePatterns = new HashMap(); // A storage for ignore patterns (instances of StringMatcher)
	private List<IHTMLCustomTagValidator> externalValidators;
	
	static private boolean isValidRegion(ITextRegion rgn) {
		String type = rgn.getType();
		if (type == null)
			return false; // no type is invalid.
		if (type == DOMRegionContext.XML_END_TAG_OPEN || type == DOMRegionContext.XML_TAG_NAME || type == DOMRegionContext.XML_TAG_CLOSE) {
			return true;
		}
		return false;
	}

	static private String getTagName(IStructuredDocumentRegion tag) {
		ITextRegionList regions = tag.getRegions();
		Iterator iter = regions.iterator();
		while (iter.hasNext()) {
			ITextRegion rgn = (ITextRegion) iter.next();
			if (rgn.getType() == DOMRegionContext.XML_TAG_NAME)
				return tag.getText(rgn);
		}
		return "";//$NON-NLS-1$
	}

	static private boolean isEmptyContent(CMElementDeclaration decl) {
		return (decl != null) && (decl.getContentType() == CMElementDeclaration.EMPTY);
	}

	public SyntaxValidator() {
		super();
		fPreferenceService = Platform.getPreferencesService();
	}

	public boolean isAdapterForType(Object type) {
		return ((type == SyntaxValidator.class) || super.isAdapterForType(type));
	}

	class ElementInfo {
		public ElementInfo() {
			super();
		}

		public IDOMElement target = null;
		public CMElementDeclaration decl = null;
		public IStructuredDocumentRegion startTag = null;
		public IStructuredDocumentRegion endTag = null;
		public boolean hasStartTag() {return startTag != null;}
		public boolean hasEndTag() {return endTag != null;}
		public boolean isXHTML = false;
		public boolean isXHTML5 = false;
	}

	public void validate(IndexedRegion indexedNode) {
		Node node = (Node) indexedNode;
		validateChildren(node);

		if (node.getNodeType() != Node.ELEMENT_NODE)
			return;
		if (!(node instanceof IDOMElement))
			return;

		ElementInfo info = new ElementInfo();
		info.target = (IDOMElement) node;

		if (info.target.getModel().isModelStateChanging()) {
			return;
		}

		// gather information to validate from target at once.
		getInfo(info);

		validateTags(info);
		if (info.target.isGlobalTag()) {
			validateNames(info);
			if (info.decl != null && info.isXHTML) {
				validateTagCase(info);
			}
		}

		//validate the syntax of the attributes
		validateAttributes(info);
	}

	private void getInfo(ElementInfo info) {
		info.decl = CMUtil.getDeclaration(info.target);
		info.startTag = info.target.getStartStructuredDocumentRegion();
		info.endTag = info.target.getEndStructuredDocumentRegion();

		Document doc = info.target.getOwnerDocument();
		if (!(doc instanceof IDOMDocument))
			return;
		String typeid = ((IDOMDocument) doc).getDocumentTypeId();
		if (typeid != null) {
			if (typeid.trim().length()!= 0){
				HTMLDocumentTypeEntry entry = HTMLDocumentTypeRegistry.getInstance().getEntry(typeid);
				info.isXHTML = (entry != null && entry.isXMLType());
			}
			else {
				info.isXHTML = getXMLTarget(doc);
				info.isXHTML5 = info.isXHTML;
			}
		}
	}
	
	private boolean getXMLTarget(Document doc) {
		if (doc == null)
			return false;
		Node child = doc.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equalsIgnoreCase("html")){
					if (child.getAttributes()!= null){
						NamedNodeMap attrs = child.getAttributes();
						for (int i = 0; i < attrs.getLength(); i++) {
							Attr a = (Attr) attrs.item(i);
							if (a.getName().equalsIgnoreCase(HTML50Namespace.ATTR_NAME_XMLNS))
									return true;
						}
					}
					return false;
				}
				
			}
			child = child.getNextSibling();
		}
		return false;
	}

	class TagErrorInfoImpl extends AbstractErrorInfo {
		private String hint = null;

		public TagErrorInfoImpl(int state, IStructuredDocumentRegion tag, String hint) {
			super(state, new Segment(tag));
			this.hint = hint;
		}

		public String getHint() {
			return hint;
		}

		public short getTargetType() {
			return Node.ELEMENT_NODE;
		}
	}

	private boolean isEndTagCorrupted(ElementInfo info) {
		ITextRegionList regions = info.endTag.getRegions();
		if (regions == null || regions.isEmpty())
			return false;
		Iterator iter = regions.iterator();
		while (iter.hasNext()) {
			ITextRegion rgn = (ITextRegion) iter.next();
			if (!isValidRegion(rgn))
				return true; // found invalid region type.
		}
		return false; // all regions are valid.
	}

	private String getEndTagFullText(ElementInfo info) {
		String hint = "";//$NON-NLS-1$
		ITextRegionList regions = info.endTag.getRegions();
		Iterator iter = regions.iterator();
		while (iter.hasNext()) {
			ITextRegion rgn = (ITextRegion) iter.next();
			String type = rgn.getType();
			if (type == null)
				continue;
			if (type == DOMRegionContext.XML_END_TAG_OPEN || type == DOMRegionContext.XML_TAG_CLOSE)
				continue;
			hint += info.endTag.getFullText(rgn);
		}
		return hint;
	}

	private void reportCorruptedEndTagError(ElementInfo info) {
		String hint = getEndTagFullText(info);
		TagErrorInfoImpl error = new TagErrorInfoImpl(UNDEFINED_NAME_ERROR, info.endTag, hint);
		this.reporter.report(error);
	}

	private void validateTags(ElementInfo info) {
		if (info.hasStartTag()) {
			if (!info.target.isStartTagClosed()) {
				// Mark the whole START tag as an error segment.
				Segment errorSeg = new Segment(info.startTag);
				report(UNCLOSED_TAG_ERROR, errorSeg, info.target);
			}
		}
		else {
			if (info.hasEndTag()) {
				if (info.decl != null) {
					//if xhtml then must have end tag
					//else determine if end tag is omissible
					if (info.isXHTML) {
						Segment errorSeg = FMUtil.getSegment(info.target, FMUtil.SEG_END_TAG_NAME);
						report(MISSING_START_TAG_ERROR, errorSeg, info.target);
					} else {
						//determine if the end tag is omissible
						boolean canOmitStartTag = false;
						if(info.decl instanceof HTMLPropertyDeclaration) {
							int omitType = ((HTMLPropertyDeclaration)info.decl).getOmitType();
							canOmitStartTag = omitType == HTMLElementDeclaration.OMIT_BOTH;
						}
					
						if (!canOmitStartTag && !info.target.hasChildNodes()) {
							if (info.target.isContainer()) {
								// Set the error mark to the start of the element.
								Segment errorSeg = FMUtil.getSegment(info.target, FMUtil.SEG_END_TAG);
								report(MISSING_START_TAG_ERROR, errorSeg, info.target);
							}
							else {
								// Mark the whole END tag as an error segment.
								Segment errorSeg = new Segment(info.endTag);
								report(UNNECESSARY_END_TAG_ERROR, errorSeg, info.target);
							}
						}
					}
				}
			}
		}

		if (info.hasEndTag()) {
			if (!info.target.isClosed()) {
				// Set the whole END tag as error segment.
				Segment errorSeg = new Segment(info.endTag);
				report(UNCLOSED_END_TAG_ERROR, errorSeg, info.target);
			}
		}
		else {
			if (info.isXHTML) { // XHTML
				//if editor closed during validation this could be null
				IStructuredDocumentRegion structRegion = info.target.getStartStructuredDocumentRegion();
				if (!info.target.isEmptyTag() && structRegion != null && DOMRegionContext.XML_TAG_OPEN.equals(structRegion.getFirstRegion().getType())) {
					/*
					 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=248963 :
					 * report empty tags not written as such, but only when
					 * they follow actual XML/HTML syntax
					 */
					if (isEmptyContent(info.decl)) {
						// EMPTY element should be written in <.../> form
						Segment errorSeg = FMUtil.getSegment(info.target, FMUtil.SEG_START_TAG);
						report(INVALID_EMPTY_ELEMENT_TAG, errorSeg, info.target);
					}
					else {
						// end tag is required.
						Segment errorSeg = FMUtil.getSegment(info.target, FMUtil.SEG_START_TAG);
						report(MISSING_END_TAG_ERROR, errorSeg, info.target);
					}
				}
			}
			else { // HTML
				if (info.hasStartTag()) {
					if (info.decl != null && CMUtil.isHTML(info.decl) && !info.target.isEmptyTag() && !CMUtil.isEndTagOmissible(info.decl) && DOMRegionContext.XML_TAG_OPEN.equals(info.startTag.getFirstRegion().getType())) {
						// Set the error mark to the end of the element.
						Segment errorSeg = FMUtil.getSegment(info.target, FMUtil.SEG_START_TAG);
						report(MISSING_END_TAG_ERROR, errorSeg, info.target);
					}
				}
			}
		}
	}

	/* perform validation about tag name definition. */
	private void validateNames(ElementInfo info) {
		boolean corrupted = info.hasEndTag() && isEndTagCorrupted(info);
		if (info.decl == null) {
			// If no declaration is retrieved, the target is really
			// unknown element.
			if (!info.hasStartTag() && corrupted) {
				reportCorruptedEndTagError(info);
			}
			else {
				if (shouldValidateElementName(info.target)) {
					// not excluded in preferences - check for extension point
					boolean validated = false;
					
					if (externalValidators == null) {
						initValidators(info.target.getStructuredDocument());
					}
					for (IHTMLCustomTagValidator v : externalValidators) {
						try {
							if (v.canValidate(info.target)) {
								validated = true;
								ValidationMessage result = v.validateTag(info.target);
								if(result != null) {
									// report only one validation result or nothing if all reports are null
									reporter.report(result);
									break;
								}
							}
						} catch (Throwable t) {
							Logger.logException(t);
						}
					}
					if (!validated) {
						Segment errorSeg = FMUtil.getSegment(info.target, FMUtil.SEG_START_TAG_NAME);
						report(UNDEFINED_NAME_ERROR, errorSeg, info.target);
					}
				}
			}
		}
		else {
			// Even if a declaration could be retrieved, if the end
			// tag is corrupted, it should be reported as undefined
			// name. (D202493)
			if (corrupted) {
				reportCorruptedEndTagError(info);
			}
		}
	}

	private void initValidators(IStructuredDocument doc) {
		externalValidators = new ArrayList<IHTMLCustomTagValidator>();
		for (IConfigurationElement e : CustomHTMLTagValidatorExtensionLoader.getInstance().getValidators()) {
			IHTMLCustomTagValidator validator;
			try {
				validator = (IHTMLCustomTagValidator) e.createExecutableExtension("class");
				validator.init(doc);
				externalValidators.add(validator);			
			} catch (CoreException e1) {
				Logger.logException(e1);
			}
		}
	}
	
	private boolean shouldValidateElementName(Element target) {
		Object adapter = (target instanceof IAdaptable ? ((IAdaptable)target).getAdapter(IResource.class) : null);
		IProject project = (adapter instanceof IResource ? ((IResource)adapter).getProject() : null);
		
		Iterator excludedElements = getExcludedElementNames(project).iterator();
		while (excludedElements.hasNext()) {
			String excluded = (String)excludedElements.next();
			StringMatcher strMatcher = (StringMatcher)fIgnorePatterns.get(excluded);
			if (strMatcher == null) {
				strMatcher = new StringMatcher(excluded);
				fIgnorePatterns.put(excluded, strMatcher);
			}
			if (strMatcher.match(target.getNodeName()))
				return false;
		}
		return true;
	}

	private Set getExcludedElementNames(IProject project) {
		IScopeContext[] fLookupOrder = new IScopeContext[] {new InstanceScope(), new DefaultScope()};
		if (project != null) {
			ProjectScope projectScope = new ProjectScope(project);
			if(projectScope.getNode(HTMLCorePlugin.getDefault().getBundle().getSymbolicName()).getBoolean(HTMLCorePreferenceNames.USE_PROJECT_SETTINGS, false))
				fLookupOrder = new IScopeContext[] {projectScope, new InstanceScope(), new DefaultScope()};
		}

		Set result = new HashSet();
		if (fPreferenceService.getBoolean(HTMLCorePlugin.getDefault().getBundle().getSymbolicName(), 
				HTMLCorePreferenceNames.IGNORE_ELEMENT_NAMES, HTMLCorePreferenceNames.IGNORE_ELEMENT_NAMES_DEFAULT, 
				fLookupOrder)) {
			String ignoreList = fPreferenceService.getString(HTMLCorePlugin.getDefault().getBundle().getSymbolicName(), 
					HTMLCorePreferenceNames.ELEMENT_NAMES_TO_IGNORE, HTMLCorePreferenceNames.ELEMENT_NAMES_TO_IGNORE_DEFAULT, 
					fLookupOrder);

			if (ignoreList.trim().length() == 0)
				return result;

			String[] names = ignoreList.split(","); //$NON-NLS-1$
			for (int i = 0; names != null && i < names.length; i++) {
				String name = names[i] == null ? null : names[i].trim();
				if (name != null && name.length() > 0)
					result.add(name.toLowerCase());
			}
		}
		return result; 
	}
	
	/* perform validation tag case only for XHTML document */
	private void validateTagCase(ElementInfo info) {
		String declared = info.decl.getElementName();
		String startTagName = "";//$NON-NLS-1$
		String endTagName = "";//$NON-NLS-1$
		if (declared == null)
			return;

		if (info.isXHTML5){
			if (info.hasStartTag()) {
				startTagName = getTagName(info.startTag);
				if (info.hasEndTag()) {
					endTagName = getTagName(info.endTag);
					if (!endTagName.equals(startTagName)){
						TagErrorInfoImpl error = new TagErrorInfoImpl(MISMATCHED_ERROR, info.endTag, endTagName);
						this.reporter.report(error);
					}
				}
			}
			
		}
		else
		{
			// start tag
			if (info.hasStartTag()) {
				startTagName = getTagName(info.startTag);
				if (!declared.equals(startTagName)) {
					TagErrorInfoImpl error = new TagErrorInfoImpl(MISMATCHED_ERROR, info.startTag, startTagName);
					this.reporter.report(error);
				}
			}
			// end tag
			if (info.hasEndTag()) {
				endTagName = getTagName(info.endTag);
				if (!info.hasStartTag() || (!endTagName.equals(startTagName))) {
					if (!declared.equals(endTagName)) {
						TagErrorInfoImpl error = new TagErrorInfoImpl(MISMATCHED_ERROR, info.endTag, endTagName);
						this.reporter.report(error);
					}
				}
			}
		}
		
	}

	private void validateChildren(Node target) {
		if ((target.getNodeType() == Node.ELEMENT_NODE) && CMUtil.isForeign((Element) target))
			return;

		for (Node child = target.getFirstChild(); child != null; child = child.getNextSibling()) {
			switch (child.getNodeType()) {
				case Node.TEXT_NODE :
					{
						IDOMNode text = (IDOMNode) child;
						int charOffset = validateTextSource(text);
						if (charOffset >= 0) {
							charOffset += text.getStartOffset();
							Segment errorSeg = new Segment(charOffset, 1);
							if (errorSeg != null)
								report(INVALID_CHAR_ERROR, errorSeg, text);
						}
						break;
					}
				case Node.COMMENT_NODE :
				case Node.DOCUMENT_TYPE_NODE :
				case Node.PROCESSING_INSTRUCTION_NODE :
				case Node.CDATA_SECTION_NODE :
					{
						IDOMNode tag = (IDOMNode) child;
						if (!tag.isClosed()) {
							Segment errorSeg = FMUtil.getSegment(tag, FMUtil.SEG_WHOLE_TAG);
							if (errorSeg != null)
								report(UNCLOSED_TAG_ERROR, errorSeg, tag);
						}
						break;
					}
				default :
					break;
			}
		}
	}

	private int validateTextSource(IDOMNode text) {
		try {
			SourceValidator validator = new SourceValidator(text);
			validator.validateSource(text.getSource());
		}
		catch (InvalidCharacterException ex) {
			return ex.getOffset();
		}
		return -1;
	}

	/**
	 * <p>Used to validate the syntax of the arguments in a tag.</p>
	 * 
	 * @param info the <code>ElementInfo</code> about the tag to validate 
	 */
	private void validateAttributes(ElementInfo info) {
		if(info != null && info.hasStartTag()) {
			ITextRegionList list = info.startTag.getRegions();

			//for each region in the tag, these make up the arguments
			for(int i = 0; i < list.size(); ++i) {
				ITextRegion region = list.get(i);
				String type = region.getType();
	
				//if the type is undefined then there is a syntax error
				if(type == DOMRegionContext.UNDEFINED) {
					String invalidText = info.startTag.getFullText(region).trim();
					Segment errorSeg = new Segment(info.startTag.getStartOffset() + region.getStart(), invalidText.length());
					report(INVALID_TEXT_IN_ELEM_ERROR, errorSeg, info.target, new String[]{invalidText}, new String[0]);
				}
			}
		}
	}

	private void report(int state, Segment errorSeg, Node node) {
		ErrorInfo info = new ErrorInfoImpl(state, errorSeg, node);
		reporter.report(info);
	}

	private void report(int state, Segment errorSeg, Node node, String[] preTargetMsgInjections, String[] postTargetMsgInjections) {
		ErrorInfo info = new ErrorInfoImpl(state, errorSeg, node, preTargetMsgInjections, postTargetMsgInjections);
		reporter.report(info);
	}
}
