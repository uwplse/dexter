package dexter.modifier;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.VarExpr;
import dexter.ir.macro.Scope;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VarExprToScopeMacroConvertor extends Transformer {
  Set<String> reservedVars = new HashSet<>(Arrays.asList("idx_x", "idx_y"));

  @Override
  public Expr transform (VarExpr e)
  {
    if (reservedVars.contains(e.name()))
      return null;

    Scope s = new Scope(e);
    s.type(e.type());
    return s;
  }
}