/*******************************************************************************
 * Copyright (c) 2005, 2013 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *******************************************************************************/

package info.fingo.xactus.processor.internal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//import java.util.Iterator;

import info.fingo.xactus.processor.StaticContext;
import info.fingo.xactus.processor.ast.XPath;
import info.fingo.xactus.processor.function.FnFunctionLibrary;
import info.fingo.xactus.processor.internal.ast.AddExpr;
import info.fingo.xactus.processor.internal.ast.AndExpr;
import info.fingo.xactus.processor.internal.ast.AnyKindTest;
import info.fingo.xactus.processor.internal.ast.AttributeTest;
import info.fingo.xactus.processor.internal.ast.AxisStep;
import info.fingo.xactus.processor.internal.ast.BinExpr;
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
import info.fingo.xactus.processor.internal.ast.NodeTest;
import info.fingo.xactus.processor.internal.ast.OrExpr;
import info.fingo.xactus.processor.internal.ast.PITest;
import info.fingo.xactus.processor.internal.ast.ParExpr;
import info.fingo.xactus.processor.internal.ast.PipeExpr;
import info.fingo.xactus.processor.internal.ast.PlusExpr;
import info.fingo.xactus.processor.internal.ast.PrimaryExpr;
import info.fingo.xactus.processor.internal.ast.QuantifiedExpr;
import info.fingo.xactus.processor.internal.ast.RangeExpr;
import info.fingo.xactus.processor.internal.ast.ReverseStep;
import info.fingo.xactus.processor.internal.ast.SchemaAttrTest;
import info.fingo.xactus.processor.internal.ast.SchemaElemTest;
import info.fingo.xactus.processor.internal.ast.SequenceType;
import info.fingo.xactus.processor.internal.ast.SingleType;
import info.fingo.xactus.processor.internal.ast.Step;
import info.fingo.xactus.processor.internal.ast.StepExpr;
import info.fingo.xactus.processor.internal.ast.StringLiteral;
import info.fingo.xactus.processor.internal.ast.SubExpr;
import info.fingo.xactus.processor.internal.ast.TextTest;
import info.fingo.xactus.processor.internal.ast.TreatAsExpr;
import info.fingo.xactus.processor.internal.ast.UnExpr;
import info.fingo.xactus.processor.internal.ast.UnionExpr;
import info.fingo.xactus.processor.internal.ast.VarExprPair;
import info.fingo.xactus.processor.internal.ast.VarRef;
import info.fingo.xactus.processor.internal.ast.XPathExpr;
import info.fingo.xactus.processor.internal.ast.XPathNode;
import info.fingo.xactus.processor.internal.ast.XPathVisitor;
import info.fingo.xactus.processor.internal.function.OpFunctionLibrary;
import info.fingo.xactus.processor.internal.types.QName;

/**
 * Normalizer that uses XPathVisitor.
 */
// XXX currently not supported anymore!
public class Normalizer implements XPathVisitor {

	private final StaticContext sc;

	/**
	 * Static Context is set to sc
	 *
	 * @param sc is the StaticContext.
	 */
	public Normalizer(StaticContext sc) {
		this.sc = sc;
	}

	/**
	 * Returns the normalized tree
	 *
	 * @param xp is the xpath expression.
	 * @return the xpath expressions.
	 */
	@Override
	public Object visit(XPath xp) {

		Collection<Expr> exprs = new ArrayList<>();
		for (Expr e : xp) {
			Expr n = (Expr) e.accept(this);
			exprs.add(n);
		}
		return new XPath(exprs);
	}

	/**
	 *
	 * @param fex is the For expression.
	 * @return fex expression.
	 */
	@Override
	public Object visit(ForExpr fex) {
		ForExpr last = fex;
		Expr ret = fex.expr();
		int depth = 0;

		for (VarExprPair ve : fex) {

			// ok we got nested fors...
			if (depth > 0) {
				Collection<VarExprPair> pairs = new ArrayList<>();
				pairs.add(ve);

				ForExpr fe = new ForExpr(pairs, ret);
				last.set_expr(fe);

				last = fe;
			}

			depth++;
		}

		// normalize return value, and set it to the last for expr
		ret.accept(this);

		// get rid of the pairs in the parent (original) for
		if (depth > 1)
			fex.truncate_pairs();

		return fex;
	}

	/**
	 *
	 * @param qex is the Quantified expression.
	 * @return qex expression.
	 */
	// XXX: code duplication
	@Override
	public Object visit(QuantifiedExpr qex) {
		QuantifiedExpr last = qex;
		Expr ret = qex.expr();
		int depth = 0;

		for (VarExprPair ve : qex) {
			// ok we got nested fors...
			if (depth > 0) {
				Collection<VarExprPair> pairs = new ArrayList<>();
				pairs.add(ve);

				QuantifiedExpr qe = new QuantifiedExpr(qex.type(), pairs, ret);
				last.set_expr(qe);

				last = qe;
			}

			depth++;
		}

		// normalize return value, and set it to the last for expr
		ret.accept(this);

		// get rid of the pairs in the parent (original) for
		if (depth > 1)
			qex.truncate_pairs();

		return qex;

	}

	private void printExprs(Iterable<Expr> i) {
		for (Expr e : i) {
			e.accept(this);
		}
	}

	/**
	 *
	 * @param ifex is the 'if' expression.
	 * @return ifex expression.
	 */
	@Override
	public Object visit(IfExpr ifex) {

		printExprs(ifex);
		ifex.then_clause().accept(this);
		ifex.else_clause().accept(this);
		return ifex;
	}

	/**
	 * @param name of binary expression.
	 * @param e    is the binary expression.
	 */
	public void printBinExpr(String name, BinExpr e) {
		e.left().accept(this);
		e.right().accept(this);
	}

	private BinExpr make_logic_expr(BinExpr e) {

		Collection<Expr> normalized = normalize_bin_args(e);
		Expr nor_arr[] = new Expr[2];
		int j = 0;

		for (Expr i : normalized) {
			nor_arr[j] = i;
			j++;
		}

		Collection<Expr> args = new ArrayList<Expr>();
		args.add(nor_arr[0]);
		e.set_left(make_function(new QName("fn", "boolean", FnFunctionLibrary.XPATH_FUNCTIONS_NS), args));

		args.clear();
		args.add(nor_arr[1]);
		e.set_right(make_function(new QName("fn", "boolean", FnFunctionLibrary.XPATH_FUNCTIONS_NS), args));

		return e;
	}

	/**
	 * @param orex is the 'or' expression.
	 * @return make logic expr(orex).
	 */
	@Override
	public Object visit(OrExpr orex) {
		return make_logic_expr(orex);
	}

	/**
	 * @param andex is the 'and' expression.
	 * @return make logic expr(andex).
	 */
	@Override
	public Object visit(AndExpr andex) {
		return make_logic_expr(andex);
	}

	/**
	 * @param cmpex is the compare expression.
	 * @return cmpex.
	 */
	@Override
	public Object visit(CmpExpr cmpex) {
		switch (cmpex.type()) {
		case CmpExpr.EQ:
			return make_CmpOp(cmpex, new QName("fs", "eq", OpFunctionLibrary.XPATH_OP_NS));

		case CmpExpr.NE:
			return make_CmpOp(cmpex, new QName("fs", "ne", OpFunctionLibrary.XPATH_OP_NS));

		case CmpExpr.LT:
			return make_CmpOp(cmpex, new QName("fs", "lt", OpFunctionLibrary.XPATH_OP_NS));

		case CmpExpr.GT:
			return make_CmpOp(cmpex, new QName("fs", "gt", OpFunctionLibrary.XPATH_OP_NS));

		case CmpExpr.LE:
			return make_CmpOp(cmpex, new QName("fs", "le", OpFunctionLibrary.XPATH_OP_NS));

		case CmpExpr.GE:
			return make_CmpOp(cmpex, new QName("fs", "ge", OpFunctionLibrary.XPATH_OP_NS));

		// XXX don't have functs!
		case CmpExpr.IS:
			return make_function(new QName("op", "node-equal"), normalize_bin_args(cmpex));

		case CmpExpr.LESS_LESS:
			return make_function(new QName("op", "node-before"), normalize_bin_args(cmpex));

		case CmpExpr.GREATER_GREATER:
			return make_function(new QName("op", "node-after"), normalize_bin_args(cmpex));
		}

		printBinExpr("CMP" + cmpex.type(), cmpex);
		return cmpex;
	}

	private Collection<Expr> normalize_bin_args(BinExpr e) {

		Expr left = (Expr) e.left().accept(this);
		Expr right = (Expr) e.right().accept(this);

		Collection<Expr> args = new ArrayList<>();
		args.add(left);
		args.add(right);

		return args;
	}

	/**
	 * @param rex is the range expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(RangeExpr rex) {
		Collection<Expr> args = normalize_bin_args(rex);
		return make_function(new QName("op", "to", OpFunctionLibrary.XPATH_OP_NS), args);
	}

	private XPathExpr make_xpathexpr(PrimaryExpr pex) {
		FilterExpr fe = new FilterExpr(pex, Collections.emptyList());
		return new XPathExpr(0, fe);
	}

	private XPathExpr make_int_lit(int i) {
		IntegerLiteral il = new IntegerLiteral(BigInteger.valueOf(i));
		return make_xpathexpr(il);
	}

	private XPathExpr make_string_lit(String s) {
		StringLiteral sl = new StringLiteral(s);
		return make_xpathexpr(sl);
	}

	private XPathExpr make_convert_operand(XPathExpr arg1, XPathExpr arg2) {

		Collection<Expr> args = new ArrayList<>();
		args.add(arg1);
		args.add(arg2);

		return make_function(new QName("fs", "convert-operand", OpFunctionLibrary.XPATH_OP_NS), args);
	}

	private XPathExpr make_double_lit(double d) {
		DoubleLiteral dl = new DoubleLiteral(d);
		return make_xpathexpr(dl);
	}

	// fs:fname( fs:convert-operand( fn:data(ARG1), 1.0E0 ),
	// fs:convert-operand( fn:data(ARG2), 1.0E0 )
	// )
	private XPathExpr make_convert_binop(BinExpr e, XPathExpr convarg, QName name) {

		Collection<Expr> args = normalize_bin_args(e);
		XPathExpr args_arr[] = new XPathExpr[2];
		int j = 0;

		for (XPathNode i : args) {
			args_arr[j] = (XPathExpr) i;
			j++;
		}

		Collection<Expr> argsfname = new ArrayList<>();
		for (j = 0; j < 2; j++) {
			XPathExpr arg = make_convert_operand(args_arr[j], convarg);
			argsfname.add(arg);
		}
		return make_function(name, argsfname);
	}

	private XPathExpr make_ArithOp(BinExpr e, QName name) {
		return make_convert_binop(e, make_double_lit(1.0), name);
	}

	// fs:fname( fs:convert_operand( fn:data(ARG1), "string"),
	// fs:convert_operand( fn:data(ARG2), "string")
	// )
	private XPathExpr make_CmpOp(BinExpr e, QName name) {
		return make_convert_binop(e, make_string_lit("string"), name);
	}

	/**
	 * @param addex is the add expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(AddExpr addex) {
		return make_ArithOp(addex, new QName("fs", "plus", OpFunctionLibrary.XPATH_OP_NS));
	}

	/**
	 * @param subex is the sub expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(SubExpr subex) {
		return make_ArithOp(subex, new QName("fs", "minus", OpFunctionLibrary.XPATH_OP_NS));
	}

	/**
	 * @param mulex is the multiply expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(MulExpr mulex) {
		return make_ArithOp(mulex, new QName("fs", "times", OpFunctionLibrary.XPATH_OP_NS));
	}

	/**
	 * @param mulex is the division expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(DivExpr mulex) {
		return make_ArithOp(mulex, new QName("fs", "div", OpFunctionLibrary.XPATH_OP_NS));
	}

	/**
	 * @param mulex is the integer division expression that always returns an
	 *              integer.
	 * @return a new function.
	 */
	// XXX: integer cast!
	@Override
	public Object visit(IDivExpr mulex) {
		return make_ArithOp(mulex, new QName("fs", "idiv", OpFunctionLibrary.XPATH_OP_NS));
	}

	/**
	 * @param mulex is the mod expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(ModExpr mulex) {
		return make_ArithOp(mulex, new QName("fs", "mod", OpFunctionLibrary.XPATH_OP_NS));
	}

	/**
	 * @param unex is the union expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(UnionExpr unex) {
		Collection<Expr> args = normalize_bin_args(unex);
		return make_function(new QName("op", "union", OpFunctionLibrary.XPATH_OP_NS), args);
	}

	/**
	 * @param pipex is the pipe expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(PipeExpr pipex) {
		Collection<Expr> args = normalize_bin_args(pipex);
		return make_function(new QName("op", "union", OpFunctionLibrary.XPATH_OP_NS), args);
	}

	/**
	 * @param iexpr is the intersect expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(IntersectExpr iexpr) {
		Collection<Expr> args = normalize_bin_args(iexpr);
		return make_function(new QName("op", "intersect", OpFunctionLibrary.XPATH_OP_NS), args);
	}

	/**
	 * @param eexpr is the except expression.
	 * @return a new function.
	 */
	@Override
	public Object visit(ExceptExpr eexpr) {
		Collection<Expr> args = normalize_bin_args(eexpr);
		return make_function(new QName("op", "except", OpFunctionLibrary.XPATH_OP_NS), args);
	}

	/**
	 * @param ioexp is the instance of expression.
	 * @return a ioexp.
	 */
	@Override
	public Object visit(InstOfExpr ioexp) {
		printBinExpr("INSTANCEOF", ioexp);
		return ioexp;
	}

	/**
	 * @param taexp is the treat as expression.
	 * @return a taexp.
	 */
	@Override
	public Object visit(TreatAsExpr taexp) {
		printBinExpr("TREATAS", taexp);
		return taexp;
	}

	/**
	 * @param cexp is the castable expression.
	 * @return cexp.
	 */
	@Override
	public Object visit(CastableExpr cexp) {
		printBinExpr("CASTABLE", cexp);
		return cexp;
	}

	/**
	 * @param cexp is the cast expression.
	 * @return cexp.
	 */
	@Override
	public Object visit(CastExpr cexp) {
		printBinExpr("CAST", cexp);
		return cexp;
	}

	/**
	 * @param name is the name.
	 * @param e    is the Un Expression.
	 */
	public void printUnExpr(String name, UnExpr e) {
		e.arg().accept(this);

	}

	/**
	 * @param e is the minus expression.
	 * @return new sub expression
	 */
	@Override
	public Object visit(MinusExpr e) {

		SubExpr se = new SubExpr(make_int_lit(0), e.arg());
		return se.accept(this);
	}

	/**
	 * @param e is the plus expression.
	 * @return new add expression
	 */
	@Override
	public Object visit(PlusExpr e) {
		
		AddExpr ae = new AddExpr(make_int_lit(0), e.arg());
		return ae.accept(this);
	}

	private XPathExpr make_function(QName name, Collection<Expr> args) {

		FunctionCall fc = new FunctionCall(name, args);
		FilterExpr fe = new FilterExpr(fc, Collections.emptyList());
		return new XPathExpr(0, fe);
	}

	private XPathExpr make_root_self_node() {

		// self::node()
		Step self_node = new ForwardStep(ForwardStep.SELF, new AnyKindTest());
		StepExpr self_node_expr = new AxisStep(self_node, Collections.emptyList());
		XPathExpr self_node_xpath = new XPathExpr(0, self_node_expr);

		// fn:root(self::node())
		Collection<Expr> args = new ArrayList<>();
		args.add(self_node_xpath);
		XPathExpr xpe = make_function(new QName("fn", "root", FnFunctionLibrary.XPATH_FUNCTIONS_NS), args);

		return xpe;
	}

	private XPathExpr make_descendant_or_self() {

		Step desc_self_node = new ForwardStep(ForwardStep.DESCENDANT_OR_SELF, new AnyKindTest());
		StepExpr se = new AxisStep(desc_self_node, Collections.emptyList());
		return new XPathExpr(0, se);
	}

	/**
	 * @param e is the xpath expression.
	 * @return result.
	 */
	@Override
	public Object visit(XPathExpr e) {
		
		XPathExpr xp = e;
		int depth = 0; // indicates how many / we traversed
		XPathExpr result = e;

		while (xp != null) {
			int slashes = xp.slashes();
			StepExpr se = xp.expr();

			if (slashes == 1) {
				// this is a single slash and nothing else...
				if (se == null)
					return make_root_self_node();

				// /RelativePathExpr
				if (depth == 0) {
					XPathExpr xpe = make_root_self_node();
					xpe.set_next(e);

					result = xpe;
				}
			}

			if (slashes == 2) {
				// //RelativePathExpr
				if (depth == 0) {
					XPathExpr desc = make_descendant_or_self();
					desc.set_slashes(1);
					e.set_slashes(1);
					desc.set_next(e);

					XPathExpr root_self = make_root_self_node();
					root_self.set_next(desc);
					return root_self;
				}
			}

			if (se != null)
				se.accept(this);

			XPathExpr next = xp.next();

			// peek if the next guy will have 2 slashes...
			if (next != null) {
				// StepExpr//StepExpr
				if (next.slashes() == 2) {
					// create the node to stick between the
					// slashes
					XPathExpr desc = make_descendant_or_self();
					desc.set_slashes(1);

					// current node / desc / next
					xp.set_next(desc);
					desc.set_next(next);
					next.set_slashes(1);
				}
			}
			xp = next;
			depth++;
		}
		return result;
	}

	/**
	 * @param e is the forward step.
	 * @return e
	 */
	// XXX: normalzie!
	@Override
	public Object visit(ForwardStep e) {
		
		int axis = e.axis();

		switch (axis) {
		case ForwardStep.AT_SYM:
			e.set_axis(ForwardStep.ATTRIBUTE);
			break;

		case ForwardStep.NONE:
			e.set_axis(ForwardStep.CHILD);
			break;

		}

		e.node_test().accept(this);

		return e;
	}

	/**
	 * @param e is the reverse step.
	 * @return e
	 */
	@Override
	public Object visit(ReverseStep e) {

		if (e.axis() == ReverseStep.DOTDOT) {
			NodeTest nt = new AnyKindTest();
			Step s = new ReverseStep(ReverseStep.PARENT, nt);

			return s;
		}

		NodeTest nt = e.node_test();
		if (nt != null)
			nt.accept(this);

		return e;
	}

	/**
	 * @param e is the Name test.
	 * @return e
	 */
	@Override
	public Object visit(NameTest e) {

		String prefix = e.name().prefix();

		// XXX: is this correct ?
		// i.e. if there is no prefix... its ok.. else it must exist
		if (prefix == null)
			return null;

		return e;
	}

	/**
	 * @param e is the veriable reference.
	 * @return e
	 */
	@Override
	public Object visit(VarRef e) {
		return e;
	}

	/**
	 * @param e is the string literal.
	 * @return e
	 */
	@Override
	public Object visit(StringLiteral e) {
		return e;
	}

	/**
	 * @param e is the integer literal.
	 * @return e
	 */
	@Override
	public Object visit(IntegerLiteral e) {
		return e;
	}

	/**
	 * @param e is the double literal.
	 * @return e
	 */
	@Override
	public Object visit(DoubleLiteral e) {
		return e;
	}

	/**
	 * @param e is the decimal literal.
	 * @return e
	 */
	@Override
	public Object visit(DecimalLiteral e) {
		return e;
	}

	/**
	 * @param e is the par expression.
	 * @return e
	 */
	@Override
	public Object visit(ParExpr e) {
		printExprs(e);
		return e;
	}

	/**
	 * @param e is the Cntx Item Expression.
	 * @return new function
	 */
	@Override
	public Object visit(CntxItemExpr e) {
		return new VarRef(new QName("fs", "dot"));
	}

	/**
	 * @param e is the fucntion call.
	 * @return e
	 */
	// XXX: how do we normalize ?
	@Override
	public Object visit(FunctionCall e) {

		printExprs(e);
		return e;
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

		return e;
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

		return e;
	}

	/**
	 * @param e is the any kind test.
	 * @return e
	 */
	@Override
	public Object visit(AnyKindTest e) {
		return e;
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
		return e;
	}

	/**
	 * @param e is the text test.
	 * @return e
	 */
	@Override
	public Object visit(TextTest e) {
		return e;
	}

	/**
	 * @param e is the common test.
	 * @return e
	 */
	@Override
	public Object visit(CommentTest e) {
		return e;
	}

	/**
	 * @param e is the PI test.
	 * @return e
	 */
	@Override
	public Object visit(PITest e) {
		String arg = e.arg();
		if (arg == null)
			arg = "";

		return e;
	}

	/**
	 * @param e is the attribute test.
	 * @return e
	 */
	// XXX NO CHECK ?
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
	// XXX NO SEMANTIC CHECK?!
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

	private void printCollExprs(Iterable<Collection<Expr>> i) {

		for (Collection<Expr> coll : i) {
			printExprs(coll);
		}
	}

	/**
	 * @param as is the axis step.
	 * @return e
	 */
	@Override
	public Object visit(AxisStep as) {

		Step s = (Step) as.step().accept(this);
		as.set_step(s);
		printCollExprs(as);
		return as;
	}

	/**
	 * @param fe is the filter expression.
	 * @return e
	 */
	@Override
	public Object visit(FilterExpr fe) {

		PrimaryExpr pe = (PrimaryExpr) fe.primary().accept(this);
		fe.set_primary(pe);
		printCollExprs(fe);
		return fe;
	}

}
