package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.Type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class VarExpr extends Expr
{
  protected String name;

  public VarExpr(String name)
  {
    this(name, null);
  }

  public VarExpr (String name, Type type)
  {
    super(type);
    this.name = name;
  }

  public String name () { return name; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof VarExpr)
      return ((VarExpr)o).name.equals(this.name);
    else
      return false;
  }
}