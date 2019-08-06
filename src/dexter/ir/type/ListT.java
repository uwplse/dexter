package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ListT implements Type
{
  protected Type elemT;

  protected ListT(Type elemT)
  {
    this.elemT = elemT;
  }

  public Type elemT () { return elemT; }

  @Override public String toString () { return "list(" + elemT + ")"; }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof ListT)
    {
      ListT oA = (ListT)o;
      return oA.elemT.equals(elemT);
    }
    return false;
  }

  @Override
  public int hashCode () { return 0; }
}