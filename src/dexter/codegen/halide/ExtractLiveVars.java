package dexter.codegen.halide;

import dexter.ir.BaseVisitor;
import dexter.ir.Expr;
import dexter.ir.bool.VarExpr;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Maaz Ahmad on 9/12/19.
 */
public class ExtractLiveVars extends BaseVisitor {
  Set<VarExpr> vars = new HashSet<>();
  Map<VarExpr, Expr> termMapping;

  public ExtractLiveVars(Map<VarExpr, Expr> tm) {
    termMapping = tm;
  }

  public Set<VarExpr> vars() { return vars; }

  public boolean contains (VarExpr v) { return vars.contains(v); }

  @Override
  public void enter (VarExpr v) {
    if (termMapping.containsKey(v))
      termMapping.get(v).accept(this);
    else
      vars.add(v);
  }
}