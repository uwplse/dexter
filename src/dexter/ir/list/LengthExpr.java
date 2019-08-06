package dexter.ir.list;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class LengthExpr extends CallExpr
{
  public LengthExpr (Expr list)
  {
    super("len", Arrays.asList(list));
  }

  public Expr list() { return args.get(0); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}