package dexter.ir.list;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.VarExpr;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ListCompExpr extends Expr
{
  protected Expr e;
  protected List<Pair<VarExpr, Expr>> iters;

  public ListCompExpr (Expr e, List<Pair<VarExpr, Expr>> iters)
  {
    this.e = e;
    this.iters = iters;
  }

  public Expr expr () { return e; }
  public List<Pair<VarExpr, Expr>> iters () { return iters; }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}