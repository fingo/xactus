/*******************************************************************************
 * Copyright (c) 2003, 2018 IBM Corporation and others.
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

import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.util.IStaticWeb;

public class StaticWebDeployable extends FlatComponentDeployable implements IStaticWeb {

	public StaticWebDeployable(IProject project, IVirtualComponent component) {
		super(project);
	}
	
	@Override
	public String getContextRoot() {
		Properties props = component.getMetaProperties();
		if(props.containsKey("context-root")) //$NON-NLS-1$
			return props.getProperty("context-root"); //$NON-NLS-1$
	    return component.getName();
    }
	
	/* TODO This is never called ?? */
//	 public String getURI(IModule module) {
//	    IVirtualComponent comp = ComponentCore.createComponent(module.getProject());
//	    String aURI = null;
//	    if (comp !=null) {
//	    	if (!comp.isBinary() && isProjectOfType(module.getProject(),IModuleConstants.WST_WEB_MODULE)) {
//        		IVirtualReference ref = component.getReference(comp.getName());
//        		aURI = ref.getRuntimePath().append(comp.getName()+".war").toString(); //$NON-NLS-1$
//        	}
//	    }
//	    	
//    	if (aURI !=null && aURI.length()>1 && aURI.startsWith("/")) //$NON-NLS-1$
//    		aURI = aURI.substring(1);
//    	return aURI;
//	 }
	 
	public String getVersion() {
		IFacetedProject facetedProject = null;
		try {
			facetedProject = ProjectFacetsManager.create(component.getProject());
			if (facetedProject !=null && ProjectFacetsManager.isProjectFacetDefined(IModuleConstants.WST_WEB_MODULE)) {
				IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(IModuleConstants.WST_WEB_MODULE);
				return facetedProject.getInstalledVersion(projectFacet).getVersionString();
			}
		} catch (Exception e) {
			//Ignore
		}
		return "1.0"; //$NON-NLS-1$
	}

}
