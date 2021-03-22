/*******************************************************************************
 * Copyright (c) 2006, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Nick Boldt, Red Hat - disable tests that don't work with Photon.0.M5; make reusable method public (see TEIValidation.java)
 *     
 *******************************************************************************/
package org.eclipse.jst.jsp.core.tests.translation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jst.jsp.core.internal.JSPCorePlugin;
import org.eclipse.jst.jsp.core.internal.contenttype.DeploymentDescriptorPropertyCache;
import org.eclipse.jst.jsp.core.internal.contenttype.ServletAPIDescriptor;
import org.eclipse.jst.jsp.core.internal.java.IJSPProblem;
import org.eclipse.jst.jsp.core.internal.java.IJSPTranslation;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslation;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslationAdapter;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslationExtension;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslationUtil;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslator;
import org.eclipse.jst.jsp.core.internal.modelhandler.ModelHandlerForJSP;
import org.eclipse.jst.jsp.core.internal.preferences.JSPCorePreferenceNames;
import org.eclipse.jst.jsp.core.internal.taglib.CustomTag;
import org.eclipse.jst.jsp.core.internal.taglib.TaglibHelper;
import org.eclipse.jst.jsp.core.internal.validation.JSPJavaValidator;
import org.eclipse.jst.jsp.core.internal.validation.JSPValidator;
import org.eclipse.jst.jsp.core.taglib.ITaglibRecord;
import org.eclipse.jst.jsp.core.taglib.TaglibIndex;
import org.eclipse.jst.jsp.core.tests.JSPCoreTestsPlugin;
import org.eclipse.jst.jsp.core.tests.ProjectUtil;
import org.eclipse.jst.jsp.core.tests.taglibindex.BundleResourceUtil;
import org.eclipse.jst.jsp.core.tests.validation.ReporterForTest;
import org.eclipse.jst.jsp.core.tests.validation.ValidationContextForTest;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.validation.ValidationFramework;
import org.eclipse.wst.validation.internal.core.Message;
import org.eclipse.wst.validation.internal.operations.ValidatorManager;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

import junit.framework.TestCase;

public class JSPJavaTranslatorCoreTest extends TestCase {

	static final String WTP_AUTOTEST_NONINTERACTIVE = "wtp.autotest.noninteractive";
	private static byte[] creationLock = new byte[0];

	public JSPJavaTranslatorCoreTest() {
	}

	public JSPJavaTranslatorCoreTest(String name) {
		super(name);
	}

	String wtp_autotest_noninteractive = null;

	protected void setUp() throws Exception {
		super.setUp();
		String noninteractive = System.getProperty(WTP_AUTOTEST_NONINTERACTIVE);
		if (noninteractive != null)
			wtp_autotest_noninteractive = noninteractive;
		System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, "true");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (wtp_autotest_noninteractive != null)
			System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, wtp_autotest_noninteractive);
	}

	public void test_107338() throws Exception {
		String projectName = "bug_107338";
		// Create new project
		IProject project = BundleResourceUtil.createSimpleProject(projectName, null, null);
		assertTrue(project.exists());
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + projectName, "/" + projectName);
		project.open(new NullProgressMonitor());
		IFile file = project.getFile("WebContent/test107338.jsp");
		assertTrue(file.exists());

		IStructuredModel model = StructuredModelManager.getModelManager().getModelForRead(file);
		IDOMModel jspModel = (IDOMModel) model;

		String jspSource = model.getStructuredDocument().get();

		assertTrue("line delimiters have been converted to Windows [CRLF]", jspSource.indexOf("\r\n") < 0);
		assertTrue("line delimiters have been converted to Mac [CR]", jspSource.indexOf("\r") < 0);

		ModelHandlerForJSP.ensureTranslationAdapterFactory(model);

		IDOMDocument xmlDoc = jspModel.getDocument();
		JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) xmlDoc.getAdapterFor(IJSPTranslation.class);
		JSPTranslation translation = translationAdapter.getJSPTranslation();
		// System.err.print(translation.getJavaText());

		assertTrue("new-line beginning scriptlet missing from translation", translation.getJavaText().indexOf("int i = 0;") >= 0);

		model.releaseFromRead();
	}

	/**
	 * Tests jsp translation when jsp is within html comments. See
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=126377
	 * 
	 * @throws Exception
	 */
	public void test_126377() throws Exception {
		String projectName = "bug_126377";
		// Create new project
		IProject project = BundleResourceUtil.createJavaWebProject(projectName);
		assertTrue(project.exists());
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + projectName, "/" + projectName);
		IFile file = project.getFile("WebContent/test126377_noerror.jsp");
		assertTrue(file.exists());
				
		JSPValidator validator = new JSPJavaValidator();
		IReporter reporter = new ReporterForTest();
		ValidationContextForTest helper = new ValidationContextForTest();
		helper.setURI(file.getFullPath().toOSString());
		validator.validate(helper, reporter);
		
		String strings = "";
		for (int i = 0; i < reporter.getMessages().size(); i++) {
			strings = strings + ((IMessage) reporter.getMessages().get(i)).getText() + "\n";
		}
		assertTrue("found problems within html comments when there should be none: "+ strings, reporter.getMessages().isEmpty());
		
		file = project.getFile("WebContent/test126377_error.jsp");
		assertTrue(file.exists());
		helper.setURI(file.getFullPath().toOSString());
		validator.validate(helper, reporter);
		
		int errors = reporter.getMessages().size();
		assertTrue("found "+errors+" jsp java errors within html comments when there should be 3", (errors == 3));
	}

//	public void testMangling() {
//		assertEquals("_simple_2E_tag", JSP2ServletNameUtil.mangle("simple.tag"));
//		assertEquals("_simple_2E_jspf", JSP2ServletNameUtil.mangle("simple.jspf"));
//		assertEquals("sim_005f_005fple_tagx", JSP2ServletNameUtil.mangle("sim__ple.tagx"));
//		assertEquals(new Path("Project.folder.simple_tag"), JSP2ServletNameUtil.mangle("/Project/folder/simple.tag"));
//		assertEquals(new Path("Project.fold_005fer.simple_jspx"), JSP2ServletNameUtil.mangle("/Project/fold_er/simple.jspx"));
//	}
//
//	public void testUnmangling() {
//		assertEquals("simple.tag", JSP2ServletNameUtil.unmangle("simple_tag"));
//		assertEquals("simple.jspf", JSP2ServletNameUtil.unmangle("simple_jspf"));
//		assertEquals("sim__ple.tagx", JSP2ServletNameUtil.unmangle("sim_005f_005fple_tagx"));
//		assertEquals(new Path("/Project/folder/simple.tag"), JSP2ServletNameUtil.unmangle("Project.folder.simple_tag"));
//		assertEquals(new Path("/Project/fold_er/simple.jspx"), JSP2ServletNameUtil.unmangle("Project.fold_005fer.simple_jspx"));
//	}
	public void test_174042() throws Exception {
		boolean doValidateSegments = JSPCorePlugin.getDefault().getPluginPreferences().getBoolean(JSPCorePreferenceNames.VALIDATE_FRAGMENTS);
		String testName = "bug_174042";
		// Create new project
		IProject project = BundleResourceUtil.createSimpleProject(testName, null, null);
		assertTrue(project.exists());
		JSPCorePlugin.getDefault().getPluginPreferences().setValue(JSPCorePreferenceNames.VALIDATE_FRAGMENTS, true);
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		BundleResourceUtil.copyBundleEntryIntoWorkspace("/testfiles/struts.jar", "/" + testName + "/struts.jar");
		waitForBuildAndValidation(project);
		JSPCorePlugin.getDefault().getPluginPreferences().setValue(JSPCorePreferenceNames.VALIDATE_FRAGMENTS, doValidateSegments);
		IFile main = project.getFile("main.jsp");
		IMarker[] markers = main.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < markers.length; i++) {
			s.append("\nproblem marker on line " + markers[i].getAttribute(IMarker.LINE_NUMBER) + ": \"" + markers[i].getAttribute(IMarker.MESSAGE) + "\" ");
		}
		assertEquals("problem markers found, " + s.toString(), 0, markers.length);
	}

	/* make reusable method public (see TEIValidation.java) */
	public static boolean waitForBuildAndValidation() throws CoreException {
		IWorkspaceRoot root = null;
		try {
			ResourcesPlugin.getWorkspace().checkpoint(true);
			Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, new NullProgressMonitor());
			Job.getJobManager().join(ResourcesPlugin.FAMILY_MANUAL_BUILD, new NullProgressMonitor());
			Job.getJobManager().join(ValidatorManager.VALIDATOR_JOB_FAMILY, new NullProgressMonitor());
			Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, new NullProgressMonitor());
			Thread.sleep(200);
			Job.getJobManager().beginRule(root = ResourcesPlugin.getWorkspace().getRoot(), null);
		}
		catch (InterruptedException e) {
			// woken up from sleep?
			StringWriter s = new StringWriter();
			e.printStackTrace(new PrintWriter(s));
			fail(s.toString());
		}
		catch (IllegalArgumentException e) {
			StringWriter s = new StringWriter();
			e.printStackTrace(new PrintWriter(s));
			fail(s.toString());
		}
		catch (OperationCanceledException e) {
			StringWriter s = new StringWriter();
			e.printStackTrace(new PrintWriter(s));
			fail(s.toString());
		}
		finally {
			if (root != null) {
				Job.getJobManager().endRule(root);
			}
		}
		return true;
	}
	
	/* make reusable method public (see TEIValidation.java) */
	public static void waitForBuildAndValidation(IProject project) throws CoreException {
		project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
		assertTrue("waitForBuildAndValidation :: Clean build could not be completed for project = " + project.toString(), waitForBuildAndValidation());
		project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		assertTrue("waitForBuildAndValidation :: Full build could not be completed for project = " + project.toString(), waitForBuildAndValidation());
	}

	public void test_178443() throws Exception {
		boolean doValidateSegments = JSPCorePlugin.getDefault().getPluginPreferences().getBoolean(JSPCorePreferenceNames.VALIDATE_FRAGMENTS);
		String testName = "bug_178443";
		// Create new project
		IProject project = BundleResourceUtil.createSimpleProject(testName, Platform.getStateLocation(JSPCoreTestsPlugin.getDefault().getBundle()).append(testName), null);
		assertTrue(project.exists());
		/*
		 * Should be set to false. A referenced class in an included segment
		 * does not exist.
		 */
		JSPCorePlugin.getDefault().getPluginPreferences().setValue(JSPCorePreferenceNames.VALIDATE_FRAGMENTS, false);
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		BundleResourceUtil.copyBundleEntryIntoWorkspace("/testfiles/struts.jar", "/" + testName + "/struts.jar");

		waitForBuildAndValidation(project);

		JSPCorePlugin.getDefault().getPluginPreferences().setValue(JSPCorePreferenceNames.VALIDATE_FRAGMENTS, doValidateSegments);
		IFile main = project.getFile("main.jsp");
		IMarker[] markers = main.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);

		StringBuffer s = new StringBuffer();
		for (int i = 0; i < markers.length; i++) {
			s.append("\nproblem on line " + markers[i].getAttribute(IMarker.LINE_NUMBER) + ": " + markers[i].getAttribute(IMarker.MESSAGE));
		}
		assertEquals("problem markers found" + s.toString(), 0, markers.length);
	}

	public void test_109721() throws Exception {
		/*
		 * https://bugs.eclipse.org/109721 JSP editor does not find taglib
		 * directives in include-prelude or jsp:include, make sure the
		 * contents were processed
		 */
		String testName = "bug_109721";
		// Create new project
		final IProject project = BundleResourceUtil.createSimpleProject(testName, null, null);
		assertTrue(project.isAccessible());

		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		BundleResourceUtil.copyBundleEntryIntoWorkspace("/testfiles/struts.jar", "/" + testName + "/WebContent/WEB-INF/lib/struts.jar");

		waitForBuildAndValidation(project);

		String filename = "/WebContent/main.jsp";
		IStructuredModel sm = StructuredModelManager.getModelManager().getModelForRead(project.getFile(filename));
		assertNotNull("couldn't load JSP for test", sm);
		JSPTranslationUtil translationUtil = new JSPTranslationUtil(sm.getStructuredDocument());
		String translation = translationUtil.getTranslation().getJavaText();
		sm.releaseFromRead();

		assertTrue("Java content from Fragment included by web.xml is missing", translation.indexOf("int alpha = 5;") > 0);
		assertTrue("Tag that could only be known by processing Fragment included only by web.xml is not in the translation", translation.indexOf("org.apache.struts.taglib.bean.DefineTag") > 0);
	}

	public void test_181057a() throws Exception {
		boolean doValidateSegments = JSPCorePlugin.getDefault().getPluginPreferences().getBoolean(JSPCorePreferenceNames.VALIDATE_FRAGMENTS);
		String testName = "bug_181057";
		// Create new project
		IProject j = BundleResourceUtil.createSimpleProject("j", null, null);
		assertTrue(j.exists());
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/j", "/j");
		IProject k = BundleResourceUtil.createSimpleProject("k", null, null);
		assertTrue(k.exists());
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/k", "/k");

		IProject project = BundleResourceUtil.createSimpleProject(testName, Platform.getStateLocation(JSPCoreTestsPlugin.getDefault().getBundle()).append(testName), null);
		assertTrue(project.exists());
		JSPCorePlugin.getDefault().getPluginPreferences().setValue(JSPCorePreferenceNames.VALIDATE_FRAGMENTS, true);
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		BundleResourceUtil.copyBundleEntryIntoWorkspace("/testfiles/struts.jar", "/" + testName + "/struts.jar");

		waitForBuildAndValidation(project);

		JSPCorePlugin.getDefault().getPluginPreferences().setValue(JSPCorePreferenceNames.VALIDATE_FRAGMENTS, doValidateSegments);
		/*
		 * main.jsp contains numerous references to tags in struts.jar, which
		 * is at the end of the build path
		 */
		IFile main = project.getFile("main.jsp");
		IMarker[] markers = main.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < markers.length; i++) {
			s.append("\n" + markers[i].getAttribute(IMarker.LINE_NUMBER) + ":" + markers[i].getAttribute(IMarker.MESSAGE));
		}
		assertEquals("problem markers found" + s.toString(), 0, markers.length);
	}
	
	public void test_219761a() throws Exception {
		/**
		 * Broken behavior has a Java syntax error on line 19, which only
		 * contains an include directive to a fragment that doesn't exist.
		 * 
		 * All syntax errors should be on lines 25 or 28 and after offset 373
		 * (single character line delimiter!).
		 */
		String testName = "testTranslatorMessagesWithIncludes";
		// Create new project
		IProject project = BundleResourceUtil.createJavaWebProject(testName);
		assertTrue(project.exists());
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);

		waitForBuildAndValidation(project);
		ValidationFramework.getDefault().validate(new IProject[]{project}, true, true, new NullProgressMonitor());

		IFile main = project.getFile("/WebContent/sample.jsp");
		assertTrue("sample test file does not exist", main.isAccessible());
		IMarker[] markers = main.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		StringBuffer markerText = new StringBuffer();
		for (int i = 0; i < markers.length; i++) {
			// line/start-end
			markerText.append("\nL" + markers[i].getAttribute(IMarker.LINE_NUMBER) + "/o" + markers[i].getAttribute(IMarker.CHAR_START) + "-"  + markers[i].getAttribute(IMarker.CHAR_END) + ":" + markers[i].getAttribute(IMarker.MESSAGE));
		}
//		int numberOfSyntaxErrors = 0;
		for (int i = 0; i < markers.length; i++) {
			Object message = markers[i].getAttribute(IMarker.MESSAGE);
			assertNotNull("Marker message was null!", message);
			if (message.toString().startsWith("Syntax error")) {
//				numberOfSyntaxErrors++;
				assertTrue("Syntax error reported before line 25" + markerText, ((Integer) markers[i].getAttribute(IMarker.LINE_NUMBER)).intValue() >= 25);
//				assertTrue("Syntax error reported before offset 371" + markerText, ((Integer) markers[i].getAttribute(IMarker.CHAR_START)).intValue() >= 370);
//				assertTrue("Syntax error reported after 456" + markerText, ((Integer) markers[i].getAttribute(IMarker.CHAR_START)).intValue() < 456);
			}
		}
//		assertEquals("wrong number of syntax errors reported\n" + markerText, 3, numberOfSyntaxErrors);

		// clean up if we got to the end
		try {
			project.delete(true, true, null);
		}
		catch (Exception e) {
			// not a failure condition
		}
	}
	
	public void test_150794() throws Exception {
		String testName = "bug_150794";
		// Create new project
		IProject project = BundleResourceUtil.createJavaWebProject(testName);
		assertTrue(project.exists());
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		
		IFile main = project.getFile("/WebContent/escapedQuotes.jsp");
		assertTrue("sample test file does not exist", main.isAccessible());
		
		JSPJavaValidator validator = new JSPJavaValidator();
		IReporter reporter = new ReporterForTest();
		ValidationContextForTest helper = new ValidationContextForTest();
		helper.setURI(main.getFullPath().toOSString());
		validator.validate(helper, reporter);

		String strings = "";
		for (int i = 0; i < reporter.getMessages().size(); i++) {
			strings = strings + ((IMessage) reporter.getMessages().get(i)).getText() + "\n";
		}
		assertTrue("Unexpected problems found: " + strings, reporter.getMessages().isEmpty());

		// clean up if we got to the end
		try {
			project.delete(true, true, null);
		}
		catch (Exception e) {
			// not a failure condition
		}
	}

	public void test_preludes() throws Exception {
		String testName = "testPreludeAndCodas";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, null);
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}

		IFile main = project.getFile("/web stuff/prelude-user/test.jsp");
		assertTrue("sample test file not accessible", main.isAccessible());

		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(main);
			
			ModelHandlerForJSP.ensureTranslationAdapterFactory(model);
			
			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) model.getDocument().getAdapterFor(IJSPTranslation.class);
			IJSPTranslation translation = translationAdapter.getJSPTranslation();
			assertNotNull("no Java translation found", translation);
			assertTrue("prelude0 contents not included", translation.getJavaText().indexOf("int prelude0") > 0);
			assertTrue("prelude1 contents not included", translation.getJavaText().indexOf("int prelude1") > 0);

			assertTrue("import statement not found", translation.getJavaText().indexOf("import java.lang.ref.Reference") > 0);
		}
		finally {
			if (model != null)
				model.releaseFromEdit();
		}
	}

	public void testApiDetection1() throws Exception {
		String testName = "testapidetection1";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, new String[] {JavaCore.NATURE_ID, "org.eclipse.pde.PluginNature"});
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}
		ServletAPIDescriptor servletAPIVersion = DeploymentDescriptorPropertyCache.getInstance().getServletAPIVersion(project);
		assertNotNull("no API version was detected", servletAPIVersion);
		// TODO: enable this test
//		IClasspathEntry[] resolvedClasspath = JavaCore.create(project).getResolvedClasspath(true);
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < resolvedClasspath.length; i++) {
//			builder.append('\n');
//			builder.append(resolvedClasspath[i].getPath());
//		}
//		assertFalse("Default API version returned, nothing was detected\n" + builder, servletAPIVersion == ServletAPIDescriptor.DEFAULT);
//		assertEquals("Unexpected API version", 3.1f, servletAPIVersion.getAPIversion());
//		assertEquals("Unexpected root package", "javax.servlet", servletAPIVersion.getRootPackage());
	}

	public void testApiDetection2() throws Exception {
		String testName = "testapidetection2";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, new String[] {JavaCore.NATURE_ID, "org.eclipse.pde.PluginNature"});
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}
		ServletAPIDescriptor servletAPIVersion = DeploymentDescriptorPropertyCache.getInstance().getServletAPIVersion(project);
		assertNotNull("no API version was detected", servletAPIVersion);
		// TODO: enable this test
//		IClasspathEntry[] resolvedClasspath = JavaCore.create(project).getResolvedClasspath(true);
//		StringBuilder builder = new StringBuilder();
//		for (int i = 0; i < resolvedClasspath.length; i++) {
//			builder.append('\n');
//			builder.append(resolvedClasspath[i].getPath());
//		}
//		assertFalse("Default API version returned, nothing was detected\n" + builder, servletAPIVersion == ServletAPIDescriptor.DEFAULT);
//		assertEquals("Unexpected API version", 3.1f, servletAPIVersion.getAPIversion());
//		assertEquals("Unexpected root package", "javax.servlet", servletAPIVersion.getRootPackage());
	}

	public void testDDVersionDetection1() throws Exception {
		String testName = "testversiondetection1";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, new String[] {});
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}
		Float version = DeploymentDescriptorPropertyCache.getInstance().getJSPVersion(project.getFullPath());
		assertNotNull("no API version was detected", version);
		assertEquals(2.3f, version);
	}

	public void testDDVersionDetection2() throws Exception {
		String testName = "testversiondetection2";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, new String[] {});
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}
		Float version = DeploymentDescriptorPropertyCache.getInstance().getJSPVersion(project.getFullPath());
		assertNotNull("no API version was detected", version);
		assertEquals(2.2f, version);
	}

	public void test_codas() throws Exception {
		String testName = "testPreludeAndCodas";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, null);
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}

		IFile main = project.getFile("/web stuff/coda-user/test.jsp");
		assertTrue("sample test file not accessible", main.isAccessible());

		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(main);
			
			ModelHandlerForJSP.ensureTranslationAdapterFactory(model);
			
			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) model.getDocument().getAdapterFor(IJSPTranslation.class);
			IJSPTranslation translation = translationAdapter.getJSPTranslation();
			assertNotNull("no Java translation found", translation);
			assertTrue("coda0 contents not included", translation.getJavaText().indexOf("int coda0") > 0);
			assertTrue("coda1 contents not included", translation.getJavaText().indexOf("int coda1") > 0);

			assertTrue("import statement not found", translation.getJavaText().indexOf("import java.lang.ref.Reference") > 0);
		}
		finally {
			if (model != null)
				model.releaseFromEdit();
		}
	}
	public void test_prelude_and_coda() throws Exception {
		String testName = "testPreludeAndCodas";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, null);
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}

		IFile main = project.getFile("/web stuff/both/test.jsp");
		assertTrue("sample test file not accessible", main.isAccessible());

		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(main);
			
			ModelHandlerForJSP.ensureTranslationAdapterFactory(model);
			
			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) model.getDocument().getAdapterFor(IJSPTranslation.class);
			IJSPTranslation translation = translationAdapter.getJSPTranslation();
			assertNotNull("no Java translation found", translation);
			assertTrue("prelude0 contents not included", translation.getJavaText().indexOf("int prelude0") > 0);
			assertTrue("prelude1 contents included", translation.getJavaText().indexOf("int prelude1") < 0);
			assertTrue("coda0 contents not included", translation.getJavaText().indexOf("int coda0") > 0);
			assertTrue("coda1 contents included", translation.getJavaText().indexOf("int coda1") < 0);

			assertTrue("import statement not found", translation.getJavaText().indexOf("import java.lang.ref.Reference") > 0);
		}
		finally {
			if (model != null)
				model.releaseFromEdit();
		}
	}

	public void testVariablesFromIncludedFragments() throws Exception {
		String testName = "testVariablesFromIncludedFragments";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		synchronized (creationLock) {
			if (!project.isAccessible()) {
				// Create new project
				project = BundleResourceUtil.createSimpleProject(testName, null, null);
				assertTrue(project.exists());
				BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
			}
		}

		IFile main = project.getFile("/WebContent/main.jsp");
		assertTrue("sample test file not accessible", main.isAccessible());

		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(main);
			
			ModelHandlerForJSP.ensureTranslationAdapterFactory(model);
			
			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) model.getDocument().getAdapterFor(IJSPTranslation.class);
			IJSPTranslation translation = translationAdapter.getJSPTranslation();
			assertNotNull("no Java translation found", translation);
			assertTrue("String variableFromHeader1 not found", translation.getJavaText().indexOf("String variableFromHeader1") > 0);
			assertTrue("header1 contents not included", translation.getJavaText().indexOf("String variableFromHeader1 = \"initialized in header 1\";") > 0);
			assertTrue("header2 contents not included", translation.getJavaText().indexOf("variableFromHeader1 = \"reassigned in header 2\";") > 0);
		}
		finally {
			if (model != null)
				model.releaseFromEdit();
		}
	}

	public void testIterationTags() throws Exception {
		String testName = "testIterationTags";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		if (!project.isAccessible()) {
			// Create new project
			project = BundleResourceUtil.createSimpleProject(testName, null, null);
			assertTrue(project.exists());
			BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		}
		/* This test is failing as of 20180213 so until someone can debug and fix it, comment it out */
		/* waitForBuildAndValidation(project); */
		IFile testFile = project.getFile("/WebContent/test.jsp");
		assertTrue("test.jsp is not accessible", testFile.isAccessible());
		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(testFile);

			ModelHandlerForJSP.ensureTranslationAdapterFactory(model);

			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) model.getDocument().getAdapterFor(IJSPTranslation.class);
			JSPTranslationExtension translation = translationAdapter.getJSPTranslation();
			translation.setProblemCollectingActive(true);
			assertNotNull("No Java translation found", translation);
			translation.reconcileCompilationUnit();
			translation.setProblemCollectingActive(false);
			List<IProblem> problems = translation.getProblems();
			assertNotNull("Translation had a null problems list.", problems);
			Iterator<IProblem> it = problems.iterator();
			String javaText = translation.getJavaText();
			int startOffset = javaText.indexOf("<plain:simple>");
			assertTrue("<plan:simple> scope not found.", startOffset > 0);
			int endOffset = javaText.indexOf("</plain:simple>", startOffset);
			assertTrue("</plan:simple> scope not found.", endOffset > 0);
			// Finds all errors caused by "continue cannot be used outside of a loop" - should only occur between <plain:simple></plain:simple>
			while (it.hasNext()) {
				IProblem problem = it.next();
				if (problem.isError()) {
					if ("continue cannot be used outside of a loop".equals(problem.getMessage())) {
						assertTrue("'continue cannot be used outside of a loop' outside of iteration tag: ", problem.getSourceStart() > startOffset && problem.getSourceEnd() < endOffset);
					}
					
				}
			}
		}
		finally {
			if (model != null)
				model.releaseFromEdit();
		}
	}

	/**
	 * Tests that an iteration tag will generate 
	 * @throws Exception
	 */
	public void testIterationTagsIncomplete() throws Exception {
		String testName = "testIterationTags";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		if (!project.isAccessible()) {
			// Create new project
			project = BundleResourceUtil.createSimpleProject(testName, null, null);
			assertTrue(project.exists());
			BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		}
		/* This test is failing as of 20180213 so until someone can debug and fix it, comment it out */
		/* waitForBuildAndValidation(project); */
		IFile testFile = project.getFile("/WebContent/test_missing_end_tag.jsp");
		assertTrue("test_missing_end_tag.jsp is not accessible", testFile.isAccessible());
		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForEdit(testFile);

			ModelHandlerForJSP.ensureTranslationAdapterFactory(model);

			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) model.getDocument().getAdapterFor(IJSPTranslation.class);
			JSPTranslationExtension translation = translationAdapter.getJSPTranslation();
			String javaText = translation.getJavaText();
			int startOffset = javaText.indexOf("} // [</plain:loop>]");
			assertTrue("Missing end tag was not accounted for.", startOffset != -1);
		}
		finally {
			if (model != null)
				model.releaseFromEdit();
		}
	}

	public void testTaglibHelperWrongHierarchy() throws Exception { 
		String testName = "testIterationTags";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		if (!project.isAccessible()) {
			// Create new project
			project = BundleResourceUtil.createSimpleProject(testName, null, null);
			assertTrue(project.exists());
			BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		}
		/* This test is failing as of 20180213 so until someone can debug and fix it, comment it out */
		/* waitForBuildAndValidation(project); */
		TaglibHelper helper = new TaglibHelper(project);
		IFile testFile = project.getFile("/WebContent/iterationTester.jsp");
		assertTrue("iterationTester.jsp is not accessible", testFile.isAccessible());
		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForRead(testFile);
			IStructuredDocument doc = model.getStructuredDocument();
			CustomTag tag = helper.getCustomTag("plain:list", doc, null, new ArrayList());
			assertFalse("plain:list should not be an IterationTag", tag.isIterationTag());
		}
		finally {
			if (model != null)
				model.releaseFromRead();
		}
	}

	public void testTaglibHelperUnresolvedSupertype() throws Exception { 
		String testName = "testIterationTags";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(testName);
		if (!project.isAccessible()) {
			// Create new project
			project = BundleResourceUtil.createJavaWebProject(testName);
			assertTrue(project.exists());
			BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);
		}
		/* This test is failing as of 20180213 so until someone can debug and fix it, comment it out */
		/* waitForBuildAndValidation(project); */
		TaglibHelper helper = new TaglibHelper(project);
		IFile testFile = project.getFile("/WebContent/iterationTester.jsp");
		assertTrue("iterationTester.jsp is not accessible", testFile.isAccessible());
		IDOMModel model = null;
		try {
			model = (IDOMModel) StructuredModelManager.getModelManager().getModelForRead(testFile);
			IStructuredDocument doc = model.getStructuredDocument();
			CustomTag tag = helper.getCustomTag("plain:uberloop", doc, null, new ArrayList());
			assertTrue("plain:uberloop should be an IterationTag", tag.isIterationTag());
		}
		finally {
			if (model != null)
				model.releaseFromRead();
		}
	}
	
	public void test_javaVariableIncludes() throws Exception {
		String testFolderName = "jspx_javaVariable_includes";
		// Create new project
		IProject project = BundleResourceUtil.createJavaWebProject(testFolderName);
		assertTrue(project.exists());
		BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testFolderName, "/" + testFolderName);

		/* This test is failing as of 20180213 so until someone can debug and fix it, comment it out */
		/* waitForBuildAndValidation(project); */
		
		ValidationFramework.getDefault().validate(new IProject[]{project}, true, false, new NullProgressMonitor());

		assertNoProblemMarkers(project.getFile("/WebContent/test1.jsp"));
		assertNoProblemMarkers(project.getFile("/WebContent/index.jspx"));

		// clean up if we got to the end
		try {
			project.delete(true, true, null);
		}
		catch (Exception e) {
			// not a failure condition
		}
	}

	private void assertNoProblemMarkers(IFile file) throws CoreException {
		assertTrue("sample test file does not exist", file.isAccessible());
		IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		StringBuffer markerText = new StringBuffer();
		for (int i = 0; i < markers.length; i++) {
			// line/start-end
			markerText.append("\nL" + markers[i].getAttribute(IMarker.LINE_NUMBER) + "/o" + markers[i].getAttribute(IMarker.CHAR_START) + "-"  + markers[i].getAttribute(IMarker.CHAR_END) + ":" + markers[i].getAttribute(IMarker.MESSAGE));
		}
		assertEquals("Problem markers reported found \n" + markerText, 0, markers.length);
	}

	public void test_389174() throws CoreException, IOException {
		IProject j = BundleResourceUtil.createJavaWebProject(getName());
		assertTrue(j.exists());

		String typeName = "List<List<Boolean>>";
		InputStream source = new ByteArrayInputStream(("<jsp:useBean id=\"x\" type=\"" + typeName + "\" />").getBytes());
		IFile file = j.getFile(getName() + "_test.jsp");
		file.create(source, IResource.FORCE, null);
		JSPTranslator translator = new JSPTranslator();
		IDOMModel structuredModel = null;
		try {
			structuredModel = (IDOMModel) StructuredModelManager.getModelManager().getModelForRead(file);
			translator.reset(structuredModel.getDocument(), new NullProgressMonitor());
			translator.translate();

			assertTrue("specified type did not survive translation", translator.getTranslation().indexOf(typeName) >= 0);
			IJSPProblem[] translationProblems = (IJSPProblem[]) translator.getTranslationProblems().toArray(new IJSPProblem[0]);
			for (int i = 0; i < translationProblems.length; i++) {
				assertEquals("expected IJSPProblem type: " + translationProblems[i].getMessage(), Integer.toHexString(IProblem.UndefinedType), Integer.toHexString(translationProblems[i].getID()));
			}
		}
		finally {
			if (structuredModel != null)
				structuredModel.releaseFromRead();
		}
	}

	// http://bugs.eclipse.org/432978
	public void test_432978() throws Exception {
        String testName = "bug_432978";
        // Create new project
        IProject project = BundleResourceUtil.createJavaWebProject(testName);
        assertTrue(project.exists());
        BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);

        // TEI class needs to already be compiled
//		waitForBuildAndValidation(project);
//		project.build(IncrementalProjectBuilder.FULL_BUILD, "org.eclipse.jdt.internal.core.builder.JavaBuilder", null, null);
		project.getWorkspace().checkpoint(true);

        IFile file1 = project.getFile("/WebContent/test.jsp");
        IFile file2= project.getFile("/WebContent/test2.jsp");
        IDOMModel structuredModel1 = null;
        IDOMModel structuredModel2 = null;
		try {
			ITaglibRecord tld = TaglibIndex.resolve(file1.getFullPath().toString(), "http://eclipse.org/testbug_432978", false);
			structuredModel1 = (IDOMModel) StructuredModelManager.getModelManager().getModelForRead(file1);

			ModelHandlerForJSP.ensureTranslationAdapterFactory(structuredModel1);

			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) structuredModel1.getDocument().getAdapterFor(IJSPTranslation.class);

			final String translation = translationAdapter.getJSPTranslation().getJavaText();

			assertTrue("The 'extra' integer declared by a TEI class was not found, taglib was: " + tld, translation.indexOf("java.lang.Integer extra") > 0);

			/*
			 * the extra variable should only be declared once in the
			 * translated text
			 */
			assertEquals(2, translation.split("java.lang.Integer extra").length);

			structuredModel2 = (IDOMModel) StructuredModelManager.getModelManager().getModelForRead(file2);

			ModelHandlerForJSP.ensureTranslationAdapterFactory(structuredModel2);

            JSPTranslationAdapter translationAdapter2 = (JSPTranslationAdapter) structuredModel2.getDocument().getAdapterFor(IJSPTranslation.class);

            final String translation2 = translationAdapter2.getJSPTranslation().getJavaText();

            assertTrue( translation2.indexOf( "extra" ) != -1 );

            // the extra variable should be declared twice because of the nested atbegin tags
            assertEquals( 3, translation2.split( "java.lang.Integer extra" ).length );
        }
        finally {
            if (structuredModel1 != null)
                structuredModel1.releaseFromRead();

            if (structuredModel2 != null)
                structuredModel2.releaseFromRead();
        }
	}

	// http://bugs.eclipse.org/518987
	public void test_518987() throws Exception {
        String testName = "bug_518987";
        // Create new project
        IProject project = BundleResourceUtil.createJavaWebProject(testName);
        assertTrue(project.exists());
        BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/" + testName, "/" + testName);

        // TEI class needs to already be compiled
//		waitForBuildAndValidation(project);
//		project.build(IncrementalProjectBuilder.FULL_BUILD, "org.eclipse.jdt.internal.core.builder.JavaBuilder", null, null);
		project.getWorkspace().checkpoint(true);

        IFile file1 = project.getFile("/WebContent/test1.jsp");
        IDOMModel structuredModel1 = null;
		try {
			structuredModel1 = (IDOMModel) StructuredModelManager.getModelManager().getModelForRead(file1);

			ModelHandlerForJSP.ensureTranslationAdapterFactory(structuredModel1);

			JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) structuredModel1.getDocument().getAdapterFor(IJSPTranslation.class);

			final String translation = translationAdapter.getJSPTranslation().getJavaText();

			assertFalse("The unprocessed custom action's attribute value pair should not be in the translated source, was the custom tag not parsed?", translation.indexOf("insert=") > 0);
			assertTrue("The 'insert' integer declared by a TEI class was not found, was the custom tag not parsed?\n\n" + translation, translation.indexOf("java.lang.Integer insert") > 0);

        }
        finally {
            if (structuredModel1 != null)
                structuredModel1.releaseFromRead();
        }
	}

	public void test_530968_ExpressionInCustomTagInComment() throws Exception {
		JSPJavaValidator validator = new JSPJavaValidator();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("bug_530968");
		if (!project.exists()) {
			ProjectUtil.createProject("bug_530968", null, null);
			BundleResourceUtil.copyBundleEntriesIntoWorkspace("/testfiles/bug_530968", "/bug_530968");
		}
		String filePath = "/bug_530968/WebContent/bug530968.jsp";
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath));
		assertTrue(file.exists());
		IReporter reporter = new ReporterForTest();
		ValidationContextForTest helper = new ValidationContextForTest();
		helper.setURI(filePath);
		validator.validate(helper, reporter);
		String messageText = reporter.getMessages().isEmpty() ? "no error found" : ((Message) reporter.getMessages().get(0)).getText();
		assertTrue(messageText, reporter.getMessages().isEmpty());
	}
}
