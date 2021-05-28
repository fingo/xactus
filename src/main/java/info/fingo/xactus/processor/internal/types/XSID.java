/*******************************************************************************
 * Copyright (c) 2009, 2011 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver (STAR) bug 228223 - initial API and implementation
 *     Mukul Gandhi - bug 334842 - improving support for the data types Name, NCName, ENTITY,
 *                                 ID, IDREF and NMTOKEN.
 *******************************************************************************/
package info.fingo.xactus.processor.internal.types;

import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.typesystem.TypeDefinition;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.builtin.BuiltinTypeLibrary;

/*
 * Implements the xs:ID data type.
 *
 * @since 1.1
 */
public class XSID extends XSNCName {

	private static final String XS_ID = "xs:ID";

	public XSID(String x) {
		super(x);
	}

	public XSID() {
		super();
	}

	public String string_type() {
		return XS_ID;
	}

	public String type_name() {
		return "ID";
	}

	public ResultSequence constructor(ResultSequence arg) throws DynamicError {
		if (arg.empty())
			return ResultBuffer.EMPTY;

		AnyAtomicType aat = (AnyAtomicType) arg.first();
		String strValue = aat.getStringValue();

		if (!isConstraintSatisfied(strValue)) {
			// invalid input
			DynamicError.throw_type_error();
		}

		return new XSID(strValue);
	}

	public TypeDefinition getTypeDefinition() {
		return BuiltinTypeLibrary.XS_ID;
	}


}
