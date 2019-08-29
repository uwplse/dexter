package dexter.modifier;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;

import java.util.List;

/**
 * Created by Maaz Ahmad on 8/19/19.
 */
public class PostfixFnNamesWithID extends Transformer {
  List<FuncDecl> functions;
  int clauseID;

  public PostfixFnNamesWithID(List<FuncDecl> fns, int id) {
    this.functions = fns;
    this.clauseID = id;
  }

  public Expr transform(FuncDecl fn)
  {
    if (fn.name().matches("int_expr|float_expr|pc_pc_fn_asn_2di|pc_pc_fn_asn_2df|pc_pc_fn_asn_1di|pc_pc_fn_asn_1df"))
      return new FuncDecl(fn.name() + "_" + clauseID, fn.params(), fn.retType(), fn.body().accept(this));
    return null;
  }

  public Expr transform(CallExpr ce)
  {
    if (ce.name().matches("int_expr|float_expr|pc_pc_fn_asn_2di|pc_pc_fn_asn_2df|pc_pc_fn_asn_1di|pc_pc_fn_asn_1df"))
      return new CallExpr(ce.name() + "_" + clauseID, ce.args());
    return null;
  }
}