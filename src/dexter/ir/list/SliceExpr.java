package dexter.ir.list;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class SliceExpr extends CallExpr
{
  public SliceExpr (Expr list, Expr start, Expr end)
  {
    super("slice", Arrays.asList(list, start, end));
  }

  public Expr list () { return this.args.get(0); }
  public Expr start  () { return this.args.get(1); }
  public Expr end () { return this.args.get(2); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}