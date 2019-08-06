package dexter.ir.list;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class AppendExpr extends CallExpr
{
  public AppendExpr (Expr list, Expr elem)
  {
    super("append", Arrays.asList(list, elem));
  }

  public Expr list () { return this.args.get(0); }
  public Expr elem  () { return this.args.get(1); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}