/*******************************************************************************
 * Copyright (c) 2005, 2009 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *     Mukul Gandhi - bug274719 - implementation of equality of xs:anyURI values
 *     David Carver (STAR) - bug 282223 - fixed casting to xs:anyURI only string,
 *         untypedAtomic, and anyURI are allowed.
 *     David Carver (STAR) - bug 283777 - implemented gt, lt comparison code.
 *******************************************************************************/

package org.eclipse.wst.xml.xpath2.processor.internal.types;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.ResultSequenceFactory;
import org.eclipse.wst.xml.xpath2.processor.internal.function.CmpEq;
import org.eclipse.wst.xml.xpath2.processor.internal.function.CmpGt;
import org.eclipse.wst.xml.xpath2.processor.internal.function.CmpLt;
import org.eclipse.wst.xml.xpath2.processor.internal.function.FnCompare;

/**
 * Represents a Universal Resource Identifier (URI) reference
 */
public class XSAnyURI extends CtrType implements CmpEq, CmpGt, CmpLt {

	private String _value;

	/**
	 * Arity 1 Constructor
	 * 
	 * @param x
	 *            String representation of the URI
	 */
	public XSAnyURI(String x) {
		_value = x;
	}

	/**
	 * Arity 0 Constructor. Initiates URI to null.
	 */
	public XSAnyURI() {
		this(null);
	}

	/**
	 * Retrieve full type pathname of this datatype
	 * 
	 * @return "xs:anyURI", the full type pathname of this datatype
	 */
	@Override
	public String string_type() {
		return "xs:anyURI";
	}

	/**
	 * Retrieve type name of this datatype
	 * 
	 * @return "anyURI", the type name of this datatype
	 */
	@Override
	public String type_name() {
		return "anyURI";
	}

	/**
	 * Transforms and retrieves the URI value of this URI datatype in String
	 * format
	 * 
	 * @return the URI value held by this instance of the URI datatype as a
	 *         String
	 */
	@Override
	public String string_value() {
		return _value;
	}

	/**
	 * Creation of a result sequence consisting of a URI from a previous result
	 * sequence.
	 * 
	 * @param arg
	 *            previous result sequence
	 * @throws DynamicError
	 * @return new result sequence consisting of the URI supplied
	 */
	@Override
	public ResultSequence constructor(ResultSequence arg) throws DynamicError {
		ResultSequence rs = ResultSequenceFactory.create_new();

		if (arg.empty())
			return rs;

		AnyType aat = (AnyType) arg.first();

		if (!(aat.string_type().equals("xs:string")
				|| aat.string_type().equals("xs:anyURI") || aat.string_type()
				.equals("xs:untypedAtomic"))) {
			throw DynamicError.throw_type_error();
		}

		rs.add(new XSAnyURI(aat.string_value()));

		return rs;
	}

	/**
	 * Equality comparison between this and the supplied representation which
	 * must be of type xs:anyURI
	 * 
	 * @param arg
	 *            The representation to compare with
	 * @return True if the two representation are of the same String. False
	 *         otherwise
	 * @throws DynamicError
	 */
	public boolean eq(AnyType arg) throws DynamicError {
		if (arg instanceof XSAnyURI) {
			if (this.string_value().equals(arg.string_value())) {
				return true;
			}
		} else {
			throw DynamicError.throw_type_error();
		}

		return false;
	}

	/**
	 * Greater than comparison between this and the supplied representation which
	 * must be of type xs:anyURI
	 * @since 1.1
	 */
	public boolean gt(AnyType arg) throws DynamicError {
		if (!(arg instanceof XSAnyURI)) {
			throw DynamicError.throw_type_error();	
		}
		
		String anyURI = this.string_value();
		String compareToURI = arg.string_value();
		if (anyURI.compareTo(compareToURI) > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Less than comparison between this and the supplied representation which
	 * must be of type xs:anyURI
	 * 
	 * @since 1.1
	 */
	public boolean lt(AnyType arg) throws DynamicError {
		if (!(arg instanceof XSAnyURI)) {
			throw DynamicError.throw_type_error();
		}
		
		String anyURI = this.string_value();
		String compareToURI = arg.string_value();
		if (anyURI.compareTo(compareToURI) < 0) {
			return true;
		}

		return false;
	}

}
