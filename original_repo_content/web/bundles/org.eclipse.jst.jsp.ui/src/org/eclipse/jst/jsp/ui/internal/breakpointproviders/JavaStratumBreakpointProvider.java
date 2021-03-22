/*******************************************************************************
 * Copyright (c) 2004, 2017 IBM Corporation and others.
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
package org.eclipse.jst.jsp.ui.internal.breakpointproviders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jst.jsp.core.text.IJSPPartitions;
import org.eclipse.jst.jsp.ui.internal.JSPUIMessages;
import org.eclipse.jst.jsp.ui.internal.JSPUIPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.wst.sse.ui.internal.StructuredResourceMarkerAnnotationModel;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.ISourceEditingTextTools;
import org.eclipse.wst.sse.ui.internal.provisional.extensions.breakpoint.IBreakpointProvider;

/**
 * A IBreakpointProvider supporting JSP breakpoints for a Non-Java Language
 * Source JSP page
 */
public class JavaStratumBreakpointProvider implements IBreakpointProvider, IExecutableExtension {
	private static final String DEFAULT_CLASS_PATTERN = "*jsp,jsp_servlet._*";
	private Object fData = null;

	public IStatus addBreakpoint(IDocument document, IEditorInput input, int editorLineNumber, int offset) throws CoreException {
		// check if there is a valid position to set breakpoint
		int pos = getValidPosition(document, editorLineNumber);
		IStatus status = null;
		if (pos >= 0) {
			IResource res = getResourceFromInput(input);
			if (res != null) {
				String path = null;
				IBreakpoint point = JDIDebugModel.createStratumBreakpoint(res, "JSP", res.getName(), path, getClassPattern(res), editorLineNumber, pos, pos, 0, true, null); //$NON-NLS-1$
				if (point == null) {
					status = new Status(IStatus.ERROR, JSPUIPlugin.ID, IStatus.ERROR, "unsupported input type", null); //$NON-NLS-1$
				}
			}
			else if (input instanceof IStorageEditorInput) {
				// For non-resources, use the workspace root and a coordinated
				// attribute that is used to
				// prevent unwanted (breakpoint) markers from being loaded
				// into the editors.
				res = ResourcesPlugin.getWorkspace().getRoot();
				String id = input.getName();
				if (input instanceof IStorageEditorInput && ((IStorageEditorInput) input).getStorage() != null && ((IStorageEditorInput) input).getStorage().getFullPath() != null) {
					id = ((IStorageEditorInput) input).getStorage().getFullPath().toString();
				}
				Map attributes = new HashMap();
				attributes.put(StructuredResourceMarkerAnnotationModel.SECONDARY_ID_KEY, id);
				String path = null;
				IBreakpoint point = JDIDebugModel.createStratumBreakpoint(res, "JSP", input.getName(), path, getClassPattern(res), editorLineNumber, pos, pos, 0, true, attributes); //$NON-NLS-1$
				if (point == null) {
					status = new Status(IStatus.ERROR, JSPUIPlugin.ID, IStatus.ERROR, "unsupported input type", null); //$NON-NLS-1$
				}
			}
		}
		else {
			status = new Status(IStatus.INFO, JSPUIPlugin.ID, IStatus.INFO, JSPUIMessages.BreakpointNotAllowed, null);
		}
		if (status == null) {
			status = new Status(IStatus.OK, JSPUIPlugin.ID, IStatus.OK, JSPUIMessages.OK, null);
		}
		return status;
	}

	private String getClassPattern(IResource resource) {
		if (resource != null) {
			String shortName = resource.getName();
			String extension = resource.getFileExtension();
			if (extension != null && extension.length() < shortName.length()) {
				shortName = shortName.substring(0, shortName.length() - extension.length() - 1);
			}
			if (fData instanceof String && fData.toString().length() > 0) {
				/*
				 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=154475
				 */
				return fData + ",_" + shortName;
			}
			else if (fData instanceof Map && resource.isAccessible() && resource.getType() == IResource.FILE) {
				IContentType[] types = Platform.getContentTypeManager().findContentTypesFor(resource.getName());
				if (types.length == 0) {
					// if failed to find quickly, be more aggressive
					IContentDescription d = null;
					try {
						// optimized description lookup, might not succeed
						d = ((IFile) resource).getContentDescription();
						if (d != null) {
							types = new IContentType[]{d.getContentType()};
						}
					}
					catch (CoreException e) {
						/*
						 * should not be possible given the accessible and
						 * file type check above
						 */
					}
				}
				// wasn't found earlier
				if (types == null) {
					types = Platform.getContentTypeManager().findContentTypesFor(resource.getName());
				}
				StringBuffer patternBuffer = new StringBuffer("_" + shortName);
				final Set contributions = new HashSet(0);
				for (int i = 0; i < types.length; i++) {
					final String id = types[i].getId();
					Object pattern = ((Map) fData).get(id);
					if (pattern != null) {
						patternBuffer.append(","); //$NON-NLS-1$
						patternBuffer.append(pattern);
					}
					 // Append contributions
					
					final Iterator it = ClassPatternRegistry.getInstance().getClassPatternSegments(id);
					while (it.hasNext()) {
						contributions.add(it.next());
					}
				}
				if (contributions.size() > 0) {
					final Iterator it = contributions.iterator();
					while (it.hasNext()) {
						patternBuffer.append(',');
						patternBuffer.append(it.next());
					}
				}
				return patternBuffer.toString();
			}
		}
		return DEFAULT_CLASS_PATTERN;
	}

	public IResource getResource(IEditorInput input) {
		return getResourceFromInput(input);
	}

	private IResource getResourceFromInput(IEditorInput input) {
		IResource resource = input.getAdapter(IFile.class);
		if (resource == null) {
			resource = input.getAdapter(IResource.class);
		}
		return resource;
	}

	/**
	 * Finds a valid position somewhere on lineNumber in document, idoc, where
	 * a breakpoint can be set and returns that position. -1 is returned if a
	 * position could not be found.
	 * 
	 * @param idoc
	 * @param editorLineNumber
	 * @return position to set breakpoint or -1 if no position could be found
	 */
	private int getValidPosition(IDocument idoc, int editorLineNumber) {
		int result = -1;
		if (idoc != null) {

			int startOffset = 0;
			int endOffset = 0;
			try {
				IRegion line = idoc.getLineInformation(editorLineNumber - 1);
				startOffset = line.getOffset();
				endOffset = Math.max(line.getOffset(), line.getOffset() + line.getLength());

				String lineText = idoc.get(startOffset, endOffset - startOffset).trim();

				// blank lines or lines with only an open or close brace or
				// scriptlet tag cannot have a breakpoint
				if (lineText.equals("") || lineText.equals("{") || //$NON-NLS-1$ //$NON-NLS-2$
							lineText.equals("}") || lineText.equals("<%")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					result = -1;
				}
				else {
					// get all partitions for current line
					ITypedRegion[] partitions = null;

					partitions = idoc.computePartitioning(startOffset, endOffset - startOffset);


					for (int i = 0; i < partitions.length; ++i) {
						String type = partitions[i].getType();
						// if found jsp java content, jsp directive tags,
						// custom
						// tags,
						// return that position
						if (type == IJSPPartitions.JSP_CONTENT_JAVA || type == IJSPPartitions.JSP_DIRECTIVE ||
								type == IJSPPartitions.JSP_DEFAULT_EL || type == IJSPPartitions.JSP_DEFAULT_EL2) {
							result = partitions[i].getOffset();
						}
					}
				}
			}
			catch (BadLocationException e) {
				result = -1;
			}
		}

		return result;
	}

	/**
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
	 *      java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		fData = data;
	}

	public void setSourceEditingTextTools(ISourceEditingTextTools tools) {
		// not used
	}
}
