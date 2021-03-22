/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.text.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.wst.sse.core.internal.ltk.modelhandler.IModelHandler;
import org.eclipse.wst.sse.core.internal.modelhandler.ModelHandlerRegistry;
import org.eclipse.wst.sse.core.internal.text.BasicStructuredDocument;



public class PositionUpdatingCornerCasesTest extends TestCase {
	private IDocument fDocument;

	public PositionUpdatingCornerCasesTest(String name) {
		super(name);
	}

	protected void checkPositions(Position[] expected) throws BadPositionCategoryException {
		Position[] actual= fDocument.getPositions(IDocument.DEFAULT_CATEGORY);
		assertTrue("invalid number of positions", actual.length == expected.length);

		for (int i= 0; i < expected.length; i++) {
			assertEquals(print(actual[i]) + " != " + print(expected[i]), expected[i], actual[i]);
		}
	}

	protected String print(Position p) {
		return "[" + p.getOffset() + "," + p.getLength() + "]";
	}

	protected void setUp() throws Exception {
//		fDocument= new Document("x-x-x-x-x-x-x-x-x-x-x");
		fDocument= createDocument("x-x-x-x-x-x-x-x-x-x-x");
		fDocument.addPosition(new Position(0, 0));
		fDocument.addPosition(new Position(0, 1));
		fDocument.addPosition(new Position(5, 0));
		fDocument.addPosition(new Position(5, 3));
	}

	private IDocument createDocument(String contents) {
		IModelHandler handler = ModelHandlerRegistry.getInstance().getHandlerForContentTypeId("org.eclipse.jst.jsp.jspsource");
		BasicStructuredDocument document = (BasicStructuredDocument) handler.getDocumentLoader().createNewStructuredDocument();
		document.set(contents);
		return document;
	}

	public static Test suite() {
		return new TestSuite(PositionUpdatingCornerCasesTest.class);
	}

	protected void tearDown() {
		fDocument= null;
	}

	public void testInsert() throws Exception {
		fDocument.replace(0, 0, "yy");

		Position[] positions= new Position[] {
				new Position( 2, 1),
				new Position( 2, 0),
				new Position( 7, 3),
				new Position( 7, 0)
		};

		checkPositions(positions);
	}
}
