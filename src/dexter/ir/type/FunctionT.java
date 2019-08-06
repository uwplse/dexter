package dexter.ir.type;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class FunctionT implements Type
{
  protected List<Type> paramsT;
  protected Type returnT;
  protected String name;

  public FunctionT(String name, List<Type> paramsT, Type returnT)
  {
    this.name = name;
    this.paramsT = paramsT;
    this.returnT = returnT;
  }

  public String name () { return name; }
  public List<Type> paramsT () { return paramsT; }
  public Type returnT () { return returnT; }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof FunctionT)
    {
      FunctionT oF = (FunctionT)o;
      return oF.paramsT.equals(paramsT) && oF.returnT.equals(returnT) && oF.name.equals(name);
    }
    return false;
  }

  @Override
  public String toString ()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(name + "(");

    Iterator<Type> it = paramsT.iterator();
    while (it.hasNext())
    {
      sb.append(it.next().toString());
      if (it.hasNext())
        sb.append(", ");
    }

    sb.append(") -> " + returnT);
    return sb.toString();
  }

  @Override
  public int hashCode () { return 0; }
}