package dexter.ir.array;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class SelectExpr extends CallExpr
{
  public SelectExpr (Expr array, List<Expr> indices)
  {
    super("select", new ArrayList<Expr>() {{ add(array); addAll(indices); }});
  }

  public Expr array() { return args.get(0); }

  public List<Expr> index() { return args.subList(1, args.size()); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}