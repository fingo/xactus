/*******************************************************************************
 * Copyright (c) 2011, 2018 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package info.fingo.xactus.processor;

import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.StaticContext;
import info.fingo.xactus.api.XPath2Engine;
import info.fingo.xactus.api.XPath2Expression;
import info.fingo.xactus.processor.ast.XPath;
import info.fingo.xactus.processor.internal.function.FnBoolean;

/**
 * @since 2.0
 */
public class Engine implements XPath2Engine {

	public XPath2Expression parseExpression(String expression, StaticContext context) {

		XPath xPath = new JFlexCupParser().parse(expression);
		xPath.setStaticContext(context);
		StaticNameResolver name_check = new StaticNameResolver(context);
		name_check.check(xPath);

		final Set<String> axes = name_check.getAxes();
		xPath.setAxes(axes.isEmpty() ? Collections.emptySet() : axes);
		xPath.setFreeVariables(name_check.getFreeVariables());
		final Set<QName> resolvedFunctions = name_check.getResolvedFunctions();
		xPath.setResolvedFunctions(resolvedFunctions.isEmpty() ? Collections.emptySet()
				: resolvedFunctions);

		xPath.setRootUsed(name_check.isRootUsed());

		return xPath;
	}

	boolean effectiveBooleanValue(ResultSequence rs) {
		return FnBoolean.fn_boolean(rs).value();
	}
}
