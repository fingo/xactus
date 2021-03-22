/*******************************************************************************
 * Copyright (c) 2001, 2019 IBM Corporation and others.
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
package org.eclipse.wst.sse.ui.internal.debug;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.ITextEditor;

public class ToggleBreakpointsTarget implements IToggleBreakpointsTarget {
	static final IToggleBreakpointsTarget instance = new ToggleBreakpointsTarget();

	public static IToggleBreakpointsTarget getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	private ToggleBreakpointsTarget() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		ITextEditor editor = part.getAdapter(ITextEditor.class);
		if (selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) selection;
			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			if (document != null && textSelection.getOffset() > -1) {
				int lineNumber = -1;
				try {
					lineNumber = document.getLineOfOffset(textSelection.getOffset());
				}
				catch (BadLocationException e) {
				}
				if (lineNumber >= 0) {
					ToggleBreakpointAction toggler = new ToggleBreakpointAction(editor, null);
					toggler.update();
					return toggler.isEnabled();
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleWatchpoints(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

	private IBreakpoint[] getBreakpoints(IResource resource, IDocument document, AbstractMarkerAnnotationModel model, int lineNumber) {
		List<IMarker> markers = new ArrayList<>();
		if (resource != null && model != null && resource.exists()) {
			try {
				IMarker[] allMarkers = resource.findMarkers(IBreakpoint.LINE_BREAKPOINT_MARKER, true, IResource.DEPTH_ZERO);
				if (allMarkers != null) {
					for (int i = 0; i < allMarkers.length; i++) {
						Position p = model.getMarkerPosition(allMarkers[i]);
						if (p == null) {
							continue;
						}
						int markerLine = -1;
						try {
							markerLine = document.getLineOfOffset(p.getOffset());
						}
						catch (BadLocationException e1) {
						}
						if (markerLine == lineNumber) {
							markers.add(allMarkers[i]);
						}
					}
				}
			}
			catch (CoreException x) {
			}
		}
		IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
		List<IBreakpoint> breakpoints = new ArrayList<>(markers.size());
		for (int i = 0; i < markers.size(); i++) {
			IBreakpoint breakpoint = manager.getBreakpoint(markers.get(i));
			if (breakpoint != null) {
				breakpoints.add(breakpoint);
			}
		}
		return breakpoints.toArray(new IBreakpoint[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		ITextEditor editor = part.getAdapter(ITextEditor.class);
		if (selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection) selection;
			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			int lineNumber = -1;
			try {
				lineNumber = document.getLineOfOffset(textSelection.getOffset());
			}
			catch (BadLocationException e) {
			}
			if (lineNumber >= 0) {
				ToggleBreakpointAction toggler = new ToggleBreakpointAction(editor, null);
				toggler.update();
				if (toggler.isEnabled()) {
					IResource resource = toggler.getResource();
					AbstractMarkerAnnotationModel model = toggler.getAnnotationModel();
					IBreakpoint[] breakpoints = getBreakpoints(resource, document, model, lineNumber);
					if (breakpoints.length > 0) {
						IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
						for (int i = 0; i < breakpoints.length; i++) {
							breakpointManager.removeBreakpoint(breakpoints[i], true);
							breakpoints[i].getMarker().delete();
						}
					}
					else {
						toggler.createBreakpoints(lineNumber + 1);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleWatchpoints(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
	}

}
