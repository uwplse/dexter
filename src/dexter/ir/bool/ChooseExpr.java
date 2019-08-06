package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ChooseExpr extends CallExpr
{
  protected int id;

  public int getId () { return id; }
  public void setId(int v) { this.id = v; }

  public ChooseExpr (List<Expr> args)
  {
    super("choose", args);
    this.id = 0;
  }

  public ChooseExpr(List<Expr> args, int id) {
    super("choose", args);
    this.id = id;
  }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}