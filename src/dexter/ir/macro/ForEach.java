package dexter.ir.macro;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.VarExpr;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ForEach extends Expr
{
  protected VarExpr id;
  protected Expr varSet;
  protected Expr subExpr;

  public ForEach(VarExpr id, Expr varSet, Expr subExpr)
  {
    this.id = id;
    this.varSet = varSet;
    this.subExpr = subExpr;
  }

  public VarExpr getId() {
    return id;
  }

  public Expr getVarSet() {
    return varSet;
  }

  public Expr getSubExpr() {
    return subExpr;
  }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }
}