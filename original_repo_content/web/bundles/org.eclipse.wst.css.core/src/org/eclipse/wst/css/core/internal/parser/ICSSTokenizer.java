/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.eclipse.wst.css.core.internal.parser;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

/**
 * currently provide this interface but may be removed in future.
 */
public interface ICSSTokenizer {

	void setInitialState(int initialState);

	void setInitialBufferSize(int bufsize);

	boolean isEOF();

	void reset(Reader reader, int i);

	ITextRegion getNextToken()  throws IOException;

	void reset(char[] cs);

	int getOffset();

}
