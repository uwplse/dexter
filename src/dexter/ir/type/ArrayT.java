package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ArrayT implements Type
{
  protected Type elemT;
  protected int dim;

  protected ArrayT(int dim, Type elemT)
  {
    this.elemT = elemT;
    this.dim = dim;
  }

  public int dim () { return dim; }
  public Type elemT () { return elemT; }

  @Override public String toString () { return "array(" + dim + "," + elemT + ")"; }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof ArrayT)
    {
      ArrayT oA = (ArrayT)o;
      return oA.elemT.equals(elemT) && oA.dim == dim;
    }
    return false;
  }

  @Override
  public int hashCode () { return 0; }
}