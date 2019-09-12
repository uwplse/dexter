package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class TupleT extends ArrayT {
  protected int size;

  protected TupleT(Type elemT, int sz)
  {
    super(1, elemT);
    this.size = sz;
  }

  public Type elemT () { return elemT; }
  public int size () { return size; }

  @Override public String toString () { return "tuple(" + size + ", " + elemT + ")"; }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof TupleT)
    {
      TupleT oA = (TupleT)o;
      return oA.elemT.equals(elemT) && oA.size == size;
    }
    return false;
  }

  @Override
  public int hashCode () { return 0; }

  @Override public void print() { System.out.println(toString()); }
}