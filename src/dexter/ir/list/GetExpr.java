package dexter.ir.list;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class GetExpr extends CallExpr
{
  public GetExpr (Expr list, Expr index)
  {
    super("get", Arrays.asList(list, index));
  }

  public Expr list () { return this.args.get(0); }
  public Expr index  () { return this.args.get(1); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}