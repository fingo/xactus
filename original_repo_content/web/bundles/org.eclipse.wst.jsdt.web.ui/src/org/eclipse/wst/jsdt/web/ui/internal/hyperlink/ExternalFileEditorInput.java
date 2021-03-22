/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
package org.eclipse.wst.jsdt.web.ui.internal.hyperlink;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
*

* Provisional API: This class/interface is part of an interim API that is still under development and expected to
* change significantly before reaching stability. It is being made available at this early stage to solicit feedback
* from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
* (repeatedly) as the API evolves.
*/
class ExternalFileEditorInput implements IEditorInput, ILocationProvider {
	// copies of this class exist in:
	// org.eclipse.wst.xml.ui.internal.hyperlink
	// org.eclipse.wst.html.ui.internal.hyperlink
	// org.eclipse.wst.jsdt.web.ui.internal.hyperlink
	/**
	 * The workbench adapter which simply provides the label.
	 * 
	 * @see Eclipse 3.1
	 */
	private class WorkbenchAdapter implements IWorkbenchAdapter {
		/*
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object o) {
			return null;
		}
		
		/*
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
		 */
		public ImageDescriptor getImageDescriptor(Object object) {
			return null;
		}
		
		/*
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
		 */
		public String getLabel(Object o) {
			return ((ExternalFileEditorInput) o).getName();
		}
		
		/*
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
		 */
		public Object getParent(Object o) {
			return null;
		}
	}
	private File fFile;
	private WorkbenchAdapter fWorkbenchAdapter = new WorkbenchAdapter();
	
	public ExternalFileEditorInput(File file) {
		super();
		fFile = file;
		fWorkbenchAdapter = new WorkbenchAdapter();
	}
	
	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof ExternalFileEditorInput) {
			ExternalFileEditorInput input = (ExternalFileEditorInput) o;
			return fFile.equals(input.fFile);
		}
		if (o instanceof IPathEditorInput) {
			IPathEditorInput input = (IPathEditorInput) o;
			return getPath().equals(input.getPath());
		}
		return false;
	}
	
	/*
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return fFile.exists();
	}
	
	/*
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (ILocationProvider.class.equals(adapter)) {
			return this;
		}
		if (IWorkbenchAdapter.class.equals(adapter)) {
			return fWorkbenchAdapter;
		}
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}
	
	/*
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	
	/*
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return fFile.getName();
	}
	
	/*
	 * @see org.eclipse.ui.IPathEditorInput#getPath()
	 * @since 3.1
	 */
	public IPath getPath() {
		return Path.fromOSString(fFile.getAbsolutePath());
	}
	
	/*
	 * @see org.eclipse.ui.editors.text.ILocationProvider#getPath(java.lang.Object)
	 */
	public IPath getPath(Object element) {
		if (element instanceof ExternalFileEditorInput) {
			ExternalFileEditorInput input = (ExternalFileEditorInput) element;
			return Path.fromOSString(input.fFile.getAbsolutePath());
		}
		return null;
	}
	
	/*
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}
	
	/*
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return fFile.getAbsolutePath();
	}
	
	/*
	 * @see java.lang.Object#hashCode()
	 */
	
	public int hashCode() {
		return fFile.hashCode();
	}
}
