/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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
package org.eclipse.jst.jsp.ui.internal.text;

import java.io.IOException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;


/**
 * Helper class for match pairs of characters.
 *
 * Copied from org.eclipse.jdt.internal.ui.text so we don't have to
 * depend on the org.eclipse.jdt.ui plugin.
 * 
 * No modifications were made.
 */
class JavaPairMatcher implements ICharacterPairMatcher {

	protected char[] fPairs;
	protected IDocument fDocument;
	protected int fOffset;

	protected int fStartPos;
	protected int fEndPos;
	protected int fAnchor;

	protected JavaCodeReader fReader = new JavaCodeReader();


	public JavaPairMatcher(char[] pairs) {
		fPairs = pairs;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.ICharacterPairMatcher#match(org.eclipse.jface.text.IDocument, int)
	 */
	public IRegion match(IDocument document, int offset) {

		fOffset = offset;

		if (fOffset < 0)
			return null;

		fDocument = document;

		if (fDocument != null && matchPairsAt() && fStartPos != fEndPos)
			return new Region(fStartPos, fEndPos - fStartPos + 1);

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.ICharacterPairMatcher#getAnchor()
	 */
	public int getAnchor() {
		return fAnchor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.ICharacterPairMatcher#dispose()
	 */
	public void dispose() {
		clear();
		fDocument = null;
		fReader = null;
	}

	/*
	 * @see org.eclipse.jface.text.source.ICharacterPairMatcher#clear()
	 */
	public void clear() {
		if (fReader != null) {
			try {
				fReader.close();
			}
			catch (IOException x) {
				// ignore
			}
		}
	}

	protected boolean matchPairsAt() {

		int i;
		int pairIndex1 = fPairs.length;
		int pairIndex2 = fPairs.length;

		fStartPos = -1;
		fEndPos = -1;

		// get the chars preceding and following the start position
		try {

			char prevChar = fDocument.getChar(Math.max(fOffset - 1, 0));
			// modified behavior for http://dev.eclipse.org/bugs/show_bug.cgi?id=16879			
			//			char nextChar= fDocument.getChar(fOffset);

			// search for opening peer character next to the activation point
			for (i = 0; i < fPairs.length; i = i + 2) {
				//				if (nextChar == fPairs[i]) {
				//					fStartPos= fOffset;
				//					pairIndex1= i;
				//				} else 
				if (prevChar == fPairs[i]) {
					fStartPos = fOffset - 1;
					pairIndex1 = i;
				}
			}

			// search for closing peer character next to the activation point
			for (i = 1; i < fPairs.length; i = i + 2) {
				if (prevChar == fPairs[i]) {
					fEndPos = fOffset - 1;
					pairIndex2 = i;
				}
				//				else if (nextChar == fPairs[i]) {
				//					fEndPos= fOffset;
				//					pairIndex2= i;
				//				}
			}
			
			/*
			 * If match for previous character fails , look for next character now.
			 * This is required for cases like <abc|>, cursor placed at |.
			 */
			if (fEndPos == -1 && fStartPos == -1) {
				char nextChar = fDocument.getChar(Math.max(fOffset, 0));
				for (i = 0; i < fPairs.length; i = i + 2) {
					if (nextChar == fPairs[i]) {
						fStartPos = fOffset;
						pairIndex1 = i;
					}
				}

				for (i = 1; i < fPairs.length; i = i + 2) {
					if (nextChar == fPairs[i]) {
						fEndPos = fOffset;
						pairIndex2 = i;
					}
				}
			}

			if (fEndPos > -1) {
				fAnchor = RIGHT;
				fStartPos = searchForOpeningPeer(fEndPos, fPairs[pairIndex2 - 1], fPairs[pairIndex2], fDocument);
				if (fStartPos > -1)
					return true;
				else
					fEndPos = -1;
			}
			else if (fStartPos > -1) {
				fAnchor = LEFT;
				fEndPos = searchForClosingPeer(fStartPos, fPairs[pairIndex1], fPairs[pairIndex1 + 1], fDocument);
				if (fEndPos > -1)
					return true;
				else
					fStartPos = -1;
			}

		}
		catch (BadLocationException x) {
		}
		catch (IOException x) {
		}

		return false;
	}

	protected int searchForClosingPeer(int offset, int openingPeer, int closingPeer, IDocument document) throws IOException {

		fReader.configureForwardReader(document, offset + 1, document.getLength(), false, true);

		int stack = 1;
		int c = fReader.read();
		while (c != JavaCodeReader.EOF) {
			if (c == openingPeer && c != closingPeer)
				stack++;
			else if (c == closingPeer)
				stack--;

			if (stack == 0)
				return fReader.getOffset();

			c = fReader.read();
		}

		return -1;
	}

	protected int searchForOpeningPeer(int offset, int openingPeer, int closingPeer, IDocument document) throws IOException {

		fReader.configureBackwardReader(document, offset, false, true);

		int stack = 1;
		int c = fReader.read();
		while (c != JavaCodeReader.EOF) {
			if (c == closingPeer && c != openingPeer)
				stack++;
			else if (c == openingPeer)
				stack--;

			if (stack == 0)
				return fReader.getOffset();

			c = fReader.read();
		}

		return -1;
	}
}
