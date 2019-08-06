package dexter.ir.integer;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.TypesFactory;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class FloatLitExpr extends Expr
{
  protected double value;

  public FloatLitExpr(double value)
  {
    super(TypesFactory.Float);
    this.value = value;
  }

  public double value () { return value; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof FloatLitExpr)
      return ((FloatLitExpr)o).value == this.value;
    else
      return false;
  }
}