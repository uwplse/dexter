package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.TypesFactory;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class BoolLitExpr extends Expr
{
  protected boolean value;

  public static final BoolLitExpr True = new BoolLitExpr(true);
  public static final BoolLitExpr False = new BoolLitExpr(false);

  public BoolLitExpr(boolean value)
  {
    super(TypesFactory.Bool);
    this.value = value;
  }

  public boolean value () { return value; }


  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof BoolLitExpr)
      return ((BoolLitExpr)o).value == this.value;
    else
      return false;
  }
}