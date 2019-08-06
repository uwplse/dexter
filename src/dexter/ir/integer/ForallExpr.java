package dexter.ir.integer;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.VarExpr;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ForallExpr extends Expr
{
  protected VarExpr var;
  protected Expr start;
  protected Expr end;
  protected Expr body;

  public ForallExpr(VarExpr var, Expr start, Expr end, Expr body)
  {
    this.var = var;
    this.start = start;
    this.end = end;
    this.body = body;
  }

  public VarExpr var () { return var; }
  public Expr start () { return start; }
  public Expr end () { return end; }
  public Expr body () { return body; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof ForallExpr)
      return ((ForallExpr)o).var.equals(var) &&
              ((ForallExpr)o).start.equals(start) &&
              ((ForallExpr)o).end.equals(end) &&
              ((ForallExpr)o).body.equals(body);
    else
      return false;
  }
}