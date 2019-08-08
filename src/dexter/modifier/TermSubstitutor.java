package dexter.modifier;

import dexter.ir.Expr;
import dexter.ir.Substitutor;
import dexter.ir.array.SelectExpr;

public class TermSubstitutor extends Substitutor {

  public TermSubstitutor(Expr from, Expr to) {
    super(from, to);
  }

  @Override
  public Expr visit (SelectExpr e) {
    if (replaceSet.containsKey(e))
      return replaceSet.get(e);

    return new SelectExpr(e.array().accept(this), e.index());
  }
}
