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
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.xml.core.internal.catalog.provisional.IRewriteEntry
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
public interface IRewriteEntry extends ICatalogElement
{
    /** The rewriteSystem Catalog type. */
    int REWRITE_TYPE_SYSTEM = 21;

    /** The URI Catalog Entry type. */
    int REWRITE_TYPE_URI = 22;

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
    void setStartString(String startString);

    /**
     * 
     * @return
     */
    String getStartString();

    /**
     * 
     * @return
     */
    String getRewritePrefix();

    /**
     * 
     * @param uri
     */
    void setRewritePrefix(String uri);
}
