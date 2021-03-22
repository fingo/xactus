/*******************************************************************************
 * Copyright (c) 2007, 2013 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *     David Carver (Intalio) - clean up find bugs
 *     Jesper S Moller - 405223 - Processing and file name/type doesn't match output type from XSL
 *******************************************************************************/
package org.eclipse.wst.xsl.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.wst.xsl.internal.launching.LaunchingPlugin;

public class XSLTRuntime {
	private static void savePreferences() {
		LaunchingPlugin.getDefault().savePluginPreferences();
	}

	private static Preferences getPreferences() {
		return LaunchingPlugin.getDefault().getPluginPreferences();
	}

	/**
	 * Creates a default Output File for the given input file string.
	 * 
	 * @return Returns an IPath for the Output File.
	 * @since 1.0
	 */
	public static IPath defaultOutputFileForInputFile(String inputFileExpression)
			throws CoreException {
		return defaultOutputFileForInputFile(inputFileExpression, "xml"); //$NON-NLS-1$
	}

	/**
	 * Creates a default Output File for the given input file string.
	 * 
	 * @return Returns an IPath for the Output File.
	 * @since 1.0
	 */
	public static IPath defaultOutputFileForInputFile(String inputFileExpression, String method)
			throws CoreException {
		String file = VariablesPlugin.getDefault().getStringVariableManager()
				.performStringSubstitution(inputFileExpression);
		IPath inputFilePath = new Path(file);
		inputFilePath = inputFilePath.removeFileExtension();
		String extension = "xml"; //$NON-NLS-1$
		if ("text".equals(method)) extension = "txt";  //$NON-NLS-1$//$NON-NLS-2$
		else if ("html".equals(method)) extension = "html"; //$NON-NLS-1$ //$NON-NLS-2$
		else if ("xhtml".equals(method)) extension = "xhtml"; //$NON-NLS-1$ //$NON-NLS-2$
		
		inputFilePath = inputFilePath.addFileExtension("out." + extension); //$NON-NLS-1$
		return inputFilePath;
	}

}
