/*******************************************************************************
 * Copyright (c) 2002, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     Angelo Zerr <angelo.zerr@gmail.com> - copied from org.eclipse.wst.xml.core.internal.catalog.provisional.ICatalogEvent
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
public interface ICatalogEvent
{
    /** */
    public static final int CHANGED = 0;

    public static final int ELEMENT_ADDED = 1;

    /** */
    public static final int ELEMENT_REMOVED = 2;

    /** */
    public static final int ELEMENT_CHANGED = 3;

    /**
     * 
     * @return
     */
    public int getEventType();

    /**
     * 
     * @return
     */
    public ICatalog getCatalog();

    /**
     * 
     * @return
     */
    public ICatalogElement getCatalogElement();
}
