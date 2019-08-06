package dexter.ir.array;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 3/4/17.
 */
public class DecrPtrExpr extends CallExpr
{
  public DecrPtrExpr(Expr ptr, Expr decr)
  {
    super("decr", Arrays.asList(ptr, decr));
  }

  public Expr ptr () { return this.args.get(0); }
  public Expr decr  () { return this.args.get(1); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}