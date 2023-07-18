/*******************************************************************************
 * Copyright (c) 2013 Jesper Steen Moeller, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jesper Steen Moller  - bug 338494 - But without dependency on magic constants
 *******************************************************************************/

package info.fingo.xactus.processor.internal;

import java.util.Collection;

import info.fingo.xactus.processor.ast.XPath;
import info.fingo.xactus.processor.internal.ast.AddExpr;
import info.fingo.xactus.processor.internal.ast.AndExpr;
import info.fingo.xactus.processor.internal.ast.AnyKindTest;
import info.fingo.xactus.processor.internal.ast.AttributeTest;
import info.fingo.xactus.processor.internal.ast.AxisStep;
import info.fingo.xactus.processor.internal.ast.CastExpr;
import info.fingo.xactus.processor.internal.ast.CastableExpr;
import info.fingo.xactus.processor.internal.ast.CmpExpr;
import info.fingo.xactus.processor.internal.ast.CntxItemExpr;
import info.fingo.xactus.processor.internal.ast.CommentTest;
import info.fingo.xactus.processor.internal.ast.DecimalLiteral;
import info.fingo.xactus.processor.internal.ast.DivExpr;
import info.fingo.xactus.processor.internal.ast.DocumentTest;
import info.fingo.xactus.processor.internal.ast.DoubleLiteral;
import info.fingo.xactus.processor.internal.ast.ElementTest;
import info.fingo.xactus.processor.internal.ast.ExceptExpr;
import info.fingo.xactus.processor.internal.ast.Expr;
import info.fingo.xactus.processor.internal.ast.FilterExpr;
import info.fingo.xactus.processor.internal.ast.ForExpr;
import info.fingo.xactus.processor.internal.ast.ForwardStep;
import info.fingo.xactus.processor.internal.ast.FunctionCall;
import info.fingo.xactus.processor.internal.ast.IDivExpr;
import info.fingo.xactus.processor.internal.ast.IfExpr;
import info.fingo.xactus.processor.internal.ast.InstOfExpr;
import info.fingo.xactus.processor.internal.ast.IntegerLiteral;
import info.fingo.xactus.processor.internal.ast.IntersectExpr;
import info.fingo.xactus.processor.internal.ast.ItemType;
import info.fingo.xactus.processor.internal.ast.MinusExpr;
import info.fingo.xactus.processor.internal.ast.ModExpr;
import info.fingo.xactus.processor.internal.ast.MulExpr;
import info.fingo.xactus.processor.internal.ast.NameTest;
import info.fingo.xactus.processor.internal.ast.OrExpr;
import info.fingo.xactus.processor.internal.ast.PITest;
import info.fingo.xactus.processor.internal.ast.ParExpr;
import info.fingo.xactus.processor.internal.ast.PipeExpr;
import info.fingo.xactus.processor.internal.ast.PlusExpr;
import info.fingo.xactus.processor.internal.ast.QuantifiedExpr;
import info.fingo.xactus.processor.internal.ast.RangeExpr;
import info.fingo.xactus.processor.internal.ast.ReverseStep;
import info.fingo.xactus.processor.internal.ast.SchemaAttrTest;
import info.fingo.xactus.processor.internal.ast.SchemaElemTest;
import info.fingo.xactus.processor.internal.ast.SequenceType;
import info.fingo.xactus.processor.internal.ast.SingleType;
import info.fingo.xactus.processor.internal.ast.StringLiteral;
import info.fingo.xactus.processor.internal.ast.SubExpr;
import info.fingo.xactus.processor.internal.ast.TextTest;
import info.fingo.xactus.processor.internal.ast.TreatAsExpr;
import info.fingo.xactus.processor.internal.ast.UnionExpr;
import info.fingo.xactus.processor.internal.ast.VarExprPair;
import info.fingo.xactus.processor.internal.ast.VarRef;
import info.fingo.xactus.processor.internal.ast.XPathExpr;
import info.fingo.xactus.processor.internal.ast.XPathVisitor;

public class DefaultVisitor implements XPathVisitor {

	/**
	 * Returns the normalized tree
	 *
	 * @param xp is the xpath expression.
	 * @return the xpath expressions.
	 */
	public Object visit(XPath xp) {
		for (Expr e : xp) {
			e.accept(this);
		}
		return null;
	}

	/**
	 *
	 * @param fex is the For expression.
	 * @return fex expression.
	 */
	@Override
	public Object visit(ForExpr fex) {
		for (VarExprPair ve : fex) {
			ve.expr().accept(this);
		}
		fex.expr().accept(this);
		return null;
	}

	/**
	 *
	 * @param qex is the Quantified expression.
	 * @return qex expression.
	 */
	@Override
	public Object visit(QuantifiedExpr qex) {
		for (VarExprPair ve : qex) {
			ve.expr().accept(this);
		}
		qex.expr().accept(this);
		return null;
	}

	/**
	 *
	 * @param ifex is the 'if' expression.
	 * @return ifex expression.
	 */
	@Override
	public Object visit(IfExpr ifex) {
		for (Expr e : ifex) {
			e.accept(this);
		}
		ifex.then_clause().accept(this);
		ifex.else_clause().accept(this);
		return ifex;
	}

	/**
	 * @param ex is the 'or' expression.
	 * @return make logic expr(orex).
	 */
	@Override
	public Object visit(OrExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the 'and' expression.
	 * @return make logic expr(andex).
	 */
	@Override
	public Object visit(AndExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param cmpex is the compare expression.
	 * @return cmpex.
	 */
	@Override
	public Object visit(CmpExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param rex is the range expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(RangeExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the add expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(AddExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the sub expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(SubExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param mulex is the multiply expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(MulExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the division expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(DivExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the integer division expression that always returns an integer.
	 * @return a new function.
	 */
	@Override
	public Object visit(IDivExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the mod expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(ModExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the union expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(UnionExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the pipe expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(PipeExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the intersect expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(IntersectExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ex is the except expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(ExceptExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param ioexp is the instance of expression.
	 * @return a ioexp.
	 */
	@Override
	public Object visit(InstOfExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param taexp is the treat as expression.
	 * @return a taexp.
	 */
	@Override
	public Object visit(TreatAsExpr ex) {
		ex.left().accept(this);
		ex.right().accept(this);
		return null;
	}

	/**
	 * @param cexp is the castable expression.
	 * @return cexp.
	 */
	@Override
	public Object visit(CastableExpr cexp) {
		cexp.left().accept(this);
		cexp.right().accept(this);
		return null;
	}

	/**
	 * @param cexp is the cast expression.
	 * @return cexp.
	 */
	@Override
	public Object visit(CastExpr cexp) {
		cexp.left().accept(this);
		cexp.right().accept(this);
		return null;
	}

	/**
	 * @param e is the minus expression.
	 * @return new sub expression
	 */
	@Override
	public Object visit(MinusExpr e) {
		e.arg().accept(this);
		return null;
	}

	/**
	 * @param e is the plus expression.
	 * @return new add expression
	 */
	@Override
	public Object visit(PlusExpr e) {
		e.arg().accept(this);
		return null;
	}

	/**
	 * @param e is the xpath expression.
	 * @return result.
	 */
	@Override
	public Object visit(XPathExpr e) {
		e.expr().accept(this);
		e.next().accept(this);
		return null;
	}

	/**
	 * @param e is the forward step.
	 * @return e
	 */
	@Override
	public Object visit(ForwardStep e) {
		e.node_test().accept(this);
		return null;
	}

	/**
	 * @param e is the reverse step.
	 * @return e
	 */
	@Override
	public Object visit(ReverseStep e) {
		e.node_test().accept(this);
		return null;
	}

	/**
	 * @param e is the Name test.
	 * @return e
	 */
	@Override
	public Object visit(NameTest e) {
		return null;
	}

	/**
	 * @param e is the veriable reference.
	 * @return e
	 */
	@Override
	public Object visit(VarRef e) {
		return null;
	}

	/**
	 * @param e is the string literal.
	 * @return e
	 */
	@Override
	public Object visit(StringLiteral e) {
		return null;
	}

	/**
	 * @param e is the integer literal.
	 * @return e
	 */
	@Override
	public Object visit(IntegerLiteral e) {
		return null;
	}

	/**
	 * @param e is the double literal.
	 * @return e
	 */
	@Override
	public Object visit(DoubleLiteral e) {
		return null;
	}

	/**
	 * @param e is the decimal literal.
	 * @return e
	 */
	@Override
	public Object visit(DecimalLiteral e) {
		return null;
	}

	/**
	 * @param e is the par expression.
	 * @return e
	 */
	@Override
	public Object visit(ParExpr pe) {
		for (Expr e : pe) {
			e.accept(this);
		}
		return null;
	}

	/**
	 * @param e is the Cntx Item Expression.
	 * @return new function
	 */
	@Override
	public Object visit(CntxItemExpr e) {
		return null;
	}

	/**
	 * @param e is the fucntion call.
	 * @return e
	 */
	@Override
	public Object visit(FunctionCall fc) {
		for (Expr e : fc) {
			e.accept(this);
		}
		return fc;
	}

	/**
	 * @param e is the single type.
	 * @return e
	 */
	@Override
	public Object visit(SingleType e) {
		return e;
	}

	/**
	 * @param e is the sequence type.
	 * @return e
	 */
	@Override
	public Object visit(SequenceType e) {
		ItemType it = e.item_type();
		if (it != null)
			it.accept(this);
		return null;
	}

	/**
	 * @param e is the item type.
	 * @return e
	 */
	@Override
	public Object visit(ItemType e) {

		switch (e.type()) {
		case ItemType.ITEM:
			break;
		case ItemType.QNAME:
			break;

		case ItemType.KINDTEST:
			e.kind_test().accept(this);
			break;
		}
		return null;
	}

	/**
	 * @param e is the any kind test.
	 * @return e
	 */
	@Override
	public Object visit(AnyKindTest e) {
		return null;
	}

	/**
	 * @param e is the document test.
	 * @return e
	 */
	@Override
	public Object visit(DocumentTest e) {

		switch (e.type()) {
		case DocumentTest.ELEMENT:
			e.elem_test().accept(this);
			break;

		case DocumentTest.SCHEMA_ELEMENT:
			e.schema_elem_test().accept(this);
			break;
		}
		return null;
	}

	/**
	 * @param e is the text test.
	 * @return e
	 */
	@Override
	public Object visit(TextTest e) {
		return null;
	}

	/**
	 * @param e is the common test.
	 * @return e
	 */
	@Override
	public Object visit(CommentTest e) {
		return null;
	}

	/**
	 * @param e is the PI test.
	 * @return e
	 */
	@Override
	public Object visit(PITest e) {
		return null;
	}

	/**
	 * @param e is the attribute test.
	 * @return e
	 */
	@Override
	public Object visit(AttributeTest e) {
		return e;
	}

	/**
	 * @param e is the schema attribute test.
	 * @return e
	 */
	@Override
	public Object visit(SchemaAttrTest e) {
		return e;
	}

	/**
	 * @param e is the element test.
	 * @return e
	 */
	@Override
	public Object visit(ElementTest e) {
		return e;
	}

	/**
	 * @param e is the schema element test.
	 * @return e
	 */
	@Override
	public Object visit(SchemaElemTest e) {
		return e;
	}

	/**
	 * @param se is the axis step.
	 * @return e
	 */
	@Override
	public Object visit(AxisStep se) {
		se.step().accept(this);
		for (Collection<Expr> i : se) {
			for (Expr e : i) {
				e.accept(this);
			}
		}
		return null;
	}

	/**
	 * @param se is the filter expression.
	 * @return e
	 */
	@Override
	public Object visit(FilterExpr se) {
		se.primary().accept(this);
		for (Collection<Expr> i : se) {
			for (Expr e : i) {
				e.accept(this);
			}
		}
		return se;
	}

}
