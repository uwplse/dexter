package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class NullExpr extends Expr
{
  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o) { return o instanceof NullExpr; }
}