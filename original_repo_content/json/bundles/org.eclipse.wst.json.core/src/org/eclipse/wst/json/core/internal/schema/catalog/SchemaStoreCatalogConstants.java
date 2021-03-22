/**
 *  Copyright (c) 2013-2015 Angelo ZERR.
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
package org.eclipse.wst.json.core.internal.schema.catalog;

/**
 * SchemaStore constants
 * 
 * @see See http://schemastore.org/api.html
 * @see https 
 *      ://github.com/SchemaStore/schemastore/blob/master/src/api/json/catalog
 *      .json
 */
public interface SchemaStoreCatalogConstants {

	/** Types of the schema entries */
	/** The SCHEMA element name. */
	String TAG_SCHEMA = "schema"; //$NON-NLS-1$
	String ATTR_SCHEMA_NAME = "name"; //$NON-NLS-1$
	String ATTR_SCHEMA_DESCRIPTION = "description"; //$NON-NLS-1$
	String ATTR_SCHEMA_FILEMATCH = "fileMatch"; //$NON-NLS-1$
	String ATTR_SCHEMA_URL = "url"; //$NON-NLS-1$
	String ATTR_SCHEMA_URI = "uri"; //$NON-NLS-1$
}
