/*******************************************************************************
 * Copyright (c) 2007, 2020 IBM Corporation and others.
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
package org.eclipse.jst.jsp.core.internal.util;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * This class encapsulates any used Module Core and Facets APIs along with
 * fallbacks for use on non-compliant projects and when those services are not
 * available at runtime.
 * 
 * Because ModuleCore API calls can result in locks needing to be acquired,
 * none of these methods should be called while other thread locks have
 * already been acquired.
 */
public final class FacetModuleCoreSupport {
	static final boolean _dump_NCDFE = false;
	private static final String WEB_INF = "WEB-INF"; //$NON-NLS-1$
	private static final IPath WEB_INF_PATH = new Path(WEB_INF);
	static final String META_INF_RESOURCES = "META-INF/resources/"; //$NON-NLS-1$
	static final IPath META_INF_RESOURCES_PATH = new Path(META_INF_RESOURCES);

	static final float DEFAULT_SERVLET_VERSION = 4.0f;

	/**
	 * @param project
	 * @return the computed IPath to the "root" of the web contents, either from facet knowledge or hueristics, or null if one can not be determined
	 */
	public static IPath computeWebContentRootPath(IPath path) {
		IPath root = null;
		try {
			root = FacetModuleCoreSupportDelegate.getWebContentRootPath(ResourcesPlugin.getWorkspace().getRoot().getProject(path.segment(0)));
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
		}
		if(root == null) {
			/*
			 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=213245
			 * 
			 * NPE in JSPTaglibDirectiveContentAssistProcessor with
			 * non-faceted project
			 */
			root = getLocalRoot(path);
		}
		return root;
	}

	/**
	 * @param project
	 * @return the version of the JST Web facet installed on the project, -1 otherwise
	 * @throws org.eclipse.core.runtime.CoreException
	 */
	public static float getDynamicWebProjectVersion(IProject project) {
		float version = -1;
		try {
			version = FacetModuleCoreSupportDelegate.getDynamicWebProjectVersion(project);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
		}
		return version;
	}

	/**
	 * @param project
	 * @return the IPath to the "root" of the web contents
	 */
	public static IPath getWebContentRootPath(IProject project) {
		if (project == null)
			return null;

		IPath path = null;
		try {
			path = FacetModuleCoreSupportDelegate.getWebContentRootPath(project);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
		}
		return path;
	}

	/**
	 * @param project
	 * @return the IPaths to acceptable "roots" in a project
	 */
	public static IPath[] getAcceptableRootPaths(IProject project) {
		if (project == null)
			return null;
		IPath[] paths = null;
		try {
			paths = FacetModuleCoreSupportDelegate.getAcceptableRootPaths(project);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
			return new IPath[]{project.getFullPath()};
		}
		return paths;
	}

	/**
	 * @param path
	 *            - the full path to a resource within the workspace
	 * @return - the runtime path of the resource if one exists, an
	 *         approximation otherwise
	 */
	public static IPath getRuntimePath(IPath path) {
		IPath result = null;
		try {
			result = FacetModuleCoreSupportDelegate.getRuntimePath(path);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
		}
		if (result == null) {
			IPath root = getLocalRoot(path);
			result = path.removeFirstSegments(root.segmentCount()).makeAbsolute();
		}
		return result;
	}

	/**
	 * @param project
	 * @return whether this project has the jst.web facet installed on it
	 * @throws CoreException
	 */
	public static boolean isDynamicWebProject(IProject project) {
		if (project == null)
			return false;
		
		try {
			return FacetModuleCoreSupportDelegate.isDynamicWebProject(project);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
		}
		return true;
	}

	/**
	 * @param project
	 * @return whether this project has the jst.webfragment facet installed on it
	 * @throws CoreException
	 */
	public static boolean isWebFragmentProject(IProject project) {
		if (project == null)
			return false;
		
		try {
			return FacetModuleCoreSupportDelegate.isWebFragmentProject(project);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
		}
		return true;
	}

	/**
	 * @param basePath -
	 *            the full path to a resource within the workspace
	 * @param reference -
	 *            the reference string to resolve
	 * @return - the full path within the workspace that corresponds to the
	 *         given reference according to the virtual pathing support
	 */
	public static IPath resolve(IPath basePath, String reference) {
		IPath resolvedPath = null;
		try {
			resolvedPath = FacetModuleCoreSupportDelegate.resolve(basePath, reference);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
		}

		if (resolvedPath == null) {
			IPath rootPath = getLocalRoot(basePath);
			if (reference.startsWith(Path.ROOT.toString())) {
				resolvedPath = rootPath.append(reference);
			}
			else {
				resolvedPath = basePath.removeLastSegments(1).append(reference);
			}
		}

		return resolvedPath;
	}

	/**
	 * @param basePath
	 * @return an approximation of the applicable Web context root path, if one could be found
	 */
	private static IPath getLocalRoot(IPath basePath) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		// existing workspace resources - this is the 93% case
		IResource file = FileBuffers.getWorkspaceFileAtLocation(basePath);

		// Try the base path as a folder first
		if (file == null && basePath.segmentCount() > 1) {
			file = workspaceRoot.getFolder(basePath);
		}
		// If not a folder, then try base path as a file
		if (file != null && !file.exists() && basePath.segmentCount() > 1) {
			file = workspaceRoot.getFile(basePath);
		}

		if (file == null && basePath.segmentCount() == 1) {
			file = workspaceRoot.getProject(basePath.segment(0));
		}

		if (file == null) {
			/*
			 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=116529
			 * 
			 * This method produces a less accurate result, but doesn't
			 * require that the file exist yet.
			 */
			IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(basePath);
			if (files.length > 0)
				file = files[0];
		}

		while (file != null) {
			/**
			 * Treat any parent folder with a WEB-INF subfolder as a web-app
			 * root
			 */
			IContainer folder = null;
			if ((file.getType() & IResource.FOLDER) != 0) {
				folder = (IContainer) file;
			}
			else {
				folder = file.getParent();
			}
			// getFolder on a workspace root must use a full path, skip
			if (folder != null && (folder.getType() & IResource.ROOT) == 0) {
				IFolder webinf = folder.getFolder(WEB_INF_PATH);
				IFolder metaResources = folder.getFolder(META_INF_RESOURCES_PATH);
				if ((webinf != null && webinf.exists()) || (metaResources != null && metaResources.exists())) {
					return folder.getFullPath();
				}
			}
			file = file.getParent();
		}

		return basePath.uptoSegment(1);
	}

	/**
	 * @param project
	 * @return the projects referenced by this project for deployment, but not
	 *         necessarily on its Java Build path, e.g. web gfragments, or
	 *         <code>null</code>
	 */
	public static IProject[] getReferenced(IProject project) {
		if (project == null)
			return null;
		IProject[] projects = null;
		try {
			projects = FacetModuleCoreSupportDelegate.getReferenced(project);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
			return null;
		}
		return projects;
	}

	/**
	 * Gets the root container for the path in the project
	 * @param project
	 * @param path
	 * @return
	 */
	public static IPath getRootContainerForPath(IProject project, IPath path) {
		if (project == null)
			return null;
		IPath root = null;
		try {
			root = FacetModuleCoreSupportDelegate.getRootContainerForPath(project, path);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
			return null;
		}
		return root;
	}

	/**
	 * Gets the default root container for the project
	 * @param project
	 * @return
	 */
	public static IPath getDefaultRootContainer(IProject project) {
		if (project == null)
			return null;
		IPath root = null;
		try {
			root = FacetModuleCoreSupportDelegate.getDefaultRoot(project);
		}
		catch (NoClassDefFoundError e) {
			if (_dump_NCDFE)
				e.printStackTrace();
			return null;
		}
		return root;
	}
}
