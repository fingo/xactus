/*******************************************************************************
 * Copyright (c) 2005, 2020 IBM Corporation and others.
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
package org.eclipse.jst.jsp.core.internal.taglib;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.jsp.core.internal.JSPCoreMessages;
import org.eclipse.jst.jsp.core.internal.contenttype.DeploymentDescriptorPropertyCache;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslatorPersister;
import org.eclipse.jst.jsp.core.internal.provisional.contenttype.ContentTypeIdForJSP;

/**
 * Manages creation and caching (ordered MRU) of TaglibHelpers.
 * Removes helpers when their classpath changes (so they are rebuilt).
 * There is one helper per project (with a specific classpath entry).
 * 
 * @author pavery
 */
public class TaglibHelperManager implements IElementChangedListener {
   

    private static TaglibHelperManager instance = null;
    // using a cache of just 3 loaders
    private TaglibHelperCache fCache = new TaglibHelperCache(3);

    private TaglibHelperManager() {
        // use instance
    }
    public static synchronized TaglibHelperManager getInstance() {
        if(instance == null)
            instance = new TaglibHelperManager();
        return instance;
    }
    
    public TaglibHelper getTaglibHelper(IFile f) {
        IProject p = f.getProject();
        return getHelperFromCache(p);
    }
    
    /**
     * @param projectPath
     */
    private TaglibHelper getHelperFromCache(IProject project) {
        return fCache.getHelper(project);
    }
    
    /**
     * Update classpath for appropriate loader.
     * @see org.eclipse.jdt.core.IElementChangedListener#elementChanged(org.eclipse.jdt.core.ElementChangedEvent)
     */
	public void elementChanged(ElementChangedEvent event) {
		// handle classpath changes
		IJavaElementDelta delta = event.getDelta();
		if (delta.getElement().getElementType() == IJavaElement.JAVA_MODEL) {
			IJavaElementDelta[] changed = delta.getChangedChildren();
			for (int i = 0; i < changed.length; i++) {
				if ((changed[i].getFlags() & IJavaElementDelta.F_CLASSPATH_CHANGED) != 0 || (changed[i].getFlags() & IJavaElementDelta.F_REORDER) != 0 || (changed[i].getFlags() & IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED) != 0 || (changed[i].getFlags() & IJavaElementDelta.F_PRIMARY_RESOURCE) != 0) {
					IJavaElement project = changed[i].getElement();
					handleClasspathChange(changed, i, project);
					DeploymentDescriptorPropertyCache.getInstance().invalidate(project.getJavaProject().getElementName());
				}
			}
		}
		else if (delta.getElement().getElementType() == IJavaElement.COMPILATION_UNIT) {
			IJavaElementDelta[] changed = delta.getChangedChildren();
			for (int i = 0; i < changed.length; i++) {
				if ((changed[i].getFlags() & IJavaElementDelta.F_SUPER_TYPES) != 0) {
					IJavaElement element = changed[i].getElement();
					handleSuperTypeChange(element);
				}
			}
		}
	}

	private void handleSuperTypeChange(IJavaElement element) {
		IJavaProject project = element.getJavaProject();
		if (element instanceof IType) {
			fCache.invalidate(project.getProject().getName(), ((IType) element).getFullyQualifiedName());
		}
	}
    
    /**
     * @param changed
     * @param i
     * @param proj
     */
    private void handleClasspathChange(IJavaElementDelta[] changed, int i, IJavaElement proj) {
        if (proj.getElementType() == IJavaElement.JAVA_PROJECT) {
			final IProject project = ((IJavaProject) proj).getProject();
			String projectName = project.getName();
			fCache.removeHelper(projectName);
			Job toucher = new Job(JSPCoreMessages.Processing_BuildPath_Changes) {
				protected IStatus run(IProgressMonitor monitor) {
					//touch JSPs
					try {
						project.accept(new IResourceProxyVisitor() {
							public boolean visit(IResourceProxy proxy) throws CoreException {
								if (!proxy.isDerived() && ContentTypeIdForJSP.indexOfJSPExtension(proxy.getName()) >= 0) {
									JSPTranslatorPersister.removePersistedTranslation(proxy.requestResource());
//									proxy.requestResource().touch(null);
								}
								return !proxy.isDerived();
							}
						}, IResource.DEPTH_INFINITE);
					}
					catch (CoreException e) {
						// ignore
					}
					return Status.OK_STATUS;
				}
			};
			toucher.setRule(project.getProject());
			toucher.setPriority(Job.BUILD);
			toucher.setUser(false);
			toucher.setSystem(false);
			toucher.schedule();
		}
    }
}
