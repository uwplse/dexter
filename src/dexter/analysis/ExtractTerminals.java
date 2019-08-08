package dexter.analysis;

import dexter.ir.BaseVisitor;
import dexter.ir.Expr;
import dexter.ir.Printer;
import dexter.ir.array.*;
import dexter.ir.bool.BoolLitExpr;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FieldExpr;
import dexter.ir.bool.VarExpr;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.IntLitExpr;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Maaz Ahmad on 8/7/19.
 */
public class ExtractTerminals extends BaseVisitor {
  Set<Expr> terminals;

  public ExtractTerminals() {
    this.terminals = new LinkedHashSet<>();
  }

  public Set<Expr> terminals() { return terminals; }

  @Override
  public Expr visit(StoreExpr n)
  {
    if (n.array() instanceof StoreExpr)
      n.array().accept(this);
    n.value().accept(this);
    return n;
  }

  @Override
  public Expr visit (SelectExpr n)
  {
    if (
      n.array() instanceof VarExpr ||
      n.array() instanceof FieldExpr ||
      n.array() instanceof IncrPtrExpr ||
      n.array() instanceof PtrExpr
    )
      terminals.add(n);
    else if (n.array() instanceof StoreExpr) {
      terminals.add(new SelectExpr(getArray(n.array()), n.index()));
      n.array().accept(this);
    }
    else
      throw new RuntimeException("Unexpected array expression: " + n.accept(new Printer()));

    return n;
  }

  @Override
  public Expr visit (CallExpr n)
  {
    if (n.name().equals("HBuffer_Get"))
      terminals.add(n);
    else if (n.name().equals("HBuffer_Set"))
      n.args().get(n.args().size()-1).accept(this);
    else
      super.visit(n);

    return n;
  }

  @Override
  public void enter (VarExpr e)
  {
    terminals.add(e);
  }

  @Override
  public void enter (IntLitExpr e)
  {
    terminals.add(e);
  }

  @Override
  public void enter (FloatLitExpr e)
  {
    terminals.add(e);
  }

  @Override
  public void enter (BoolLitExpr e)
  {
    terminals.add(e);
  }

  private Expr getArray(Expr e)
  {
    if (e instanceof  VarExpr)
      return e;
    else if (e instanceof  IncrPtrExpr || e instanceof DecrPtrExpr)
      return e;
    else if (e instanceof StoreExpr)
      return getArray(((StoreExpr) e).array());
    else
      throw new RuntimeException("Unexpected array expression: " + e.accept(new Printer()));
  }
}