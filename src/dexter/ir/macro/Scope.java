package dexter.ir.macro;

import dexter.ir.Expr;
import dexter.ir.Visitor;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Scope extends Expr
{
  protected Expr expr;

  public Scope(Expr e) { this.expr = e; }

  public Expr getExpr () { return this.expr; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }
}