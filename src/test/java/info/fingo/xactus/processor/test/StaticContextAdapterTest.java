/*******************************************************************************
 * Copyright (c) 2011, 2017 Jesper S Moller and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Lukasz Wycisk - bug 361804 - StaticContextAdapter returns mock function
 *******************************************************************************/

package info.fingo.xactus.processor.test;

import junit.framework.TestCase;

import info.fingo.xactus.processor.DefaultDynamicContext;
import info.fingo.xactus.processor.DefaultEvaluator;
import info.fingo.xactus.processor.Evaluator;
import info.fingo.xactus.processor.JFlexCupParser;
import info.fingo.xactus.processor.ResultSequence;
import info.fingo.xactus.processor.StaticChecker;
import info.fingo.xactus.processor.StaticNameResolver;
import info.fingo.xactus.processor.XPathParser;
import info.fingo.xactus.processor.ast.XPath;
import info.fingo.xactus.processor.function.FnFunctionLibrary;
import info.fingo.xactus.processor.internal.DefaultStaticContext;
import info.fingo.xactus.processor.internal.types.XSDecimal;

public class StaticContextAdapterTest extends TestCase {

	public void testFunctionCall()
	{
		XPathParser xpp = new JFlexCupParser();
		XPath xpath = xpp.parse( "fn:sum((1,2,3))" );

		DefaultStaticContext sc = new DefaultStaticContext( null );
		sc.add_namespace( "fn", "http://www.w3.org/2005/xpath-functions" );
		sc.add_function_library( new FnFunctionLibrary() );

		StaticChecker namecheck = new StaticNameResolver( sc );
		namecheck.check( xpath );

		DefaultDynamicContext dc = new DefaultDynamicContext( null, null );
		dc.add_namespace( "fn", "http://www.w3.org/2005/xpath-functions" );
		dc.add_function_library( new FnFunctionLibrary() );

		Evaluator eval = new DefaultEvaluator( dc, null );
		ResultSequence rs = eval.evaluate( xpath );
		assertEquals(1, rs.size());

		XSDecimal result = (XSDecimal) rs.first();
		String actual = result.getStringValue();
		assertEquals("6", actual);
	}

}
