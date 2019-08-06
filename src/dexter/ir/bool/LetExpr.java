package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class LetExpr extends Expr
{
  protected Set<VarExpr> vars;
  protected Expr body;
  protected Expr assumptions;

  public LetExpr(Set<VarExpr> vars, Expr body)
  {
    this.vars = new HashSet<>(vars);
    this.body = body;
    this.assumptions = null;
  }

  public LetExpr(Set<VarExpr> vars, Expr body, Expr assumptions) {
    this.vars = new HashSet<>(vars);
    this.body = body;
    this.assumptions = assumptions;
  }

  public Set<VarExpr> vars () { return vars; }
  public Expr body () { return body; }
  public Expr assumptions () { return assumptions; }

  public void vars (Set<VarExpr> vs) { this.vars = vs; }
  public void body (Expr b) { this.body = b; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof LetExpr)
      return ((LetExpr)o).vars.equals(this.vars) &&
              ((LetExpr)o).body.equals(body) &&
              (((LetExpr) o).assumptions == null ? this.assumptions == null : ((LetExpr) o).assumptions.equals(this.assumptions) && this.assumptions != null);
    else
      return false;
  }
}