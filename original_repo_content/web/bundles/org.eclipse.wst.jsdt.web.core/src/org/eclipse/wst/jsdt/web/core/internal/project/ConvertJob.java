/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
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

package org.eclipse.wst.jsdt.web.core.internal.project;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.jsdt.internal.core.util.Messages;
import org.eclipse.wst.jsdt.web.core.internal.Logger;

/**
 * Installs the JSDT facet, if asked, and setting it as a "fixed" facet.
 * 
 */
class ConvertJob extends WorkspaceJob {
	final static String JSDT_FACET = "wst.jsdt.web";
	private IProject fProject;
	private boolean fInstall = true;
	private boolean fUseExplicitWorkingCopy = false;

	ConvertJob(IProject project, boolean install) {
		this(project, install, false);
	}

	/**
	 * Unless required, recommended {@code useExplicitWorkingCopy = false} due to
	 * {@link https://bugs.eclipse.org/bugs/show_bug.cgi?id=510377 bug 510377}.
	 */
	ConvertJob(IProject project, boolean install, boolean useExplicitWorkingCopy) {
		super(Messages.converter_ConfiguringForJavaScript);
		fProject = project;
		fInstall = install;
		fUseExplicitWorkingCopy = useExplicitWorkingCopy;
	}

	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		try {
			IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(JSDT_FACET);
			IFacetedProject facetedProject = ProjectFacetsManager.create(fProject);

			if (facetedProject != null && fProject.isAccessible()) {
				if (fInstall) {
					IProjectFacetVersion latestVersion = projectFacet.getLatestVersion();
					facetedProject.installProjectFacet(latestVersion, null, monitor);
				}


				if (fUseExplicitWorkingCopy) {
					IFacetedProjectWorkingCopy copy = facetedProject.createWorkingCopy();
					Set fixed = new HashSet(facetedProject.getFixedProjectFacets());
					fixed.add(projectFacet);
					copy.setFixedProjectFacets(fixed);
					copy.commitChanges(new NullProgressMonitor());
					copy.dispose();
				}
				else {
					Set fixed = new HashSet(facetedProject.getFixedProjectFacets());
					if (!fixed.contains(projectFacet)) {
						fixed.add(projectFacet);
						facetedProject.setFixedProjectFacets(fixed);
					}
				}
			}
		}
		catch (IllegalArgumentException e) {
			// unknown facet ID, bad installation configuration?
		}
		catch (Exception e) {
			Logger.logException(e);
		}
		return Status.OK_STATUS;
	}

}
