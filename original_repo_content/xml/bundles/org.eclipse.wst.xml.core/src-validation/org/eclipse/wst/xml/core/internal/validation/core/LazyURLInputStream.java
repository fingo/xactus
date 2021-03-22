/*******************************************************************************
 * Copyright (c) 2001, 2012 IBM Corporation and others.
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

package org.eclipse.wst.xml.core.internal.validation.core;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.wst.common.uriresolver.URIHelper;


/**
 * This class allows InputStreams to be created and returned to xerces without
 * actually opening file handles or network connections until it is absolutely
 * neccessary.
 */
public class LazyURLInputStream extends InputStream
{  
//  private static int debugTotalOpenStreamCount = 0; 
  protected InputStream inner;
  protected String url;
  protected boolean error;
  boolean hasMarks;
  boolean pretendFileIsStillOpen;
  public LazyURLInputStream(String url)
  {
    this.url = url;
	inner = null;
	pretendFileIsStillOpen = false;
  }

  private void createInnerStreamIfRequired() throws IOException
  {
    if (inner == null && !error)
    {
//      debugTotalOpenStreamCount++;
    
      try
      {
    	inner = URIHelper.getInputStream(url, 0);
		pretendFileIsStillOpen = false;
		hasMarks = false;
      }
      finally 
      {
        if (inner == null)
        {  
          error = true;
        }
      }
    }
  }

	protected void closeStream() throws IOException {
		if (inner != null && !pretendFileIsStillOpen) {
			inner.close();
			pretendFileIsStillOpen = true;
		}
  	}

  public int available() throws IOException
  {
	if (pretendFileIsStillOpen) return 0;
	createInnerStreamIfRequired();
	if (inner == null) throw new IOException("Stream not available"); //$NON-NLS-1$
	return inner.available();
  }

  public void close() throws IOException
  {
	if (pretendFileIsStillOpen) {
		// Stop behaving as if the stream were still open.
		pretendFileIsStillOpen = false;
	} else {
		if (inner != null) {
//			debugTotalOpenStreamCount--;
			inner.close();
		}
	}
  }

  public void mark(int readlimit)
  {
	if (pretendFileIsStillOpen) return;
	hasMarks = true;
	try {
		createInnerStreamIfRequired();
		inner.mark(readlimit);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  public boolean markSupported()
  {
	if (pretendFileIsStillOpen) return false;
	try {
		createInnerStreamIfRequired();
		if (inner == null) return false;
		return inner.markSupported();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
  }

  public int read() throws IOException
  {
	if (pretendFileIsStillOpen) return -1;
	createInnerStreamIfRequired();
	if (inner == null) throw new IOException("Stream not available"); //$NON-NLS-1$
	int bytesRead = inner.read();
	if (bytesRead == -1 && !hasMarks) closeStream();
	return bytesRead;
  }


	public int read(byte[] b) throws IOException {
		if (pretendFileIsStillOpen) return -1;
		createInnerStreamIfRequired();
		if (inner == null) throw new IOException("Stream not available"); //$NON-NLS-1$
		int bytesRead = inner.read(b);
		if (bytesRead == -1 && !hasMarks) closeStream();
		return bytesRead;
	}

  public int read(byte[] b, int off, int len) throws IOException
  {    
	if (pretendFileIsStillOpen) return -1;
	createInnerStreamIfRequired();
	if (inner == null) throw new IOException("Stream not available"); //$NON-NLS-1$
	int bytesRead = inner.read(b, off, len);
	if (bytesRead == -1 && !hasMarks) closeStream();
	return bytesRead;
  }

  public void reset() throws IOException
  {
	if (pretendFileIsStillOpen) return;
	createInnerStreamIfRequired();
	if (inner == null) throw new IOException("Stream not available"); //$NON-NLS-1$
	inner.reset();
  }

  public long skip(long n) throws IOException
  {   
	if (pretendFileIsStillOpen) return 0;
	createInnerStreamIfRequired();
	if (inner == null) throw new IOException("Stream not available"); //$NON-NLS-1$
	return inner.skip(n);
  }
}
