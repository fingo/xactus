/*******************************************************************************
 * Copyright (c) 2003, 2017 IBM Corporation and others.
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

package org.eclipse.wst.web.internal.deployables;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IModuleArtifact;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.util.WebResource;

public class StaticWebDeployableObjectAdapterUtil {

	private final static String[] extensionsToExclude = new String[]{"sql", "xmi"}; //$NON-NLS-1$ //$NON-NLS-2$
	static String INFO_DIRECTORY = "WEB-INF"; //$NON-NLS-1$

	public static IModuleArtifact getModuleObject(Object obj) {
		IResource resource = null;
		if (obj instanceof IResource)
			resource = (IResource) obj;
		else if (obj instanceof IAdaptable)
			resource = ((IAdaptable) obj).getAdapter(IResource.class);
		
		if (resource == null)
			return null;
		
		if (resource instanceof IProject) {
			IProject project = (IProject) resource;
			if (hasInterestedComponents(project))
				return new WebResource(getModule(project), new Path("")); //$NON-NLS-1$
			return null;	
		}
		
		IProject project = ProjectUtilities.getProject(resource);
		if (project != null && !hasInterestedComponents(project))
			return null;
		
		IVirtualComponent comp = ComponentCore.createComponent(project);
		// determine path
		IPath rootPath = comp.getRootFolder().getProjectRelativePath();
		IPath resourcePath = resource.getProjectRelativePath();

		// Check to make sure the resource is under the webApplication directory
		if (resourcePath.matchingFirstSegments(rootPath) != rootPath.segmentCount())
			return null;

		// Do not allow resource under the web-inf directory
		resourcePath = resourcePath.removeFirstSegments(rootPath.segmentCount());
		if (resourcePath.segmentCount() > 1 && resourcePath.segment(0).equals(INFO_DIRECTORY))
			return null;

		if (shouldExclude(resource))
			return null;

		// return Web resource type
		return new WebResource(getModule(project), resourcePath);

	}

	/**
	 * Method shouldExclude.
	 * 
	 * @param resource
	 * @return boolean
	 */
	private static boolean shouldExclude(IResource resource) {
		String fileExt = resource.getFileExtension();

		// Exclude files of certain extensions
		for (int i = 0; i < extensionsToExclude.length; i++) {
			String extension = extensionsToExclude[i];
			if (extension.equalsIgnoreCase(fileExt))
				return true;
		}
		return false;
	}

	protected static IModule getModule(IProject project) {
		if (hasInterestedComponents(project))
			return ServerUtil.getModule(project);
		return null;
	}
	
	protected static boolean hasInterestedComponents(IProject project) {
		return isProjectOfType(project, IModuleConstants.WST_WEB_MODULE);
	}
	
	protected static boolean isProjectOfType(IProject project, String typeID) {
		IFacetedProject facetedProject = null;
		try {
			facetedProject = ProjectFacetsManager.create(project);
		} catch (CoreException e) {
			return false;
		}

		if (facetedProject != null && ProjectFacetsManager.isProjectFacetDefined(typeID)) {
			IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(typeID);
			return projectFacet != null && facetedProject.hasProjectFacet(projectFacet);
		}
		return false;
	}
}
