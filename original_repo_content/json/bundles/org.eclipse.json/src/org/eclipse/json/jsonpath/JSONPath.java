/**
 *  Copyright (c) 2013-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.json.jsonpath;

public class JSONPath implements IJSONPath {

	private final String[] segments;
	private String name;
	
	public JSONPath(String[] segments) {
		this.segments = segments;
		name = name(segments);
	}

	public JSONPath(String expression) {
		this.segments = expression.split("[.]");
		name = name(segments);
	}
	
	private String name(String[] segs) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("/");
		if (segs != null) {
			for (String seg:segs) {
				buffer.append(seg);
				buffer.append("/");
			}
		}
		return buffer.toString();
	}

	@Override
	public String[] getSegments() {
		return segments;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
