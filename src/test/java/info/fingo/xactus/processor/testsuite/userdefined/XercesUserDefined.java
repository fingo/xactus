/*******************************************************************************
 * Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver (STAR) - initial API and implementation
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/
package info.fingo.xactus.processor.testsuite.userdefined;

import org.apache.xerces.xs.XSTypeDefinition;
import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.api.typesystem.TypeDefinition;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.AnyAtomicType;
import info.fingo.xactus.processor.internal.types.CtrType;
import info.fingo.xactus.processor.internal.types.XSString;
import info.fingo.xactus.processor.internal.types.xerces.XercesTypeDefinition;

public class XercesUserDefined extends CtrType {

	private XSTypeDefinition typeInfo;
	private String value;

	public XercesUserDefined(XSTypeDefinition typeInfo) {
		this.typeInfo = typeInfo;
	}

	public info.fingo.xactus.api.ResultSequence constructor(info.fingo.xactus.api.ResultSequence arg) throws DynamicError {
			if (arg.empty())
				return ResultBuffer.EMPTY;

			AnyAtomicType aat = (AnyAtomicType) arg.first();

			return new XSString(aat.string_value());
	}

	public String string_type() {
		return null;
	}

	public String getStringValue() {
		return value;
	}

	public String type_name() {
		return typeInfo.getName();
	}

	public TypeDefinition getTypeDefinition() {
		return XercesTypeDefinition.createTypeDefinition((XSTypeDefinition)typeInfo);
	}
}
