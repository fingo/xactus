/*******************************************************************************
 * Copyright (c) 2008, 2019 Jesper Steen M�ller
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jesper Steen M�ller - initial XSL launching test
 *     David Carver (STAR) - bug 262046 - refactored for better reliability.
 *******************************************************************************/

package org.eclipse.wst.xsl.launching.tests.testcase;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.xsl.launching.tests.AbstractLaunchingTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XSLLaunchingTest extends AbstractLaunchingTest {

	private static final String TRANSFORM_COMMENTS = "TransformComments";
	private static final String SIMPLE_TRANSFORM = "SimpleTransform";

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		IPath path = folder.getFullPath();
		copyConfigurationToWorkspace(path, "SimpleTransform.launch");
		copyConfigurationToWorkspace(path, "TransformComments.launch");
		testProject.refreshLocal(IResource.DEPTH_INFINITE,
				new NullProgressMonitor());
		while (testProject.isSynchronized(IResource.DEPTH_INFINITE) == false) {
			Thread.sleep(100);
		}
	}

	@After
	@Override
	public void tearDown() throws Exception {
		env.dispose();
		super.tearDown();
	}
	
	@Test
	public void testSimpleTransformation() throws Exception {
		IPath folder = testProject.getFullPath();
		env.addFileFromResource(folder, "1-input.xml", "1-input.xml");
		env.addFileFromResource(folder, "1-transform.xsl", "1-transform.xsl");
		refreshProject();

		launchConfiguration(SIMPLE_TRANSFORM);
		IFile output = testProject.getFile("1-input.out.xml");
		Document doc = parseXml(output.getContents(true));
		assertEquals("root-out", doc.getDocumentElement().getNodeName());
	}

	/**
	 * Test to make sure comments are being copied out to the output file. bug
	 * 253703
	 * 
	 * @throws CoreException
	 * @throws InterruptedException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	@Test
	public void testTransformComments() throws Exception {
		IPath folder = testProject.getFullPath();
		env.addFileFromResource(folder, "testCommentInput.xml",
				"testCommentInput.xml");
		env.addFileFromResource(folder, "testComments.xsl", "testComments.xsl");
		env.addFileFromResource(folder, "expected.xml",
				"testCommentsExpected.xml");
		refreshProject();

		launchConfiguration(TRANSFORM_COMMENTS);
		IFile output = testProject.getFile("testCommentInput.out.xml");
		IFile expected = testProject.getFile("expected.xml");

		String result = readFile(output.getContents());
		String wanted = readFile(expected.getContents());

		assertEquals("Unexpected results:", wanted, result);
	}
	

}
