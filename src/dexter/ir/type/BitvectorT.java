package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class BitvectorT implements Type {
  protected int width;

  protected BitvectorT(int width)
  {
    this.width = width;
  }

  public int width () { return width; }

  @Override public String toString () { return "bv" + width; }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof BitvectorT)
    {
      BitvectorT oA = (BitvectorT)o;
      return oA.width == width;
    }
    return false;
  }

  @Override
  public int hashCode () { return 0; }
}