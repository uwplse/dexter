package dexter.ir.list;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class EqualsExpr extends CallExpr
{
  public EqualsExpr (Expr list1, Expr list2)
  {
    super("equals", Arrays.asList(list1, list2));
  }

  public Expr list1 () { return args.get(0); }
  public Expr list2 () { return args.get(1); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}