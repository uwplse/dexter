package dexter.ir.type;

import dexter.ir.bool.VarExpr;

import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ClassT implements Type
{
  protected String name;
  protected List<VarExpr> fields;

  protected ClassT (String name, List<VarExpr> fields)
  {
    this.name = name;
    this.fields = fields;
  }

  public String name () { return name; }
  public List<VarExpr> fields () { return fields; }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof ClassT)
    {
      ClassT oC = (ClassT)o;
      return oC.name.equals(name) && oC.fields.equals(fields);
    }
    return false;
  }

  @Override
  public String toString ()
  {
    return name;
  }

  @Override
  public int hashCode () { return 0; }

  @Override public void print() { System.out.println(toString()); }
}