/*******************************************************************************
 * Copyright (c) 2004, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Frits Jalvingh - contributions for bug 150794
 *******************************************************************************/

package org.eclipse.jst.jsp.core.internal.parser.internal;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jst.jsp.core.internal.Logger;
import org.eclipse.jst.jsp.core.internal.contenttype.IntStack;
import org.eclipse.jst.jsp.core.internal.regions.DOMJSPRegionContexts;
import org.eclipse.wst.sse.core.internal.ltk.parser.BlockMarker;
import org.eclipse.wst.sse.core.internal.ltk.parser.BlockTokenizer;
import org.eclipse.wst.sse.core.internal.ltk.parser.TagMarker;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.core.internal.util.Debug;
import org.eclipse.wst.sse.core.utils.StringUtils;
import org.eclipse.wst.xml.core.internal.parser.ContextRegionContainer;

/**
 * Generate with -skel skeleton-jsp.sse
 */
%%

%{
	private int fTokenCount = 0;
 
	// required holders for white-space compacting
	private boolean fShouldLoadBuffered = false;
	private String fBufferedContext = null;
	private int fBufferedStart = 1;
	private int fBufferedLength = 0;
	private ITextRegion fBufferedEmbeddedContainer = null;
	private ITextRegion fProxyUnknownRegion = null;
	private String f_context = null;

	// state stack for handling embedded regions
	private IntStack fStateStack = new IntStack();
	// a "hint" as to what an embedded region should be evaluated
	private String fEmbeddedHint = UNDEFINED;
	// a "hint" as to what state to enter once an embedded region has
	//   been completed
	private int fEmbeddedPostState = YYINITIAL;
	
	// the container used to create embedded regions
	private ContextRegionContainer fEmbeddedContainer = null;
	private static final String PROXY_CONTEXT = "PROXY_CONTEXT";
	private static final String PROXY_UNKNOWN_CONTEXT = "PROXY_UNKNOWN_CONTEXT";

	private String context = null;
	private int start = 0;
	private int textLength = 0;
	private int length = 0;

	// offset for tracking position specific block tags
	private int fOffset = 0;
	
	// the name of the current tag being opened
	private String fCurrentTagName = null;

	// the name of the current tag inside of an embedded region
	private String internalTagName = null;
	private String internalContext = null;

	// the list of tag name BlockMarkers
	private List fBlockMarkers = new ArrayList(0);
	private List fNestablePrefixes = new ArrayList(1);
	
	// where the last internal container block was found
	private int fLastInternalBlockStart = -1;

	// required to not seek text blocks on an end tag
	private boolean fIsBlockingEnabled = false;
	private boolean fIsCaseSensitiveBlocking = true;

	private static final boolean fForbidJSP = false;
	
	private int fELlevel = 0;

	private JSPParserRegionFactory fRegionFactory = new JSPParserRegionFactory();

	// Is the embedded tag a JSP tag
	private boolean fEmbeddedTag = false;
	// Is the non-embedded tag a JSP tag
	private boolean fContainerTag = false;
	// Is the tokenizer in a non-embedded tag (between < and >)
	private boolean fInTagContainer = false;
	// Is the tokenizer in an embedded tag (between < and >)
	private boolean fInTagEmbedded = false;

	/**
	 * user method 
	 */
	public final void addBlockMarker(BlockMarker marker) {
		if(containsTagName(marker.getTagName()))
			return;
		fBlockMarkers.add(marker);
	}
	/**
	 * user method
	 */
	public final void addNestablePrefix(TagMarker marker) {
		fNestablePrefixes.add(marker);
	}
	/* user method */
	public List getNestablePrefixes() {
		return fNestablePrefixes;
	}
	/**
	 * user method
	 */
	private boolean isNestable(String tagName) {
		//Iterator blocks = fNestablePrefixes.iterator();
		//while(blocks.hasNext()) {
		//	TagMarker marker = (TagMarker)blocks.next();
		//	String markerName = marker.getTagName();
		//	if(tagName.length() > markerName.length() + 1 && tagName.startsWith(markerName) && tagName.charAt(markerName.length()) == ':') {
		//		return marker.isGlobal() || getOffset() >= marker.getMarker().getStart();
		//	}
		//}
		//return false;
		return true;
	}
	/**
	 * user method 
	 */
	public final void removeNestablePrefix(String name) {
		if (fNestablePrefixes != null) {
			Iterator nestables = fNestablePrefixes.iterator();
			while (nestables.hasNext()) {
				if (((TagMarker) nestables.next()).getTagName().equalsIgnoreCase(name))
					nestables.remove();
			}
		}
	}
	/**
	 * user method 
	 */
	public final void removeBlockMarker(BlockMarker marker) {
		fBlockMarkers.remove(marker);
	}
	/**
	 * user method 
	 */
	public final void removeBlockMarker(String tagname) {
		if (fBlockMarkers != null) {
			Iterator blocks = fBlockMarkers.iterator();
			while (blocks.hasNext()) {
				if (((BlockMarker) blocks.next()).getTagName().equals(tagname))
					blocks.remove();
			}
		}
	}
	/* user method */
	private final void assembleEmbeddedTagSequence(String startType, String endTagName) {
		assembleEmbeddedContainer(startType, null, endTagName);
	}
	/* user method */
	private final void assembleEmbeddedContainer(String startType, String[] endTypes) {
		assembleEmbeddedContainer(startType, endTypes, null);
	}
	/* user method */
	private final void assembleEmbeddedContainer(String startType, String endType) {
		assembleEmbeddedContainer(startType, new String[]{endType}, null);
	}
	/**
	 *  user method 
	 * 
	 * Assembles an embedded container beginning with the given startType as
	 * the first ContextRegion within it and of the type fEmbeddedHint.  The
	 * endTypes[] array contains the context types that will cause a successful
	 * exit.  Use of the endTagName parameter alters this behavior to force an
	 * exit on an XML_TAG_CLOSE after seeing an XML_TAG_NAME whose significant
	 * text matches the endTagName String.  All contents in between are
	 * insignificant, and yes, this means comments are allowed inside.
	 **/
	private final void assembleEmbeddedContainer(String startType, String[] endTypes, String endTagName) {
		// the context of the region being added to the embedded container
		internalContext = startType;
		// keep track of where this container began; to provide relative indeces for the regions
		int containerStart = yychar;
		boolean notFinished = true;
		// keep track of where we seem to be so that the endTagName can be checked
		boolean isInEndTag = false;
		boolean isInFirstTag = true;
		// create the embedded container and setup its "type"
		if (fEmbeddedContainer == null) {
			fEmbeddedContainer = new ContextRegionContainer();
			fEmbeddedContainer.setType(fEmbeddedHint);
			fEmbeddedContainer.setStart(containerStart);
			// TODO: parent region needs to be set .... but not sure where to get it from 
			//		fEmbeddedContainer.setParent(parentRegion);
		}
		int initialLength = fEmbeddedContainer.getRegions().size();
		containerStart = fEmbeddedContainer.getStart();
		while (notFinished) {
			// add the region to the container
			if (internalContext != null && internalContext != PROXY_CONTEXT) {
				ITextRegion newToken = fRegionFactory.createToken(internalContext, yychar - containerStart, yylength(), yylength());
				fEmbeddedContainer.getRegions().add(newToken);
				fEmbeddedContainer.setLength(fEmbeddedContainer.getLength() + yylength());
				fEmbeddedContainer.setTextLength(fEmbeddedContainer.getTextLength() + yylength());
				// DW, 4/16/2003 token regions no longer have parents
				//newToken.setParent(fEmbeddedContainer);
			}
			try {
				// longscan determines whether to attempt a blockTagScan within the embedded container
				boolean longscan = false;
				// save the tokenizer state in case of a block tag scan
				int previousState = yystate();
				String previousCurrentTagName = fCurrentTagName;
				int previousPostState = fEmbeddedPostState;
				String previousEmbeddedHint = fEmbeddedHint;
				// determine if a block tag scan is necessary
				if (internalContext == XML_TAG_NAME) {
					internalTagName = yytext();
					if (endTagName != null && endTagName.length() == 0){
						endTagName = internalTagName;
					}
					if(!isNestable(internalTagName)) {
						internalTagName = null;
						// snagged a tag name we shouldn't have
						fEmbeddedPostState = ST_ABORT_EMBEDDED;
						notFinished = false;
					}
				}
				else if (internalContext == XML_TAG_OPEN || internalContext == XML_END_TAG_OPEN) {
					internalTagName = null;
				}
				// do upkeep for endTagName usage; must be here since the next token could be the close
				if (internalContext == XML_END_TAG_OPEN) {
					isInEndTag = true;
				} else if (internalContext == XML_TAG_CLOSE) {
					isInFirstTag = isInEndTag = false;
				} else {
				 	ITextRegionList embeddedRegions = fEmbeddedContainer.getRegions();
					if (embeddedRegions.size() > 2 && (embeddedRegions.get(embeddedRegions.size()-1)).getType() == XML_TAG_CLOSE && (embeddedRegions.get(embeddedRegions.size() - 3)).getType() == XML_TAG_OPEN && internalTagName != null) {
						if (containsTagName(internalTagName)) {
							longscan = true;
							yybegin(ST_BLOCK_TAG_SCAN);
						}
					}
				}
				if (longscan)
					fCurrentTagName = internalTagName;
				// read the next region and context
				internalContext = primGetNextToken();
				if (longscan) {
					// Returning from a block tag scan requires restoring some state variables
					// as well as handling the block region and setting up for normal scanning
					// inside the embedded container
					ITextRegion newToken = fRegionFactory.createToken(internalContext, yychar - containerStart, yylength(), yylength());
					fEmbeddedContainer.getRegions().add(newToken);
					fEmbeddedContainer.setLength(fEmbeddedContainer.getLength() + yylength());
					fEmbeddedContainer.setTextLength(fEmbeddedContainer.getTextLength() + yylength());
					// DW, 4/16/2003 token regions no longer have parents
					// newToken.setParent(fEmbeddedContainer);
					longscan = false;
					fEmbeddedPostState = previousPostState;
					fEmbeddedHint = previousEmbeddedHint;
					fCurrentTagName = previousCurrentTagName;
					yybegin(previousState);
					internalContext = primGetNextToken();
				}
			} catch (IOException e) {
				// primGetNextToken() calls may throw an IOException
				// catch and do nothing since the isEOF check below
				// will properly exit if the input was too short
			} catch (Exception f) {
				// some other exception happened; never should
				Logger.logException(f);
			}
			boolean isEndingType = yystate() == ST_ABORT_EMBEDDED;
			ITextRegionList embeddedList = fEmbeddedContainer.getRegions();
			if(!isEndingType) {
				// check for ending context
				if (endTagName == null) {
					for (int i = 0; i < endTypes.length; i++) {
						isEndingType = isEndingType || (internalContext == endTypes[i]) || (embeddedList.size() - initialLength) >= 2 && (embeddedList.get(embeddedList.size()-1)).getType() == endTypes[i];
					}
				}
				else {
					isEndingType = ((isInEndTag && internalContext == XML_TAG_CLOSE) || (isInFirstTag && internalContext == XML_EMPTY_TAG_CLOSE)) && internalTagName != null && internalTagName.equals(endTagName);
				}
			}
			notFinished = notFinished && ((!isEndingType) && !isEOF() && (endTagName != null || internalContext != UNDEFINED) && !(internalContext == PROXY_CONTEXT && (embeddedList.get(embeddedList.size()-1)).getType() == UNDEFINED));
		}
		// finish adding the last context
		if (internalContext != null && internalContext != PROXY_CONTEXT) {
			ITextRegion newToken = fRegionFactory.createToken(internalContext, yychar - containerStart, yylength(), yylength());
			fEmbeddedContainer.getRegions().add(newToken);
			// DW, 4/16/2003 token regions no longer have parents
			//newToken.setParent(fEmbeddedContainer);
			fEmbeddedContainer.setLength(yychar - containerStart + yylength());
			fEmbeddedContainer.setTextLength(yychar - containerStart + yylength());
		}
		yybegin(fEmbeddedPostState);
	}
	/* user method */
	public final boolean isCaseSensitiveBlocking() {
		return fIsCaseSensitiveBlocking;
	}
	/* user method */
	public final void setCaseSensitiveBlocking(boolean newValue) {
		fIsCaseSensitiveBlocking = newValue;
	}
	/* user method */
	public boolean getBlockMarkerAllowsJSP() {
		return getBlockMarkerAllowsJSP(fCurrentTagName);
	}
	/* user method */
	public boolean getBlockMarkerAllowsJSP(String name) {
		Iterator iterator = fBlockMarkers.iterator();
		while(iterator.hasNext()) {
			BlockMarker marker = (BlockMarker)iterator.next();
			boolean casesensitive = marker.isCaseSensitive();
			if(casesensitive && marker.getTagName().equals(name))
				return marker.allowsJSP();
			else if(!casesensitive && marker.getTagName().equalsIgnoreCase(name))
				return marker.allowsJSP();
		}
		return true;
	}
	/* user method */
	public boolean getBlockMarkerCaseSensitivity() {
		return getBlockMarkerCaseSensitivity(fCurrentTagName);
	}
	public boolean getBlockMarkerCaseSensitivity(String name) {
		Iterator iterator = fBlockMarkers.iterator();
		while(iterator.hasNext()) {
			BlockMarker marker = (BlockMarker)iterator.next();
			boolean casesensitive = marker.isCaseSensitive();
			if(casesensitive && marker.getTagName().equals(name))
				return casesensitive;
			else if(!casesensitive && marker.getTagName().equalsIgnoreCase(name))
				return casesensitive;
		}
		return true;
	}
	/* user method */
	public String getBlockMarkerContext() {
		return getBlockMarkerContext(fCurrentTagName);
	}
	/* user method */
	public String getBlockMarkerContext(String name) {
		Iterator iterator = fBlockMarkers.iterator();
		while(iterator.hasNext()) {
			BlockMarker marker = (BlockMarker)iterator.next();
			if(marker.getTagName().equals(name))
				return marker.getContext();
		}
		return BLOCK_TEXT;
	}
	/* user method */
	public List getBlockMarkers() {
		return fBlockMarkers;
	}
	/* user method */
	public final int getOffset() {
		return fOffset + yychar;
	}
	private final boolean isBlockMarker() {
		return isBlockMarker(fCurrentTagName);
	}
	private final boolean isBlockMarker(String tagName) {
		if (!fIsBlockingEnabled)
			return false;
		return containsTagName(tagName);
	}
	/**
	 * user method
	 */
	public final void beginBlockTagScan(String newTagName) {
		beginBlockMarkerScan(newTagName, BLOCK_TEXT);
	}
	/**
	 * user method
	 *
	 * Special tokenizer setup.  Allows tokenization to be initiated at the
	 * start of a text block within a "newTagName" tag.
	 *
	 * Example: 
	 *	Tokenizer toker = new Tokenizer();
	 *	toker.setCaseSensitiveBlocking(false);
	 *	toker.reset(new java.io.StringReader("afiuhqwkejhtasihgalkwhtq</scripter></scr></script>asgdasga"));
	 *	toker.beginBlockMarkerScan("script", BLOCK_TEXT);
	 *	toker.getRegions(); 
	 *
	 * Returns:
	 *	BLOCK_TEXT: 0-40
	 *	XML_END_TAG_OPEN: 41-42
	 *	XML_TAG_NAME: 43-48
	 *	XML_TAG_CLOSE: 49-49
	 *	XML_CONTENT: 50-57
	 *
	 */
	public final void beginBlockMarkerScan(String newTagName, String blockcontext) {
		yybegin(ST_BLOCK_TAG_SCAN);
		fCurrentTagName = newTagName;
	}

/**
 * Method doScan.
 * 
 * Returns a context region for all of the text from the current position upto the end of input or
 * to right *before* the first occurence of searchString
 * 
 * @param searchString - target string to search for ex.: "-->", "</tagname"
 * @param requireTailSeparator - whether the target must be immediately followed by whitespace or '>'
 * @param allowJSP - check for and allow for JSP markup <%%>
 * @param context - the context of the scanned region if non-zero length
 * @param exitState - the state to go to if the region was of non-zero length
 * @param abortState - the state to go to if the searchString was found immediately
 * @return String - the context found: the desired context on a non-zero length match, the abortContext on immediate success
 * @throws IOException
 */
private final String doScan(String searchString, boolean requireTailSeparator, boolean allowJSP, boolean allowCDATA, String searchContext, int exitState, int immediateFallbackState) throws IOException {
	boolean stillSearching = true;
	boolean wasBlockingEnabled = fIsBlockingEnabled;
	try {
		// Disable further block (probably)
		fIsBlockingEnabled = false;
		int searchStringLength = searchString.length();
		int n = 0;
		char lastCheckChar;
		int i;
		boolean same = false;
		// Check for JSP starts ("<%") if the tag is global like SCRIPT or STYLE
		boolean checkJSPs = allowJSP && !fForbidJSP;
		boolean checkedForJSPsOnce = !checkJSPs;
		boolean checkedJSPsAtStartOnce = false;
		
		while (stillSearching) {
			n = 0;
			// Ensure that enough data from the input exists to compare against the search String.
			n = yy_advance();
			while(n != YYEOF && yy_currentPos < searchStringLength)
				n = yy_advance();
	//		c = (char) n;
			// If the input was too short or we've exhausted the input, stop immediately.
			if (n == YYEOF && checkedForJSPsOnce) {
				stillSearching = false;
			}
			else {
				/**
				 * Look for starting JSPs "<%"
				 */
				checkedForJSPsOnce = true;
				// 1) yy_currentPos - searchStringLength : There's at least searchStringLength of input available; once that's read, check for JSPs
				// ---
				// Look for a JSP beginning at current-searchStringLength; if so, backup and switch scanner states to handle it.
				// Ensure that we've not encountered a complete block (<%%>) that was *shorter* than the closeTagString and
				// thus found twice at current-targetLength [since the first scan would have come out this far anyway].
				if(checkJSPs && yy_currentPos > searchStringLength && yy_currentPos - searchStringLength != fLastInternalBlockStart && yy_currentPos - searchStringLength > yy_startRead &&
					yy_buffer[yy_currentPos - searchStringLength] == '<' && yy_buffer[yy_currentPos - searchStringLength + 1] == '%') {
					fLastInternalBlockStart = yy_markedPos = yy_currentPos - searchStringLength;
					yy_currentPos = yy_markedPos + 1;
					int resumeState = yystate();
					yybegin(ST_BLOCK_TAG_INTERNAL_SCAN);
					if(yy_markedPos == yy_startRead) {
						String jspContext = primGetNextToken();
						yybegin(resumeState);
						return jspContext;
					}
					return searchContext;
				}
				// 2) yy_currentPos - jspstarter.length : There's not searchStringLength of input available; check for a JSP 2 spots back in what we could read
				// ---
				// Look for a JSP beginning at the current position; this case wouldn't be handled by the preceding section
				// since it relies upon *having* closeTagStringLength amount of input to work as designed.  Must be sure we don't
				// spill over the end of the buffer while checking.
				else if(checkJSPs && yy_startRead != fLastInternalBlockStart && yy_currentPos > 0 && yy_currentPos < yy_buffer.length - 1 &&
						yy_buffer[yy_currentPos - 1] == '<' && yy_buffer[yy_currentPos] == '%') {
					fLastInternalBlockStart = yy_markedPos = yy_currentPos - 1;
					yy_currentPos = yy_markedPos + 1;
					int resumeState = yystate();
					yybegin(ST_BLOCK_TAG_INTERNAL_SCAN);
					if(yy_markedPos == yy_startRead) {
						String jspContext = primGetNextToken();
						yybegin(resumeState);
						return jspContext;
					}
					return searchContext;
				}
				// 3) yy_currentPos..(yy_currentPos+jspStartlength-1) : Check at the start of the block one time
				// ---
				// Look for a JSP beginning immediately in the block area; this case wouldn't be handled by the preceding section
				// since it relies upon yy_currentPos equaling exactly the previous end +1 to work as designed.
				else if(checkJSPs && !checkedJSPsAtStartOnce && yy_startRead != fLastInternalBlockStart && yy_startRead > 0 &&
						yy_startRead < yy_buffer.length - 1 && yy_buffer[yy_startRead] == '<' && yy_buffer[yy_startRead + 1] == '%') {
					checkedJSPsAtStartOnce = true;
					fLastInternalBlockStart = yy_markedPos = yy_startRead;
					yy_currentPos = yy_markedPos + 1;
					int resumeState = yystate();
					yybegin(ST_BLOCK_TAG_INTERNAL_SCAN);
					if(yy_markedPos == yy_startRead) {
						String jspContext = primGetNextToken();
						yybegin(resumeState);
						return jspContext;
					}
					return searchContext;
				}
	
	
				/**
				 * Look for starting CDATA "<![CDATA["
				 */
				// 1) yy_currentPos - searchStringLength: There's at least searchStringLength of input available; once that's read, check for CDATA
				// ---
				// Look for a CDATA beginning at current-searchStringLength; if so, backup and switch scanner states to handle it.
				// Ensure that we've not encountered a complete block (<[!CDATA[]]>) that was *shorter* than the closeTagString and
				// thus found twice at current-targetLength [since the first scan would have come out this far anyway].
	/*			if(checkCDATA && yy_currentPos > searchStringLength && yy_currentPos + searchStringLength < yy_buffer.length && yy_currentPos - searchStringLength != fLastInternalBlockStart && 
					charsMatch(cdataStarter, yy_buffer, 0, yy_currentPos - searchStringLength)) {
					fLastInternalBlockStart = yy_markedPos = yy_currentPos - searchStringLength;
					yy_currentPos = yy_markedPos + 1;
					int resumeState = yystate();
					// go to a state where CDATA can be found
					if (fEmbeddedContainer == null) {
						fEmbeddedContainer = new ContextRegionContainer();
						fEmbeddedContainer.setType(searchContext);
						fEmbeddedContainer.setStart(yychar);
					}
					ITextRegion newToken = fRegionFactory.createToken(searchContext, yychar, yylength(), yylength());
					fEmbeddedContainer.getRegions().add(newToken);
					fEmbeddedContainer.setLength(fEmbeddedContainer.getLength() + yylength());
					fEmbeddedContainer.setTextLength(fEmbeddedContainer.getTextLength() + yylength());
					yybegin(YYINITIAL);
					String context = primGetNextToken();
					if(context.equals(XMLRegionContexts.XML_CDATA_OPEN)) {
						assembleEmbeddedContainer(XMLRegionContexts.XML_CDATA_OPEN, XMLRegionContexts.XML_CDATA_CLOSE);
					}
					yybegin(resumeState);
					return searchContext;
				}
	*//*
				// 2) yy_currentPos - cdataStarter.length: There's not searchStringLength of input available; check for a CDATA right here spots back in what we could read
				// ---
				// Look for a JSP beginning at the current position; this case wouldn't be handled by the preceding section
				// since it relies upon *having* closeTagStringLength amount of input to work as designed.  Must be sure we don't
				// spill over the end of the buffer while checking.
				else if(checkCDATA && yy_startRead != fLastInternalBlockStart && yy_currentPos > 0 && yy_currentPos < yy_buffer.length - 1 &&
						yy_buffer[yy_currentPos - 1] == '<' && yy_buffer[yy_currentPos] == '%') {
					fLastInternalBlockStart = yy_markedPos = yy_currentPos - 1;
					yy_currentPos = yy_markedPos + 1;
					int resumeState = yystate();
					yybegin(ST_BLOCK_TAG_INTERNAL_SCAN);
					if(yy_markedPos == yy_startRead) {
						String jspContext = primGetNextToken();
						yybegin(resumeState);
						return jspContext;
					}
					return searchContext;
				}
				// 3) yy_currentPos : Check at the start of the block one time
				// ---
				// Look for a JSP beginning immediately in the block area; this case wouldn't be handled by the preceding section
				// since it relies upon yy_currentPos equaling exactly the previous end +1 to work as designed.
				else if(checkCDATA && !checkedForCDATAOnce && yy_startRead != fLastInternalBlockStart && yy_startRead > 0 &&
						yy_startRead < yy_buffer.length - 1 && yy_buffer[yy_startRead] == '<' && yy_buffer[yy_startRead + 1] == '%') {
					checkedForCDATAOnce = true;
					fLastInternalBlockStart = yy_markedPos = yy_startRead;
					yy_currentPos = yy_markedPos + 1;
					int resumeState = yystate();
					yybegin(ST_BLOCK_TAG_INTERNAL_SCAN);
					if(yy_markedPos == yy_startRead) {
						String jspContext = primGetNextToken();
						yybegin(resumeState);
						return jspContext;
					}
					return searchContext;
				}
	*/
				// Check the characters in the target versus the last targetLength characters read from the buffer
				// and see if it matches
				if (n == YYEOF) {
					stillSearching = false;
				}
				else {
					same = true;
					// safety check for array accesses
					if(yy_currentPos >= searchStringLength && yy_currentPos <= yy_buffer.length) {
						for(i = 0; i < searchStringLength && same; i++) {
							if(fIsCaseSensitiveBlocking)
								same = yy_buffer[i + yy_currentPos - searchStringLength] == searchString.charAt(i);
							else
								same = Character.toLowerCase(yy_buffer[i + yy_currentPos - searchStringLength]) == Character.toLowerCase(searchString.charAt(i));
						}
					}
					// safety check failed; no match is possible right now
					else {
						same = false;
					}
				}
				if (same && requireTailSeparator && yy_currentPos < yy_buffer.length) {
					// Additional check for close tags to ensure that targetString="</script" doesn't match
					// "</scriptS"
					lastCheckChar = yy_buffer[yy_currentPos];
					// Succeed on "</script>" and "</script "
					if(lastCheckChar == '>' || Character.isWhitespace(lastCheckChar))
						stillSearching = false;
				}
				else {
					stillSearching = !same || (yy_currentPos < yy_startRead + searchStringLength);
				}
			}
		}
		if (n != YYEOF || same) {
			// We've stopped short of the end or definitely found a match
			yy_markedPos = yy_currentPos - searchStringLength;
			yy_currentPos = yy_markedPos + 1;
			// If the searchString occurs at the very beginning of what would have
			// been a Block, resume scanning normally immediately
			if (yy_markedPos == yy_startRead) {
				yybegin(immediateFallbackState);
				return primGetNextToken();
			}
		}
		else {
			// We ran through the rest of the input
			yy_markedPos = yy_currentPos;
			yy_currentPos++;
		}
		yybegin(exitState);
		// If the ending occurs at the very beginning of what would have
		// been a Block, resume scanning normally immediately
		if(yy_markedPos == yy_startRead)
			return primGetNextToken();
		return searchContext;
	}
	finally {
		fIsBlockingEnabled = wasBlockingEnabled;
	}
}
/**
 * user method 
 * does a lookahead for the current tag name
 */
private final String doBlockTagScan() throws IOException {
	fIsCaseSensitiveBlocking = getBlockMarkerCaseSensitivity();
	return doScan("</" + fCurrentTagName, true, getBlockMarkerAllowsJSP(), true, getBlockMarkerContext(fCurrentTagName), YYINITIAL, YYINITIAL);
}
	/**
	 * user method
	 *
	 * Converts the raw context String returned by the primGetNextToken()
	 * method into a full ITextRegion by pulling in values for the
	 * current offset within the scanning text.
	 *
	 * Returns null when EOF is encountered and attaches intermittently
	 * discovered whitespace onto the end of useful regions.
	 *
	 * Note that this algorithm caches the token following the one being returned
	 * so that whitespace can be collapsed.
	 */
	public final ITextRegion getNextToken() throws IOException {
		fEmbeddedContainer = null;
		// load the starting non-whitespace token (assume that it is so)
		if (fShouldLoadBuffered) {
			if (fBufferedEmbeddedContainer != null) {
				ITextRegion container = fBufferedEmbeddedContainer;
				fBufferedEmbeddedContainer = null;
				fShouldLoadBuffered = false;
				return container;
			}
			context = fBufferedContext;
			start = fBufferedStart;
			textLength = length = fBufferedLength;
			fShouldLoadBuffered = false;
		} else {
			context = primGetNextToken();
			if (context == PROXY_CONTEXT) {
				return fEmbeddedContainer;
			} else if (context == XML_TAG_NAME || f_context == JSP_ROOT_TAG_NAME || f_context == JSP_DIRECTIVE_NAME) {
				if(containsTagName(yy_buffer, yy_startRead, yy_markedPos-yy_startRead))
					fCurrentTagName = yytext();
				else
					fCurrentTagName = null;
			} else if (context == XML_TAG_OPEN) {
				fIsBlockingEnabled = true;
			} else if (context == XML_END_TAG_OPEN) {
				fIsBlockingEnabled = false;
			}
			start = yychar;
			textLength = length = yylength();
			if (yy_atEOF) {
				fTokenCount++;
				return null;
			}
		}
		// store the next token
		f_context = primGetNextToken();
		if (f_context == PROXY_CONTEXT) {
			fBufferedEmbeddedContainer = fEmbeddedContainer;
			fShouldLoadBuffered = true;
		} else if (f_context == XML_TAG_NAME || f_context == JSP_ROOT_TAG_NAME || f_context == JSP_DIRECTIVE_NAME) {
			if(containsTagName(yy_buffer, yy_startRead, yy_markedPos-yy_startRead))
				fCurrentTagName = yytext();
			else
				fCurrentTagName = null;
		} else if (f_context == XML_TAG_OPEN) {
			fIsBlockingEnabled = true;
		} else if (f_context == XML_END_TAG_OPEN) {
			fIsBlockingEnabled = false;
		} else if (f_context == PROXY_UNKNOWN_CONTEXT) {
			fBufferedEmbeddedContainer = fProxyUnknownRegion;
		}
		fBufferedContext = f_context;
		fBufferedStart = yychar;
		fBufferedLength = yylength();
		fShouldLoadBuffered = true;
		if (fBufferedContext == WHITE_SPACE) {
			fShouldLoadBuffered = false;
			length += fBufferedLength;
		}
		if (context == null) {
			// EOF
			if (Debug.debugTokenizer) {
				System.out.println(getClass().getName() + " discovered " + fTokenCount + " tokens."); //$NON-NLS-2$//$NON-NLS-1$
			}
			return null;
		}
		fTokenCount++;
		return fRegionFactory.createToken(context, start, textLength, length, null, fCurrentTagName);
	}
	/* user method */
	public JSPTokenizer(){
		super();
	}
	/* user method */
	public JSPTokenizer(char[] charArray){
			this(new CharArrayReader(charArray));
	}
	/* user method */
	public void reset(char[] charArray) {
		reset(new CharArrayReader(charArray), 0);
	}
	/* user method */
	public void reset(char[] charArray, int newOffset) {
		reset(new CharArrayReader(charArray), newOffset);
	}
	/* user method */
	public void reset(java.io.InputStream in) {
		reset(new java.io.InputStreamReader(in), 0);
	}
	/* user method */
	public void reset(java.io.InputStream in, int newOffset) {
		reset(new java.io.InputStreamReader(in), newOffset);
	}
	/* user method */
	public void reset(java.io.Reader in) {
		reset(in, 0);
	}
	/**
	 * user method *
	 *
	 * Reset internal counters and vars to "newly created" values, in the hopes
	 * that resetting a pre-existing tokenizer is faster than creating a new one.
	 *
	 * This method contains code blocks that were essentially duplicated from the
	 * <em>generated</em> output of this specification before this method was
	 * added.  Those code blocks were under the above copyright.
	 */
	public void reset(java.io.Reader in, int newOffset) {
		if (Debug.debugTokenizer) {
			System.out.println("resetting tokenizer");//$NON-NLS-1$
		}
		fOffset = newOffset;
	
		/* the input device */
		yy_reader = in;
	
		/* the current state of the DFA */
		yy_state = 0;
	
		/* the current lexical state */
		yy_lexical_state = YYINITIAL;
	
		/* this buffer contains the current text to be matched and is
		the source of the yytext() string */
		java.util.Arrays.fill(yy_buffer, (char)0);
	
		/* the textposition at the last accepting state */
		yy_markedPos = 0;
	
		/* the textposition at the last state to be included in yytext */
		yy_pushbackPos = 0;
	
		/* the current text position in the buffer */
		yy_currentPos = 0;
	
		/* startRead marks the beginning of the yytext() string in the buffer */
		yy_startRead = 0;
	
		/** 
		 * endRead marks the last character in the buffer, that has been read
		 * from input 
		 */
		yy_endRead = 0;
	
		/* number of newlines encountered up to the start of the matched text */
		//yyline = 0;
	
		/* the number of characters up to the start of the matched text */
		yychar = 0;
	
		/* yy_atEOF == true <=> the scanner has returned a value for EOF */
		yy_atEOF = false;
	
		/* denotes if the user-EOF-code has already been executed */
		yy_eof_done = false;
	
	
		/* user vars: */
		fTokenCount = 0;
	 
		fShouldLoadBuffered = false;
		fBufferedContext = null;
		fBufferedStart = 1;
		fBufferedLength = 0;
		fStateStack = new IntStack();
	
		fLastInternalBlockStart = -1;
	
		context = null;
		start = 0;
		textLength = 0;
		length = 0;
	
		fEmbeddedContainer = null;
		
		fELlevel = 0;
	}
	/**
	 * user method
	 *
	 */
	public BlockTokenizer newInstance() {
		JSPTokenizer newInstance = new JSPTokenizer();
		// global tagmarkers can be shared; they have no state and 
		// are never destroyed (e.g. 'release')
		for(int i = 0; i < fBlockMarkers.size(); i++) {
			BlockMarker blockMarker = (BlockMarker) fBlockMarkers.get(i);
			if(blockMarker.isGlobal())
				newInstance.addBlockMarker(blockMarker);
		}
		for(int i = 0; i < fNestablePrefixes.size(); i++) {
			TagMarker marker = (TagMarker) fNestablePrefixes.get(i);
			if(marker.isGlobal())
				newInstance.addNestablePrefix(marker);
		}
		return newInstance;
	}
	/* user method */
	private final String scanXMLCommentText() throws IOException {
		// Scan for '-->' and return the text up to that point as
		//   XML_COMMENT_TEXT unless the string occurs IMMEDIATELY, in which
		//  case change to the ST_XML_COMMENT_END state and return the next
		//  context as usual.
		return doScan("-->", false, true, true, XML_COMMENT_TEXT, ST_XML_COMMENT_END, ST_XML_COMMENT_END);
	}
	/* user method */
	private final String scanJSPCommentText() throws IOException {
		// Scan for '--%>' and return the text up to that point as
		//   JSP_COMMENT_TEXT unless the string occurs IMMEDIATELY, in which
		//  case change to the ST_JSP_COMMENT_END state and return the next
		//  context as usual.
		return doScan("--%>", false, false, true, JSP_COMMENT_TEXT, ST_JSP_COMMENT_END, ST_JSP_COMMENT_END);
	}
	
	/* user method */
	private boolean isJspTag() {
		return (fContainerTag && fEmbeddedContainer != null) || (fContainerTag && fInTagContainer) || (fEmbeddedTag && fInTagEmbedded);
	}
%}

%eof{
// do nothing, this is the downstream parser's job
%eof}

%public
%class JSPTokenizer
%implements BlockTokenizer, DOMJSPRegionContexts
%function primGetNextToken
%type String
%char
%unicode
%pack

%state ST_CDATA_TEXT
%state ST_CDATA_END
%state ST_XML_COMMENT
%state ST_XML_COMMENT_END
%state ST_PI
%state ST_PI_WS
%state ST_PI_CONTENT
%state ST_XML_PI_ATTRIBUTE_NAME
%state ST_XML_PI_EQUALS
%state ST_XML_PI_ATTRIBUTE_VALUE
%state ST_XML_PI_TAG_CLOSE
%state ST_DHTML_ATTRIBUTE_NAME
%state ST_DHTML_EQUALS
%state ST_DHTML_ATTRIBUTE_VALUE
%state ST_DHTML_TAG_CLOSE

// scriptlet state(s)
%state ST_JSP_CONTENT
%state ST_JSP_DIRECTIVE_NAME
%state ST_JSP_DIRECTIVE_NAME_WHITESPACE
%state ST_JSP_DIRECTIVE_ATTRIBUTE_NAME
%state ST_JSP_DIRECTIVE_EQUALS
%state ST_JSP_DIRECTIVE_ATTRIBUTE_VALUE

// normal tag states
%state ST_XML_TAG_NAME
%state ST_XML_ATTRIBUTE_NAME
%state ST_XML_EQUALS
%state ST_XML_ATTRIBUTE_VALUE

// declaration (DTD) states
%state ST_XML_DECLARATION
%state ST_XML_DECLARATION_CLOSE

%state ST_XML_DOCTYPE_DECLARATION
%state ST_XML_DOCTYPE_EXTERNAL_ID
%state ST_XML_DOCTYPE_ID_PUBLIC
%state ST_XML_DOCTYPE_ID_SYSTEM

%state ST_XML_ELEMENT_DECLARATION
%state ST_XML_ELEMENT_DECLARATION_CONTENT

%state ST_XML_ATTLIST_DECLARATION
%state ST_XML_ATTLIST_DECLARATION_CONTENT


%state ST_BLOCK_TAG_SCAN
%state ST_BLOCK_TAG_INTERNAL_SCAN

%state ST_JSP_COMMENT
%state ST_JSP_COMMENT_END

%state ST_JSP_ATTRIBUTE_VALUE
%state ST_XML_ATTRIBUTE_VALUE_SQUOTED
%state ST_XML_ATTRIBUTE_VALUE_DQUOTED

%state ST_ABORT_EMBEDDED

%state ST_JSP_EL
%state ST_JSP_EL_SQUOTES
%state ST_JSP_EL_DQUOTES
%state ST_JSP_EL_SQUOTES_END
%state ST_JSP_EL_DQUOTES_END

%state ST_JSP_DQUOTED_EL
%state ST_JSP_SQUOTED_EL

%state ST_JSP_VBL
%state ST_JSP_VBL_SQUOTES
%state ST_JSP_VBL_DQUOTES
%state ST_JSP_VBL_SQUOTES_END
%state ST_JSP_VBL_DQUOTES_END

%state ST_JSP_DQUOTED_VBL
%state ST_JSP_SQUOTED_VBL

// Letter = ([A-Za-z])
// Digit = ([0-9])

/**
 * smaller tokens
 */
genericTagOpen       = <
genericTagClose      = >
genericEndTagOpen    = <\/
genericEmptyTagClose = \/>

PIstart = <\?
PIend   = \?>


// [1] document ::= prolog element Misc*
document = ({prolog} {element} {Misc}*)

// [2] Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
 //Char = (.)
Char = [\x09\x0A\x0D\x20-\uD7FF\uE000-\uFFFD] 

// [3] S ::= (0x20 | 0x9 | 0xD | 0xA)+
S = [\x20\x09\x0D\x0A]+

// [4] NameChar ::= Letter | Digit | '.' | '-' | '_' | ':' | CombiningChar | Extender
NameChar = ({Letter} | {Digit} | \. | \- | _ | : | {CombiningChar} | {Extender} | \[ | \] | \( | \) | # | \*)

// [5] Name ::= (Letter | '_' | ':') NameChar*
//Name = ({NameChar}{NameChar}*)
Name = ({Letter} | _ | :){NameChar}*

// [6] Names ::= {Name} ({S} {Name})*
Names = ({Name} ({S} {Name})*)

// [7] Nmtoken ::= (NameChar)+
Nmtoken = ({NameChar}+)

// [8] Nmtokens ::= Nmtoken (S Nmtoken)*
Nmtokens = ({Nmtoken} ({S} {Nmtoken})*)

// [9] EntityValue ::= '"' ([^%&"] | PEReference | Reference)* '"' |  "'" ([^%&'] | PEReference | Reference)* "'"
EntityValue = (\" ([^%&\"] | {PEReference} | {Reference})* \" |  \' ([^%&\'] | {PEReference} | {Reference})* \')

// \x24 = '$', \x7b = '{', \x23 = '#'
// [10] AttValue ::= '"' ([^<&"] | Reference)* '"' |  "'" ([^<&'] | Reference)* "'"
AttValue = ( \"([^<"\x24\x23] | [\x24\x23][^\x7b\x3c"] | \\[\x24\x23][\x7b] | {Reference})* [\x24\x23]*\" | \'([^<'\x24\x23] | [\x24\x23][^\x7b\x3c'] | \\[\x24\x23][\x7b] | {Reference})*[\x24\x23]*\'  | ([^\'\"\040\011\012\015<>/]|\/+[^\'\"\040\011\012\015<>/] )*)

// As Attvalue, but accepts escaped versions of the lead-in quote also
QuotedAttValue = ( \"([^<"\x24\x23] | [\x24\x23][^\x7b"] | \\[\x24\x23][\x7b] | \\\" | {Reference})*[\x24\x23]*\" | \'([^<'\x24\x23] | [\x24\x23][^\x7b'] | \\[\x24\x23][\x7b] | \\\' | {Reference})*[\x24\x23]*\'  | ([^\'\"\040\011\012\015<>/]|\/+[^\'\"\040\011\012\015<>/] )*)

// [11] SystemLiteral ::= ('"' [^"]* '"') | ("'" [^']* "'") 
SystemLiteral = ((\" [^\"]* \") | (\' [^\']* \')) 

// [12] PubidLiteral ::= '"' PubidChar* '"' | "'" (PubidChar - "'")* "'"
PubidLiteral = (\" {PubidChar}* \" | \' ({PubidChar}\')* "'")

// [13] PubidChar ::= #x20 | #xD | #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]
PubidChar = ([\040\015\012] | [a-zA-Z0-9] | [\-\'()\+,.\/:=?;!\*#@\$_%])

// [14] CharData ::= [^<&]* - ([^<&]* ']]>' [^<&]*)
// implement lookahead behavior during action definition
CharData = ([^<&(\]\]>)]*)

// [15] Comment ::= '<!--' ((Char - '-') | ('-' (Char - '-')))* '-->'
CommentStart = (<!\-\-)
CommentEnd   = (\-\->)
Comment = ({CommentStart}.*{CommentEnd})

// [16] PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'
PI = (<\?{PITarget} {Char}* \?>)

// [17] PITarget ::= Name - (('X' | 'x') ('M' | 'm') ('L' | 'l'))
PITarget = ({Name}((X|x)(M|m)(L|l)))

// [18] CDSect ::= CDStart CData CDEnd
CDSect = ({CDStart}{CData}{CDEnd})

// [19] CDStart ::= '<![CDATA['
CDStart = <!\[CDATA\[

// [20] CData ::= (Char* - (Char* ']]>' Char*)) 
// implement lookahead behavior during action definition
CData = ([^(\]\]>)]*)

// [21] CDEnd ::= ']]>'
CDEnd = (\]\]>)

// [22] prolog ::= XMLDecl? Misc* (doctypedecl Misc*)?
prolog = ({XMLDecl}? {Misc}* ({doctypedecl} {Misc}*)?)

// [23] XMLDecl ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
XMLDecl = (<\?xml {VersionInfo} {EncodingDecl}? {SDDecl}? {S}? \?>)

// [24] VersionInfo ::= S 'version' Eq (' VersionNum ' | " VersionNum ")
VersionInfo = ({S}version{Eq}(\'{VersionNum}\' | \"{VersionNum}\"))

// [25] Eq ::= S? '=' S?
Eq = (\=)

// [26] VersionNum ::= ([a-zA-Z0-9_.:] | '-')+
VersionNum = (([a-zA-Z0-9_.:]|\-)+)

// [27] Misc ::= Comment | PI |  S
Misc = ({Comment} | {PI} | {S})

// [28] doctypedecl ::= '<!DOCTYPE' S Name (S ExternalID)?  S? ('[' (markupdecl | PEReference | S)* ']' S?)? '>'
doctypedecl = (<\!DOCTYPE{S}{Name} ({S}{ExternalID})? {S}? (\[ ({markupdecl}|{PEReference}|{S})* \]{S}?)?>)

// [29] markupdecl ::= elementdecl | AttlistDecl | EntityDecl | NotationDecl | PI | Comment
markupdecl = ({elementdecl} | {AttlistDecl} | {EntityDecl} | {NotationDecl} | {PI} | {Comment})

// [30] extSubset ::= TextDecl? extSubsetDecl
extSubset = ({TextDecl}? {extSubsetDecl})

// [31] extSubsetDecl ::= ( markupdecl | conditionalSect | PEReference | S )*
extSubsetDecl = (( {markupdecl} | {conditionalSect} | {PEReference} | {S} )*)

// [32]  SDDecl  ::= S 'standalone' Eq (("'" ('yes' | 'no') "'") | ('"' ('yes' | 'no') '"'))
SDDecl  = ({S}standalone{Eq}{S}*((\'(yes|no)\')|(\"(yes|no)\")))

// [33]  LanguageID  ::= Langcode ('-' Subcode)*
LanguageID  = ({Langcode}(\-{Subcode})*)

// [34]  Langcode ::= ISO639Code |  IanaCode |  UserCode
Langcode = ({ISO639Code} |  {IanaCode} |  {UserCode})

// [35]  ISO639Code ::= ([a-z] | [A-Z]) ([a-z] | [A-Z])
ISO639Code = (([a-z]|[A-Z])([a-z]|[A-Z]))

// [36]  IanaCode ::= ('i' | 'I') '-' ([a-z] | [A-Z])+
IanaCode = ((i|I)\-([a-z]|[A-Z])+)

// [37]  UserCode ::= ('x' | 'X') '-' ([a-z] | [A-Z])+
UserCode = ((x|X)\-([a-z]|[A-Z])+)

// [38]  Subcode ::= ([a-z] | [A-Z])+
Subcode = (([a-z]|[A-Z])+)

// [39]  element  ::= EmptyElemTag | STag content ETag
element  = ({EmptyElemTag} | {STag} {content} {ETag})

// [40]  STag  ::= '<' Name (S Attribute)* S? '>'
STag = (<{Name}({S}{Attribute})*{S}?>)

// [41]  Attribute ::= Name Eq AttValue
Attribute = ({Name}{S}*{Eq}{S}*{AttValue})

// [42]  ETag  ::= 'Name S? '>'
ETag = (<\/{Name}{S}?>)

// [43]  content  ::= (element | CharData | Reference | CDSect | PI | Comment)*
content = (({element} | {CharData} | {Reference} | {CDSect} | {PI} | {Comment})*)

// [44]  EmptyElemTag  ::= '<' Name (S Attribute)* S? '/>'
EmptyElemTag = (<{Name}({S}{Attribute})*{S}?\/>)

// [45] elementdecl ::= '<!ELEMENT' S Name S contentspec S? '>'
elementdecl = (<\!ELEMENT{S}{Name}{S}{contentspec}{S}?>)

// [46] contentspec ::= 'EMPTY' | 'ANY' | Mixed | children
contentspec = (EMPTY|ANY|{Mixed}|{children})

// [47] children ::= (choice | seq) ('?' | '*' | '+')?
children = (({choice}|{seq})(\?|\*|\+)?)

// CAUSES LOOP THROUGH DEFS OF CHOICE AND SEQ
// [48] cp ::= (Name | choice | seq) ('?' | '*' | '+')?
cp = (({Name} | {choice} | {seq}) (\?|\*|\+)?)

// [49] choice ::= '(' S? cp ( S? '|' S? cp )* S? ')'
// choice = \({S}?{cp}({S}?\|{S}?{cp})*{S}?\)
choice = \({S}?{Name}({S}?\|{S}?{Name})*{S}?\)

// [50] seq ::= '(' S? cp ( S? ',' S? cp )* S? ')'
// seq = (\({S}?{cp}({S}?\,{S}?{cp})*{S}?\))
seq = (\({S}?{Name}({S}?\,{S}?{Name})*{S}?\))

// [51] Mixed ::= '(' S? '#PCDATA' (S? '|' S? Name)* S?  ')*' | '(' S? '#PCDATA' S? ')'
Mixed = ({S}?\#PCDATA({S}?\|{S}?{Name})*{S}?)*\|({S}?\#PCDATA{S}?)

// [52] AttlistDecl ::= '<!ATTLIST' S Name AttDef* S? '>'
AttlistDecl = (<\!ATTLIST{S}{Name}{AttDef}*{S}?>)

// [53] AttDef ::= S Name S AttType S DefaultDecl
AttDef = ({S}{Name}{S}{AttType}{S}{DefaultDecl})

// [54] AttType ::= StringType | TokenizedType | EnumeratedType 
AttType = ({StringType} | {TokenizedType} | {EnumeratedType})

// [55] StringType ::= 'CDATA'
StringType = (CDATA)

// [56] TokenizedType ::= 'ID' | 'IDREF' | 'IDREFS' | 'ENTITY' | 'ENTITIES' | 'NMTOKEN' | 'NMTOKENS'
TokenizedType = (ID|IDREF|IDREFS|ENTITY|ENTITIES|NMTOKEN|NMTOKENS)

// [57] EnumeratedType ::= NotationType | Enumeration 
EnumeratedType = ({NotationType} | {Enumeration})

// [58] NotationType ::= 'NOTATION' S '(' S? Name (S? '|' S? Name)* S? ')' 
NotationType = (NOTATION{S}\({S}?{Name}({S}?\|{S}?{Name})*{S}?\))

// [59] Enumeration ::= '(' S? Nmtoken (S? '|' S?  Nmtoken)* S? ')'
Enumeration = (\({S}?{Nmtoken}({S}?\|{S}?{Nmtoken})*{S}?\))

// [60] DefaultDecl ::= '#REQUIRED' | '#IMPLIED' | (('#FIXED' S)? AttValue)
DefaultDecl = (\#REQUIRED|\#IMPLIED|((\#FIXED{S})?{AttValue}))

// [61] conditionalSect ::= includeSect | ignoreSect 
conditionalSect = ({includeSect} | {ignoreSect})

// [62] includeSect ::= '<![' S? 'INCLUDE' S? '[' extSubsetDecl ']]>' 
includeSect = (<\!\[{S}?INCLUDE{S}?\[{extSubsetDecl}\]\]>)

// [63] ignoreSect ::= '<![' S? 'IGNORE' S? '[' ignoreSectContents* ']]>'
ignoreSect = (<\!\[{S}?IGNORE{S}?\[{ignoreSectContents}*\]\]>)

// [64] ignoreSectContents ::= Ignore ('<![' ignoreSectContents ']]>' Ignore)*
ignoreSectContents = ({Ignore}(<\!\[{ignoreSectContents}\]\]>{Ignore})*)

// [65] Ignore ::= Char* - (Char* ('<![' | ']]>') Char*)
Ignore =  ([^(\<\!\[|\]\]\>)]*)

// [66] CharRef ::= '&#' [0-9]+ ';' | '&#x' [0-9a-fA-F]+ ';'
CharRef = (&#[0-9]+;|&#x[0-9a-fA-F]+;)

// [67] Reference ::= EntityRef | CharRef
Reference = ({EntityRef} | {CharRef})

// [68] EntityRef = '&' Name ';'
EntityRef = (&{Name};)

// [69] PEReference ::= '%' Name ';'
PEReference = (%{Name};)

// [70] EntityDecl ::= GEDecl | PEDecl
EntityDecl = ({GEDecl} | {PEDecl})

// [71] GEDecl ::= '<!ENTITY' S Name S EntityDef S? '>'
GEDecl = (<\!ENTITY{S}{Name}{S}{EntityDef}{S}?>)

// [72] PEDecl ::= '<!ENTITY' S '%' S Name S PEDef S? '>'
PEDecl = (<\!ENTITY{S}\%{S}{Name}{S}{PEDef}{S}?>)

// [73] EntityDef ::= EntityValue | (ExternalID NDataDecl?)
EntityDef = ({EntityValue} | ({ExternalID}{NDataDecl}?))

// [74] PEDef ::= EntityValue | ExternalID
PEDef = ({EntityValue} | {ExternalID})

// [75] ExternalID ::= 'SYSTEM' S SystemLiteral | 'PUBLIC' S PubidLiteral S SystemLiteral 
ExternalID = (SYSTEM{S}{SystemLiteral}|PUBLIC{S}{PubidLiteral}{S}{SystemLiteral} )

// [76] NDataDecl ::= S 'NDATA' S Name
NDataDecl = ({S}NDATA{S}{Name})

// [77] TextDecl ::= '<?xml' VersionInfo? EncodingDecl S? '?>'
TextDecl = (<\?xml{VersionInfo}?{EncodingDecl}{S}?\?>)

// [78] extParsedEnt ::= TextDecl? content
extParsedEnt = ({TextDecl}?{content})

// [79] extPE ::= TextDecl? extSubsetDecl
extPE = ({TextDecl}?{extSubsetDecl})

// [80] EncodingDecl ::= S 'encoding' Eq ('"' EncName '"' |  "'" EncName "'" ) 
EncodingDecl = ({S}encoding{S}*{Eq}{S}*(\"{EncName}\"|\'{EncName}\'))

// [81] EncName ::= [A-Za-z] ([A-Za-z0-9._] | '-')*
EncName = ([A-Za-z]([A-Za-z0-9._]|\-)*)

// [82] NotationDecl ::= '<!NOTATION' S Name S (ExternalID |  PublicID) S? '>'
NotationDecl = (<\!NOTATION{S}{Name}{S}({ExternalID}|{PublicID}){S}?>)

// [83] PublicID ::= 'PUBLIC' S PubidLiteral
PublicID = (PUBLIC{S}{PubidLiteral})

// [84]  Letter ::= BaseChar | Ideographic
Letter = ({BaseChar} | {Ideographic})

// [85]  BaseChar ::= [#x0041-#x005A] | [#x0061-#x007A] | [#x00C0-#x00D6]
// | [#x00D8-#x00F6] | [#x00F8-#x00FF] | [#x0100-#x0131]
// | [#x0134-#x013E] | [#x0141-#x0148] | [#x014A-#x017E]
// | [#x0180-#x01C3] | [#x01CD-#x01F0] | [#x01F4-#x01F5]
// | [#x01FA-#x0217] | [#x0250-#x02A8] | [#x02BB-#x02C1] | #x0386
// | [#x0388-#x038A] | #x038C | [#x038E-#x03A1] | [#x03A3-#x03CE]
// | [#x03D0-#x03D6] | #x03DA | #x03DC | #x03DE | #x03E0
// | [#x03E2-#x03F3] | [#x0401-#x040C] | [#x040E-#x044F]
// | [#x0451-#x045C] | [#x045E-#x0481] | [#x0490-#x04C4]
// | [#x04C7-#x04C8] | [#x04CB-#x04CC] | [#x04D0-#x04EB]
// | [#x04EE-#x04F5] | [#x04F8-#x04F9] | [#x0531-#x0556] | #x0559
// | [#x0561-#x0586] | [#x05D0-#x05EA] | [#x05F0-#x05F2]
// | [#x0621-#x063A] | [#x0641-#x064A] | [#x0671-#x06B7]
// | [#x06BA-#x06BE] | [#x06C0-#x06CE] | [#x06D0-#x06D3] | #x06D5
// | [#x06E5-#x06E6] | [#x0905-#x0939] | #x093D | [#x0958-#x0961]
// | [#x0985-#x098C] | [#x098F-#x0990] | [#x0993-#x09A8]
// | [#x09AA-#x09B0] | #x09B2 | [#x09B6-#x09B9] | [#x09DC-#x09DD]
// | [#x09DF-#x09E1] | [#x09F0-#x09F1] | [#x0A05-#x0A0A]
// | [#x0A0F-#x0A10] | [#x0A13-#x0A28] | [#x0A2A-#x0A30]
// | [#x0A32-#x0A33] | [#x0A35-#x0A36] | [#x0A38-#x0A39]
// | [#x0A59-#x0A5C] | #x0A5E | [#x0A72-#x0A74] | [#x0A85-#x0A8B]
// | #x0A8D | [#x0A8F-#x0A91] | [#x0A93-#x0AA8] | [#x0AAA-#x0AB0]
// | [#x0AB2-#x0AB3] | [#x0AB5-#x0AB9] | #x0ABD | #x0AE0
// | [#x0B05-#x0B0C] | [#x0B0F-#x0B10] | [#x0B13-#x0B28]
// | [#x0B2A-#x0B30] | [#x0B32-#x0B33] | [#x0B36-#x0B39] | #x0B3D
// | [#x0B5C-#x0B5D] | [#x0B5F-#x0B61] | [#x0B85-#x0B8A]
// | [#x0B8E-#x0B90] | [#x0B92-#x0B95] | [#x0B99-#x0B9A] | #x0B9C
// | [#x0B9E-#x0B9F] | [#x0BA3-#x0BA4] | [#x0BA8-#x0BAA]
// | [#x0BAE-#x0BB5] | [#x0BB7-#x0BB9] | [#x0C05-#x0C0C]
// | [#x0C0E-#x0C10] | [#x0C12-#x0C28] | [#x0C2A-#x0C33]
// | [#x0C35-#x0C39] | [#x0C60-#x0C61] | [#x0C85-#x0C8C]
// | [#x0C8E-#x0C90] | [#x0C92-#x0CA8] | [#x0CAA-#x0CB3]
// | [#x0CB5-#x0CB9] | #x0CDE | [#x0CE0-#x0CE1] | [#x0D05-#x0D0C]
// | [#x0D0E-#x0D10] | [#x0D12-#x0D28] | [#x0D2A-#x0D39]
// | [#x0D60-#x0D61] | [#x0E01-#x0E2E] | #x0E30 | [#x0E32-#x0E33]
// | [#x0E40-#x0E45] | [#x0E81-#x0E82] | #x0E84 | [#x0E87-#x0E88]
// | #x0E8A | #x0E8D | [#x0E94-#x0E97] | [#x0E99-#x0E9F]
// | [#x0EA1-#x0EA3] | #x0EA5 | #x0EA7 | [#x0EAA-#x0EAB]
// | [#x0EAD-#x0EAE] | #x0EB0 | [#x0EB2-#x0EB3] | #x0EBD
// | [#x0EC0-#x0EC4] | [#x0F40-#x0F47] | [#x0F49-#x0F69]
// | [#x10A0-#x10C5] | [#x10D0-#x10F6] | #x1100 | [#x1102-#x1103]
// | [#x1105-#x1107] | #x1109 | [#x110B-#x110C] | [#x110E-#x1112]
// | #x113C | #x113E | #x1140 | #x114C | #x114E | #x1150
// | [#x1154-#x1155] | #x1159 | [#x115F-#x1161] | #x1163 | #x1165
// | #x1167 | #x1169 | [#x116D-#x116E] | [#x1172-#x1173] | #x1175
// | #x119E | #x11A8 | #x11AB | [#x11AE-#x11AF] | [#x11B7-#x11B8]
// | #x11BA | [#x11BC-#x11C2] | #x11EB | #x11F0 | #x11F9
// | [#x1E00-#x1E9B] | [#x1EA0-#x1EF9] | [#x1F00-#x1F15]
// | [#x1F18-#x1F1D] | [#x1F20-#x1F45] | [#x1F48-#x1F4D]
// | [#x1F50-#x1F57] | #x1F59 | #x1F5B | #x1F5D | [#x1F5F-#x1F7D]
// | [#x1F80-#x1FB4] | [#x1FB6-#x1FBC] | #x1FBE | [#x1FC2-#x1FC4]
// | [#x1FC6-#x1FCC] | [#x1FD0-#x1FD3] | [#x1FD6-#x1FDB]
// | [#x1FE0-#x1FEC] | [#x1FF2-#x1FF4] | [#x1FF6-#x1FFC] | #x2126
// | [#x212A-#x212B] | #x212E | [#x2180-#x2182] | [#x3041-#x3094]
// | [#x30A1-#x30FA] | [#x3105-#x312C] | [#xAC00-#xD7A3]
BaseChar = [\u0041-\u005A\u0061-\u007A\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u00FF\u0100-\u0131\u0134-\u013E\u0141-\u0148\u014A-\u017E\u0180-\u01C3\u01CD-\u01F0\u01F4-\u01F5\u01FA-\u0217\u0250-\u02A8\u02BB-\u02C1\u0386\u0388-\u038A\u038C\u038E-\u03A1\u03A3-\u03CE\u03D0-\u03D6\u03DA\u03DC\u03DE\u03E0\u03E2-\u03F3\u0401-\u040C\u040E-\u044F\u0451-\u045C\u045E-\u0481\u0490-\u04C4\u04C7-\u04C8\u04CB-\u04CC\u04D0-\u04EB\u04EE-\u04F5\u04F8-\u04F9\u0531-\u0556\u0559\u0561-\u0586\u05D0-\u05EA\u05F0-\u05F2\u0621-\u063A\u0641-\u064A\u0671-\u06B7\u06BA-\u06BE\u06C0-\u06CE\u06D0-\u06D3\u06D5\u06E5-\u06E6\u0905-\u0939\u093D\u0958-\u0961\u0985-\u098C\u098F-\u0990\u0993-\u09A8\u09AA-\u09B0\u09B2\u09B6-\u09B9\u09DC-\u09DD\u09DF-\u09E1\u09F0-\u09F1\u0A05-\u0A0A\u0A0F-\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32-\u0A33\u0A35-\u0A36\u0A38-\u0A39\u0A59-\u0A5C\u0A5E\u0A72-\u0A74\u0A85-\u0A8B\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8\u0AAA-\u0AB0\u0AB2-\u0AB3\u0AB5-\u0AB9\u0ABD\u0AE0\u0B05-\u0B0C\u0B0F-\u0B10\u0B13-\u0B28\u0B2A-\u0B30\u0B32-\u0B33\u0B36-\u0B39\u0B3D\u0B5C-\u0B5D\u0B5F-\u0B61\u0B85-\u0B8A\u0B8E-\u0B90\u0B92-\u0B95\u0B99-\u0B9A\u0B9C\u0B9E-\u0B9F\u0BA3-\u0BA4\u0BA8-\u0BAA\u0BAE-\u0BB5\u0BB7-\u0BB9\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33\u0C35-\u0C39\u0C60-\u0C61\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3\u0CB5-\u0CB9\u0CDE\u0CE0-\u0CE1\u0D05-\u0D0C\u0D0E-\u0D10\u0D12-\u0D28\u0D2A-\u0D39\u0D60-\u0D61\u0E01-\u0E2E\u0E30\u0E32-\u0E33\u0E40-\u0E45\u0E81-\u0E82\u0E84\u0E87-\u0E88\u0E8A\u0E8D\u0E94-\u0E97\u0E99-\u0E9F\u0EA1-\u0EA3\u0EA5\u0EA7\u0EAA-\u0EAB\u0EAD-\u0EAE\u0EB0\u0EB2-\u0EB3\u0EBD\u0EC0-\u0EC4\u0F40-\u0F47\u0F49-\u0F69\u10A0-\u10C5\u10D0-\u10F6\u1100\u1102-\u1103\u1105-\u1107\u1109\u110B-\u110C\u110E-\u1112\u113C\u113E\u1140\u114C\u114E\u1150\u1154-\u1155\u1159\u115F-\u1161\u1163\u1165\u1167\u1169\u116D-\u116E\u1172-\u1173\u1175\u119E\u11A8\u11AB\u11AE-\u11AF\u11B7-\u11B8\u11BA\u11BC-\u11C2\u11EB\u11F0\u11F9\u1E00-\u1E9B\u1EA0-\u1EF9\u1F00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u2126\u212A-\u212B\u212E\u2180-\u2182\u3041-\u3094\u30A1-\u30FA\u3105-\u312C\uAC00-\uD7A3]

// [86]  Ideographic ::= [#x4E00-#x9FA5] | #x3007 | [#x3021-#x3029]
Ideographic = [\u4E00-\u9FA5\u3007\u3021-\u3029]

// [87]      CombiningChar    ::=    [#x0300-#x0345]    | [#x0360-#x0361]
// | [#x0483-#x0486]          | [#x0591-#x05A1]         | [#x05A3-#x05B9]
// | [#x05BB-#x05BD]       | #x05BF       | [#x05C1-#x05C2]	 | #x05C4
// | [#x064B-#x0652]    | #x0670    | [#x06D6-#x06DC]   | [#x06DD-#x06DF]
// | [#x06E0-#x06E4]          | [#x06E7-#x06E8]         | [#x06EA-#x06ED]
// | [#x0901-#x0903]       | #x093C       | [#x093E-#x094C]      | #x094D
// | [#x0951-#x0954]    | [#x0962-#x0963]    | [#x0981-#x0983]   | #x09BC
// | #x09BE       | #x09BF       | [#x09C0-#x09C4]      | [#x09C7-#x09C8]
// | [#x09CB-#x09CD]   | #x09D7   | [#x09E2-#x09E3]   | #x0A02   | #x0A3C
// | #x0A3E       | #x0A3F       | [#x0A40-#x0A42]      | [#x0A47-#x0A48]
// | [#x0A4B-#x0A4D]    | [#x0A70-#x0A71]    | [#x0A81-#x0A83]   | #x0ABC
// | [#x0ABE-#x0AC5]          | [#x0AC7-#x0AC9]         | [#x0ACB-#x0ACD]
// | [#x0B01-#x0B03]    | #x0B3C    | [#x0B3E-#x0B43]   | [#x0B47-#x0B48]
// | [#x0B4B-#x0B4D]          | [#x0B56-#x0B57]         | [#x0B82-#x0B83]
// | [#x0BBE-#x0BC2]    | [#x0BC6-#x0BC8]    | [#x0BCA-#x0BCD]   | #x0BD7
// | [#x0C01-#x0C03]          | [#x0C3E-#x0C44]         | [#x0C46-#x0C48]
// | [#x0C4A-#x0C4D]          | [#x0C55-#x0C56]         | [#x0C82-#x0C83]
// | [#x0CBE-#x0CC4]          | [#x0CC6-#x0CC8]         | [#x0CCA-#x0CCD]
// | [#x0CD5-#x0CD6]          | [#x0D02-#x0D03]         | [#x0D3E-#x0D43]
// | [#x0D46-#x0D48]       | [#x0D4A-#x0D4D]       | #x0D57      | #x0E31
// | [#x0E34-#x0E3A]    | [#x0E47-#x0E4E]    | #x0EB1   | [#x0EB4-#x0EB9]
// | [#x0EBB-#x0EBC]    | [#x0EC8-#x0ECD]    | [#x0F18-#x0F19]   | #x0F35
// | #x0F37      | #x0F39     | #x0F3E     | #x0F3F     | [#x0F71-#x0F84]
// | [#x0F86-#x0F8B]    | [#x0F90-#x0F95]    | #x0F97   | [#x0F99-#x0FAD]
// | [#x0FB1-#x0FB7]       | #x0FB9       | [#x20D0-#x20DC]      | #x20E1
// | [#x302A-#x302F] | #x3099 | #x309A
CombiningChar =    [\u0300-\u0345\u0360-\u0361\u0483-\u0486\u0591-\u05A1\u05A3-\u05B9\u05BB-\u05BD\u05BF\u05C1-\u05C2\u05C4\u064B-\u0652\u0670\u06D6-\u06DC\u06DD-\u06DF\u06E0-\u06E4\u06E7-\u06E8\u06EA-\u06ED\u0901-\u0903\u093C\u093E-\u094C\u094D\u0951-\u0954\u0962-\u0963\u0981-\u0983\u09BC\u09BE\u09BF\u09C0-\u09C4\u09C7-\u09C8\u09CB-\u09CD\u09D7\u09E2-\u09E3\u0A02\u0A3C\u0A3E\u0A3F\u0A40-\u0A42\u0A47-\u0A48\u0A4B-\u0A4D\u0A70-\u0A71\u0A81-\u0A83\u0ABC\u0ABE-\u0AC5\u0AC7-\u0AC9\u0ACB-\u0ACD\u0B01-\u0B03\u0B3C\u0B3E-\u0B43\u0B47-\u0B48\u0B4B-\u0B4D\u0B56-\u0B57\u0B82-\u0B83\u0BBE-\u0BC2\u0BC6-\u0BC8\u0BCA-\u0BCD\u0BD7\u0C01-\u0C03\u0C3E-\u0C44\u0C46-\u0C48\u0C4A-\u0C4D\u0C55-\u0C56\u0C82-\u0C83\u0CBE-\u0CC4\u0CC6-\u0CC8\u0CCA-\u0CCD\u0CD5-\u0CD6\u0D02-\u0D03\u0D3E-\u0D43\u0D46-\u0D48\u0D4A-\u0D4D\u0D57\u0E31\u0E34-\u0E3A\u0E47-\u0E4E\u0EB1\u0EB4-\u0EB9\u0EBB-\u0EBC\u0EC8-\u0ECD\u0F18-\u0F19\u0F35\u0F37\u0F39\u0F3E\u0F3F\u0F71-\u0F84\u0F86-\u0F8B\u0F90-\u0F95\u0F97\u0F99-\u0FAD\u0FB1-\u0FB7\u0FB9\u20D0-\u20DC\u20E1\u302A-\u302F\u3099\u309A]

// [88]   Digit  ::=  [#x0030-#x0039] | [#x0660-#x0669] | [#x06F0-#x06F9]
// | [#x0966-#x096F]          | [#x09E6-#x09EF]         | [#x0A66-#x0A6F]
// | [#x0AE6-#x0AEF]          | [#x0B66-#x0B6F]         | [#x0BE7-#x0BEF]
// | [#x0C66-#x0C6F]          | [#x0CE6-#x0CEF]         | [#x0D66-#x0D6F]
// | [#x0E50-#x0E59] | [#x0ED0-#x0ED9] | [#x0F20-#x0F29]
Digit =  [\u0030-\u0039\u0660-\u0669\u06F0-\u06F9\u0966-\u096F\u09E6-\u09EF\u0A66-\u0A6F\u0AE6-\u0AEF\u0B66-\u0B6F\u0BE7-\u0BEF\u0C66-\u0C6F\u0CE6-\u0CEF\u0D66-\u0D6F\u0E50-\u0E59\u0ED0-\u0ED9\u0F20-\u0F29]

// [89]  Extender ::= #x00B7 | #x02D0 | #x02D1 | #x0387 | #x0640 | #x0E46
// | #x0EC6       | #x3005       | [#x3031-#x3035]      | [#x309D-#x309E]
// | [#x30FC-#x30FE]
Extender = [\u00B7\u02D0\u02D1\u0387\u0640\u0E46\u0EC6\u3005\u3031-\u3035\u309D-\u309E\u30FC-\u30FE]



/**
 * JSP and scripting marker allowances
 */

jspCommentStart = <%--
jspCommentEnd   = --%>

jspScriptletStart        = <%
jspExpressionStart       = {jspScriptletStart}=
jspDeclarationStart      = {jspScriptletStart}\!
jspScriptletEnd          = %>
jspDirectiveStart        = {jspScriptletStart}@

%%

/* white space within a tag */
<ST_XML_EQUALS, ST_XML_ATTRIBUTE_NAME, ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE, ST_PI, ST_XML_PI_EQUALS, ST_XML_PI_ATTRIBUTE_NAME, ST_XML_PI_ATTRIBUTE_VALUE, ST_XML_DECLARATION, ST_XML_DOCTYPE_DECLARATION, ST_XML_ELEMENT_DECLARATION, ST_XML_ATTLIST_DECLARATION, ST_XML_DECLARATION_CLOSE, ST_XML_DOCTYPE_ID_PUBLIC, ST_XML_DOCTYPE_ID_SYSTEM, ST_XML_DOCTYPE_EXTERNAL_ID, ST_JSP_DIRECTIVE_NAME, ST_JSP_DIRECTIVE_ATTRIBUTE_NAME, ST_JSP_DIRECTIVE_EQUALS, ST_JSP_DIRECTIVE_ATTRIBUTE_VALUE,ST_XML_ATTRIBUTE_VALUE_DQUOTED,ST_XML_ATTRIBUTE_VALUE_SQUOTED,ST_DHTML_ATTRIBUTE_NAME,ST_DHTML_EQUALS,ST_DHTML_ATTRIBUTE_VALUE,ST_DHTML_TAG_CLOSE> {S}* {
	if(Debug.debugTokenizer)
		dump("white space");//$NON-NLS-1$
        return WHITE_SPACE;
}

// BEGIN REGULAR XML
/* handle opening a new tag almost anywhere */


<YYINITIAL, ST_XML_TAG_NAME, ST_XML_EQUALS, ST_XML_ATTRIBUTE_NAME, ST_XML_DECLARATION, ST_JSP_DIRECTIVE_NAME, ST_JSP_DIRECTIVE_NAME_WHITESPACE, ST_JSP_DIRECTIVE_ATTRIBUTE_NAME, ST_JSP_DIRECTIVE_EQUALS, ST_JSP_DIRECTIVE_ATTRIBUTE_VALUE> {genericTagOpen} {
	if(Debug.debugTokenizer)
		dump("\nstart tag open");//$NON-NLS-1$
	if (!fStateStack.empty() && fStateStack.peek()== ST_XML_COMMENT){
		fStateStack.pop();
		fEmbeddedHint = XML_COMMENT_TEXT;
		yybegin(ST_XML_TAG_NAME);
		String tagName = "";
		assembleEmbeddedTagSequence(XML_TAG_OPEN, tagName); // ?
		return PROXY_CONTEXT;
	}
	fEmbeddedHint = XML_TAG_NAME;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
        yybegin(ST_XML_TAG_NAME);
        return XML_TAG_OPEN;
}
/* VERY special cases for tags as values */
/* quoted JSP */
<ST_XML_ATTRIBUTE_VALUE_DQUOTED> ["] {
	return isJspTag()? JSP_TAG_ATTRIBUTE_VALUE_DQUOTE : XML_TAG_ATTRIBUTE_VALUE_DQUOTE;
}
<ST_XML_ATTRIBUTE_VALUE_SQUOTED> ['] {
	return isJspTag() ? JSP_TAG_ATTRIBUTE_VALUE_SQUOTE : XML_TAG_ATTRIBUTE_VALUE_SQUOTE;
}
<ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> ["] {
	String type = yy_lexical_state == ST_XML_ATTRIBUTE_VALUE ? XML_TAG_ATTRIBUTE_VALUE_DQUOTE : JSP_TAG_ATTRIBUTE_VALUE_DQUOTE;

	if (Debug.debugTokenizer) {
		System.out.println("begin embedded region: " + fEmbeddedHint+", "+type);//$NON-NLS-1$
	}
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_DQUOTED;
	yybegin(ST_XML_ATTRIBUTE_VALUE_DQUOTED);
	fStateStack.push(yystate());
	if(Debug.debugTokenizer)
		dump("JSP attribute value start - complex double quoted");//$NON-NLS-1$
	assembleEmbeddedContainer(type, type);
	fStateStack.pop();
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
	yybegin(ST_XML_ATTRIBUTE_NAME);
	if (fEmbeddedContainer.getLastRegion().getType() == UNDEFINED) {
		fProxyUnknownRegion = fRegionFactory.createToken(XML_TAG_ATTRIBUTE_VALUE, fEmbeddedContainer.getStart(), fEmbeddedContainer.getTextLength(), fEmbeddedContainer.getLength());
		return PROXY_UNKNOWN_CONTEXT;
	}
	return PROXY_CONTEXT;
}
<ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> ['] {
	String type = yy_lexical_state == ST_XML_ATTRIBUTE_VALUE ? XML_TAG_ATTRIBUTE_VALUE_SQUOTE : JSP_TAG_ATTRIBUTE_VALUE_SQUOTE;
	if (Debug.debugTokenizer) {
		System.out.println("begin embedded region: " + fEmbeddedHint+", "+type);//$NON-NLS-1$
	}
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_SQUOTED;
	yybegin(ST_XML_ATTRIBUTE_VALUE_SQUOTED);
	fStateStack.push(yystate());
	if(Debug.debugTokenizer)
		dump("JSP attribute value start - complex single quoted");//$NON-NLS-1$
	assembleEmbeddedContainer(type, type);
	fStateStack.pop();
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_ATTRIBUTE_NAME);
    if (fEmbeddedContainer.getLastRegion().getType() == UNDEFINED) {
		fProxyUnknownRegion = fRegionFactory.createToken(XML_TAG_ATTRIBUTE_VALUE, fEmbeddedContainer.getStart(), fEmbeddedContainer.getTextLength(), fEmbeddedContainer.getLength());
		return PROXY_UNKNOWN_CONTEXT;
	}
	return PROXY_CONTEXT;
}

<ST_XML_ATTRIBUTE_VALUE_DQUOTED> ([^<"\x24\x23]+|[\x24\x23]{S}*)
{
	return XML_TAG_ATTRIBUTE_VALUE;
}
<ST_XML_ATTRIBUTE_VALUE_SQUOTED> ([^<'\x24\x23]+|[\x24\x23]{S}*)
{
	return XML_TAG_ATTRIBUTE_VALUE;
}


<ST_XML_ATTRIBUTE_VALUE_DQUOTED,ST_XML_ATTRIBUTE_VALUE_SQUOTED> {genericTagOpen} {
	if (Debug.debugTokenizer) {
		System.out.println("begin embedded region: " + fEmbeddedHint+", genericTagOpen");//$NON-NLS-1$
	}
	int incomingState = yystate();
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	if(Debug.debugTokenizer)
		dump("tag inside of JSP attribute value start");//$NON-NLS-1$
	yybegin(ST_XML_TAG_NAME);
	assembleEmbeddedContainer(XML_TAG_OPEN, new String[]{XML_TAG_CLOSE,XML_EMPTY_TAG_CLOSE});
	if(yystate() != ST_ABORT_EMBEDDED)
        yybegin(incomingState);
	return PROXY_CONTEXT;
}
<ST_XML_ATTRIBUTE_VALUE_DQUOTED,ST_XML_ATTRIBUTE_VALUE_SQUOTED> {genericEndTagOpen} {
	if (Debug.debugTokenizer) {
		System.out.println("begin embedded region: " + fEmbeddedHint+", genericEndTagOpen");//$NON-NLS-1$
	}
	int incomingState = yystate();
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	if(Debug.debugTokenizer)
		dump("JSP attribute value start - end tag");//$NON-NLS-1$
	yybegin(ST_XML_TAG_NAME);
	assembleEmbeddedContainer(XML_END_TAG_OPEN, new String[]{XML_TAG_CLOSE,XML_EMPTY_TAG_CLOSE});
	if(yystate() != ST_ABORT_EMBEDDED)
        yybegin(incomingState);
	return PROXY_CONTEXT;
}

/* unquoted */
<ST_XML_ATTRIBUTE_VALUE> {genericTagOpen} {
	if (Debug.debugTokenizer) {
		System.out.println("begin embedded region: " + fEmbeddedHint+", unquoted genericTagOpen");//$NON-NLS-1$
	}
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	fStateStack.push(yystate());
	if(Debug.debugTokenizer)
		dump("JSP tag embedded name start - start tag");//$NON-NLS-1$
	yybegin(ST_XML_TAG_NAME);
	assembleEmbeddedContainer(XML_TAG_OPEN, new String[]{XML_TAG_CLOSE,XML_EMPTY_TAG_CLOSE});
	fStateStack.pop();
        yybegin(ST_XML_ATTRIBUTE_NAME);
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
	return PROXY_CONTEXT;
}

<YYINITIAL, ST_XML_TAG_NAME, ST_XML_EQUALS, ST_XML_ATTRIBUTE_NAME, ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE, ST_XML_DECLARATION> {genericEndTagOpen} {
	if(Debug.debugTokenizer)
		dump("\nend tag open");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_NAME;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
        yybegin(ST_XML_TAG_NAME);
        return XML_END_TAG_OPEN;
}
/* specially treated JSP tag names */
<ST_XML_TAG_NAME> jsp:root {
	if(Debug.debugTokenizer)
		dump("jsp:root tag name");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_ATTRIBUTE_NAME);
        return JSP_ROOT_TAG_NAME;
}
<ST_XML_TAG_NAME> jsp:directive.(page|include|tag|taglib|attribute|variable) {
	if(Debug.debugTokenizer)
		dump("jsp directive tag name");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_ATTRIBUTE_NAME);
        return JSP_DIRECTIVE_NAME;
}
/* the tag's name was found, start scanning for attributes */
<ST_XML_TAG_NAME> {Name} {
	if(Debug.debugTokenizer)
		dump("tag name");//$NON-NLS-1$
    String tagname = yytext();
    boolean jspTag = tagname.indexOf(':') != -1;
	if (fEmbeddedContainer != null) {
    	fEmbeddedTag = jspTag;
		fInTagEmbedded = true;
    }
	else {
		fContainerTag = jspTag;
		fInTagContainer = true;
	}
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_ATTRIBUTE_NAME);
        return XML_TAG_NAME;
}

/* another attribute name was found, resume looking for the equals sign */
<ST_XML_ATTRIBUTE_NAME, ST_XML_EQUALS> ({Letter} | _ | : | \[ | \] | \( | \) | \* | @ | #){NameChar}* {
	if(Debug.debugTokenizer)
		dump("attr name");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
        yybegin(ST_XML_EQUALS);
        return XML_TAG_ATTRIBUTE_NAME;
}
/* an equal sign was found, what's next is the value */
<ST_XML_EQUALS> {Eq} {
	if(Debug.debugTokenizer)
		dump("equals");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
        yybegin(isJspTag() ? ST_JSP_ATTRIBUTE_VALUE : ST_XML_ATTRIBUTE_VALUE);
        return XML_TAG_ATTRIBUTE_EQUALS;
}
/* the value was found, look for the next name */
<ST_XML_ATTRIBUTE_VALUE> {AttValue} { /* only allow for non-JSP tags for this does not obey JSP quoting rules */
	if(Debug.debugTokenizer)
		dump("attr value");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_ATTRIBUTE_NAME);
        return XML_TAG_ATTRIBUTE_VALUE;
}
<ST_JSP_ATTRIBUTE_VALUE> {QuotedAttValue} { /* JSP attribute values have escape semantics */
	if(Debug.debugTokenizer)
		dump("jsp attr value");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
	yybegin(ST_XML_ATTRIBUTE_NAME);
	return XML_TAG_ATTRIBUTE_VALUE;
}

/* the tag's close was found */
<ST_XML_TAG_NAME, ST_XML_EQUALS, ST_XML_ATTRIBUTE_NAME, ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> {genericTagClose} {
	if(Debug.debugTokenizer)
		dump("tag close");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
	if(isBlockMarker()) {
		fEmbeddedHint = getBlockMarkerContext();
		fEmbeddedPostState = ST_BLOCK_TAG_SCAN;
        	yybegin(ST_BLOCK_TAG_SCAN);
	}
	else
        	yybegin(YYINITIAL);

	if (fEmbeddedContainer != null)
		fInTagEmbedded = false;
	else
		fInTagContainer = false;

	return XML_TAG_CLOSE;
}
/* the tag's close was found, but the tag doesn't need a matching end tag */
<ST_XML_TAG_NAME, ST_XML_EQUALS, ST_XML_ATTRIBUTE_NAME, ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> {genericEmptyTagClose} {
        yybegin(YYINITIAL);
	fEmbeddedHint = UNDEFINED;
	if(Debug.debugTokenizer)
		dump("empty tag close");//$NON-NLS-1$

	if (fEmbeddedContainer != null)
		fInTagEmbedded = false;
	else
		fInTagContainer = false;

	return XML_EMPTY_TAG_CLOSE;
}

<ST_XML_TAG_NAME> [^</>\u0041-\u005A\u0061-\u007A\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u00FF\u0100-\u0131\u0134-\u013E\u0141-\u0148\u014A-\u017E\u0180-\u01C3\u01CD-\u01F0\u01F4-\u01F5\u01FA-\u0217\u0250-\u02A8\u02BB-\u02C1\u0386\u0388-\u038A\u038C\u038E-\u03A1\u03A3-\u03CE\u03D0-\u03D6\u03DA\u03DC\u03DE\u03E0\u03E2-\u03F3\u0401-\u040C\u040E-\u044F\u0451-\u045C\u045E-\u0481\u0490-\u04C4\u04C7-\u04C8\u04CB-\u04CC\u04D0-\u04EB\u04EE-\u04F5\u04F8-\u04F9\u0531-\u0556\u0559\u0561-\u0586\u05D0-\u05EA\u05F0-\u05F2\u0621-\u063A\u0641-\u064A\u0671-\u06B7\u06BA-\u06BE\u06C0-\u06CE\u06D0-\u06D3\u06D5\u06E5-\u06E6\u0905-\u0939\u093D\u0958-\u0961\u0985-\u098C\u098F-\u0990\u0993-\u09A8\u09AA-\u09B0\u09B2\u09B6-\u09B9\u09DC-\u09DD\u09DF-\u09E1\u09F0-\u09F1\u0A05-\u0A0A\u0A0F-\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32-\u0A33\u0A35-\u0A36\u0A38-\u0A39\u0A59-\u0A5C\u0A5E\u0A72-\u0A74\u0A85-\u0A8B\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8\u0AAA-\u0AB0\u0AB2-\u0AB3\u0AB5-\u0AB9\u0ABD\u0AE0\u0B05-\u0B0C\u0B0F-\u0B10\u0B13-\u0B28\u0B2A-\u0B30\u0B32-\u0B33\u0B36-\u0B39\u0B3D\u0B5C-\u0B5D\u0B5F-\u0B61\u0B85-\u0B8A\u0B8E-\u0B90\u0B92-\u0B95\u0B99-\u0B9A\u0B9C\u0B9E-\u0B9F\u0BA3-\u0BA4\u0BA8-\u0BAA\u0BAE-\u0BB5\u0BB7-\u0BB9\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33\u0C35-\u0C39\u0C60-\u0C61\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3\u0CB5-\u0CB9\u0CDE\u0CE0-\u0CE1\u0D05-\u0D0C\u0D0E-\u0D10\u0D12-\u0D28\u0D2A-\u0D39\u0D60-\u0D61\u0E01-\u0E2E\u0E30\u0E32-\u0E33\u0E40-\u0E45\u0E81-\u0E82\u0E84\u0E87-\u0E88\u0E8A\u0E8D\u0E94-\u0E97\u0E99-\u0E9F\u0EA1-\u0EA3\u0EA5\u0EA7\u0EAA-\u0EAB\u0EAD-\u0EAE\u0EB0\u0EB2-\u0EB3\u0EBD\u0EC0-\u0EC4\u0F40-\u0F47\u0F49-\u0F69\u10A0-\u10C5\u10D0-\u10F6\u1100\u1102-\u1103\u1105-\u1107\u1109\u110B-\u110C\u110E-\u1112\u113C\u113E\u1140\u114C\u114E\u1150\u1154-\u1155\u1159\u115F-\u1161\u1163\u1165\u1167\u1169\u116D-\u116E\u1172-\u1173\u1175\u119E\u11A8\u11AB\u11AE-\u11AF\u11B7-\u11B8\u11BA\u11BC-\u11C2\u11EB\u11F0\u11F9\u1E00-\u1E9B\u1EA0-\u1EF9\u1F00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u2126\u212A-\u212B\u212E\u2180-\u2182\u3041-\u3094\u30A1-\u30FA\u3105-\u312C\uAC00-\uD7A3\u4E00-\u9FA5\u3007\u3021-\u3029]* {
	if(Debug.debugTokenizer)
		dump("inappropriate tag name");//$NON-NLS-1$
	if(!fStateStack.empty() && (fStateStack.peek()==ST_XML_ATTRIBUTE_VALUE_SQUOTED||fStateStack.peek()==ST_XML_ATTRIBUTE_VALUE_DQUOTED)) {
		yybegin(ST_ABORT_EMBEDDED);
		char c = yy_buffer[yy_markedPos - 1];
		if (fStateStack.peek()==ST_XML_ATTRIBUTE_VALUE_DQUOTED && c == '\"') {
			return isJspTag() ? JSP_TAG_ATTRIBUTE_VALUE_DQUOTE : XML_TAG_ATTRIBUTE_VALUE_DQUOTE;
		}		
		if (fStateStack.peek()==ST_XML_ATTRIBUTE_VALUE_SQUOTED && c == '\'') {
			return isJspTag() ? JSP_TAG_ATTRIBUTE_VALUE_SQUOTE : XML_TAG_ATTRIBUTE_VALUE_SQUOTE;
		}
		yypushback(yylength()-1);
		return XML_TAG_ATTRIBUTE_VALUE;
	}
	yybegin(YYINITIAL);
        return XML_CONTENT;
}

// END REGULAR XML

// BEGIN NESTED XML TAGS
<ST_XML_ATTRIBUTE_NAME, ST_XML_EQUALS> <{Name} {
	String tagName = yytext().substring(1);
	// pushback to just after the opening bracket
	yypushback(yylength() - 1);
	/*
	 * If this tag can not be nested or we're already searching for an
	 * attribute name, equals, or value, return immediately.
	 */
	if (!isNestable(tagName) || (!fStateStack.empty() && (fStateStack.peek() == ST_XML_ATTRIBUTE_NAME || fStateStack.peek() == ST_XML_EQUALS || fStateStack.peek() == ST_XML_ATTRIBUTE_VALUE || fStateStack.peek() == ST_JSP_ATTRIBUTE_VALUE || fStateStack.peek() == ST_XML_ATTRIBUTE_VALUE_DQUOTED || fStateStack.peek() == ST_XML_ATTRIBUTE_VALUE_SQUOTED))) {
		yybegin(ST_XML_TAG_NAME);
		return XML_TAG_OPEN;
	}
	if(Debug.debugTokenizer)
		dump("tag in place of attr name");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	fStateStack.push(yystate());
	// embedded container should be looking for the name (again) next
	yybegin(ST_XML_TAG_NAME);
	assembleEmbeddedTagSequence(XML_TAG_OPEN, tagName); // ?
	fStateStack.pop();
	yybegin(ST_XML_EQUALS);
	return PROXY_CONTEXT;
}
<ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> <{Name} {
	String tagName = yytext().substring(1);
	// pushback to just after the opening bracket
	yypushback(yylength() - 1);
	/*
	 * If this tag can not be nested or we're already searching for an
	 * attribute name, equals, or value, return immediately.
	 */
	if (!isNestable(tagName) || (!fStateStack.empty() && (fStateStack.peek() == ST_XML_ATTRIBUTE_NAME || fStateStack.peek() == ST_XML_EQUALS || fStateStack.peek() == ST_XML_ATTRIBUTE_VALUE || fStateStack.peek() == ST_JSP_ATTRIBUTE_VALUE))) {
		yybegin(ST_XML_TAG_NAME);
		return XML_TAG_OPEN;
	}
	if(Debug.debugTokenizer)
		dump("tag in place of attr value");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	fStateStack.push(yystate());
	// embedded container should be looking for the name (again) next
	yybegin(ST_XML_TAG_NAME);
	assembleEmbeddedTagSequence(XML_TAG_OPEN, tagName); // ?
	fStateStack.pop();
	yybegin(ST_XML_ATTRIBUTE_NAME);
	return PROXY_CONTEXT;
}
// END NESTED XML

// XML & JSP Comments

<YYINITIAL, ST_XML_TAG_NAME, ST_XML_EQUALS, ST_XML_ATTRIBUTE_NAME, ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE, ST_XML_DECLARATION, ST_JSP_DIRECTIVE_NAME, ST_JSP_DIRECTIVE_NAME_WHITESPACE, ST_JSP_DIRECTIVE_ATTRIBUTE_NAME, ST_JSP_DIRECTIVE_EQUALS, ST_JSP_DIRECTIVE_ATTRIBUTE_VALUE> {CommentStart} {
	if(Debug.debugTokenizer)
		dump("\ncomment start");//$NON-NLS-1$
	fEmbeddedHint = XML_COMMENT_TEXT;
	fEmbeddedPostState = ST_XML_COMMENT;
	yybegin(ST_XML_COMMENT);
	return XML_COMMENT_OPEN;
}
<ST_XML_COMMENT> .|\r|\n {
	if(Debug.debugTokenizer)
		dump("comment content");//$NON-NLS-1$
	return scanXMLCommentText();
}

<ST_XML_COMMENT_END> {CommentEnd} {
	if(Debug.debugTokenizer)
		dump("comment end");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
	yybegin(YYINITIAL);
	return XML_COMMENT_CLOSE;
}

<ST_JSP_CONTENT> {jspScriptletEnd} {
	if(Debug.debugTokenizer)
		dump("JSP end");//$NON-NLS-1$
	if (Debug.debugTokenizer) {
		if(fStateStack.peek()!=YYINITIAL)
			System.out.println("end embedded region");//$NON-NLS-1$
	}
	yybegin(fStateStack.pop());
	return JSP_CLOSE;
}
<ST_JSP_CONTENT> .|\n|\r {
	if(Debug.debugTokenizer)
		dump("JSP code content");//$NON-NLS-1$
	return doScan("%>", false, false, false, JSP_CONTENT, ST_JSP_CONTENT, ST_JSP_CONTENT);
}
{jspScriptletStart} {
	/* JSP scriptlet begun (anywhere)
	 * A consequence of the start anywhere possibility is that the
	 *  incoming state must be checked to see if it's erroneous
	 *  due to the order of precedence generated
	 */
	// begin sanity checks
	if(yystate() == ST_JSP_CONTENT) {
		// at the beginning?!
		yypushback(1);
		return JSP_CONTENT;
	}
	else if(yystate() == ST_BLOCK_TAG_SCAN) {
		yypushback(2);
		return doBlockTagScan();
	}
	else if(yystate() == ST_XML_COMMENT) {
		yypushback(2);
		return scanXMLCommentText();
	}
	else if(yystate() == ST_JSP_COMMENT) {
		yypushback(2);
		return scanJSPCommentText();
	}
	// finished sanity checks
	fStateStack.push(yystate());
	if(fStateStack.peek()==YYINITIAL) {
		// the simple case, just a regular scriptlet out in content
		if(Debug.debugTokenizer)
			dump("\nJSP scriptlet start");//$NON-NLS-1$
		yybegin(ST_JSP_CONTENT);
		return JSP_SCRIPTLET_OPEN;
	}
	else {
		if (Debug.debugTokenizer) {
			System.out.println("begin embedded region: " + fEmbeddedHint+", jspScriptletStart");//$NON-NLS-1$
		}
		if(Debug.debugTokenizer)
			dump("JSP scriptlet start");//$NON-NLS-1$
		if(yystate() == ST_XML_ATTRIBUTE_VALUE_DQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_DQUOTED;
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE_SQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_SQUOTED;
		else if(yystate() == ST_CDATA_TEXT) {
			fEmbeddedPostState = ST_CDATA_TEXT;
			fEmbeddedHint = XML_CDATA_TEXT;
		}
		yybegin(ST_JSP_CONTENT);
		assembleEmbeddedContainer(JSP_SCRIPTLET_OPEN, JSP_CLOSE);
		if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN) {
			yybegin(ST_BLOCK_TAG_SCAN);
			return BLOCK_TEXT;
		}
		// required help for successive embedded regions
		if(yystate() == ST_XML_TAG_NAME) {
			fEmbeddedHint = XML_TAG_NAME;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if((yystate() == ST_XML_ATTRIBUTE_NAME || yystate() == ST_XML_EQUALS)) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
			fEmbeddedPostState = ST_XML_EQUALS;
		}
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
        else if(yystate() == ST_JSP_ATTRIBUTE_VALUE) {
            fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
            fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
        }
		
		return PROXY_CONTEXT;
	}
}
{jspExpressionStart} {
	/* JSP expression begun (anywhere)
	 * A consequence of the start anywhere possibility is that the
	 *  incoming state must be checked to see if it's erroneous
	 *  due to the order of precedence generated
	 */
	// begin sanity checks
	if(yystate() == ST_JSP_CONTENT) {
		// at the beginning?!
		yypushback(2);
		return JSP_CONTENT;
	}
	else if(yystate() == ST_BLOCK_TAG_SCAN) {
		yypushback(3);
		return doBlockTagScan();
	}
	else if(yystate() == ST_XML_COMMENT) {
		yypushback(3);
		return scanXMLCommentText();
	}
	else if(yystate() == ST_JSP_COMMENT) {
		yypushback(3);
		return scanJSPCommentText();
	}
	// end sanity checks
	fStateStack.push(yystate());
	if(fStateStack.peek()==YYINITIAL) {
		// the simple case, just an expression out in content
		if(Debug.debugTokenizer)
			dump("\nJSP expression start");//$NON-NLS-1$
		yybegin(ST_JSP_CONTENT);
		return JSP_EXPRESSION_OPEN;
	}
	else {
		if (Debug.debugTokenizer) {
			System.out.println("begin embedded region: " + fEmbeddedHint+", jspExpressionStart");//$NON-NLS-1$
		}
		if(Debug.debugTokenizer)
			dump("JSP expression start");//$NON-NLS-1$
		if(yystate() == ST_XML_ATTRIBUTE_VALUE_DQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_DQUOTED;
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE_SQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_SQUOTED;
		else if(yystate() == ST_CDATA_TEXT) {
			fEmbeddedPostState = ST_CDATA_TEXT;
			fEmbeddedHint = XML_CDATA_TEXT;
		}
		yybegin(ST_JSP_CONTENT);
		assembleEmbeddedContainer(JSP_EXPRESSION_OPEN, JSP_CLOSE);
		if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN) {
			yybegin(ST_BLOCK_TAG_SCAN);
			return BLOCK_TEXT;
		}
		// required help for successive embedded regions
		if(yystate() == ST_XML_TAG_NAME) {
			fEmbeddedHint = XML_TAG_NAME;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if((yystate() == ST_XML_ATTRIBUTE_NAME || yystate() == ST_XML_EQUALS)) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
			fEmbeddedPostState = ST_XML_EQUALS;
		}
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if(yystate() == ST_JSP_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		
		return PROXY_CONTEXT;
	}
}
{jspDeclarationStart} {
	/* JSP declaration begun (anywhere)
	 * A consequence of the start anywhere possibility is that the
	 *  incoming state must be checked to see if it's erroneous
	 *  due to the order of precedence generated
	 */
	// begin sanity checks
	if(yystate() == ST_JSP_CONTENT) {
		// at the beginning?!
		yypushback(2);
		return JSP_CONTENT;
	}
	else if(yystate() == ST_BLOCK_TAG_SCAN) {
		yypushback(3);
		return doBlockTagScan();
	}
	else if(yystate() == ST_XML_COMMENT) {
		yypushback(3);
		return scanXMLCommentText();
	}
	else if(yystate() == ST_JSP_COMMENT) {
		yypushback(3);
		return scanJSPCommentText();
	}
	// end sanity checks
	fStateStack.push(yystate());
	if(fStateStack.peek()==YYINITIAL) {
		// the simple case, just a declaration out in content
		if(Debug.debugTokenizer)
			dump("\nJSP declaration start");//$NON-NLS-1$
		yybegin(ST_JSP_CONTENT);
		return JSP_DECLARATION_OPEN;
	}
	else {
		if (Debug.debugTokenizer) {
			System.out.println("begin embedded region: " + fEmbeddedHint+", jspDeclarationStart");//$NON-NLS-1$
		}
		if(Debug.debugTokenizer)
			dump("JSP declaration start");//$NON-NLS-1$
		if(yystate() == ST_XML_ATTRIBUTE_VALUE_DQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_DQUOTED;
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE_SQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_SQUOTED;
		else if(yystate() == ST_CDATA_TEXT) {
			fEmbeddedPostState = ST_CDATA_TEXT;
			fEmbeddedHint = XML_CDATA_TEXT;
		}
		yybegin(ST_JSP_CONTENT);
		assembleEmbeddedContainer(JSP_DECLARATION_OPEN, JSP_CLOSE);
		if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN) {
			yybegin(ST_BLOCK_TAG_SCAN);
			return BLOCK_TEXT;
		}
		// required help for successive embedded regions
		if(yystate() == ST_XML_TAG_NAME) {
			fEmbeddedHint = XML_TAG_NAME;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if((yystate() == ST_XML_ATTRIBUTE_NAME || yystate() == ST_XML_EQUALS)) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
			fEmbeddedPostState = ST_XML_EQUALS;
		}
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if(yystate() == ST_JSP_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		return PROXY_CONTEXT;
	}
}
{jspCommentStart} {
	/* JSP comment begun (anywhere)
	 * A consequence of the start anywhere possibility is that the
	 *  incoming state must be checked to see if it's erroneous
	 *  due to the order of precedence generated
	 */
	// begin sanity checks
	if(yystate() == ST_JSP_CONTENT) {
		// at the beginning?!
		yypushback(3);
		return JSP_CONTENT;
	}
	else if(yystate() == ST_BLOCK_TAG_SCAN) {
		yypushback(4);
		return doBlockTagScan();
	}
	else if(yystate() == ST_XML_COMMENT) {
		yypushback(4);
		return scanXMLCommentText();
	}
	else if(yystate() == ST_JSP_COMMENT) {
		yypushback(4);
		return scanJSPCommentText();
	}
	else if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN)  {
		yybegin(ST_JSP_COMMENT);
		assembleEmbeddedContainer(JSP_COMMENT_OPEN, JSP_COMMENT_CLOSE);
		if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN)
			yybegin(ST_BLOCK_TAG_SCAN);
		return PROXY_CONTEXT;
	}
	// finished sanity checks
	if(yystate()==YYINITIAL) {
		// the simple case, just a regular scriptlet out in content
		if(Debug.debugTokenizer)
			dump("\nJSP comment start");//$NON-NLS-1$
		yybegin(ST_JSP_COMMENT);
		return JSP_COMMENT_OPEN;
	}
	else {
		if (Debug.debugTokenizer) {
			System.out.println("begin embedded region: " + fEmbeddedHint+", jspCommentStart");//$NON-NLS-1$
		}
		if(Debug.debugTokenizer)
			dump("JSP comment start");//$NON-NLS-1$
		if(yystate() == ST_XML_ATTRIBUTE_VALUE_DQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_DQUOTED;
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE_SQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_SQUOTED;
		else if(yystate() == ST_CDATA_TEXT) {
			fEmbeddedPostState = ST_CDATA_TEXT;
			fEmbeddedHint = XML_CDATA_TEXT;
		}
		yybegin(ST_JSP_COMMENT);
		// the comment container itself will act as comment text
		fEmbeddedHint = JSP_COMMENT_TEXT;
		assembleEmbeddedContainer(JSP_COMMENT_OPEN, JSP_COMMENT_CLOSE);
		if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN) {
			yybegin(ST_BLOCK_TAG_SCAN);
			return BLOCK_TEXT;
		}
		/*
		 * required help for successive embedded regions; mark this one as a
		 * comment so it will be otherwise ignored but preserved (which is why
		 * we can't use white-space)
		 */
		if(yystate() == ST_XML_TAG_NAME) {
			fEmbeddedHint = XML_TAG_NAME;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if((yystate() == ST_XML_ATTRIBUTE_NAME || yystate() == ST_XML_EQUALS)) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
			fEmbeddedPostState = ST_XML_EQUALS;
		}
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if(yystate() == ST_JSP_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		return PROXY_CONTEXT;
	}
}

<ST_BLOCK_TAG_INTERNAL_SCAN> {jspCommentStart}  {
	yybegin(ST_JSP_COMMENT);
	assembleEmbeddedContainer(JSP_COMMENT_OPEN, JSP_COMMENT_CLOSE);
	if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN)
		yybegin(ST_BLOCK_TAG_SCAN);
	return PROXY_CONTEXT;
}

{jspDirectiveStart} {
	/* JSP directive begun (anywhere)
	 * A consequence of the start anywhere possibility is that the
	 *  incoming state must be checked to see if it's erroneous
	 *  due to the order of precedence generated
	 */
	// begin sanity checks
	if(yystate() == ST_JSP_CONTENT) {
		// at the beginning?!
		yypushback(2);
		return JSP_CONTENT;
	}
	else if(yystate() == ST_BLOCK_TAG_SCAN) {
		yypushback(3);
		return doBlockTagScan();
	}
	else if(yystate() == ST_XML_COMMENT) {
		yypushback(3);
		return scanXMLCommentText();
	}
	else if(yystate() == ST_JSP_COMMENT) {
		yypushback(3);
		return scanJSPCommentText();
	}
	// end sanity checks
	fStateStack.push(yystate());
	if(fStateStack.peek()==YYINITIAL) {
		// the simple case, just a declaration out in content
		if(Debug.debugTokenizer)
			dump("\nJSP directive start");//$NON-NLS-1$
		yybegin(ST_JSP_DIRECTIVE_NAME);
		return JSP_DIRECTIVE_OPEN;
	}
	else {
		if (Debug.debugTokenizer) {
			System.out.println("begin embedded region: " + fEmbeddedHint+", jspDirectiveStart");//$NON-NLS-1$
		}
		if(Debug.debugTokenizer)
			dump("JSP declaration start");//$NON-NLS-1$
		if(yystate() == ST_XML_ATTRIBUTE_VALUE_DQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_DQUOTED;
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE_SQUOTED)
			fEmbeddedPostState = ST_XML_ATTRIBUTE_VALUE_SQUOTED;
		else if(yystate() == ST_CDATA_TEXT) {
			fEmbeddedPostState = ST_CDATA_TEXT;
			fEmbeddedHint = XML_CDATA_TEXT;
		}
		yybegin(ST_JSP_DIRECTIVE_NAME);
		assembleEmbeddedContainer(JSP_DIRECTIVE_OPEN, new String[]{JSP_DIRECTIVE_CLOSE, JSP_CLOSE});
		if(yystate() == ST_BLOCK_TAG_INTERNAL_SCAN) {
			yybegin(ST_BLOCK_TAG_SCAN);
			return BLOCK_TEXT;
		}
		// required help for successive embedded regions
		if(yystate() == ST_XML_TAG_NAME) {
			fEmbeddedHint = XML_TAG_NAME;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		else if((yystate() == ST_XML_ATTRIBUTE_NAME || yystate() == ST_XML_EQUALS)) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
			fEmbeddedPostState = ST_XML_EQUALS;
		}
		else if(yystate() == ST_XML_ATTRIBUTE_VALUE) {
			fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
			fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
		}
		return PROXY_CONTEXT;
	}
}


<ST_JSP_DIRECTIVE_NAME> {Name} {
	if(Debug.debugTokenizer)
		dump("JSP directive name");//$NON-NLS-1$
	yybegin(ST_JSP_DIRECTIVE_NAME_WHITESPACE);
	return JSP_DIRECTIVE_NAME;
}
<ST_JSP_DIRECTIVE_NAME_WHITESPACE> {S}? {
	if(Debug.debugTokenizer)
		dump("white space");//$NON-NLS-1$
	yybegin(ST_JSP_DIRECTIVE_ATTRIBUTE_NAME);
	return WHITE_SPACE;
}
<ST_JSP_DIRECTIVE_ATTRIBUTE_NAME, ST_JSP_DIRECTIVE_EQUALS> {Name} {
	if(Debug.debugTokenizer)
		dump("attr name");//$NON-NLS-1$
        yybegin(ST_JSP_DIRECTIVE_EQUALS);
        return XML_TAG_ATTRIBUTE_NAME;
}
<ST_JSP_DIRECTIVE_EQUALS> {Eq} {
	if(Debug.debugTokenizer)
		dump("equals");//$NON-NLS-1$
        yybegin(ST_JSP_DIRECTIVE_ATTRIBUTE_VALUE);
        return XML_TAG_ATTRIBUTE_EQUALS;
}
<ST_JSP_DIRECTIVE_ATTRIBUTE_VALUE> {AttValue} {
	if(Debug.debugTokenizer)
		dump("attr value");//$NON-NLS-1$
        yybegin(ST_JSP_DIRECTIVE_ATTRIBUTE_NAME);
        return XML_TAG_ATTRIBUTE_VALUE;
}
<ST_JSP_DIRECTIVE_NAME, ST_JSP_DIRECTIVE_NAME_WHITESPACE, ST_JSP_DIRECTIVE_ATTRIBUTE_NAME, ST_JSP_DIRECTIVE_EQUALS, ST_JSP_DIRECTIVE_ATTRIBUTE_VALUE> {jspScriptletEnd} {
	if(Debug.debugTokenizer)
		dump("JSP end");//$NON-NLS-1$
	if (Debug.debugTokenizer) {
		if(fStateStack.peek()!=YYINITIAL)
			System.out.println("end embedded region");//$NON-NLS-1$
	}
	yybegin(fStateStack.pop());
	return JSP_DIRECTIVE_CLOSE;
}

<YYINITIAL> {jspCommentStart} {
	if(Debug.debugTokenizer)
		dump("\nJSP comment start");//$NON-NLS-1$
	yybegin(ST_JSP_COMMENT);
	return JSP_COMMENT_OPEN;
}

<ST_XML_ATTRIBUTE_VALUE_DQUOTED> \x24\x7b {
	int enterState = yystate();
	yybegin(ST_JSP_DQUOTED_EL);
	assembleEmbeddedContainer(JSP_EL_OPEN, new String[]{JSP_EL_CLOSE, XML_TAG_ATTRIBUTE_VALUE_DQUOTE, JSP_TAG_ATTRIBUTE_VALUE_DQUOTE});
	// abort early when an unescaped double quote is found in the EL
	if(fEmbeddedContainer.getLastRegion().getType().equals(XML_TAG_ATTRIBUTE_VALUE_DQUOTE) || fEmbeddedContainer.getLastRegion().getType().equals(JSP_TAG_ATTRIBUTE_VALUE_DQUOTE)) {
		yybegin(ST_ABORT_EMBEDDED);
		fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	}
	else {
		yybegin(enterState);
	}
	return PROXY_CONTEXT;
}
<ST_XML_ATTRIBUTE_VALUE_DQUOTED> \x23\x7b {
	int enterState = yystate();
	yybegin(ST_JSP_DQUOTED_VBL);
	assembleEmbeddedContainer(JSP_VBL_OPEN, new String[]{JSP_VBL_CLOSE, XML_TAG_ATTRIBUTE_VALUE_DQUOTE, JSP_TAG_ATTRIBUTE_VALUE_DQUOTE});
	// abort early when an unescaped double quote is found in the VBL
	if(fEmbeddedContainer.getLastRegion().getType().equals(XML_TAG_ATTRIBUTE_VALUE_DQUOTE) || fEmbeddedContainer.getLastRegion().getType().equals(JSP_TAG_ATTRIBUTE_VALUE_DQUOTE)) {
		yybegin(ST_ABORT_EMBEDDED);
		fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	}
	else {
		yybegin(enterState);
	}
	return PROXY_CONTEXT;
}
<ST_XML_ATTRIBUTE_VALUE_SQUOTED> \x24\x7b {
	int enterState = yystate();
	yybegin(ST_JSP_SQUOTED_EL);
	assembleEmbeddedContainer(JSP_EL_OPEN, new String[]{JSP_EL_CLOSE, XML_TAG_ATTRIBUTE_VALUE_SQUOTE, JSP_TAG_ATTRIBUTE_VALUE_SQUOTE});
	// abort early when an unescaped single quote is found in the EL
	if(fEmbeddedContainer.getLastRegion().getType().equals(XML_TAG_ATTRIBUTE_VALUE_SQUOTE) || fEmbeddedContainer.getLastRegion().getType().equals(JSP_TAG_ATTRIBUTE_VALUE_SQUOTE)) {
		yybegin(ST_ABORT_EMBEDDED);
		fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	}
	else {
		yybegin(enterState);
	}
	return PROXY_CONTEXT;
}
<ST_XML_ATTRIBUTE_VALUE_SQUOTED> \x23\x7b {
	int enterState = yystate();
	yybegin(ST_JSP_SQUOTED_VBL);
	assembleEmbeddedContainer(JSP_VBL_OPEN, new String[]{JSP_VBL_CLOSE, XML_TAG_ATTRIBUTE_VALUE_SQUOTE, JSP_TAG_ATTRIBUTE_VALUE_SQUOTE});
	// abort early when an unescaped single quote is found in the VBL
	if(fEmbeddedContainer.getLastRegion().getType().equals(XML_TAG_ATTRIBUTE_VALUE_SQUOTE) || fEmbeddedContainer.getLastRegion().getType().equals(JSP_TAG_ATTRIBUTE_VALUE_SQUOTE)) {
		yybegin(ST_ABORT_EMBEDDED);
		fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	}
	else {
		yybegin(enterState);
	}
	return PROXY_CONTEXT;
}

// unescaped double quote, return as ending region
<ST_JSP_DQUOTED_EL,ST_JSP_DQUOTED_VBL> ["] {
	return isJspTag() ? JSP_TAG_ATTRIBUTE_VALUE_DQUOTE: XML_TAG_ATTRIBUTE_VALUE_DQUOTE;
}
// unescaped single quote, return as ending region
<ST_JSP_SQUOTED_EL,ST_JSP_SQUOTED_VBL> ['] {
	return isJspTag() ? JSP_TAG_ATTRIBUTE_VALUE_SQUOTE : XML_TAG_ATTRIBUTE_VALUE_SQUOTE;
}


// paired escaped double quotes
<ST_JSP_DQUOTED_EL> \\\" ([^\\]|\\[^"}]+)* \\\" {
	return JSP_EL_QUOTED_CONTENT;
}
// everything else EL
<ST_JSP_DQUOTED_EL> ([^\\}"]|\\[^"])+ {
	return JSP_EL_CONTENT;
}
<ST_JSP_DQUOTED_EL> \\\" {
	return JSP_EL_CONTENT;
}
// paired escaped quotes
<ST_JSP_SQUOTED_EL> \\\' ([^\\]|\\[^'}]+)* \\\' {
	return JSP_EL_QUOTED_CONTENT;
}
// everything else EL
<ST_JSP_SQUOTED_EL> ([^\\}']|\\[^'])+ {
	return JSP_EL_CONTENT;
}
<ST_JSP_SQUOTED_EL> \\\' {
	return JSP_EL_CONTENT;
}
<ST_JSP_DQUOTED_EL,ST_JSP_SQUOTED_EL> } {
	return JSP_EL_CLOSE;
}


// paired escaped quotes
<ST_JSP_DQUOTED_VBL> \\\" ([^\\]|\\[^"}]+)* \\\" {
	return JSP_VBL_QUOTED_CONTENT;
}
// everything else VBL
<ST_JSP_DQUOTED_VBL> ([^\\}"]|\\[^"])+ {
	return JSP_VBL_CONTENT;
}
<ST_JSP_DQUOTED_VBL> \\\" {
	return JSP_VBL_CONTENT;
}
// paired escaped quotes
<ST_JSP_SQUOTED_VBL> \\\' ([^\\]|\\[^'}]+)* \\\' {
	return JSP_VBL_QUOTED_CONTENT;
}
// everything else VBL
<ST_JSP_SQUOTED_VBL> ([^\\}']|\\[^'])+ {
	return JSP_VBL_CONTENT;
}
<ST_JSP_SQUOTED_VBL> \\\' {
	return JSP_VBL_CONTENT;
}

<ST_JSP_DQUOTED_VBL,ST_JSP_SQUOTED_VBL> } {
	return JSP_VBL_CLOSE;
}


// XML content area EL
<YYINITIAL> \x24\x7b[^\x7d]*/\x7d {
	yybegin(ST_JSP_EL);
	if(yylength() > 2)
		yypushback(yylength() - 2);
	fELlevel++;
	fEmbeddedHint = XML_CONTENT;
	fEmbeddedPostState = YYINITIAL;
	assembleEmbeddedContainer(JSP_EL_OPEN, JSP_EL_CLOSE);
	fEmbeddedHint = XML_CONTENT;
	yybegin(YYINITIAL);
	return PROXY_CONTEXT;
}
// XML attribute name EL
<ST_XML_ATTRIBUTE_NAME> \x24\x7b[^\x7d]*/\x7d {
	yybegin(ST_JSP_EL);
	if(yylength() > 2)
		yypushback(yylength() - 2);
	fELlevel++;
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	assembleEmbeddedContainer(JSP_EL_OPEN, JSP_EL_CLOSE);
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	yybegin(ST_XML_ATTRIBUTE_NAME);
	return PROXY_CONTEXT;
}
<ST_JSP_EL> \x24\x7b {
	fELlevel++;
	if(fELlevel == 1) {
		return JSP_EL_OPEN;
	}
}
// XML content area VBL
<YYINITIAL> \x23\x7b[^\x7d]*/\x7d {
	yybegin(ST_JSP_VBL);
	if(yylength() > 2)
		yypushback(yylength() - 2);
	fELlevel++;
	fEmbeddedHint = XML_CONTENT;
	fEmbeddedPostState = YYINITIAL;
	assembleEmbeddedContainer(JSP_VBL_OPEN, JSP_VBL_CLOSE);
	fEmbeddedHint = XML_CONTENT;
	yybegin(YYINITIAL);
	return PROXY_CONTEXT;
}
<ST_JSP_VBL> \x23\x7b {
	fELlevel++;
	if(fELlevel == 1) {
		return JSP_VBL_OPEN;
	}
}
// XML attribute name VBL
<ST_XML_ATTRIBUTE_NAME> \x23\x7b[^\x7d]*/\x7d {
	yybegin(ST_JSP_VBL);
	if(yylength() > 2)
		yypushback(yylength() - 2);
	fELlevel++;
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	assembleEmbeddedContainer(JSP_VBL_OPEN, JSP_VBL_CLOSE);
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	yybegin(ST_XML_ATTRIBUTE_NAME);
	return PROXY_CONTEXT;
}
// return anything not starting quotes or ending the VBL as content
<ST_JSP_VBL> [^\x7d\x22\x27]* {
	return JSP_VBL_CONTENT;
}
// return anything not starting quotes or ending the EL as content
<ST_JSP_EL> [^\x7d\x22\x27]* {
	return JSP_EL_CONTENT;
}


// quotes
<ST_JSP_EL> \x22 {
	yybegin(ST_JSP_EL_DQUOTES);
	return JSP_EL_DQUOTE;
}
<ST_JSP_EL_DQUOTES> \x22 {
	yybegin(ST_JSP_EL);
	return JSP_EL_DQUOTE;
}
<ST_JSP_EL_DQUOTES> [^\x22]+/\x22 {
	yybegin(ST_JSP_EL_DQUOTES_END);
	return JSP_EL_QUOTED_CONTENT;
}
<ST_JSP_EL_DQUOTES_END> \x22 {
	yybegin(ST_JSP_EL);
	return JSP_EL_DQUOTE;
}
<ST_JSP_EL> \x27 {
	yybegin(ST_JSP_EL_SQUOTES);
	return JSP_EL_SQUOTE;
}
<ST_JSP_EL_SQUOTES> \x27 {
	yybegin(ST_JSP_EL);
	return JSP_EL_SQUOTE;
}
<ST_JSP_EL_SQUOTES> [^\x27]+/\x27 {
	yybegin(ST_JSP_EL_SQUOTES_END);
	return JSP_EL_QUOTED_CONTENT;
}
<ST_JSP_EL_SQUOTES_END> \x27 {
	yybegin(ST_JSP_EL);
	return JSP_EL_SQUOTE;
}
// quotes
<ST_JSP_VBL> \x22 {
	yybegin(ST_JSP_VBL_DQUOTES);
	return JSP_VBL_DQUOTE;
}
<ST_JSP_VBL_DQUOTES> \x22 {
	yybegin(ST_JSP_VBL);
	return JSP_VBL_DQUOTE;
}
<ST_JSP_VBL_DQUOTES> [^\x22]+/\x22 {
	yybegin(ST_JSP_VBL_DQUOTES_END);
	return JSP_VBL_QUOTED_CONTENT;
}
<ST_JSP_VBL_DQUOTES_END> \x22 {
	yybegin(ST_JSP_VBL);
	return JSP_VBL_DQUOTE;
}
<ST_JSP_VBL> \x27 {
	yybegin(ST_JSP_VBL_SQUOTES);
	return JSP_VBL_SQUOTE;
}
<ST_JSP_VBL_SQUOTES> \x27 {
	yybegin(ST_JSP_VBL);
	return JSP_VBL_SQUOTE;
}
<ST_JSP_VBL_SQUOTES> [^\x27]+/\x27 {
	yybegin(ST_JSP_VBL_SQUOTES_END);
	return JSP_VBL_QUOTED_CONTENT;
}
<ST_JSP_VBL_SQUOTES_END> \x27 {
	yybegin(ST_JSP_VBL);
	return JSP_VBL_SQUOTE;
}

// unquoted content
<ST_JSP_EL> ([^}\x27\x22\x24]|\x24[^\x7b\x27\x22])+ {
	//System.out.println(JSP_EL_CONTENT+ ":[" + yytext() + "]");
	return JSP_EL_CONTENT;
}
<ST_JSP_EL> } {
	fELlevel--;
	if(fELlevel == 0) {
		yybegin(YYINITIAL);
		return JSP_EL_CLOSE;
	}
	return JSP_EL_CONTENT;
}
<ST_JSP_VBL> } {
	fELlevel--;
	if(fELlevel == 0) {
		yybegin(YYINITIAL);
		return JSP_VBL_CLOSE;
	}
	return JSP_VBL_CONTENT;
}
// EL unquoted in tag (section 2.1 declares it as valid in template text (XML_CONTENT) or attribute values
<ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> \x24\x7b[^\x7d]+/\x7d {
	if (Debug.debugTokenizer) {
		System.out.println("begin embedded region: " + fEmbeddedHint+", el-unquoted");//$NON-NLS-1$
	}
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	fStateStack.push(yystate());
	if(yylength() > 2)
		yypushback(yylength() -2);
	if(Debug.debugTokenizer)
		dump("EL in attr value");//$NON-NLS-1$
	yybegin(ST_JSP_EL);
	fELlevel++;
	assembleEmbeddedContainer(JSP_EL_OPEN, new String[]{JSP_EL_CLOSE});
	fStateStack.pop();
	yybegin(ST_XML_ATTRIBUTE_NAME);
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
	return PROXY_CONTEXT;
}
// VBL unquoted in tag or attribute values
<ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> \x23\x7b[^\x7d]+/\x7d {
	if (Debug.debugTokenizer) {
		System.out.println("begin embedded region: " + fEmbeddedHint+", el-unquoted");//$NON-NLS-1$
	}
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
	fStateStack.push(yystate());
	if(yylength() > 2)
		yypushback(yylength() -2);
	if(Debug.debugTokenizer)
		dump("VBL in attr value");//$NON-NLS-1$
	yybegin(ST_JSP_VBL);
	fELlevel++;
	assembleEmbeddedContainer(JSP_VBL_OPEN, new String[]{JSP_VBL_CLOSE});
	fStateStack.pop();
	yybegin(ST_XML_ATTRIBUTE_NAME);
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
	return PROXY_CONTEXT;
}

<ST_JSP_COMMENT> .|\r|\n {
	if(Debug.debugTokenizer)
		dump("\nJSP comment text");//$NON-NLS-1$
	return scanJSPCommentText();
}
<ST_JSP_COMMENT_END> {jspCommentEnd} {
	if(Debug.debugTokenizer)
		dump("\nJSP comment close");//$NON-NLS-1$
	yybegin(YYINITIAL);
	return JSP_COMMENT_CLOSE;
}

// XML misc

{CDStart} {
	if(Debug.debugTokenizer)
		dump("\nCDATA start");//$NON-NLS-1$
	fStateStack.push(yystate());
	yybegin(ST_CDATA_TEXT);
	return XML_CDATA_OPEN;
}
<ST_CDATA_TEXT> .|\r|\n {
	if(Debug.debugTokenizer)
		dump("CDATA text");//$NON-NLS-1$
	fEmbeddedPostState = ST_CDATA_TEXT;
	fEmbeddedHint = XML_CDATA_TEXT;
	String returnedContext = doScan("]]>", false, true, true, XML_CDATA_TEXT, ST_CDATA_END,  ST_CDATA_END);//$NON-NLS-1$
	if(returnedContext == XML_CDATA_TEXT)
		yybegin(ST_CDATA_END);
	return returnedContext;
}
<ST_CDATA_END> {CDEnd} {
	if(Debug.debugTokenizer)
		dump("CDATA end");//$NON-NLS-1$
	yybegin(fStateStack.pop());
	return XML_CDATA_CLOSE;
}

<YYINITIAL> {PEReference} {
	if(Debug.debugTokenizer)
		dump("\nPEReference");//$NON-NLS-1$
	return XML_PE_REFERENCE;
}
<YYINITIAL> {CharRef} {
	if(Debug.debugTokenizer)
		dump("\nCharRef");//$NON-NLS-1$
	return XML_CHAR_REFERENCE;
}
<YYINITIAL> {EntityRef} {
	if(Debug.debugTokenizer)
		dump("\nEntityRef");//$NON-NLS-1$
	return XML_ENTITY_REFERENCE;
}

<YYINITIAL> {PIstart} {
	if(Debug.debugTokenizer)
		dump("\nprocessing instruction start");//$NON-NLS-1$
	yybegin(ST_PI);
        return XML_PI_OPEN;
}
// the next four are order dependent
<ST_PI> ((X|x)(M|m)(L|l)) {
	if(Debug.debugTokenizer)
		dump("XML processing instruction target");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_PI_ATTRIBUTE_NAME);
        return XML_TAG_NAME;
}
<ST_PI> ([iI][mM][pP][oO][rR][tT]) {
	if(Debug.debugTokenizer)
		dump("DHTML processing instruction target");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_DHTML_ATTRIBUTE_NAME);
        return XML_TAG_NAME;
}
<ST_PI> xml-stylesheet {
	if(Debug.debugTokenizer)
		dump("XSL processing instruction target");//$NON-NLS-1$
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_PI_ATTRIBUTE_NAME);
        return XML_TAG_NAME;
}
<ST_PI> {Name} {
	if(Debug.debugTokenizer)
		dump("processing instruction target");//$NON-NLS-1$
	fEmbeddedHint = XML_CONTENT;
        yybegin(ST_PI_WS);
        return XML_TAG_NAME;
}
<ST_PI_WS> {S}+ {
        yybegin(ST_PI_CONTENT);
        return WHITE_SPACE;
}
<ST_PI, ST_PI_WS> \?> {
	if(Debug.debugTokenizer)
		dump("processing instruction end");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
        yybegin(YYINITIAL);
        return XML_PI_CLOSE;
}
<ST_PI_CONTENT> . {
		// block scan until close is found
	return doScan("?>", false, false, false, XML_PI_CONTENT, ST_XML_PI_TAG_CLOSE, ST_XML_PI_TAG_CLOSE);
}
<ST_PI_CONTENT,ST_XML_PI_TAG_CLOSE> \?> {
		// ended with nothing inside
		fEmbeddedHint = UNDEFINED;
        yybegin(YYINITIAL);
        return XML_PI_CLOSE;
}

<ST_XML_PI_ATTRIBUTE_NAME, ST_XML_PI_EQUALS> {Name} {
	if(Debug.debugTokenizer)
		dump("XML processing instruction attribute name");//$NON-NLS-1$
        yybegin(ST_XML_PI_EQUALS);
        return XML_TAG_ATTRIBUTE_NAME;
}
<ST_XML_PI_EQUALS> {Eq} {
	if(Debug.debugTokenizer)
		dump("XML processing instruction '='");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
        yybegin(ST_XML_PI_ATTRIBUTE_VALUE);
        return XML_TAG_ATTRIBUTE_EQUALS;
}
/* the value was found, look for the next name */
<ST_XML_PI_ATTRIBUTE_VALUE> {AttValue} {
	if(Debug.debugTokenizer)
		dump("XML processing instruction attribute value");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_XML_PI_ATTRIBUTE_NAME);
        return XML_TAG_ATTRIBUTE_VALUE;
}
/* the PI's close was found */
<ST_XML_PI_EQUALS, ST_XML_PI_ATTRIBUTE_NAME, ST_XML_PI_ATTRIBUTE_VALUE> {PIend} {
	if(Debug.debugTokenizer)
		dump("XML processing instruction end");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
        yybegin(YYINITIAL);
        return XML_PI_CLOSE;
}
// DHTML
<ST_DHTML_ATTRIBUTE_NAME, ST_DHTML_EQUALS> {Name} {
	if(Debug.debugTokenizer)
		dump("DHTML processing instruction attribute name");//$NON-NLS-1$
        yybegin(ST_DHTML_EQUALS);
        return XML_TAG_ATTRIBUTE_NAME;
}
<ST_DHTML_EQUALS> {Eq} {
	if(Debug.debugTokenizer)
		dump("DHTML processing instruction '='");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_VALUE;
	fEmbeddedPostState = ST_XML_ATTRIBUTE_NAME;
        yybegin(ST_DHTML_ATTRIBUTE_VALUE);
        return XML_TAG_ATTRIBUTE_EQUALS;
}
/* the value was found, look for the next name */
<ST_DHTML_ATTRIBUTE_VALUE> {AttValue} | ([\'\"]([^\'\"\040\011\012\015<>/]|\/+[^\'\"\040\011\012\015<>/] )* ) {
	if(Debug.debugTokenizer)
		dump("DHTML processing instruction attribute value");//$NON-NLS-1$
	fEmbeddedHint = XML_TAG_ATTRIBUTE_NAME;
	fEmbeddedPostState = ST_XML_EQUALS;
        yybegin(ST_DHTML_ATTRIBUTE_NAME);
        return XML_TAG_ATTRIBUTE_VALUE;
}
/* The DHTML PI's close was found */
<ST_DHTML_EQUALS, ST_DHTML_ATTRIBUTE_NAME, ST_DHTML_ATTRIBUTE_VALUE> [/]*> {
	if(Debug.debugTokenizer)
		dump("DHTML processing instruction end");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
        yybegin(YYINITIAL);
        return XML_PI_CLOSE;
}

// XML declarations

<YYINITIAL, ST_XML_TAG_NAME, ST_XML_EQUALS, ST_XML_ATTRIBUTE_NAME, ST_XML_ATTRIBUTE_VALUE, ST_JSP_ATTRIBUTE_VALUE> {genericTagOpen}! {
	fStateStack.push(yystate());
	if(Debug.debugTokenizer)
		dump("\ndeclaration start");//$NON-NLS-1$
        yybegin(ST_XML_DECLARATION);
	return XML_DECLARATION_OPEN;
}
<ST_XML_DECLARATION> [Ee][Ll][Ee][Mm][Ee][Nn][Tt] {
	if(Debug.debugTokenizer)
		dump("element");//$NON-NLS-1$
	yybegin(ST_XML_ELEMENT_DECLARATION);
	return XML_ELEMENT_DECLARATION;
}
<ST_XML_DECLARATION> [Dd][Oo][Cc][Tt][Yy][Pp][Ee] {
	if(Debug.debugTokenizer)
		dump("doctype");//$NON-NLS-1$
	yybegin(ST_XML_DOCTYPE_DECLARATION);
	return XML_DOCTYPE_DECLARATION;
}
<ST_XML_DECLARATION> [Aa][Tt][Tt][Ll][Ii][Ss][Tt] {
	if(Debug.debugTokenizer)
		dump("attlist");//$NON-NLS-1$
	yybegin(ST_XML_ATTLIST_DECLARATION);
	return XML_ATTLIST_DECLARATION;
}

// begin DOCTYPE handling procedure
<ST_XML_DOCTYPE_DECLARATION, ST_XML_DOCTYPE_EXTERNAL_ID, ST_XML_DOCTYPE_ID_SYSTEM, ST_XML_DOCTYPE_ID_PUBLIC, ST_XML_DECLARATION_CLOSE> \[[^\]]*\] {
	return XML_DOCTYPE_INTERNAL_SUBSET;
}

<ST_XML_DOCTYPE_DECLARATION> {Name} {
	if(Debug.debugTokenizer)
		dump("doctype type");//$NON-NLS-1$
	yybegin(ST_XML_DOCTYPE_EXTERNAL_ID);
	return XML_DOCTYPE_NAME;
}
<ST_XML_DOCTYPE_EXTERNAL_ID> [Pp][Uu][Bb][Ll][Ii][Cc] {
	if(Debug.debugTokenizer)
		dump("doctype external id");//$NON-NLS-1$
	fEmbeddedHint = XML_DOCTYPE_EXTERNAL_ID_PUBREF;
	yybegin(ST_XML_DOCTYPE_ID_PUBLIC);
	return XML_DOCTYPE_EXTERNAL_ID_PUBLIC;
}
<ST_XML_DOCTYPE_EXTERNAL_ID> [Ss][Yy][Ss][Tt][Ee][Mm] {
	if(Debug.debugTokenizer)
		dump("doctype external id");//$NON-NLS-1$
	fEmbeddedHint = XML_DOCTYPE_EXTERNAL_ID_SYSREF;
	yybegin(ST_XML_DOCTYPE_ID_SYSTEM);
	return XML_DOCTYPE_EXTERNAL_ID_SYSTEM;
}
<ST_XML_DOCTYPE_ID_PUBLIC> {AttValue}|{PubidLiteral} {
	if(Debug.debugTokenizer)
		dump("doctype public reference");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
	fEmbeddedPostState = YYINITIAL;
	yybegin(ST_XML_DOCTYPE_ID_SYSTEM);
	return XML_DOCTYPE_EXTERNAL_ID_PUBREF;
}
<ST_XML_DOCTYPE_ID_SYSTEM> {AttValue}|{SystemLiteral} {
	if(Debug.debugTokenizer)
		dump("doctype system reference");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
	fEmbeddedPostState = YYINITIAL;
	yybegin(ST_XML_DECLARATION_CLOSE);
	return XML_DOCTYPE_EXTERNAL_ID_SYSREF;
}
// end DOCTYPE handling

// begin ELEMENT handling procedure
<ST_XML_ELEMENT_DECLARATION> {AttValue}|{PubidLiteral} {
	if(Debug.debugTokenizer)
		dump("elementdecl name");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
	fEmbeddedPostState = YYINITIAL;
	yybegin(ST_XML_ELEMENT_DECLARATION_CONTENT);
	return XML_ELEMENT_DECL_NAME;
}
<ST_XML_ELEMENT_DECLARATION_CONTENT> [^>]* {
	if(Debug.debugTokenizer)
		dump("elementdecl contentspec");//$NON-NLS-1$
	return XML_ELEMENT_DECL_CONTENT;
}

<ST_XML_ELEMENT_DECLARATION_CONTENT> {genericTagClose} {
	if(Debug.debugTokenizer)
		dump("elementdecl close");//$NON-NLS-1$
	if (Debug.debugTokenizer) {
		if(fStateStack.peek()!=YYINITIAL)
			System.out.println("end embedded region");//$NON-NLS-1$
	}
	yybegin(fStateStack.pop());
	return XML_DECLARATION_CLOSE;
}
// end ELEMENT handling

// begin ATTLIST handling procedure
<ST_XML_ATTLIST_DECLARATION> {AttValue}|{PubidLiteral} {
	if(Debug.debugTokenizer)
		dump("attlist name");//$NON-NLS-1$
	fEmbeddedHint = UNDEFINED;
	fEmbeddedPostState = YYINITIAL;
	yybegin(ST_XML_ATTLIST_DECLARATION_CONTENT);
	return XML_ATTLIST_DECL_NAME;
}
<ST_XML_ATTLIST_DECLARATION_CONTENT> [^>]* {
	if(Debug.debugTokenizer)
		dump("attlist contentspec");//$NON-NLS-1$
	return XML_ATTLIST_DECL_CONTENT;
}

<ST_XML_ATTLIST_DECLARATION_CONTENT> {genericTagClose} {
	if(Debug.debugTokenizer)
		dump("attlist close");//$NON-NLS-1$
	if (Debug.debugTokenizer) {
		if(fStateStack.peek()!=YYINITIAL)
			System.out.println("end embedded region");//$NON-NLS-1$
	}
	yybegin(fStateStack.pop());
	return XML_DECLARATION_CLOSE;
}
// end ATTLIST handling

<ST_XML_DECLARATION, ST_XML_DOCTYPE_DECLARATION, ST_XML_DOCTYPE_EXTERNAL_ID, ST_XML_ATTLIST_DECLARATION, ST_XML_ELEMENT_DECLARATION, ST_XML_DECLARATION_CLOSE, ST_XML_DOCTYPE_ID_PUBLIC, ST_XML_DOCTYPE_ID_SYSTEM, ST_XML_DOCTYPE_EXTERNAL_ID> {genericTagClose} {
	if(Debug.debugTokenizer)
		dump("declaration end");//$NON-NLS-1$
	if (Debug.debugTokenizer) {
		if(fStateStack.peek()!=YYINITIAL)
			System.out.println("end embedded region");//$NON-NLS-1$
	}
	yybegin(fStateStack.pop());
	return XML_DECLARATION_CLOSE;
}
// end DECLARATION handling

<YYINITIAL> ([^<&%\x24\x23]*|\x23+|\x24+|[&%]{S}+{Name}[^&%<]*|[\x24\x23][^\x7b<&%][^<&%\x24\x23]*|\\[\x24\x23][\x7b]|[&%]{Name}([^;&%<]*|{S}+;*)) {
	if(Debug.debugTokenizer)
		dump("\nXML content");//$NON-NLS-1$
	return XML_CONTENT;
}

<YYINITIAL> (%)+ {
	if(Debug.debugTokenizer)
		dump("non-reference %");//$NON-NLS-1$
	return XML_CONTENT;
}

<ST_BLOCK_TAG_SCAN> .|\r|\n {
		return doBlockTagScan();
	}

. {
	if (Debug.debugTokenizer)
		System.out.println("!!!unexpected!!!: \"" + yytext() + "\":" + //$NON-NLS-2$//$NON-NLS-1$
			yychar + "-" + (yychar + yylength()));//$NON-NLS-1$
	return UNDEFINED;
}

\040 {
	if(Debug.debugTokenizer)
		dump("SPACE");//$NON-NLS-1$
	return WHITE_SPACE;
}
\011 {
	if(Debug.debugTokenizer)
		dump("0x9");//$NON-NLS-1$
	return WHITE_SPACE;
}
\015 
{
	if(Debug.debugTokenizer)
		dump("CARRIAGE RETURN");//$NON-NLS-1$
	return WHITE_SPACE;
}
\012 {
	if(Debug.debugTokenizer)
		dump("LINE FEED");//$NON-NLS-1$
	return WHITE_SPACE;
}
