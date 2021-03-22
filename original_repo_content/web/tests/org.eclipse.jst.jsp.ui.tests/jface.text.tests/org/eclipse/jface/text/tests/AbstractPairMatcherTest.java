/*******************************************************************************
 * Copyright (c) 2006, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Christian Plesner Hansen (plesner@quenta.org) - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.text.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextStore;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

/**
 * Generic test of simple character pair matchers
 *
 * @since 3.3
 */
public abstract class AbstractPairMatcherTest extends TestCase {

	/**
	 * Constructs a new character pair matcher.
	 * 
	 * @param chars the characters to match
	 * @return the character pair matcher
	 */
	protected abstract ICharacterPairMatcher createMatcher(final String chars);

	/**
	 * Returns the partitioning treated by the matcher.
	 * 
	 * @return the partition
	 */
	protected abstract String getDocumentPartitioning();

	public AbstractPairMatcherTest(String name) {
		super(name);
	}

	public AbstractPairMatcherTest() {
		super();
	}

	/* --- T e s t s --- */

	/** Tests that the test case reader works */
	public void testTestCaseReader() {
		performReaderTest("#( )%", 3,  0,  "( )");
		performReaderTest("%( )#", 0,  3,  "( )");
		performReaderTest("( )%",  3,  -1, "( )");
		performReaderTest("#%",    0,  0,  "");
	}

	/**
	 * Very simple checks.
	 * 
	 * @throws BadLocationException
	 */
	public void testSimpleMatchSameMatcher() throws BadLocationException {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		performMatch(matcher, "#(   )%");
		performMatch(matcher, "#[   ]%");
		performMatch(matcher, "#{   }%");
		performMatch(matcher, "(%   )#");
		performMatch(matcher, "[%   ]#");
		performMatch(matcher, "{%   }#");
		matcher.dispose();
	}

	/**
	 * Very simple checks.
	 * 
	 * @throws BadLocationException
	 */
	public void testSimpleMatchDifferentMatchers() throws BadLocationException {
		performMatch("()[]{}", "#(   )%");
		performMatch("()[]{}", "#[   ]%");
		performMatch("()[]{}", "#{   }%");
		performMatch("()[]{}", "(%   )#");
		performMatch("()[]{}", "[%   ]#");
		performMatch("()[]{}", "{%   }#");
	}

	/**
	 * Close matches.
	 * 
	 * @throws BadLocationException
	 */
	public void testCloseMatches() throws BadLocationException {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		performMatch(matcher, "#()%");
		performMatch(matcher, "(%)#");
		performMatch(matcher, "#(())%");
		performMatch(matcher, "(%())#");
		performMatch(matcher, "((%)#)");
		performMatch(matcher, "(#()%)");
		matcher.dispose();
	}


	/**
	 * Checks of simple situations where no matches should be found.
	 * 
	 * @throws BadLocationException
	 */
	public void testIncompleteMatch() throws BadLocationException {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		performMatch(matcher, "(% ");
		performMatch(matcher, "%(  )");
		performMatch(matcher, "( % )");
		performMatch(matcher, "(  %)");
		performMatch(matcher, "%");
		matcher.dispose();
	}

	/**
	 * Test that it doesn't match across different partitions.
	 * 
	 * @throws BadLocationException
	 */
	public void testPartitioned() throws BadLocationException {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		performMatch(matcher, "(% |a a| )#");
		performMatch(matcher, "#( |a a| )%");
		performMatch(matcher, "|b #( )% b|");
		performMatch(matcher, "( |b )% b|");
		performMatch(matcher, "(% |b ) b|");
		performMatch(matcher, "|a ( a| )%");
		performMatch(matcher, "|a (% a| )");
		performMatch(matcher, "|c #( c| ) ( |c )% c|");
		performMatch(matcher, "|c (% c| ) ( |c )# c|");
		performMatch(matcher, "(% |a ) a| |b ) b| |c ) c| )#");
		matcher.dispose();
	}

	/**
	 * Test that it works properly next to partition boundaries.
	 * 
	 * @throws BadLocationException
	 */
	public void testTightPartitioned() throws BadLocationException {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		performMatch(matcher, "(|b)%b|");
		performMatch(matcher, "(%|b)b|");
		performMatch(matcher, "|a(a|)%");
		performMatch(matcher, "|a(%a|)");
		performMatch(matcher, "|c#(c|)(|c)%c|");
		performMatch(matcher, "|c(%c|)(|c)#c|");
		performMatch(matcher, "(%|a)a||b)b||c)c|)#");
		matcher.dispose();
	}

	/** Test that nesting works properly */
	public void testNesting() {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		performMatch(matcher, " ( #( ( ( ) ) ( ) )% ) ");
		performMatch(matcher, " ( (% ( ( ) ) ( ) )# ) ");
		performMatch(matcher, " ( #( { ( ) } [ ] )% ) ");
		performMatch(matcher, " ( (% { ( ) } [ ] )# ) ");
		performMatch(matcher, " ( ( #{ ( ) }% [ ] ) ) ");
		performMatch(matcher, " ( ( {% ( ) }# [ ] ) ) ");
		performMatch(matcher, "a(b#(c(d(e)f)g(h)i)%j)k");
		performMatch(matcher, "a(b(%c(d(e)f)g(h)i)#j)k");
		performMatch(matcher, "a(b#(c{d(e)f}g[h]i)%j)k");
		performMatch(matcher, "a(b(%c{d(e)f}g[h]i)#j)k");
		performMatch(matcher, "a(b(c#{d(e)f}%g[h]i)j)k");
		performMatch(matcher, "a(b(c{%d(e)f}#g[h]i)j)k");
		matcher.dispose();
	}

	/**
	 * Test a few boundary conditions.
	 * 
	 * * @throws BadLocationException
	 */
	public void testBoundaries() throws BadLocationException {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}");
		final StringDocument doc= new StringDocument("abcdefghijkl");
		assertNull(matcher.match(null, 0));
		assertNull(matcher.match(doc, -1));
		assertNull(matcher.match(doc, doc.getLength() + 1));
		matcher.dispose();
	}

	public void testBug156426() {
		final ICharacterPairMatcher matcher= createMatcher("()[]{}<>");
		performMatch(matcher, " #( a < b )% ");
		performMatch(matcher, " (% a < b )# ");
		performMatch(matcher, " #( a > b )% ");
		performMatch(matcher, " (% a > b )# ");
		matcher.dispose();
	}

	/* --- U t i l i t i e s --- */

	/**
	 * Checks that the test case reader reads the test case as specified.
	 * 
	 * @param testString the string to test
	 * @param expectedPos the expected position
	 * @param expectedMatch the expected match
	 * @param expectedString the expected string
	 */
	private void performReaderTest(String testString, int expectedPos, int expectedMatch, String expectedString) {
		TestCase t0= createTestCase(testString);
		assertEquals(expectedPos, t0.fPos);
		assertEquals(expectedMatch, t0.fMatch);
		assertEquals(expectedString, t0.fString);
	}

	/**
	 * Checks that the given matcher matches the input as specified.
	 * 
	 * @param matcher the matcher
	 * @param testCase the test string
	 */
	protected void performMatch(final ICharacterPairMatcher matcher, final String testCase) {
		final TestCase test= createTestCase(testCase);
		matcher.clear();
		final IRegion region= matcher.match(test.getDocument(), test.fPos);
		if (test.fMatch == -1) {
			// if no match point has been specified there should be
			// no match
			if (region != null) System.out.println(region.getOffset());
			assertNull(region);
		} else {
			assertNotNull(region);
			final boolean isForward= test.fPos > test.fMatch;
			assertEquals(isForward, matcher.getAnchor() == ICharacterPairMatcher.RIGHT);
			// If the match is forward, the curser is one character
			// after the start of the match, so we need to count one
			// step backwards
			final int offset= isForward ? test.getOffset() : test.getOffset() - 1;
			final int length= isForward ? test.getLength() : test.getLength() + 1;
			assertEquals(length, region.getLength());
			assertEquals(offset, region.getOffset());
		}
	}

	private void performMatch(final String delims, final String testCase) {
		final ICharacterPairMatcher matcher= createMatcher(delims);
		performMatch(matcher, testCase);
		matcher.dispose();
	}

	/**
	 * Creates a text case from a string. In the given string a '%' represents the position of the
	 * cursor and a '#' represents the position of the expected matching character.
	 * 
	 * @param str the string for which to create the test case
	 * @return the created test case
	 */
	public TestCase createTestCase(String str) {
		int pos= str.indexOf("%");
		assertFalse(pos == -1);
		int match= str.indexOf("#");
		// account for the length of the first position marker,
		// if there is one
		if (match != -1 && match < pos) pos -= 1;
		if (pos < match) match -= 1;
		final String stripped= str.replaceAll("%", "").replaceAll("#", "");
		return new TestCase(stripped, pos, match);
	}

	private class TestCase {

		public final String fString;
		public final int fPos, fMatch;

		public TestCase(String string, int pos, int match) {
			fString= string;
			fPos= pos;
			fMatch= match;
		}

		public IDocument getDocument() {
			return new StringDocument(fString);
		}

		public int getLength() {
			return Math.abs(fPos - fMatch);
		}

		public int getOffset() {
			if (fPos > fMatch) return fMatch;
			return fPos;
		}

	}

	private class StringDocument extends Document {

		public StringDocument(String str) {
			this.setTextStore(new StringTextStore(str));
			this.set(str);
			final IDocumentPartitioner part= createPartitioner();
			this.setDocumentPartitioner(getDocumentPartitioning(), part);
			part.connect(this);
		}

	}

	private static class StringTextStore implements ITextStore {

		private String fString;

		public StringTextStore(final String str) {
			fString= str;
		}

		public char get(int offset) {
			return fString.charAt(offset);
		}

		public String get(int offset, int length) {
			return fString.substring(offset, offset + length);
		}

		public int getLength() {
			return fString.length();
		}

		public void replace(int offset, int length, String text) {
			throw new UnsupportedOperationException();
		}

		public void set(String text) {
			fString= text;
		}

	}

	private static String DEFAULT_PARTITION= IDocument.DEFAULT_CONTENT_TYPE;

	private static IDocumentPartitioner createPartitioner() {
		final RuleBasedPartitionScanner scan= new RuleBasedPartitionScanner();
		final List/*<IPredicateRule>*/ rules= new ArrayList/*<IPredicateRule>*/();
		rules.add(new SingleLineRule("|a", "a|", new Token("a")));
		rules.add(new SingleLineRule("|b", "b|", new Token("b")));
		rules.add(new SingleLineRule("|c", "c|", new Token("c")));
		scan.setPredicateRules((IPredicateRule[]) rules.toArray(new IPredicateRule[rules.size()]));
		scan.setDefaultReturnToken(new Token(DEFAULT_PARTITION));
		return new FastPartitioner(scan, new String[] { DEFAULT_PARTITION, "a", "b", "c" });
	}

}
