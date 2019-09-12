package dexter.ir.type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class IntT implements Type
{
  protected IntT() {}

  @Override public String toString () { return "int"; }

  @Override public void print() { System.out.println(toString()); }
}