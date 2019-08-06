package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class PtrT extends ArrayT {
  protected Type elemT;
  protected int dim;

  protected PtrT(Type elemT)
  {
    super(1, elemT);
    this.elemT = elemT;
    this.dim = dim;
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof PtrT)
    {
      PtrT oA = (PtrT)o;
      return oA.elemT.equals(elemT) && oA.dim == dim;
    }
    return false;
  }

  @Override public String toString () { return "ptr(" + elemT + ")"; }

  @Override
  public int hashCode () { return 0; }
}