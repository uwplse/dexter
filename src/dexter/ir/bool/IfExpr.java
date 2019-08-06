package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class IfExpr extends Expr
{
  protected Expr condition;
  protected Expr consequent;
  protected Expr alternate;

  public IfExpr (Expr condition, Expr consequent, Expr alternate)
  {
    this.condition = condition;
    this.consequent = consequent;
    this.alternate = alternate;
  }

  public Expr cond () { return condition; }
  public Expr cons () { return consequent; }
  public Expr alt () { return alternate; }

  @Override public <T> T accept(Visitor<T> v) { return v.visit(this); }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof IfExpr)
    {
      IfExpr other = (IfExpr)o;
      return other.condition.equals(condition) && other.consequent.equals(consequent) &&
             other.alternate.equals(alternate);
    }
    else
      return false;
  }
}