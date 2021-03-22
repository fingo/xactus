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
package org.eclipse.wst.css.ui.tests.contentassist;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.extensions.TestSetup;
import org.junit.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension5;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.css.ui.StructuredTextViewerConfigurationCSS;
import org.eclipse.wst.css.ui.tests.ProjectUtil;
import org.eclipse.wst.sse.core.utils.StringUtils;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;

public class TestCSSContentAssistComputers extends TestCase {
	/** The name of the project that all of these tests will use */
	private static final String PROJECT_NAME = "TestCSSContentAssistComputers";
	
	/** so we don't have to depend on JSDT */
	private static final String JAVA_SCRIPT_NATURE_ID = "org.eclipse.wst.jsdt.core.jsNature";
	
	/** The location of the testing files */
	private static final String PROJECT_FILES = "/testresources/contentassist";
	
	/** The project that all of the tests use */
	static IProject fProject;
	
	/**
	 * Used to keep track of the already open editors so that the tests don't go through
	 * the trouble of opening the same editors over and over again
	 */
	static Map<IFile, StructuredTextEditor> fFileToEditorMap = new HashMap<>();
	
	/**
	 * <p>Default constructor<p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @see #suite()
	 */
	public TestCSSContentAssistComputers() {
		super("Test CSS Content Assist Computers");
	}
	
	/**
	 * <p>Constructor that takes a test name.</p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @param name The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public TestCSSContentAssistComputers(String name) {
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
		TestSuite ts = new TestSuite(TestCSSContentAssistComputers.class, "Test CSS Content Assist Computers");
		return new TestCSSContentAssistComputersSetup(ts);
	}
	
	public void testSelectorProposals() throws Exception {
		// default page, templates page, standard page, default page again
		int[] expectedProposalCounts = new int[] {101, 0, 101, 101};
		runProposalTest("test1.css", 2, 0, expectedProposalCounts);
	}
	
	public void testDeclarationPropertyProposals() throws Exception {
		// default page, templates page, standard page, default page again
		int[] expectedProposalCounts = new int[] {122, 0, 122, 122};
		runProposalTest("test1.css", 5, 0, expectedProposalCounts);
	}
	
	public void testDeclarationValueProposals() throws Exception {
		// default page, templates page, standard page, default page again
		int[] expectedProposalCounts = new int[] {11, 0, 11, 11};
		runProposalTest("test1.css", 9, 16, expectedProposalCounts);
	}
	
	public void testEmptyDoc() throws Exception {
		// default page, templates page, standard page, default page again
		int[] expectedProposalCounts = new int[] {103, 1, 102, 103};
		runProposalTest("test2.css", 0, 0, expectedProposalCounts);
	}
	
	public void testFinishSelectorProposals() throws Exception {
		// default page, templates page, standard page, default page again
		int[] expectedProposalCounts = new int[] {20, 0, 20, 20};
		runProposalTest("test3.css", 2, 1, expectedProposalCounts);
	}
	
	public void testFinishDeclarationProposals() throws Exception {
		// default page, templates page, standard page, default page again
		int[] expectedProposalCounts = new int[] {4, 0, 4, 4};
		runProposalTest("test4.css", 3, 14, expectedProposalCounts);
	}
	
	public void testFinishDeclarationValueProposals() throws Exception {
		// default page, templates page, standard page, default page again
		int[] expectedProposalCounts = new int[] {4, 0, 4, 4};
		runProposalTest("test4.css", 7, 23, expectedProposalCounts);
	}

	public void testAdditionalProposalInfo() throws Exception {
		IFile file = getFile("test4.css");
		StructuredTextEditor editor = getEditor(file);
		StructuredTextViewer viewer = editor.getTextViewer();
		int offset = viewer.getDocument().getLineLength(7) + 23;
		ICompletionProposal[][] pages = getProposals(viewer, offset, 4);
		
		assertTrue("Not enough pages", pages.length > 0);
		ICompletionProposal[] proposals = pages[0];
		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i] instanceof ICompletionProposalExtension5) {
				Object obj = ((ICompletionProposalExtension5) proposals[i]).getAdditionalProposalInfo(null);
				assertTrue("Additional info must be of type ProposalInfo", obj instanceof ProposalInfo);
				ProposalInfo info = (ProposalInfo) obj;
				assertNotNull("CSSMMNode for Proposal Info should not be null", info);
			}
		}
		editor.close(false);
	}
	/**
	 * <p>Run a proposal test by opening the given file and invoking content assist for
	 * each expected proposal count at the given line number and line character
	 * offset and then compare the number of proposals for each invocation (pages) to the
	 * expected number of proposals.</p>
	 * 
	 * @param fileName
	 * @param lineNum
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
		editor.close(false);
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
		StructuredTextViewerConfigurationCSS configuration = new StructuredTextViewerConfigurationCSS();
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
        getDeclaredMethod("fireSessionEndEvent");
		privateFireSessionEndEventMethod.setAccessible(true);
		privateFireSessionEndEventMethod.invoke(contentAssistant);
		
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
		for(int page = 0; page < expectedProposalCounts.length; ++page) {
			if(expectedProposalCounts[page] > pages[page].length) {
				error.append("\nProposal page " + page + " did not have the expected number of proposals: was " +
						pages[page].length + " expected " + expectedProposalCounts[page]);
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
		StructuredTextEditor editor = fFileToEditorMap.get(file);
		
		if(editor == null) {
			try {
				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = workbenchWindow.getActivePage();
				IEditorPart editorPart = IDE.openEditor(page, file, "org.eclipse.wst.css.core.csssource.source", true);
				if(editorPart instanceof StructuredTextEditor) {
					editor = ((StructuredTextEditor)editorPart);
				} else {
					fail("Unable to open structured text editor: " + editorPart.getClass().getName());
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
	private static class TestCSSContentAssistComputersSetup extends TestSetup {
		private static final String WTP_AUTOTEST_NONINTERACTIVE = "wtp.autotest.noninteractive";
		private static String previousWTPAutoTestNonInteractivePropValue = null;
		
		/**
		 * Default constructor
		 * 
		 * @param test do setup for the given test
		 */
		public TestCSSContentAssistComputersSetup(Test test) {
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
			fProject = ProjectUtil.createProject(PROJECT_NAME, null, new String[] {JAVA_SCRIPT_NATURE_ID});
			ProjectUtil.copyBundleEntriesIntoWorkspace(PROJECT_FILES, PROJECT_NAME);
		}

		/**
		 * <p>This is run once after all of the tests have been run</p>
		 * 
		 * @see junit.extensions.TestSetup#tearDown()
		 */
		public void tearDown() throws Exception {
			//close out the editors
			Iterator<StructuredTextEditor> iter = fFileToEditorMap.values().iterator();
			while(iter.hasNext()) {
				StructuredTextEditor editor = iter.next();
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
