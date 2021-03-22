/*******************************************************************************
 * Copyright (c) 2010, 2020 IBM Corporation and others.
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
package org.eclipse.wst.xml.ui.tests.contentassist;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.sse.core.utils.StringUtils;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.eclipse.wst.xml.ui.tests.ProjectUtil;

import junit.extensions.TestSetup;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestXMLContentAssistComputers extends TestCase {
	/** The name of the project that all of these tests will use */
	private static final String PROJECT_NAME = "TestXMLContentAssistComputers";
	
	/** The location of the testing files */
	private static final String PROJECT_FILES = "/testresources/contentassist";
	
	/** The project that all of the tests use */
	private static IProject fProject;
	/**
	 * Used to keep track of the already open editors so that the tests don't go through
	 * the trouble of opening the same editors over and over again
	 */
	private static Map fFileToEditorMap = new HashMap();
	
	/**
	 * <p>Default constructor<p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @see #suite()
	 */
	public TestXMLContentAssistComputers() {
		super("Test XML Content Assist Computers");
	}
	
	/**
	 * <p>Constructor that takes a test name.</p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @param name The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public TestXMLContentAssistComputers(String name) {
		super(name);
	}
	
	/**
	 * <p>Use this method to add these tests to a larger test suite so set up
	 * and tear down can be performed</p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this class
	 * with set up and tear down.
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite(TestXMLContentAssistComputers.class, "Test XML Content Assist Computers");
		return new TestXMLContentAssistComputersSetup(ts);
	}
	
	public void testChildElementProposals1() throws Exception {
		// default page, templates page, tags page, default page again
		int[] expectedProposalCounts = new int[] {5, 2, 3};
		runProposalTest("test1.xml", 11, 8, expectedProposalCounts);
	}
	
	public void testChildElementProposals2() throws Exception {
		// default page, templates page, tags page, default page again
		int[] expectedProposalCounts = new int[] {7, 2, 5};
		runProposalTest("test1.xml", 24, 6, expectedProposalCounts);
	}
	
	public void testChildElementProposals3() throws Exception {
		// default page, templates page, tags page, default page again
		int[] expectedProposalCounts = new int[] {3, 2, 1};
		runProposalTest("test3.xml", 3, 3, expectedProposalCounts);
	}
	
	public void testChildElementAcceptance1() throws Exception {
		// default page, templates page, tags page, default page again
		int[] expectedProposalCounts = new int[] {3,2,1};
		runProposalAcceptanceTest("test3.xml", "test3after.xml", 3, 3, expectedProposalCounts, 0, 0, 258);
	}
	
	public void testAttributeProposals() throws Exception {
		// default page, templates page, tags page, default page again
		int[] expectedProposalCounts = new int[] {5, 4, 1};
		runProposalTest("test1.xml", 10, 10, expectedProposalCounts);
	}
	
	public void testFinishClosingTagNamePropsoals() throws Exception {
		// default page, templates page, tags page, default page again
		int[] expectedProposalCounts = new int[] {1, 0, 1};
		runProposalTest("test2.xml", 2, 19, expectedProposalCounts);
	}
	
	public void testFinishClosingTagPropsoals() throws Exception {
		// default page, templates page, tags page, default page again
		int[] expectedProposalCounts = new int[] {6, 4, 2};
		runProposalTest("test2.xml", 3, 0, expectedProposalCounts);
	}

	public void testXMLLinkedPositions() throws Exception {
		IFile file = getFile("test1.xml");
		StructuredTextEditor editor  = getEditor(file);
		StructuredTextViewer viewer = editor.getTextViewer();
		int offset = viewer.getDocument().getLineOffset(13);

		ICompletionProposal[][] pages = getProposals(viewer, offset, 4);
		assertNotNull("No proposals returned.", pages);
		assertTrue("Not enough pages.", pages.length > 0);
		assertTrue("Not enough proposals.", pages[0].length > 0);
		ICompletionProposalExtension2 proposal = null;
		// Check for the member proposal
		for (int i = 0; i < pages[0].length; i++) {
			if ("Member".equals(pages[0][i].getDisplayString())) {
				assertTrue("Proposal not of the proper type", pages[0][i] instanceof ICompletionProposalExtension2);
				proposal = (ICompletionProposalExtension2) pages[0][i];
				break;
			}
		}
		assertNotNull("No appropriate proposal found.", proposal);
		proposal.apply(viewer, (char) 0, 0, offset);
		String[] categories = viewer.getDocument().getPositionCategories();
		String category = null;
		for (int i = 0; i < categories.length; i++) {
			if (categories[i].startsWith("org.eclipse.jface.text.link.LinkedModeModel")) {
				category = categories[i];
			}
		}
		assertNotNull("Could not find the linked model position category.", category);
		assertTrue("No linked positions were generated.", viewer.getDocument().getPositions(category).length > 0);
	}

	/**
	 * <p>Run a proposal test by opening the given file and invoking content assist for
	 * each expected proposal count at the given line number and line character
	 * offset and then compare the number of proposals for each invocation (pages) to the
	 * expected number of proposals.</p>
	 * 
	 * @param fileName
	 * @param lineNum (0 based)
	 * @param lineRelativeCharOffset
	 * @param expectedProposalCounts
	 * @throws Exception
	 */
	private static void runProposalTest(String fileName,
			int lineNum, int lineRelativeCharOffset,
			int[] expectedProposalCounts) throws Exception{
		
		IFile file = getFile(fileName);
		StructuredTextEditor editor  = getEditor(file);
		StructuredTextViewer viewer = editor.getTextViewer();
		int offset = viewer.getDocument().getLineOffset(lineNum) + lineRelativeCharOffset;

		ICompletionProposal[][] pages = getProposals(viewer, offset, expectedProposalCounts.length);
		
		verifyProposalCounts(pages, expectedProposalCounts);
	}
	
	/**
	 * <p>Run a proposal test by opening the given file and invoking content assist for
	 * each expected proposal count at the given line number and line character
	 * offset and then compare the number of proposals for each invocation (pages) to the
	 * expected number of proposals.</p>
	 * 
	 * @param beforeFileName
	 * @param lineNum (0 based)
	 * @param lineRelativeCharOffset
	 * @param expectedProposalCounts
	 * @param proposalPage
	 * @param proposalExt2ToAccept
	 * @param proposalOffset
	 * @throws Exception
	 */
	private static void runProposalAcceptanceTest(String beforeFileName, String afterFileName,
			int lineNum, int lineRelativeCharOffset,
			int[] expectedProposalCounts,
			int proposalPage,
			int proposalExt2ToAccept,
			int proposalOffset) throws Exception{
		
		IFile file = getFile(beforeFileName);
		StructuredTextEditor editor  = getEditor(file);
		StructuredTextViewer viewer = editor.getTextViewer();
		int offset = viewer.getDocument().getLineOffset(lineNum) + lineRelativeCharOffset;

		ICompletionProposal[][] pages = getProposals(viewer, offset, expectedProposalCounts.length);
		
		verifyProposalCounts(pages, expectedProposalCounts);

		((ICompletionProposalExtension2) pages[proposalPage][proposalExt2ToAccept]).apply(viewer, (char) 0, 0, proposalOffset);
		FileBuffers.getTextFileBufferManager().connect(getFile(afterFileName).getFullPath(), LocationKind.IFILE, null);
		ITextFileBuffer buffer = FileBuffers.getTextFileBufferManager().getTextFileBuffer(getFile(afterFileName).getFullPath(), LocationKind.IFILE);
		String after = buffer.getDocument().get().replace("\r\n", "\n".replace("\r", "\n"));
		FileBuffers.getTextFileBufferManager().disconnect(getFile(afterFileName).getFullPath(), LocationKind.IFILE, null);
		assertEquals(after, viewer.getDocument().get().replace("\r\n", "\n").replace("\r", "\n"));
	}
	
	/**
	 * <p>Invoke content assist on the given viewer at the given offset, for the given number of pages
	 * and return the results of each page</p>
	 * 
	 * @param viewer
	 * @param offset
	 * @param pageCount
	 * @return
	 * @throws Exception
	 */
	private static ICompletionProposal[][] getProposals(StructuredTextViewer viewer, int offset, int pageCount) throws Exception {
		//setup the viewer
		StructuredTextViewerConfigurationXML configuration = new StructuredTextViewerConfigurationXML();
		ContentAssistant contentAssistant = (ContentAssistant)configuration.getContentAssistant(viewer);
		viewer.configure(configuration);
		viewer.setSelectedRange(offset, 0);
		
		//get the processor
		String partitionTypeID = viewer.getDocument().getPartition(offset).getType();
		IContentAssistProcessor processor = contentAssistant.getContentAssistProcessor(partitionTypeID);

		//fire content assist session about to start
		Method privateFireSessionBeginEventMethod = ContentAssistant.class.
		        getDeclaredMethod("fireSessionBeginEvent", new Class[] {boolean.class});
		privateFireSessionBeginEventMethod.setAccessible(true);
		privateFireSessionBeginEventMethod.invoke(contentAssistant, new Object[] {Boolean.TRUE});

		//get content assist suggestions
		ICompletionProposal[][] pages = new ICompletionProposal[pageCount][];
		for(int p = 0; p < pageCount; ++p) {
			pages[p] = processor.computeCompletionProposals(viewer, offset);
		}
		
		//fire content assist session ending
		Method privateFireSessionEndEventMethod = ContentAssistant.class.
        getDeclaredMethod("fireSessionEndEvent", null);
		privateFireSessionEndEventMethod.setAccessible(true);
		privateFireSessionEndEventMethod.invoke(contentAssistant, null);
		
		return pages;
	}
	
	/**
	 * <p>Compare the expected number of proposals per page to the actual number of proposals
	 * per page</p>
	 * 
	 * @param pages
	 * @param expectedProposalCounts
	 */
	private static void verifyProposalCounts(ICompletionProposal[][] pages, int[] expectedProposalCounts) {
		StringBuffer error = new StringBuffer();
		for (int page = 0; page < expectedProposalCounts.length; ++page) {
			if (expectedProposalCounts[page] != pages[page].length) {
				String[] displayNames = new String[pages[page].length];
				for (int i = 0; i < displayNames.length; i++) {
					displayNames[i] = pages[page][i].getDisplayString();
				}
				error.append("\nProposal page " + page + " did not have the expected number of proposals: was " +
						pages[page].length + " expected " + expectedProposalCounts[page] + "[" + StringUtils.pack(displayNames) +  "]");
			}
		}
		
		//if errors report them
		if(error.length() > 0) {
			Assert.fail(error.toString());
		}
	}
	
	/**
	 * <p>Given a file name in <code>fProject</code> attempts to get an <code>IFile</code>
	 * for it, if the file doesn't exist the test fails.</p>
	 * 
	 * @param name the name of the file to get
	 * @return the <code>IFile</code> associated with the given <code>name</code>
	 */
	private static IFile getFile(String name) {
		IFile file = fProject.getFile(name);
		assertTrue("Test file " + file + " can not be found", file.exists());
		
		return file;
	}
	
	/**
	 * <p>Given a <code>file</code> get an editor for it. If an editor has already
	 * been retrieved for the given <code>file</code> then return the same already
	 * open editor.</p>
	 * 
	 * <p>When opening the editor it will also standardized the line
	 * endings to <code>\n</code></p>
	 * 
	 * @param file open and return an editor for this
	 * @return <code>StructuredTextEditor</code> opened from the given <code>file</code>
	 */
	private static StructuredTextEditor getEditor(IFile file)  {
		StructuredTextEditor editor = (StructuredTextEditor)fFileToEditorMap.get(file);
		
		if(editor == null) {
			try {
				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = workbenchWindow.getActivePage();
				IEditorPart editorPart = IDE.openEditor(page, file, true, true);
				if(editorPart instanceof XMLMultiPageEditorPart) {
					XMLMultiPageEditorPart xmlEditorPart = (XMLMultiPageEditorPart)editorPart;
					editor = xmlEditorPart.getAdapter(StructuredTextEditor.class);
				} else if(editorPart instanceof StructuredTextEditor) {
					editor = ((StructuredTextEditor)editorPart);
				} else {
					fail("Unable to open structured text editor");
				}
				
				if(editor != null) {
					standardizeLineEndings(editor);
					fFileToEditorMap.put(file, editor);
				} else {
					fail("Could not open editor for " + file);
				}
			} catch (Exception e) {
				fail("Could not open editor for " + file + " exception: " + e.getMessage());
			}
		}
		
		return editor;
	}
	
	/**
	 * <p>Line endings can be an issue when running tests on different OSs.
	 * This function standardizes the line endings to use <code>\n</code></p>
	 * 
	 * <p>It will get the text from the given editor, change the line endings,
	 * and then save the editor</p>
	 * 
	 * @param editor standardize the line endings of the text presented in this
	 * editor.
	 */
	private static void standardizeLineEndings(StructuredTextEditor editor) {
		IDocument doc = editor.getTextViewer().getDocument();
		String contents = doc.get();
		contents = StringUtils.replace(contents, "\r\n", "\n");
		contents = StringUtils.replace(contents, "\r", "\n");
		doc.set(contents);
	}
	
	/**
	 * <p>This inner class is used to do set up and tear down before and
	 * after (respectively) all tests in the inclosing class have run.</p>
	 */
	private static class TestXMLContentAssistComputersSetup extends TestSetup {
		private static final String WTP_AUTOTEST_NONINTERACTIVE = "wtp.autotest.noninteractive";
		private static String previousWTPAutoTestNonInteractivePropValue = null;
		
		/**
		 * Default constructor
		 * 
		 * @param test do setup for the given test
		 */
		public TestXMLContentAssistComputersSetup(Test test) {
			super(test);
		}

		/**
		 * <p>This is run once before all of the tests</p>
		 * 
		 * @see junit.extensions.TestSetup#setUp()
		 */
		public void setUp() throws Exception {
			//setup properties
			String noninteractive = System.getProperty(WTP_AUTOTEST_NONINTERACTIVE);
			if (noninteractive != null) {
				previousWTPAutoTestNonInteractivePropValue = noninteractive;
			} else {
				previousWTPAutoTestNonInteractivePropValue = "false";
			}
			System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, "true");
			
			//setup project
			fProject = ProjectUtil.createProject(PROJECT_NAME, null, null);
			ProjectUtil.copyBundleEntriesIntoWorkspace(PROJECT_FILES, PROJECT_NAME);
		}

		/**
		 * <p>This is run once after all of the tests have been run</p>
		 * 
		 * @see junit.extensions.TestSetup#tearDown()
		 */
		public void tearDown() throws Exception {
			//close out the editors
			Iterator iter = fFileToEditorMap.values().iterator();
			while(iter.hasNext()) {
				StructuredTextEditor editor = (StructuredTextEditor)iter.next();
				editor.doSave(null);
				editor.close(false);
			}
			
			//remove project
			fProject.delete(true, new NullProgressMonitor());
			
			//restore properties
			if (previousWTPAutoTestNonInteractivePropValue != null) {
				System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, previousWTPAutoTestNonInteractivePropValue);
			}
		}
	}
}
