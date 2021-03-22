/*******************************************************************************
 * Copyright (c) 2004, 2020 IBM Corporation and others.
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
package org.eclipse.wst.html.core.internal.modelquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.html.core.internal.provisional.HTMLCMProperties;
import org.eclipse.wst.xml.core.internal.contentmodel.CMContent;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMGroup;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNodeList;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class HMQUtil {

	private final static List<CMContent> nullVector = new ArrayList<>();

	/* HMQUtil class must not be instantiated. */
	private HMQUtil() {
		super();
	}

	/* gather all element declarations in inclusion of each parent of the target. 
	 * the inclusion of the target is also gathered. */
	public static Collection<CMContent> getInclusions(Element target) {
		if (target == null)
			return nullVector;
		Collection<CMContent> inclusions = gatherInclusions(getAncestorDeclarations(target));

		Map<String, CMContent> availables = new HashMap<>();
		Iterator<CMContent> iter = inclusions.iterator();
		while (iter.hasNext()) {
			CMContent inclusion = iter.next();
			switch (inclusion.getNodeType()) {
				case CMNode.GROUP :
					extractDeclarations(availables, (CMGroup) inclusion);
					break;
				case CMNode.ELEMENT_DECLARATION :
					addInclusion(availables, (CMElementDeclaration) inclusion);
					break;
			}
		}

		return availables.values();
	}

	private static Collection<CMContent> getAncestorDeclarations(Element target) {
		List<CMContent> ancestors = new ArrayList<>();

		Document doc = target.getOwnerDocument();
		ModelQuery query = ModelQueryUtil.getModelQuery(doc);
		CMElementDeclaration decl = query.getCMElementDeclaration(target);
		ancestors.add(decl);

		Element parent = getParent(target);
		while (parent != null) {
			decl = query.getCMElementDeclaration(parent);
			if (decl != null)
				ancestors.add(decl);
			parent = getParent(parent);
		}
		return ancestors;
	}

	private static void addInclusion(Map<String, CMContent> availables, CMElementDeclaration decl) {
		String name = decl.getElementName();
		if (availables.containsKey(name))
			return;
		availables.put(name, decl);
	}

	private static Collection<CMContent> gatherInclusions(Collection<CMContent> ancestors) {
		List<CMContent> inclusions = new ArrayList<>();
		Iterator<CMContent> iter = ancestors.iterator();
		while (iter.hasNext()) {
			CMElementDeclaration decl = (CMElementDeclaration) iter.next();
			if (decl.supports(HTMLCMProperties.INCLUSION)) {
				CMContent inclusion = (CMContent) decl.getProperty(HTMLCMProperties.INCLUSION);
				if (inclusion != null)
					inclusions.add(inclusion);
			}
		}
		return inclusions;
	}

	private static Element getParent(Node target) {
		Node parent = target.getParentNode();
		while (parent != null) {
			if (parent.getNodeType() == Node.ELEMENT_NODE)
				return (Element) parent;
			parent = parent.getParentNode();
		}
		return null;
	}

	private static void extractDeclarations(Map<String, CMContent> availables, CMGroup group) {
		CMNodeList content = group.getChildNodes();
		for (int i = 0; i < content.getLength(); i++) {
			CMNode cmn = content.item(i);
			if (cmn == null || cmn.getNodeType() != CMNode.ELEMENT_DECLARATION)
				continue;
			addInclusion(availables, (CMElementDeclaration) cmn);
		}
	}
}
