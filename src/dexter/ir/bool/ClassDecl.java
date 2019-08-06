package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ClassDecl extends Expr
{
  protected String name;
  protected List<VarExpr> fields;

  public ClassDecl (String name, List<VarExpr> fields)
  {
    this.name = name;
    this.fields = new ArrayList<>(fields);
  }

  public String name () { return name; }
  public List<VarExpr> fields () { return fields; }

  @Override
  public <T> T accept(Visitor<T> v)
  {
    return v.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof ClassDecl)
    {
      ClassDecl other = (ClassDecl)o;
      return other.name.equals(this.name) && other.fields.equals(this.fields);
    }
    else
      return false;
  }
}