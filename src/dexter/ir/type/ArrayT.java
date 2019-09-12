package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ArrayT extends CollectionT
{
  protected ArrayT(int dim, Type elemT)
  {
    super(dim, elemT);
  }

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

  @Override public String toString () { return "array(" + dim + "," + elemT + ")"; }

  @Override public void print() { System.out.println(toString()); }
}