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
package org.eclipse.wst.json.core.internal.schema.catalog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.json.core.JSONCorePlugin;
import org.eclipse.wst.json.core.internal.Logger;
import org.eclipse.wst.json.core.schema.catalog.ICatalog;
import org.eclipse.wst.json.core.schema.catalog.ICatalogElement;
import org.eclipse.wst.json.core.schema.catalog.ICatalogEntry;
import org.eclipse.wst.json.core.schema.catalog.ICatalogEvent;
import org.eclipse.wst.json.core.schema.catalog.ICatalogListener;
import org.eclipse.wst.json.core.schema.catalog.IDelegateCatalog;
import org.eclipse.wst.json.core.schema.catalog.INextCatalog;
import org.eclipse.wst.json.core.schema.catalog.IRewriteEntry;
import org.eclipse.wst.json.core.schema.catalog.ISuffixEntry;

public class Catalog implements ICatalog {

	class CatalogLS {
		public void load() {
		}

		public synchronized void save() {
			// try {
			// new CatalogWriter().write(Catalog.this, location);
			// } catch (Exception e) {
			// Logger.logException(e);
			// }
		}
	}

	class DefaultCatalogLS extends CatalogLS {
		public void load() {
			NextCatalog userCatalogReference = new NextCatalog();
			userCatalogReference.setId(JSONCorePlugin.USER_CATALOG_ID);
			userCatalogReference.setCatalogLocation(USER_CATALOG_FILE);
			addCatalogElement(userCatalogReference);

			NextCatalog systemCatalogReference = new NextCatalog();
			systemCatalogReference.setId(JSONCorePlugin.SYSTEM_CATALOG_ID);
			systemCatalogReference.setCatalogLocation(SYSTEM_CATALOG_FILE);
			addCatalogElement(systemCatalogReference);

			/*
			 * Here we save the file in order to 'reflect' the catalog that
			 * we've created from plug-in extensions to disk. The 'default'
			 * catalog is only ever written to disk and never read from disk.
			 */
			save();
		}
	}

	private static Comparator LONGEST_REWRITE_FIRST = new Comparator() {
		public int compare(Object entry1, Object entry2) {
			String start1 = ((IRewriteEntry) entry1).getStartString();
			String start2 = ((IRewriteEntry) entry2).getStartString();

			// Bigger is earlier
			return start2.length() - start1.length();
		}
	};

	private static Comparator LONGEST_SUFFIX_FIRST = new Comparator() {
		public int compare(Object entry1, Object entry2) {
			String suffix1 = ((ISuffixEntry) entry1).getSuffix();
			String suffix2 = ((ISuffixEntry) entry2).getSuffix();

			// Bigger is earlier
			return suffix2.length() - suffix1.length();
		}
	};

	private static Comparator LONGEST_DELEGATE_PREFIX_FIRST = new Comparator() {
		public int compare(Object entry1, Object entry2) {
			String prefix1 = ((IDelegateCatalog) entry1).getStartString();
			String prefix2 = ((IDelegateCatalog) entry2).getStartString();

			// Bigger is earlier
			return prefix2.length() - prefix1.length();
		}
	};

	class InternalResolver {
		protected Map schemaMap = new HashMap();

		// These are sorted by longest "key" first.
		protected List rewriteSystemList = new LinkedList();
		protected List rewriteUriList = new LinkedList();
		protected List suffixSystemList = new LinkedList();
		protected List suffixUriList = new LinkedList();
		protected List delegatePublicList = new LinkedList();
		protected List delegateSystemList = new LinkedList();
		protected List delegateUriList = new LinkedList();

		InternalResolver() {
			synchronized (catalogElements) {
				for (Iterator i = catalogElements.iterator(); i.hasNext();) {
					ICatalogElement catalogElement = (ICatalogElement) i.next();
					if (catalogElement.getType() == ICatalogElement.TYPE_ENTRY) {
						ICatalogEntry entry = (ICatalogEntry) catalogElement;
						Map map = getEntryMap(entry.getEntryType());
						String[] names = entry.getKey().split(",");
						for (int j = 0; j < names.length; j++) {
							map.put(names[j].trim(), entry);
						}
					} else if (catalogElement.getType() == ICatalogElement.TYPE_REWRITE) {
						IRewriteEntry entry = (IRewriteEntry) catalogElement;
						if (entry.getEntryType() == IRewriteEntry.REWRITE_TYPE_SYSTEM) {
							rewriteSystemList.add(entry);
						} else {
							rewriteUriList.add(entry);
						}
					} else if (catalogElement.getType() == ICatalogElement.TYPE_SUFFIX) {
						ISuffixEntry entry = (ISuffixEntry) catalogElement;
						if (entry.getEntryType() == ISuffixEntry.SUFFIX_TYPE_SYSTEM) {
							suffixSystemList.add(entry);
						} else {
							suffixUriList.add(entry);
						}
					} else if (catalogElement.getType() == ICatalogElement.TYPE_DELEGATE) {
						IDelegateCatalog delegate = (IDelegateCatalog) catalogElement;
						if (delegate.getEntryType() == IDelegateCatalog.DELEGATE_TYPE_PUBLIC) {
							delegatePublicList.add(delegate);
						} else if (delegate.getEntryType() == IDelegateCatalog.DELEGATE_TYPE_SYSTEM) {
							delegateSystemList.add(delegate);
						} else {
							delegateUriList.add(delegate);
						}
					}
				}
			}

			Collections.sort(rewriteSystemList, LONGEST_REWRITE_FIRST);
			Collections.sort(rewriteUriList, LONGEST_REWRITE_FIRST);

			Collections.sort(suffixSystemList, LONGEST_SUFFIX_FIRST);
			Collections.sort(suffixUriList, LONGEST_SUFFIX_FIRST);

			Collections.sort(delegatePublicList, LONGEST_DELEGATE_PREFIX_FIRST);
			Collections.sort(delegateSystemList, LONGEST_DELEGATE_PREFIX_FIRST);
			Collections.sort(delegateUriList, LONGEST_DELEGATE_PREFIX_FIRST);
		}

		private Map getEntryMap(int entryType) {
			Map map = schemaMap;
			switch (entryType) {
			case ICatalogEntry.ENTRY_TYPE_SCHEMA:
				map = schemaMap;
				break;
			default:
				break;
			}
			return map;
		}

		protected String getMappedURI(Map map, String key) {
			CatalogEntry entry = (CatalogEntry) map.get(key);
			if (entry == null)
				return null;
			String uri = entry.getURI();
			try {
				// TODO CS : do we really want to resolve these here?
				// I'm guessing we should return the 'platform:' form of the URI
				// to the caller.
				if (uri.startsWith("platform:")) //$NON-NLS-1$
				{
					URL entryURL = new URL(entry.getAbsolutePath(uri));
					uri = Platform.resolve(entryURL).toString();

					// we need to ensure URI's are of form "file:///D:/XXX" and
					// NOT
					// "file:D:/XXX". Otherwise the EMF URI class gets confused
					// (see bug 103607)
					String FILE_SCHEME = "file:"; //$NON-NLS-1$
					if (uri.startsWith(FILE_SCHEME)
							&& !uri.startsWith(FILE_SCHEME + "/")) //$NON-NLS-1$
					{
						uri = FILE_SCHEME
								+ "///" + uri.substring(FILE_SCHEME.length()); //$NON-NLS-1$
					}
				}
				return uri;
			} catch (IOException e) {
				return null;
			}
		}

		private String resolveRewrite(List rewriteList, String searchString) {
			for (Iterator it = rewriteList.iterator(); it.hasNext();) {
				IRewriteEntry entry = (IRewriteEntry) it.next();
				String startString = entry.getStartString();
				if (searchString.startsWith(startString)) {
					return entry.getRewritePrefix()
							+ searchString.substring(startString.length());
				}
			}
			return null;
		}

		private String resolveSuffix(List suffixList, String searchString) {
			for (Iterator it = suffixList.iterator(); it.hasNext();) {
				ISuffixEntry entry = (ISuffixEntry) it.next();
				if (searchString.endsWith(entry.getSuffix())) {
					return entry.getURI();
				}
			}
			return null;
		}

		protected String resolveDelegateCatalogs(List delegateCatalogs,
				String key, String systemId) throws MalformedURLException,
				IOException {
			String result = null;
			for (Iterator iterator = delegateCatalogs.iterator(); iterator
					.hasNext();) {
				IDelegateCatalog delegate = (IDelegateCatalog) iterator.next();

				if (key.startsWith(delegate.getStartString())) {

					ICatalog catalog = delegate.getReferencedCatalog();
					if (catalog != null) {
						switch (delegate.getEntryType()) {
						case IDelegateCatalog.DELEGATE_TYPE_SCHEMA:
							result = catalog.resolveSchema(systemId);
							break;
						default:
							break;
						}
						if (result != null) {
							return result;
						}
					}
				}
			}
			return null;
		}

		public String resolveSchema(String fileName)
				throws MalformedURLException, IOException {
			String result = getMappedURI(schemaMap, fileName);
			// if (result == null) {
			// result = resolveRewrite(rewriteUriList, fileName);
			// }
			// if (result == null) {
			// result = resolveSuffix(suffixUriList, fileName);
			// }
			// if (result == null) {
			// result = resolveDelegateCatalogs(delegateUriList, fileName,
			// fileName); // uri
			// // is
			// // treated
			// // as
			// // the
			// // systemId
			// }
			if (result == null) {
				result = resolveSubordinateCatalogs(
						ICatalogEntry.ENTRY_TYPE_SCHEMA, null, fileName);
			}
			return result;
		}
	}

	class SystemCatalogLS extends CatalogLS {
		public void load() {
			new CatalogContributorRegistryReader(Catalog.this).readRegistry();

			new CatalogSchemastoreReader(Catalog.this).readSchemastore();
			/*
			 * Here we save the file in order to 'reflect' the catalog that
			 * we've created from plugin extensions to disk. The 'system'
			 * catalog is only ever written to disk and never read from disk.
			 */
			save();
		}
	}

	class UserCatalogLS extends CatalogLS {
		public void load() {
			new CatalogUserCatalogReader(Catalog.this).readCatalog();
			
			save();
		}
	}

	public static final String DEFAULT_CATALOG_FILE = "default_catalog.xml"; //$NON-NLS-1$

	public static final String SYSTEM_CATALOG_FILE = "system_catalog.xml"; //$NON-NLS-1$

	public static final String USER_CATALOG_FILE = "user_catalog.xml"; //$NON-NLS-1$

	protected String base;

	protected List catalogElements = new ArrayList();

	protected CatalogLS catalogLS;

	protected String id;

	protected InternalResolver internalResolver;

	protected boolean isNotificationEnabled;

	protected List listenerList = new ArrayList();

	protected String location;

	protected CatalogSet resourceSet;

	public Catalog(CatalogSet catalogResourceSet, String id, String location) {
		this.resourceSet = catalogResourceSet;
		this.id = id;
		this.location = location;

		if (JSONCorePlugin.DEFAULT_CATALOG_ID.equals(id)) {
			catalogLS = new DefaultCatalogLS();
		} else if (JSONCorePlugin.SYSTEM_CATALOG_ID.equals(id)) {
			catalogLS = new SystemCatalogLS();
		} else {
			catalogLS = new UserCatalogLS();
		}
	}

	public void addCatalogElement(ICatalogElement element) {
		synchronized (catalogElements) {
			if (!catalogElements.contains(element)) {
				catalogElements.add(element);
			} else {
				return;
			}
		}
		element.setOwnerCatalog(this);
		internalResolver = null;
		notifyAddElement(element);
	}

	public void addEntriesFromCatalog(ICatalog catalog) {
		try {
			setNotificationEnabled(false);
			if (catalog != null) {
				ICatalogElement[] entries = ((Catalog) catalog)
						.getCatalogElements();
				for (int i = 0; i < entries.length; i++) {
					CatalogElement clone = (CatalogElement) ((CatalogElement) entries[i])
							.clone();
					addCatalogElement(clone);
				}
			} else {
				Logger.log(Logger.ERROR,
						"argument was null in Catalog.addEntriesFromCatalog"); //$NON-NLS-1$
			}
		} finally {
			setNotificationEnabled(true);
		}
		internalResolver = null;
		notifyChanged();
	}

	public void addListener(ICatalogListener listener) {
		listenerList.add(listener);
	}

	public void clear() {
		synchronized (catalogElements) {
			catalogElements.clear();
		}
		internalResolver = null;
		notifyChanged();
	}

	public ICatalogElement createCatalogElement(int type) {
		switch (type) {
		case ICatalogElement.TYPE_ENTRY:
			return new CatalogEntry(); // TODO: Should be kind of deprecated
		case ICatalogElement.TYPE_NEXT_CATALOG:
			return new NextCatalog();
		case ICatalogEntry.ENTRY_TYPE_SCHEMA:
			return new CatalogEntry(type);
			// case ICatalogElement.TYPE_REWRITE:
			// case IRewriteEntry.REWRITE_TYPE_SYSTEM:
			// case IRewriteEntry.REWRITE_TYPE_URI:
			// return new RewriteEntry(type);
			// case ICatalogElement.TYPE_SUFFIX:
			// case ISuffixEntry.SUFFIX_TYPE_SYSTEM:
			// case ISuffixEntry.SUFFIX_TYPE_URI:
			// return new SuffixEntry(type);
			// case ICatalogElement.TYPE_DELEGATE:
			// case IDelegateCatalog.DELEGATE_TYPE_PUBLIC:
			// case IDelegateCatalog.DELEGATE_TYPE_SYSTEM:
			// case IDelegateCatalog.DELEGATE_TYPE_URI:
			// return new DelegateCatalog(type);
		default:
			throw new IllegalArgumentException("Unknown element type " + type);//$NON-NLS-1 // Makes no sense at all!
		}
	}

	public String getBase() {
		return base;
	}

	private List getCatalogElements(int type) {
		List result = new ArrayList();
		ICatalogElement[] elements = (ICatalogElement[]) catalogElements
				.toArray(new ICatalogElement[catalogElements.size()]);
		for (int i = 0; i < elements.length; i++) {
			ICatalogElement element = elements[i];
			if (element.getType() == type) {
				result.add(element);
			}
		}
		return result;
	}

	public ICatalogEntry[] getCatalogEntries() {
		List result = getCatalogElements(ICatalogElement.TYPE_ENTRY);
		return (ICatalogEntry[]) result
				.toArray(new ICatalogEntry[result.size()]);
	}

	public IDelegateCatalog[] getDelegateCatalogs() {
		List result = getCatalogElements(ICatalogElement.TYPE_DELEGATE);
		return (IDelegateCatalog[]) result.toArray(new IDelegateCatalog[result
				.size()]);
	}

	public IRewriteEntry[] getRewriteEntries() {
		List result = getCatalogElements(ICatalogElement.TYPE_REWRITE);
		return (IRewriteEntry[]) result
				.toArray(new IRewriteEntry[result.size()]);
	}

	public ISuffixEntry[] getSuffixEntries() {
		List result = getCatalogElements(ICatalogElement.TYPE_SUFFIX);
		return (ISuffixEntry[]) result.toArray(new ISuffixEntry[result.size()]);
	}

	protected CatalogSet getCatalogSet() {
		return resourceSet;
	}

	public String getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public INextCatalog[] getNextCatalogs() {
		List result = getCatalogElements(ICatalogElement.TYPE_NEXT_CATALOG);
		return (INextCatalog[]) result.toArray(new INextCatalog[result.size()]);
	}

	protected InternalResolver getOrCreateInternalResolver() {
		if (internalResolver == null) {
			internalResolver = new InternalResolver();
		}
		return internalResolver;
	}

	protected boolean isNotificationEnabled() {
		return isNotificationEnabled;
	}

	public void load() throws IOException {
		catalogLS.load();
	}

	protected void notifyAddElement(ICatalogElement entry) {
		if (isNotificationEnabled) {
			ICatalogEvent event = new CatalogEvent(this, entry,
					ICatalogEvent.ELEMENT_ADDED);
			notifyListeners(event);
		}
	}

	protected void notifyChanged() {
		ICatalogEvent event = new CatalogEvent(this, null,
				ICatalogEvent.CHANGED);
		notifyListeners(event);
	}

	protected void notifyListeners(ICatalogEvent event) {
		List list = new ArrayList();
		list.addAll(listenerList);
		for (Iterator i = list.iterator(); i.hasNext();) {
			ICatalogListener listener = (ICatalogListener) i.next();
			listener.catalogChanged(event);
		}
	}

	protected void notifyRemoveElement(ICatalogElement element) {
		if (isNotificationEnabled) {
			ICatalogEvent event = new CatalogEvent(this, element,
					ICatalogEvent.ELEMENT_REMOVED);
			notifyListeners(event);
		}
	}

	public void removeCatalogElement(ICatalogElement element) {
		synchronized (catalogElements) {
			catalogElements.remove(element);
		}
		internalResolver = null;
		notifyRemoveElement(element);

	}

	public void removeListener(ICatalogListener listener) {
		listenerList.remove(listener);
	}

	protected String resolveSubordinateCatalogs(int entryType, String publicId,
			String systemId) throws MalformedURLException, IOException {
		String result = null;
		INextCatalog[] nextCatalogs = getNextCatalogs();
		for (int i = 0; i < nextCatalogs.length; i++) {
			INextCatalog nextCatalog = nextCatalogs[i];
			ICatalog catalog = nextCatalog.getReferencedCatalog();
			if (catalog != null) {
				switch (entryType) {
				case ICatalogEntry.ENTRY_TYPE_SCHEMA:
					result = catalog.resolveSchema(systemId);
					break;
				default:
					break;
				}
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	public void save() throws IOException {
		catalogLS.save();
	}

	public void setBase(String base) {
		this.base = base;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	protected void setNotificationEnabled(boolean b) {
		isNotificationEnabled = b;
	}

	public ICatalogElement[] getCatalogElements() {
		return (ICatalogElement[]) catalogElements
				.toArray(new ICatalogElement[catalogElements.size()]);
	}

	@Override
	public String resolveSchema(String fileMatch) throws MalformedURLException,
			IOException {
		return getOrCreateInternalResolver().resolveSchema(fileMatch);
	}

}
