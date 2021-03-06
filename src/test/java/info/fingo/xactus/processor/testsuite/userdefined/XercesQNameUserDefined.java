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

import org.apache.xerces.xs.XSObject;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.ResultSequence;
import info.fingo.xactus.processor.ResultSequenceFactory;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.QName;

public class XercesQNameUserDefined extends QName {

	private XSObject typeInfo;

	public XercesQNameUserDefined(XSObject typeInfo) {
		this.typeInfo = typeInfo;
	}

	public ResultSequence constructor(ResultSequence arg) throws DynamicError {
		ResultSequence rs = ResultSequenceFactory.create_new();

		if (arg.empty())
			return rs;


		//AnyAtomicType aat = (AnyAtomicType) arg.first();
		AnyType aat = arg.first();

		QName qname = QName.parse_QName(aat.getStringValue());

		rs.add(qname);

		return rs;


	}

	public String type_name() {
		return typeInfo.getName();
	}
}
