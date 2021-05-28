/*******************************************************************************
 * Copyright (c) 2009, 2011 Jesper Steen Moller, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jesper Steen Moller - initial API and implementation
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package info.fingo.xactus.processor.internal.utils;

import info.fingo.xactus.processor.DynamicError;
import info.fingo.xactus.processor.internal.types.AnyAtomicType;
import info.fingo.xactus.processor.internal.types.XSDate;
import info.fingo.xactus.processor.internal.types.XSDateTime;
import info.fingo.xactus.processor.internal.types.XSDayTimeDuration;
import info.fingo.xactus.processor.internal.types.XSYearMonthDuration;

public class ScalarTypePromoter extends NumericTypePromoter {

    @Override
    protected boolean checkCombination(Class newType) {

        Class targetType = this.getTargetType();
        if (targetType == XSDayTimeDuration.class || targetType == XSYearMonthDuration.class) {
            return targetType == newType;
        }
        if (newType == XSDate.class) {
            return targetType == XSDate.class;
        }

        if (newType == XSDateTime.class) {
            return targetType == XSDateTime.class;
        }

        return super.checkCombination(newType);
    }

    @Override
    public AnyAtomicType doPromote(AnyAtomicType value) throws DynamicError {
        if (this.getTargetType() == XSDate.class) {
            return XSDate.parse_date(value.getStringValue());
        } else if (this.getTargetType() == XSDateTime.class) {
            return XSDateTime.parseDateTime(value.getStringValue());
        } else if (this.getTargetType() == XSYearMonthDuration.class) {
            return XSYearMonthDuration.parseYMDuration(value.getStringValue());
        } else if (this.getTargetType() == XSDayTimeDuration.class) {
            return XSDayTimeDuration.parseDTDuration(value.getStringValue());
        }
        return super.doPromote(value);
    }

    @Override
    protected Class substitute(Class typeToConsider) {
        if (typeToConsider == XSDayTimeDuration.class || typeToConsider == XSYearMonthDuration.class) {
            return typeToConsider;
        }

        return super.substitute(typeToConsider);
    }

}
