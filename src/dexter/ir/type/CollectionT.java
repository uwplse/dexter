package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public abstract class CollectionT implements Type
{
  protected Type elemT;
  protected int dim;

  protected CollectionT(int dim, Type elemT)
  {
    this.elemT = elemT;
    this.dim = dim;
  }

  public int dim () { return dim; }
  public Type elemT () { return elemT; }

  @Override
  public int hashCode () { return 0; }
}