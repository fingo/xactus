/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.html.core.internal.validate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class CustomHTMLAttributeValidatorExtensionLoader {
private List<IConfigurationElement> validators;
	
	public List<IConfigurationElement> getValidators() {
		return validators;
	}
	
	private CustomHTMLAttributeValidatorExtensionLoader() {
		validators = new ArrayList<IConfigurationElement>();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.wst.html.core.customAttributeValidator");
		for (IConfigurationElement e : configurationElements) {
			validators.add(e);
		}
	}

	private static class UnknownValidatorExtensionLoaderHolder {
		public static final CustomHTMLAttributeValidatorExtensionLoader instance = new CustomHTMLAttributeValidatorExtensionLoader();
	}

	public static CustomHTMLAttributeValidatorExtensionLoader getInstance() {
		return UnknownValidatorExtensionLoaderHolder.instance;
	}
}
