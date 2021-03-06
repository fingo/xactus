/*******************************************************************************
 * Copyright (c) 2011 Jesper Moller and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jesper Moller - initial API and implementation
 *     Lukasz Wycisk - bug 361804 - StaticContextAdapter returns mock function
 *******************************************************************************/

package info.fingo.xactus.processor.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import info.fingo.xactus.api.CollationProvider;
import info.fingo.xactus.api.EvaluationContext;
import info.fingo.xactus.api.Function;
import info.fingo.xactus.api.FunctionLibrary;
import info.fingo.xactus.api.ResultSequence;
import info.fingo.xactus.api.StaticContext;
import info.fingo.xactus.api.StaticVariableResolver;
import info.fingo.xactus.api.typesystem.TypeDefinition;
import info.fingo.xactus.api.typesystem.TypeModel;
import info.fingo.xactus.processor.DefaultDynamicContext;
import info.fingo.xactus.processor.DynamicContext;
import info.fingo.xactus.processor.internal.types.NodeItemTypeImpl;
import info.fingo.xactus.processor.internal.types.QName;
import info.fingo.xactus.processor.internal.types.SimpleAtomicItemTypeImpl;
import info.fingo.xactus.processor.internal.types.builtin.BuiltinTypeLibrary;
import org.w3c.dom.Node;

public class StaticContextAdapter implements
		info.fingo.xactus.api.StaticContext {
	private final info.fingo.xactus.processor.StaticContext sc;

	public StaticContextAdapter(
			info.fingo.xactus.processor.StaticContext sc) {
		this.sc = sc;
	}

	public boolean isXPath1Compatible() {
		return sc.xpath1_compatible();
	}

	public StaticVariableResolver getInScopeVariables() {
		return new StaticVariableResolver() {

			public boolean isVariablePresent(javax.xml.namespace.QName name) {
				return sc.variable_exists(qn(name));
			}

			public info.fingo.xactus.api.typesystem.ItemType getVariableType(javax.xml.namespace.QName name) {
				return new SimpleAtomicItemTypeImpl(BuiltinTypeLibrary.XS_ANYTYPE);
			}
		};
	}

	private QName qn(javax.xml.namespace.QName name) {
		return new QName(name);
	}

	public TypeDefinition getInitialContextType() {
		return BuiltinTypeLibrary.XS_UNTYPED;
	}

	public Map<String, FunctionLibrary> getFunctionLibraries() {
		if (sc instanceof DefaultStaticContext) {
			DefaultStaticContext dsc = (DefaultStaticContext)sc;
			return dsc.get_function_libraries();
		}
		return Collections.emptyMap();
	}

	public CollationProvider getCollationProvider() {
		if (sc instanceof DynamicContext) {
			final DynamicContext dc = (DynamicContext)sc;
			return new CollationProvider() {

				public String getDefaultCollation() {
					return dc.default_collation_name();
				}

				public Comparator getCollation(String name) {
					return dc.get_collation(name);
				}
			};
		}

		return new CollationProvider() {

			public String getDefaultCollation() {
				return null;
			}

			public Comparator getCollation(String name) {
				return null;
			}
		};
	}

	public URI getBaseUri() {
		// TODO Auto-generated method stub
		try {
			return new URI(sc.base_uri().getStringValue());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public NamespaceContext getNamespaceContext() {
		return new NamespaceContext() {

			public Iterator getPrefixes(String arg0) {
				return Collections.emptyList().iterator();
			}

			public String getPrefix(String arg0) {
				return "x";
			}

			public String getNamespaceURI(String prefix) {
				String ns = sc.resolve_prefix(prefix);
				return ns != null ? ns : XMLConstants.NULL_NS_URI;
			}
		};
	}

	public String getDefaultNamespace() {
		return sc.default_namespace();
	}

	public String getDefaultFunctionNamespace() {
		return sc.default_function_namespace();
	}

	public TypeModel getTypeModel() {
		return sc.getTypeModel(null);
	}

	public Function resolveFunction(javax.xml.namespace.QName name, int arity) {
		if (sc.function_exists(new QName(name), arity)) {
			if (sc instanceof DefaultStaticContext) {
				DefaultStaticContext dc = (DefaultStaticContext)sc;
				return dc.function(new QName(name), arity);
			}
		}
		throw new IllegalArgumentException("Function not found "+name);
	}

	public TypeDefinition getCollectionType(String collectionName) {
		return BuiltinTypeLibrary.XS_UNTYPED;
	}

	public TypeDefinition getDefaultCollectionType() {
		return BuiltinTypeLibrary.XS_UNTYPED;

	}

	public info.fingo.xactus.api.typesystem.ItemType getDocumentType(
			URI documentUri) {
		return new NodeItemTypeImpl(info.fingo.xactus.api.typesystem.ItemType.OCCURRENCE_OPTIONAL, Node.DOCUMENT_NODE);
	}
}