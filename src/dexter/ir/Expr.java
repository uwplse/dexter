package dexter.ir;

import dexter.ir.type.Type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public abstract class Expr
{
  protected Type type;

  public Expr ()
  {
    this.type = null;
  }

  public Expr (Type t)
  {
    this.type = t;
  }

  public abstract <T> T accept(Visitor<T> v);

  public Type type() { return type; }
  public Expr type(Type t) { this.type = t; return this; }

  public Expr substitute (Expr from, Expr to) { return this.accept(new Substitutor(from, to)); }

  public void print() { System.out.println(this); }

  @Override
  public String toString() { return this.accept(new Printer()); }

  @Override
  public int hashCode () {
    return 0;
  }
}