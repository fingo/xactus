/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.css.core.internal.formatter.CSSSourceFormatter
 *                                           modified in order to process JSON Objects.
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.format;

import org.eclipse.jface.text.IRegion;
import org.eclipse.wst.json.core.document.IJSONNode;

public interface IJSONSourceFormatter {

	StringBuilder cleanup(IJSONNode node);

	StringBuilder cleanup(IJSONNode node, IRegion region);

	StringBuilder format(IJSONNode node);

	StringBuilder format(IJSONNode node, IRegion region);
}
