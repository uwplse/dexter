package dexter.ir.macro;

import dexter.ir.Expr;
import dexter.ir.Visitor;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Bound extends Expr
{
  public enum CATEGORY {
    LOWER_BOUND,
    UPPER_BOUND
  }

  protected int index;
  protected CATEGORY cat;

  public Bound(CATEGORY cat, int idx) { this.index = idx; this.cat = cat; }

  public int getIndex () { return this.index; }
  public CATEGORY getCategory () { return this.cat; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }
}