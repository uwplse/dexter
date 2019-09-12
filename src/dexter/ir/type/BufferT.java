package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class BufferT extends CollectionT
{
  protected BufferT(Type elemT, int dims)
  {
    super(dims, elemT);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof BufferT)
    {
      BufferT oA = (BufferT)o;
      return oA.elemT.equals(elemT) && oA.dim == dim;
    }
    return false;
  }

  @Override public String toString () { return "buffer(" + elemT + "," + dim + ")"; }

  @Override public void print() { System.out.println(toString()); }
}