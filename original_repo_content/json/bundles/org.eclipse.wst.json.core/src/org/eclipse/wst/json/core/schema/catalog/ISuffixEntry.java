/*******************************************************************************
 * Copyright (c) 2009, 2015 Jesper Steen Moeller
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jesper Steen Moeller - Added XML Catalogs 1.1 support
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.xml.core.internal.catalog.provisional.ISuffixEntry
 *                                           modified in order to process JSON Objects.                         
 *******************************************************************************/
package org.eclipse.wst.json.core.schema.catalog;

/**
 * 
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * 
 */
public interface ISuffixEntry extends ICatalogElement
{
    /** The rewriteSystem Catalog type. */
    int SUFFIX_TYPE_SYSTEM = 41;

    /** The URI Catalog Entry type. */
    int SUFFIX_TYPE_URI = 42;

    /**
     * 
     * @param entryType
     */
    void setEntryType(int entryType);

    /**
     * 
     * @return
     */
    int getEntryType();

    /**
     * 
     * @param key
     */
    void setSuffix(String suffixString);

    /**
     * 
     * @return
     */
    String getSuffix();

    /**
     * 
     * @return
     */
    String getURI();

    /**
     * 
     * @param uri
     */
    void setURI(String uri);
}
