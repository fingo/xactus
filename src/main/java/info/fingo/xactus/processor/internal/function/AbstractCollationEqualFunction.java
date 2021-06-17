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
 *     David Carver (STAR) - initial API and implementation
 *     David Carver (STAR) - bug 296882 - fixed function that would always return false.
 *******************************************************************************/
package info.fingo.xactus.processor.internal.function;

import java.math.BigInteger;
import java.util.Iterator;

import info.fingo.xactus.api.DynamicContext;
import info.fingo.xactus.api.ResultBuffer;
import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.AnyAtomicType;
import info.fingo.xactus.processor.internal.types.AnyType;
import info.fingo.xactus.processor.internal.types.NumericType;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.processor.internal.types.XSBoolean;
import info.fingo.xactus.processor.internal.types.XSDateTime;
import info.fingo.xactus.processor.internal.types.XSDouble;
import info.fingo.xactus.processor.internal.types.XSDuration;
import info.fingo.xactus.processor.internal.types.XSFloat;
import info.fingo.xactus.processor.internal.types.XSString;
import info.fingo.xactus.processor.internal.types.XSUntypedAtomic;

public abstract class AbstractCollationEqualFunction extends Function {

	public AbstractCollationEqualFunction(QName name, int arity) {
		super(name, arity);
	}

	public AbstractCollationEqualFunction(QName name, int min_arity,
										  int max_arity) {
		super(name, min_arity, max_arity);
	}

	protected static boolean hasValue(AnyType itema,
									  AnyType itemb,
									  DynamicContext context,
									  String collationURI) throws DynamicError {
		return hasValue(itema, itemb, context, collationURI, false);
	}

	protected static boolean hasValue(AnyType itema,
									  AnyType itemb,
									  DynamicContext context,
									  String collationURI,
									  boolean nanEqualsNan) throws DynamicError {
		if( !(itema instanceof CmpEq) && !(itema instanceof XSUntypedAtomic) )
			return false;

		if (nanEqualsNan &&
			itema instanceof org.eclipse.wst.xml.xpath2.processor.internal.types.NaNable &&
			itemb instanceof org.eclipse.wst.xml.xpath2.processor.internal.types.NaNable &&
			((org.eclipse.wst.xml.xpath2.processor.internal.types.NaNable) itema).nan() &&
			((org.eclipse.wst.xml.xpath2.processor.internal.types.NaNable) itemb).nan()) {
			return true;
		}

		if (isBoolean(itema, itemb)) {
			return ((XSBoolean) itema).eq(itemb, context);
		}

		if (isNumeric(itema, itemb)) {
			return ((NumericType) itema).eq(itemb, context);
		}

		if (isDuration(itema, itemb)) {
			return ((XSDuration) itema).eq(itemb, context);
		}

		if (itema instanceof QName && itemb instanceof QName ) {
			return ((QName)itema).eq(itemb, context);
		}

		if (needsStringComparison(itema, itemb)) {
			return BigInteger.ZERO.equals(
				FnCompare.compare_string(
					collationURI,
					new XSString(itema.getStringValue()),
					new XSString(itemb.getStringValue()),
					context));
		}

		return false;
	}

	protected static boolean hasValue(ResultBuffer rs,
									  AnyAtomicType item,
									  DynamicContext context,
									  String collationURI) throws DynamicError {
		return hasValue(rs,item, context, collationURI, false);
	}

	protected static boolean hasValue(ResultBuffer rs,
									  AnyAtomicType item,
									  DynamicContext context,
									  String collationURI,
									  boolean nanEqualsNan) throws DynamicError {
		for (Iterator i = rs.iterator(); i.hasNext();) {
			AnyType at = (AnyType) i.next();

			if (hasValue(item, at, context, collationURI, nanEqualsNan)) {
				return true;
			}
		}

		return false;
	}

	protected static boolean isDuration(AnyAtomicType item, AnyType at) {
		return at instanceof XSDuration && item instanceof XSDuration;
	}

	protected static boolean isBoolean(AnyAtomicType item, AnyType at) {
		return at instanceof XSBoolean && item instanceof XSBoolean;
	}

	protected static boolean isNumeric(AnyAtomicType item, AnyType at) {
		return at instanceof NumericType && item instanceof NumericType;
	}

	protected static boolean needsStringComparison(AnyAtomicType item,
												   AnyType at) {
		AnyType anyItem = (AnyType) item;
		return needsStringComparison(anyItem, at);
	}

	protected static boolean isDuration(AnyType item, AnyType at) {
		return at instanceof XSDuration && item instanceof XSDuration;
	}

	protected static boolean isDate(AnyType item, AnyType at) {
		return at instanceof XSDateTime && item instanceof XSDateTime;
	}


	protected static boolean isBoolean(AnyType cmptype, AnyType at) {
		return at instanceof XSBoolean && cmptype instanceof XSBoolean;
	}

	protected static boolean isNumeric(AnyType item, AnyType at) {
		return at instanceof NumericType && item instanceof NumericType;
	}

	protected static boolean needsStringComparison(AnyType item, AnyType at) {
		if (item instanceof NumericType) {
			if (at instanceof XSFloat) {
				XSFloat f = (XSFloat) at;
				if (f.nan()) {
					return true;
				}
			}

			if (at instanceof XSDouble) {
				XSDouble d = (XSDouble) at;
				if (d.nan()) {
					return true;
				}
			}
		}

		return at instanceof XSString || at instanceof XSUntypedAtomic;
	}
}
