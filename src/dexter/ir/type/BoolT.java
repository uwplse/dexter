package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class BoolT implements Type
{
  protected BoolT() {}

  @Override public String toString () { return "bool"; }

  @Override public void print() { System.out.println(toString()); }
}