package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class FieldExpr extends Expr
{
  protected Expr target;
  protected String field;

  public FieldExpr(Expr target, String field)
  {
    this.target = target;
    this.field = field;
  }

  public Expr target () { return target; }
  public String field () { return field; }

  @Override
  public <T> T accept(Visitor<T> v)
  {
    return v.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof FieldExpr)
    {
      FieldExpr other = (FieldExpr)o;
      return other.target.equals(this.target) && other.field.equals(this.field);
    }
    else
      return false;
  }
}