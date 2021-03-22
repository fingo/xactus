/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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
package org.eclipse.wst.css.ui.internal.contentproperties;

import java.util.Iterator;

import org.eclipse.wst.css.core.internal.metamodel.CSSProfile;
import org.eclipse.wst.css.core.internal.metamodel.CSSProfileRegistry;
import org.eclipse.wst.css.ui.internal.CSSUIMessages;
import org.eclipse.wst.sse.ui.internal.contentproperties.ui.ComboList;

/**
 * @deprecated This class only contains helper methods that you should
 *             actually implement yourself.
 */
public final class ContentSettingsRegistry {
	private static final String NONE = CSSUIMessages.UI_none;

	public static void setCSSMetaModelRegistryInto(ComboList combo) {
		combo.add(NONE, ""); //$NON-NLS-1$
		CSSProfileRegistry reg = CSSProfileRegistry.getInstance();
		Iterator i = reg.getProfiles();
		while (i.hasNext()) {
			CSSProfile profile = (CSSProfile) i.next();
			String id = profile.getProfileID();
			String name = profile.getProfileName();
			combo.add(name, id);
		}
		combo.sortByKey(1);
	}

}
