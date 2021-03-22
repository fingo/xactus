/*******************************************************************************
 * Copyright (c) 2007, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.jst.jsp.core.internal.contenttype;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.jsp.core.internal.Logger;
import org.eclipse.jst.jsp.core.internal.util.CommonXML;
import org.eclipse.jst.jsp.core.internal.util.FacetModuleCoreSupport;
import org.eclipse.jst.jsp.core.internal.util.FileContentCache;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A cache for property group information stored in web.xml files. Information
 * is not persisted.
 */
public final class DeploymentDescriptorPropertyCache {
	private static final PropertyGroup[] NO_PROPERTY_GROUPS = new PropertyGroup[0];

	static class DeploymentDescriptor {
		PropertyGroup[] groups;
		long modificationStamp;
		StringMatcher[] urlPatterns;
		Float version = new Float(DEFAULT_WEBAPP_VERSION);
	}

	/**
	 * Representation of the JSP 2.0 property-group definitions from a servlet
	 * deployment descriptor.
	 */
	public static final class PropertyGroup {
		static PropertyGroup createFrom(IPath path, Node propertyGroupNode, int groupNumber) {
			PropertyGroup group = new PropertyGroup(path, groupNumber);
			Node propertyGroupID = propertyGroupNode.getAttributes().getNamedItem(ID);
			if (propertyGroupID != null) {
				group.setId(propertyGroupID.getNodeValue());
			}
			Node node = propertyGroupNode.getFirstChild();
			while (node != null) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String name = node.getLocalName();
					if (name == null) {
						name = node.getNodeName();
					}
					if (IS_XML.equals(name)) {
						group.setIsXML(getContainedText(node));
					}
					else if (EL_IGNORED.equals(name)) {
						group.setElignored(getContainedText(node));
					}
					else if (INCLUDE_CODA.equals(name)) {
						group.addCoda(getContainedText(node));
					}
					else if (INCLUDE_PRELUDE.equals(name)) {
						group.addPrelude(getContainedText(node));
					}
					else if (SCRIPTING_INVALID.equals(name)) {
						group.setScriptingInvalid(getContainedText(node));
					}
					else if (PAGE_ENCODING.equals(name)) {
						group.setPageEncoding(getContainedText(node));
					}
					else if (URL_PATTERN.equals(name)) {
						group.addUrlPattern(getContainedText(node));
					}
				}

				node = node.getNextSibling();
			}

			return group;
		}

		private boolean el_ignored;

		private String id;

		private IPath[] include_coda = new IPath[0];

		private IPath[] include_prelude = new IPath[0];
		private boolean is_xml;
		private StringMatcher[] urlMatchers;
		private String page_encoding;
		private boolean scripting_invalid;
		String[] url_patterns;
		private IPath webxmlPath;

		int number;

		private PropertyGroup(IPath path, int number) {
			super();
			this.webxmlPath = path;
			this.number = number;
		}

		void addCoda(String containedText) {
			if (containedText.length() > 0) {
				IPath[] codas = new IPath[include_coda.length + 1];
				System.arraycopy(include_coda, 0, codas, 0, include_coda.length);
				codas[include_coda.length] = webxmlPath.removeLastSegments(2).append(containedText);
				include_coda = codas;
			}
		}

		void addPrelude(String containedText) {
			if (containedText.length() > 0) {
				IPath[] preludes = new IPath[include_prelude.length + 1];
				System.arraycopy(include_prelude, 0, preludes, 0, include_prelude.length);
				preludes[include_prelude.length] = webxmlPath.removeLastSegments(2).append(containedText);
				include_prelude = preludes;
			}
		}


		void addUrlPattern(String pattern) {
			if (url_patterns == null) {
				url_patterns = new String[1];
			}
			else {
				String[] patterns = new String[url_patterns.length + 1];
				System.arraycopy(url_patterns, 0, patterns, 0, url_patterns.length);
				url_patterns = patterns;
			}
			url_patterns[url_patterns.length - 1] = pattern;

			if (urlMatchers == null) {
				urlMatchers = new StringMatcher[1];
			}
			else {
				StringMatcher[] matchers = new StringMatcher[urlMatchers.length + 1];
				System.arraycopy(urlMatchers, 0, matchers, 0, urlMatchers.length);
			}
			urlMatchers[urlMatchers.length - 1] = new StringMatcher(pattern);
		}

		public String getId() {
			return id;
		}

		public IPath[] getIncludeCoda() {
			return include_coda;
		}

		public IPath[] getIncludePrelude() {
			return include_prelude;
		}

		public String getPageEncoding() {
			return page_encoding;
		}

		public String[] getUrlPatterns() {
			return url_patterns;
		}

		public boolean isELignored() {
			return el_ignored;
		}

		public boolean isIsXML() {
			return is_xml;
		}

		public boolean isScriptingInvalid() {
			return scripting_invalid;
		}

		boolean matches(String pattern, boolean optimistic) {
			if (urlMatchers == null) {
				return optimistic;
			}
			for (int i = 0; i < urlMatchers.length; i++) {
				if (urlMatchers[i].match(pattern)) {
					return true;
				}
			}
			return false;
		}

		 void setElignored(String el_ignored) {
			this.el_ignored = Boolean.valueOf(el_ignored).booleanValue();
		}

		 void setId(String id) {
			this.id = id;
		}

		 void setIsXML(String is_xml) {
			this.is_xml = Boolean.valueOf(is_xml).booleanValue();
		}

		 void setPageEncoding(String page_encoding) {
			this.page_encoding = page_encoding;
		}

		 void setScriptingInvalid(String scripting_invalid) {
			this.scripting_invalid = Boolean.valueOf(scripting_invalid).booleanValue();
		}

		void setUrlPatterns(String[] url_patterns) {
			this.url_patterns = url_patterns;
			if (url_patterns != null && url_patterns.length > 0) {
				this.urlMatchers = new StringMatcher[url_patterns.length];
				for (int i = 0; i < url_patterns.length; i++) {
					this.urlMatchers[i] = new StringMatcher(url_patterns[i]);
				}
			}
		}

		public String toString() {
			return number + ":" + url_patterns; //$NON-NLS-1$
		}
	}

	static class ResourceDeltaVisitor implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) {
			IResource resource = delta.getResource();
			if (resource.getType() == IResource.FILE) {
				if (delta.getKind() == IResourceDelta.CHANGED && (delta.getFlags() == IResourceDelta.ENCODING || delta.getFlags() == IResourceDelta.MARKERS))
					return false;

				IPath path = resource.getFullPath();
				int segmentCount = path.segmentCount();
				if (segmentCount > 1 && path.lastSegment().equals(WEB_XML) && path.segment(segmentCount - 2).equals(WEB_INF)) {
					getInstance().deploymentDescriptorChanged(path);
				}
			}
			else if (resource.getType() == IResource.PROJECT && (delta.getKind() == IResourceDelta.ADDED || delta.getKind() == IResourceDelta.REMOVED)) {
				String name = resource.getName();
				if (_debugResolutionCache) {
					System.out.println("Removing DeploymentDescriptorPropertyCache resolution cache for project " + name); //$NON-NLS-1$
				}
				synchronized (LOCK) {
					getInstance().invalidate(name);
				}
			}
			return true;
		}
	}

	static class ResourceChangeListener implements IResourceChangeListener {
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			if (event.getType() != IResourceChangeEvent.POST_CHANGE)
				return;
			if (delta.getKind() == IResourceDelta.CHANGED && (delta.getFlags() == IResourceDelta.ENCODING || delta.getFlags() == IResourceDelta.MARKERS))
				return;

			IResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
			try {
				delta.accept(visitor);
			}
			catch (CoreException e) {
				Logger.logException(e);
			}
		}
	}

	private static class ResourceErrorHandler implements ErrorHandler {
		private boolean fDoLogExceptions = false;
		private IPath fPath;

		ResourceErrorHandler(boolean logExceptions) {
			super();
			fDoLogExceptions = logExceptions;
		}

		public void error(SAXParseException exception) throws SAXException {
			if (fDoLogExceptions)
				Logger.log(Logger.WARNING, "SAXParseException with " + fPath + " (error) while reading descriptor: " + exception.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			if (fDoLogExceptions)
				Logger.log(Logger.WARNING, "SAXParseException with " + fPath + " (fatalError) while reading descriptor: " + exception.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		public void setPath(IPath path) {
			fPath = path;
		}

		public void warning(SAXParseException exception) throws SAXException {
			if (fDoLogExceptions)
				Logger.log(Logger.WARNING, "SAXParseException with " + fPath + " (warning) while reading descriptor: " + exception.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	/**
	 * Copied from org.eclipse.core.internal.propertytester.StringMatcher, but
	 * should be replaced with a more accurate implementation of the rules in
	 * Servlet spec SRV.11.2 and RFC 2396
	 */
	private static class StringMatcher {
		private static final char SINGLE_WILD_CARD = '\u0000';

		/**
		 * Boundary value beyond which we don't need to search in the text
		 */
		private int bound = 0;

		private boolean hasLeadingStar;

		private boolean hasTrailingStar;

		final String pattern;

		private final int patternLength;

		/**
		 * The pattern split into segments separated by *
		 */
		private String segments[];

		/**
		 * StringMatcher constructor takes in a String object that is a simple
		 * pattern which may contain '*' for 0 and many characters and '?' for
		 * exactly one character.
		 * 
		 * Literal '*' and '?' characters must be escaped in the pattern e.g.,
		 * "\*" means literal "*", etc.
		 * 
		 * Escaping any other character (including the escape character
		 * itself), just results in that character in the pattern. e.g., "\a"
		 * means "a" and "\\" means "\"
		 * 
		 * If invoking the StringMatcher with string literals in Java, don't
		 * forget escape characters are represented by "\\".
		 * 
		 * @param pattern
		 *            the pattern to match text against
		 */
		StringMatcher(String pattern) {
			if (pattern == null)
				throw new IllegalArgumentException();
			this.pattern = pattern;
			patternLength = pattern.length();
			parseWildCards();
		}

		/**
		 * @param text
		 *            a simple regular expression that may only contain '?'(s)
		 * @param start
		 *            the starting index in the text for search, inclusive
		 * @param end
		 *            the stopping point of search, exclusive
		 * @param p
		 *            a simple regular expression that may contain '?'
		 * @return the starting index in the text of the pattern , or -1 if
		 *         not found
		 */
		private int findPosition(String text, int start, int end, String p) {
			boolean hasWildCard = p.indexOf(SINGLE_WILD_CARD) >= 0;
			int plen = p.length();
			for (int i = start, max = end - plen; i <= max; ++i) {
				if (hasWildCard) {
					if (regExpRegionMatches(text, i, p, 0, plen))
						return i;
				}
				else {
					if (text.regionMatches(true, i, p, 0, plen))
						return i;
				}
			}
			return -1;
		}

		/**
		 * Given the starting (inclusive) and the ending (exclusive) positions
		 * in the <code>text</code>, determine if the given substring matches
		 * with aPattern
		 * 
		 * @return true if the specified portion of the text matches the
		 *         pattern
		 * @param text
		 *            a String object that contains the substring to match
		 */
		public boolean match(String text) {
			if (text == null)
				return false;
			final int end = text.length();
			final int segmentCount = segments.length;
			if (segmentCount == 0 && (hasLeadingStar || hasTrailingStar)) // pattern
				// contains
				// only
				// '*'(s)
				return true;
			if (end == 0)
				return patternLength == 0;
			if (patternLength == 0)
				return false;
			int currentTextPosition = 0;
			if ((end - bound) < 0)
				return false;
			int segmentIndex = 0;
			String current = segments[segmentIndex];

			/* process first segment */
			if (!hasLeadingStar) {
				int currentLength = current.length();
				if (!regExpRegionMatches(text, 0, current, 0, currentLength))
					return false;
				segmentIndex++;
				currentTextPosition = currentTextPosition + currentLength;
			}
			if ((segmentCount == 1) && (!hasLeadingStar) && (!hasTrailingStar)) {
				// only one segment to match, no wild cards specified
				return currentTextPosition == end;
			}
			/* process middle segments */
			while (segmentIndex < segmentCount) {
				current = segments[segmentIndex];
				int currentMatch = findPosition(text, currentTextPosition, end, current);
				if (currentMatch < 0)
					return false;
				currentTextPosition = currentMatch + current.length();
				segmentIndex++;
			}

			/* process final segment */
			if (!hasTrailingStar && currentTextPosition != end) {
				int currentLength = current.length();
				return regExpRegionMatches(text, end - currentLength, current, 0, currentLength);
			}
			return segmentIndex == segmentCount;
		}

		/**
		 * Parses the pattern into segments separated by wildcard '*'
		 * characters.
		 */
		private void parseWildCards() {
			if (pattern.startsWith("*"))//$NON-NLS-1$
				hasLeadingStar = true;
			if (pattern.endsWith("*")) {//$NON-NLS-1$
				/* make sure it's not an escaped wildcard */
				if (patternLength > 1 && pattern.charAt(patternLength - 2) != '\\') {
					hasTrailingStar = true;
				}
			}

			List<String> temp = new ArrayList<>();

			int pos = 0;
			StringBuffer buf = new StringBuffer();
			while (pos < patternLength) {
				char c = pattern.charAt(pos++);
				switch (c) {
					case '\\' :
						if (pos >= patternLength) {
							buf.append(c);
						}
						else {
							char next = pattern.charAt(pos++);
							/* if it's an escape sequence */
							if (next == '*' || next == '?' || next == '\\') {
								buf.append(next);
							}
							else {
								/*
								 * not an escape sequence, just insert
								 * literally
								 */
								buf.append(c);
								buf.append(next);
							}
						}
						break;
					case '*' :
						if (buf.length() > 0) {
							/* new segment */
							temp.add(buf.toString());
							bound += buf.length();
							buf.setLength(0);
						}
						break;
					case '?' :
						/*
						 * append special character representing single match
						 * wildcard
						 */
						buf.append(SINGLE_WILD_CARD);
						break;
					default :
						buf.append(c);
				}
			}

			/* add last buffer to segment list */
			if (buf.length() > 0) {
				temp.add(buf.toString());
				bound += buf.length();
			}
			segments = temp.toArray(new String[temp.size()]);
		}

		/**
		 * 
		 * @return boolean
		 * @param text
		 *            a String to match
		 * @param tStart
		 *            the starting index of match, inclusive
		 * @param p
		 *            a simple regular expression that may contain '?'
		 * @param pStart
		 *            The start position in the pattern
		 * @param plen
		 *            The length of the pattern
		 */
		private boolean regExpRegionMatches(String text, int tStart, String p, int pStart, int plen) {
			while (plen-- > 0) {
				char tchar = text.charAt(tStart++);
				char pchar = p.charAt(pStart++);

				// process wild cards, skipping single wild cards
				if (pchar == SINGLE_WILD_CARD)
					continue;
				if (pchar == tchar)
					continue;
				if (Character.toUpperCase(tchar) == Character.toUpperCase(pchar))
					continue;
				// comparing after converting to upper case doesn't handle all
				// cases;
				// also compare after converting to lower case
				if (Character.toLowerCase(tchar) == Character.toLowerCase(pchar))
					continue;
				return false;
			}
			return true;
		}
		
		public String toString() {
			return "StringMatcher: " + pattern; //$NON-NLS-1$
		}
	}

	private static final DeploymentDescriptorPropertyCache _instance = new DeploymentDescriptorPropertyCache();
	private static final boolean _debugResolutionCache = false;

	// Java Servlet API version
	static final float DEFAULT_WEBAPP_VERSION = 5f; // Jakarta EE 9

	static final String EL_IGNORED = "el-ignored"; //$NON-NLS-1$
	static final String ID = "id"; //$NON-NLS-1$
	static final String INCLUDE_CODA = "include-coda"; //$NON-NLS-1$
	static final String INCLUDE_PRELUDE = "include-prelude"; //$NON-NLS-1$

	static final String IS_XML = "is-xml"; //$NON-NLS-1$
	private static String JSP_PROPERTY_GROUP = "jsp-property-group"; //$NON-NLS-1$
	static final String PAGE_ENCODING = "page-encoding"; //$NON-NLS-1$

	static final String SCRIPTING_INVALID = "scripting-invalid"; //$NON-NLS-1$
	static final String URL_PATTERN = "url-pattern"; //$NON-NLS-1$
	
	private static final String SCHEMA_LOCATION = "xsi:schemaLocation"; //$NON-NLS-1$
	private static final String SERVLET_MAPPING = "servlet-mapping"; //$NON-NLS-1$
	private static final String WEB_APP_ELEMENT_LOCAL_NAME = ":web-app"; //$NON-NLS-1$
	private static final String WEB_APP_ELEMENT_NAME = "web-app"; //$NON-NLS-1$

	private static final String WEB_APP_VERSION_NAME = "version"; //$NON-NLS-1$
	private static final String WEB_INF = "WEB-INF"; //$NON-NLS-1$
	private static final String WEB_XML = "web.xml"; //$NON-NLS-1$
	private static final String SLASH_WEB_INF_WEB_XML = Path.ROOT.toString() + WEB_INF + IPath.SEPARATOR + WEB_XML;

	static String getContainedText(Node parent) {
		NodeList children = parent.getChildNodes();
		if (children.getLength() == 1) {
			return children.item(0).getNodeValue().trim();
		}
		StringBuffer s = new StringBuffer();
		Node child = parent.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
				String reference = ((EntityReference) child).getNodeValue();
				if (reference == null && child.getNodeName() != null) {
					reference = "&" + child.getNodeName() + ";"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				if (reference != null) {
					s.append(reference.trim());
				}
			}
			else {
				s.append(child.getNodeValue().trim());
			}
			child = child.getNextSibling();
		}
		return s.toString().trim();
	}

	static class NoEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicID, String systemID) throws SAXException, IOException {
			InputSource result = new InputSource(new ByteArrayInputStream(new byte[0]));
			result.setPublicId(publicID);
			result.setSystemId(systemID != null ? systemID : "/_" + getClass().getName()); //$NON-NLS-1$
			return result;
		}
	}

	public static DeploymentDescriptorPropertyCache getInstance() {
		return _instance;
	}

	/**
	 * This method is not meant to be called by clients.
	 */
	public static void start() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(getInstance().fResourceChangeListener, IResourceChangeEvent.POST_CHANGE);
	}

	/**
	 * This method is not meant to be called by clients.
	 */
	public static void stop() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(getInstance().fResourceChangeListener);
	}

	private ResourceErrorHandler errorHandler;

	private Map<IPath, Reference<DeploymentDescriptor>> fDeploymentDescriptors = new HashMap<>();

	private IResourceChangeListener fResourceChangeListener = new ResourceChangeListener();

	// for use when reading TLDs
	private EntityResolver resolver;

	/**
	 * Map of project names to a map of resource paths to a deployment
	 * descriptor path. Mostly for caching.
	 */
	Map<String, Map<IPath, IPath>> resolvedMap = new HashMap<>();

	/**
	 * Map of project names to a structure representing the available Servlet API.
	 */
	Map<String, ServletAPIDescriptor> apiVersions = new HashMap<>();

	final static Object LOCK = new Object();

	private DeploymentDescriptorPropertyCache() {
		super();
	}

	private void _parseDocument(IPath path, Float[] version, List<PropertyGroup> groupList, List<StringMatcher> urlPatterns, SubProgressMonitor subMonitor, Document document) {
		Element webapp = document.getDocumentElement();
		if (webapp != null) {
			if (webapp.getTagName().equals(WEB_APP_ELEMENT_NAME) || webapp.getNodeName().endsWith(WEB_APP_ELEMENT_LOCAL_NAME)) {
				// this convention only started with 2.4?
				if (webapp.hasAttribute(WEB_APP_VERSION_NAME)) {
					String versionValue = webapp.getAttribute(WEB_APP_VERSION_NAME);
					versionValue = versionValue.trim();
					if (versionValue.length() > 0) {
						try {
							version[0] = Float.valueOf(versionValue);
						}
						catch (NumberFormatException e) {
							// doesn't matter
						}
					}
				}
				if (version[0] == null) {
					// try determining from schema declarations
					String schemaLocations = webapp.getAttribute(SCHEMA_LOCATION);
					if (schemaLocations != null && schemaLocations.length() > 0) {
						if (schemaLocations.contains("/web-app_5_0.xsd")) {
							version[0] = new Float(5);
						}
						/**
						 * Technically there's a 4.0.3, but treat it the same
						 */
						else if (schemaLocations.contains("/web-app_4_0.xsd")) {
							version[0] = new Float(4);
						}
						else if (schemaLocations.contains("/web-app_3_1.xsd")) {
							version[0] = new Float(3.1);
						}
						else if (schemaLocations.contains("/web-app_3_0.xsd")) {
							version[0] = new Float(3);
						}
					}
				}
				if (version[0] == null) {
					// try determining the version from the doctype reference
					DocumentType doctype = document.getDoctype();
					if (doctype != null) {
						String systemId = doctype.getSystemId();
						String publicId = doctype.getPublicId();
						if ((systemId != null && systemId.endsWith("web-app_2_3.dtd")) || (publicId != null && publicId.indexOf("Web Application 2.3") > 0)) { //$NON-NLS-1$ //$NON-NLS-2$
							version[0] = new Float(2.3);
						}
						else if ((systemId != null && systemId.endsWith("web-app_2_2.dtd")) || (publicId != null && publicId.indexOf("Web Application 2.2") > 0)) { //$NON-NLS-1$ //$NON-NLS-2$
							version[0] = new Float(2.2);
						}
						else if ((systemId != null && systemId.endsWith("web-app_2_1.dtd")) || (publicId != null && publicId.indexOf("Web Application 2.1") > 0)) { //$NON-NLS-1$ //$NON-NLS-2$
							version[0] = new Float(2.1);
						}
					}
				}
			}
		}
		if (version[0] == null) {
			version[0] = new Float(DEFAULT_WEBAPP_VERSION);
		}

		NodeList propertyGroupElements = document.getElementsByTagName(JSP_PROPERTY_GROUP);
		int length = propertyGroupElements.getLength();
		subMonitor.beginTask("Reading Property Groups", length); //$NON-NLS-1$
		for (int i = 0; i < length; i++) {
			PropertyGroup group = PropertyGroup.createFrom(path, propertyGroupElements.item(i), i);
			subMonitor.worked(1);
			if (group != null) {
				groupList.add(group);
			}
		}
		
		// 325554 : only apply to URL patterns for Servlet mappings
		NodeList urlPatternElements = document.getElementsByTagName(URL_PATTERN);
		int urlPatternElementCount = urlPatternElements.getLength();
		for (int i = 0; i < urlPatternElementCount; i++) {
			Node urlPatternElement = urlPatternElements.item(i);
			if (SERVLET_MAPPING.equals(urlPatternElement.getParentNode().getNodeName())) {
				String urlPattern = getContainedText(urlPatternElement);
				if (urlPattern != null && urlPattern.length() > 0) {
					urlPatterns.add(new StringMatcher(urlPattern));
				}
			}
		}
	}

	/**
	 * Convert the SRV spec version to the JSP spec version
	 */
	private float convertSpecVersions(float version) {
		if (version > 0) {
			if (version == 5f)
				return 3f;
			if (version == 4f)
				return 2.3f;
			if (version == 3.1f)
				return 2.3f;
			if (version == 3f)
				return 2.2f;
			if (version == 2.5f)
				return 2.1f;
			else if (version == 2.4f)
				return 2.0f;
			else if (version == 2.3f)
				return 1.2f;
			else if (version == 2.2f)
				return 1.1f;
			else if (version == 2.1f)
				return 1.0f;
		}
		return convertSpecVersions(DEFAULT_WEBAPP_VERSION);
	}

	void deploymentDescriptorChanged(final IPath fullPath) {
		if (fDeploymentDescriptors.containsKey(fullPath.makeAbsolute())) {
			updateCacheEntry(fullPath);
		}
	}

	/**
	 * @param project
	 * @return Descriptor for the Servlet API version found on the project's Java
	 *         Build Path, <code>null</code> if none was discoverable.
	 */
	private ServletAPIDescriptor discoverServletAPIVersion(IProject project) {
		IJavaProject javaProject = JavaCore.create(project);
		if (!javaProject.exists()) {
			return null;
		}
		try {
			if (javaProject.findType("jakarta.servlet.GenericFilter") != null) {
				return new ServletAPIDescriptor("jakarta.servlet", 5);
			}
			if (javaProject.findType("javax.servlet.GenericFilter") != null) {
				return new ServletAPIDescriptor("javax.servlet", 4);
			}
			if (javaProject.findType("javax.servlet.ReadListener") != null) {
				return new ServletAPIDescriptor("javax.servlet", 3.1f);
			}
			if (javaProject.findType("javax.servlet.SessionCookieConfig") != null) {
				return new ServletAPIDescriptor("javax.servlet", 3);
			}
			IType servletRequestType = javaProject.findType("javax.servlet.http.HttpServletRequest");
			if (servletRequestType != null) {
				IMethod[] methods = servletRequestType.getMethods();
				for (int i = 0; i < methods.length; i++) {
					if ("getContextPath".equals(methods[i].getElementName())) {
						return new ServletAPIDescriptor("javax.servlet", 2.5f);
					}
				}
			}
			if (javaProject.findType("javax.servlet.ServletRequestAttributeEvent") != null) {
				return new ServletAPIDescriptor("javax.servlet", 2.4f);
			}
			if (javaProject.findType("javax.servlet.Filter") != null) {
				return new ServletAPIDescriptor("javax.servlet", 2.3f);
			}
		}
		catch (JavaModelException e) {
			Logger.logException(e);
		}
		return null;
	}

	/**
	 * parse the specified resource using Xerces, and if that fails, use the
	 * SSE XML parser to find the property groups.
	 */
	private DeploymentDescriptor fetchDescriptor(IPath path, IProgressMonitor monitor) {
		monitor.beginTask(Messages.DeploymentDescriptorPropertyCache_1, 3);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		PropertyGroup groups[] = null;

		IStructuredModel model = null;
		List<PropertyGroup> propertyGroupList = new ArrayList<>();
		List<StringMatcher> urlPatterns = new ArrayList<>();
		Float[] version = new Float[1];
		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 2);
		DocumentBuilder builder = CommonXML.getDocumentBuilder(false);
		builder.setEntityResolver(getEntityNonResolver());
		builder.setErrorHandler(getErrorHandler(path));
		try {
			InputSource inputSource = new InputSource();
			String s = FileContentCache.getInstance().getContents(path);
			if (s != null) {
				inputSource.setCharacterStream(new StringReader(s));
				inputSource.setSystemId(path.toString());
				Document document = builder.parse(inputSource);
				_parseDocument(path, version, propertyGroupList, urlPatterns, subMonitor, document);
			}
		}
		catch (SAXException e1) {
			/* encountered a fatal parsing error, try our own parser */
			try {
				/**
				 * Chiefly because the web.xml file itself is editable, use
				 * SSE to get the DOM Document because it is more fault
				 * tolerant.
				 */
				model = StructuredModelManager.getModelManager().getModelForRead(file);
				monitor.worked(1);
				if (model instanceof IDOMModel) {
					IDOMDocument document = ((IDOMModel) model).getDocument();
					_parseDocument(path, version, propertyGroupList, urlPatterns, subMonitor, document);
				}
			}
			catch (Exception e) {
				Logger.logException(e);
			}
			finally {
				if (model != null) {
					model.releaseFromRead();
				}
			}
		}
		catch (IOException e1) {
			/* file is unreadable, create no property groups */
		}
		finally {
			groups = propertyGroupList.toArray(new PropertyGroup[propertyGroupList.size()]);
			subMonitor.done();
		}

		if (groups == null) {
			groups = NO_PROPERTY_GROUPS;
		}

		DeploymentDescriptor deploymentDescriptor = new DeploymentDescriptor();
		deploymentDescriptor.modificationStamp = file.getModificationStamp();
		deploymentDescriptor.groups = groups;
		deploymentDescriptor.urlPatterns = (urlPatterns.toArray(new StringMatcher[urlPatterns.size()]));
		deploymentDescriptor.version = version[0];
		monitor.done();
		fDeploymentDescriptors.put(path, new SoftReference<>(deploymentDescriptor));
		return deploymentDescriptor;
	}

	private DeploymentDescriptor getCachedDescriptor(IPath jspFilePath) {
		IPath webxmlPath = getRelevantWebXMLPath(jspFilePath);
		if (webxmlPath == null)
			return null;

		IFile webxmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(webxmlPath);
		if (!webxmlFile.isAccessible())
			return null;

		Reference<DeploymentDescriptor> descriptorHolder = fDeploymentDescriptors.get(webxmlPath);
		DeploymentDescriptor descriptor = null;

		if (descriptorHolder == null || ((descriptor = descriptorHolder.get()) == null) || (descriptor.modificationStamp == IResource.NULL_STAMP) || (descriptor.modificationStamp != webxmlFile.getModificationStamp())) {
			descriptor = fetchDescriptor(webxmlPath, new NullProgressMonitor());
		}
		return descriptor;
	}

	/**
	 * Returns an EntityResolver that will not retrieve external resources
	 */
	private EntityResolver getEntityNonResolver() {
		if (resolver == null) {
			resolver = new NoEntityResolver();
		}
		return resolver;
	}

	/**
	 * Returns an ErrorHandler that will not stop the parser on reported
	 * errors
	 */
	private ErrorHandler getErrorHandler(IPath path) {
		if (errorHandler == null) {
			errorHandler = new ResourceErrorHandler(false);
		}
		errorHandler.setPath(path);
		return errorHandler;
	}

	/**
	 * @param fullPath
	 * @return the JSP version supported by the web application containing the
	 *         path. A value stated within a web.xml file takes priority.
	 */
	public float getJSPVersion(IPath fullPath) {
		float version = DEFAULT_WEBAPP_VERSION;
		/* try applicable web.xml file first */
		DeploymentDescriptor descriptor = getCachedDescriptor(fullPath);
		if (descriptor != null && descriptor.version != null) {
			version = descriptor.version.floatValue();
			return convertSpecVersions(version);
		}

		/* check facet settings */
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(fullPath.segment(0));
		float facetVersion = FacetModuleCoreSupport.getDynamicWebProjectVersion(project);

		if (facetVersion > 0) {
			// use it if set
			version = facetVersion;
		}
		else {
			// try to detect from classpath
			ServletAPIDescriptor detected = getServletAPIVersion(project);
			if (detected != null) {
				return convertSpecVersions(detected.getAPIversion());
			}
		}

		return convertSpecVersions(version);
	}

	/**
	 * @param jspFilePath
	 * @return PropertyGroups matching the file at the given path or an empty
	 *         array if no web.xml file exists or no matching property group
	 *         was defined. A returned PropertyGroup object should be
	 *         considered short-lived and not saved for later use.
	 */
	public PropertyGroup[] getPropertyGroups(IPath jspFilePath) {
		List<PropertyGroup> matchingGroups = new ArrayList<>(1);
		DeploymentDescriptor descriptor = getCachedDescriptor(jspFilePath);
		if (descriptor == null)
			return NO_PROPERTY_GROUPS;

		for (int i = 0; i < descriptor.groups.length; i++) {
			if (descriptor.groups[i].matches(FacetModuleCoreSupport.getRuntimePath(jspFilePath).toString(), false)) {
				matchingGroups.add(descriptor.groups[i]);
			}
		}
		if (matchingGroups.isEmpty()) {
			for (int i = 0; i < descriptor.groups.length; i++) {
				if (descriptor.groups[i].matches(FacetModuleCoreSupport.getRuntimePath(jspFilePath).toString(), true)) {
					matchingGroups.add(descriptor.groups[i]);
				}
			}
		}
		return matchingGroups.toArray(new PropertyGroup[matchingGroups.size()]);
	}

	/**
	 * @param project
	 * @return Descriptor for the Servlet API version found on the project's Java
	 *         Build Path, <code>null</code> if none was discoverable.
	 */
	public ServletAPIDescriptor getServletAPIVersion(IProject project) {
		ServletAPIDescriptor descriptor = apiVersions.get(project.getName());
		if (descriptor == null) {
			descriptor = discoverServletAPIVersion(project);
			if (descriptor != null) {
				apiVersions.put(project.getName(), descriptor);
			}
			else {
				apiVersions.put(project.getName(), ServletAPIDescriptor.DEFAULT);
				descriptor = ServletAPIDescriptor.DEFAULT;
			}
		}
		return descriptor;
	}

	/**
	 * @param fullPath
	 *            the full path of the JSP file
	 * @param reference
	 *            a path reference to test for
	 * @return a matching url-mapping value in the corresponding deployment
	 *         descriptor for the given JSP file path, if a deployment
	 *         descriptor could be found, <code>null</code> otherwise
	 */
	public String getURLMapping(IPath fullPath, String reference) {
		DeploymentDescriptor descriptor = getCachedDescriptor(fullPath);
		if (descriptor == null)
			return null;
		StringMatcher[] mappings = descriptor.urlPatterns;
		for (int i = 0; i < mappings.length; i++) {
			if (mappings[i].match(reference)) {
				return mappings[i].pattern;
			}
		}
		return null;
	}
	
	private IPath getRelevantWebXMLPath(IPath fullPath) {
		IPath resolved = null;
		Map<IPath, IPath> mapForProject = null;
		synchronized (LOCK) {
			mapForProject = resolvedMap.get(fullPath.segment(0));
			if (mapForProject != null) {
				resolved = mapForProject.get(fullPath);
			}
			else {
				mapForProject = new HashMap<IPath, IPath>();
				resolvedMap.put(fullPath.segment(0), mapForProject);
			}
		}
		if (resolved != null) {
			if (_debugResolutionCache) {
				System.out.println("DeploymentDescriptorPropertyCache resolution cache hit for " + fullPath); //$NON-NLS-1$ 
			}
		}
		else {
			if (_debugResolutionCache) {
				System.out.println("DeploymentDescriptorPropertyCache resolution cache miss for " + fullPath); //$NON-NLS-1$ 
			}
			resolved = FacetModuleCoreSupport.resolve(fullPath, SLASH_WEB_INF_WEB_XML);
			mapForProject.put(fullPath, resolved);
		}
		return resolved;
	}

	/**
	 * Find the web.xml file that applies for the given path. It can take the
	 * better part of a full second to calculate this depending on the project
	 * layout and metamodels not under our control, so cache the result.
	 *
	 * @param fullPath
	 *            - the full path of the resource
	 * @return the IFile representing the relevant deployment descriptor, or
	 *         <code>null</code> if there isn't one.
	 */
	public IFile getWebXML(IPath fullPath) {
		IPath webxmlPath = getRelevantWebXMLPath(fullPath);
		if (webxmlPath == null)
			return null;

		return ResourcesPlugin.getWorkspace().getRoot().getFile(webxmlPath);
	}

	public void invalidate(String name) {
		getInstance().resolvedMap.remove(name);
		getInstance().apiVersions.remove(name);
	}
	
	private void updateCacheEntry(IPath fullPath) {
		/* don't update right now; remove and wait for another query to do that work */
		fDeploymentDescriptors.remove(fullPath);
	}
}
