package dexter.ir.array;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class IncrPtrExpr extends CallExpr
{
  public IncrPtrExpr(Expr ptr, Expr incr)
  {
    super("incr", Arrays.asList(ptr, incr));
  }

  public Expr ptr () { return this.args.get(0); }
  public Expr incr  () { return this.args.get(1); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}