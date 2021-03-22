/*******************************************************************************
 * Copyright (c) 2007, 2010 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.internal.debug.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Table;

/**
 * An <code>AbstractLaunchConfigurationTab</code> specialised for blocks that
 * contain a table. This abstract class conveniently saves and restores the
 * table's column settings.
 * 
 * @author Doug Satchwell
 * @since 1.0
 */
public abstract class AbstractTableBlock extends AbstractLaunchConfigurationTab {
	private int fSortColumn;

	protected abstract Table getTable();

	protected abstract IDialogSettings getDialogSettings();

	protected abstract String getQualifier();

	protected void setSortColumn(int column) {
		fSortColumn = column;
	}

	/**
	 * Persist table settings into the give dialog store, prefixed with the
	 * given key.
	 */
	public void saveColumnSettings() {
		int columnCount = getTable().getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			getDialogSettings()
					.put(
							getQualifier() + ".columnWidth" + i, getTable().getColumn(i).getWidth()); //$NON-NLS-1$
		}
		getDialogSettings().put(getQualifier() + ".sortColumn", fSortColumn); //$NON-NLS-1$
	}

	/**
	 * Restore table settings from the given dialog store using the given key.
	 */
	public void restoreColumnSettings() {
		getTable().layout(true);
		restoreColumnWidths(getDialogSettings(), getQualifier());
		int col = 0;
		try {
			col = getDialogSettings().getInt(getQualifier() + ".sortColumn"); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			col = 1;
		}
		setSortColumn(col);
	}

	private void restoreColumnWidths(IDialogSettings settings, String qualifier) {
		int columnCount = getTable().getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			int width = -1;
			try {
				width = settings.getInt(qualifier + ".columnWidth" + i); //$NON-NLS-1$
			} catch (NumberFormatException e) {
			}

			if (width > 0)
				getTable().getColumn(i).setWidth(width);
		}
	}

	@Override
	public void dispose() {
		if (getTable() != null && !getTable().isDisposed())
			saveColumnSettings();
		super.dispose();
	}
}
