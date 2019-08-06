package dexter.ir.array;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class StoreExpr extends CallExpr
{
  public StoreExpr (Expr array, Expr value, List<Expr> indices)
  {
    super("store", new ArrayList<Expr>() {{ add(array); add(value); addAll(indices); }});
  }

  public Expr array() { return args.get(0); }

  public Expr value() { return args.get(1); }

  public List<Expr> index() { return args.subList(2, args.size()); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}