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
package org.eclipse.wst.html.core.tests.utils;

import java.io.StringWriter;

public class TestWriter extends StringWriter {
	// we don't really want to use the system EOL, since
	// we want a common once across platforms, CVS, etc.
	public final static String commonEOL = "\r\n";

	//System.getProperty("line.separator"); 

	public void writeln(String line) {
		write(line);
		write(commonEOL);
	}
}