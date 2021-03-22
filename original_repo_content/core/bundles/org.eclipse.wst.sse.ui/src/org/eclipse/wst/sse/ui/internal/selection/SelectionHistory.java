/*******************************************************************************
 * Copyright (c) 2001, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *******************************************************************************/
package org.eclipse.wst.sse.ui.internal.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.texteditor.ITextEditor;

public class SelectionHistory {
	private ITextEditor fEditor;
	private List<IRegion> fHistory;
	private List<IAction> fHistoryActions;
	private int fSelectionChangeListenerCounter;
	private ISelectionChangedListener fSelectionListener;

	public SelectionHistory(ITextEditor editor) {
		Assert.isNotNull(editor);
		fEditor = editor;
		fHistory = new ArrayList<>(3);
		fSelectionListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (fSelectionChangeListenerCounter == 0)
					flush();
			}
		};
		fEditor.getSelectionProvider().addSelectionChangedListener(fSelectionListener);
	}

	public void dispose() {
		fEditor.getSelectionProvider().removeSelectionChangedListener(fSelectionListener);
		fEditor = null;
		if (fHistory != null && !fHistory.isEmpty()) {
			fHistory.clear();
		}
		if (fHistoryActions != null && !fHistoryActions.isEmpty()) {
			fHistoryActions.clear();
		}
	}

	public void flush() {
		if (fHistory.isEmpty())
			return;
		fHistory.clear();
		updateHistoryAction();
	}

	public IRegion getLast() {
		if (isEmpty())
			return null;
		int size = fHistory.size();
		IRegion result = fHistory.remove(size - 1);
		updateHistoryAction();
		return result;
	}

	public void ignoreSelectionChanges() {
		fSelectionChangeListenerCounter++;
	}

	public boolean isEmpty() {
		return fHistory.isEmpty();
	}

	public void listenToSelectionChanges() {
		fSelectionChangeListenerCounter--;
	}

	public void remember(IRegion region) {
		fHistory.add(region);
		updateHistoryAction();
	}

	public void setHistoryAction(IAction action) {
		Assert.isNotNull(action);
		
		if (fHistoryActions == null)
			fHistoryActions = new ArrayList<IAction>();
		if (!fHistoryActions.contains(action))
			fHistoryActions.add(action);
		
		// update action
		if (fHistory != null && !fHistory.isEmpty())
			action.setEnabled(true);
		else
			action.setEnabled(false);
	}

	private void updateHistoryAction() {
		if (fHistoryActions != null && !fHistoryActions.isEmpty()) {
			boolean enabled = false;
			if (fHistory != null && !fHistory.isEmpty())
				enabled = true;

			Iterator<IAction> iter = fHistoryActions.iterator();
			while (iter.hasNext()) {
				iter.next().setEnabled(enabled);
			}
		}
	}
}
