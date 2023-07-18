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
 *******************************************************************************/

package info.fingo.xactus.processor.internal.ast;

import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.api.typesystem.TypeDefinition;

/**
 * Common base class for Attribute and Element tests.
 */
public abstract class AttrElemTest extends KindTest {
	
	private final QName name;
	private final QName type; // may be null
	private final boolean wild; // use wild qname ?

	/**
	 * Constructor for Attribute and Element tests. This takes in 3 inputs,
	 * Name, wildcard test(true/false) and type.
	 *
	 * @param name
	 *            QName.
	 * @param wild
	 *            Wildcard test? True/False.
	 * @param type
	 *            QName type.
	 */
	public AttrElemTest(QName name, boolean wild, QName type) {
		this.name = name;
		this.wild = wild;
		this.type = type;
	}

	/**
	 * Constructor for Attribute and Element tests. This takes in 2 inputs, Name
	 * and wildcard test(true/false).
	 *
	 * @param name
	 *            QName.
	 * @param wild
	 *            Wildcard test? True/False.
	 */
	public AttrElemTest(QName name, boolean wild) {
		this(name, wild, null);
	}

	/**
	 * Default Constructor for Attribute and Element tests. This takes in no
	 * inputs.
	 */
	public AttrElemTest() {
		this(null, false);
	}

	/**
	 * Support for wildcard test.
	 *
	 * @return Result of wildcard test.
	 */
	public boolean wild() {
		return wild;
	}

	/**
	 * Support for name test.
	 *
	 * @return Result of name test.
	 */
	@Override
	public QName name() {
		return name;
	}

	/**
	 * Support for type test.
	 *
	 * @return Result of type test.
	 */
	public QName type() {
		return type;
	}

	protected short getDerviationTypes() {
		return TypeDefinition.DERIVATION_LIST 
				| TypeDefinition.DERIVATION_EXTENSION
				| TypeDefinition.DERIVATION_RESTRICTION
				| TypeDefinition.DERIVATION_SUBSTITUTION;
	}
	
}
