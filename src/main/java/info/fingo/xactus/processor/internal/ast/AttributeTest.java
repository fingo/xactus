/*******************************************************************************
 * Copyright (c) 2005, 2011 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *     David Carver - bug 298535 - Attribute instance of improvements
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.ast;

import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.StaticContext;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.AttrType;
import info.fingo.xactus.processor.internal.types.NodeType;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.api.Item;
import info.fingo.xactus.api.typesystem.TypeDefinition;
import info.fingo.xactus.api.typesystem.TypeModel;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * Class used to match an attribute node by its name and/or type.
 */
public class AttributeTest extends AttrElemTest {

	private AnyType anyType = null;

	/**
	 * Constructor for AttributeTest. This one takes in 3 inputs, Name, wildcard
	 * test(true/false) and type.
	 *
	 * @param name
	 *            QName.
	 * @param wild
	 *            Wildcard test, True/False.
	 * @param type
	 *            QName type.
	 */
	public AttributeTest(QName name, boolean wild, QName type) {
		super(name, wild, type);
	}

	/**
	 * Constructor for AttributeTest. This one takes in 2 inputs, Name and
	 * wildcard test(true/false).
	 *
	 * @param name
	 *            QName.
	 * @param wild
	 *            Wildcard test, True/False.
	 */
	public AttributeTest(QName name, boolean wild) {
		super(name, wild);
	}

	/**
	 * Default Constructor for AttributeTest.
	 */
	public AttributeTest() {
		super();
	}

	@Override
	public AnyType createTestType(ResultSequence rs, StaticContext sc) {
		if (name() == null && !wild()) {
			return new AttrType();
		}

		Item at = rs.first();

		if (!(at instanceof NodeType)) {
			return new AttrType();
		}

		return createAttrType(at, sc);
	}

	private AnyType createAttrType(Item at, StaticContext sc) {
		anyType = new AttrType();
		NodeType nodeType = (NodeType) at;
		Node node = nodeType.node_value();
		if (node == null) {
			return anyType;
		}

		String nodeName = node.getLocalName();

		if (wild()) {
			if (type() != null) {
				anyType = createAttrForXSDType(node, sc);
			}
		} else if (nodeName.equals(name().local())) {
			if (type() != null) {
				anyType = createAttrForXSDType(node, sc);
			} else {
				anyType = new AttrType((Attr) node, sc.getTypeModel());
			}
		}
		return anyType;
	}

	private AnyType createAttrForXSDType(Node node, StaticContext sc) {
		Attr attr = (Attr) node;

		TypeModel typeModel = sc.getTypeModel();
		TypeDefinition typedef = typeModel.getType(attr);

		if (typedef != null) {
			if (typedef.derivedFrom(type().namespace(), type().local(),
					getDerviationTypes())) {
				anyType = new AttrType(attr, sc.getTypeModel());
			}
		} else {
			anyType = new AttrType(attr, sc.getTypeModel());
		}
		return anyType;
	}

	@Override
	public boolean isWild() {
		return wild();
	}

	@Override
	public Class<AttrType> getXDMClassType() {
		return AttrType.class;
	}

	/**
	 * Support for Visitor interface.
	 *
	 * @return Result of Visitor operation.
	 */
	@Override
	public Object accept(XPathVisitor v) {
		return v.visit(this);
	}

}
