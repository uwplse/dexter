package dexter.ir.integer;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.TypesFactory;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class IntLitExpr extends Expr
{
  protected int value;
  protected boolean canModelAsVar;

  public IntLitExpr(int value)
  {
    super(TypesFactory.Int);
    this.value = value;
    this.canModelAsVar = true;
  }

  public IntLitExpr(int value, boolean m)
  {
    super(TypesFactory.Int);
    this.value = value;
    this.canModelAsVar = m;
  }

  public int value () { return value; }
  public boolean canModelAsVar () { return canModelAsVar; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof IntLitExpr)
      return ((IntLitExpr)o).value == this.value;
    else
      return false;
  }
}