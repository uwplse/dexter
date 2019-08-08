package dexter.analysis;

import dexter.ir.BaseVisitor;
import dexter.ir.Expr;
import dexter.ir.Printer;
import dexter.ir.Transformer;
import dexter.ir.array.*;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FieldExpr;
import dexter.ir.bool.IfExpr;
import dexter.ir.bool.VarExpr;
import dexter.ir.type.ArrayT;
import dexter.ir.type.ClassT;
import dexter.ir.type.PtrT;

/**
 * Created by Maaz Ahmad on 8/7/19.
 */
public class ExtractHeuristics extends Transformer {
  VarExpr target;

  public ExtractHeuristics (VarExpr t) {
    this.target = t;
  }

  @Override
  public Expr transform (IncrPtrExpr e) {
    return e.ptr().accept(this);
  }

  @Override
  public Expr transform (DecrPtrExpr e) {
    return e.ptr().accept(this);
  }

  @Override
  public Expr transform (PtrExpr e) {
    if (e.data() instanceof FieldExpr)
      return ((FieldExpr) e.data()).target().accept(this);
    else if (e.data() instanceof StoreExpr)
      return e.data().accept(this);
    else
      throw new RuntimeException("NYI");
  }

  @Override
  public Expr transform (IfExpr e) {
    Expr cons = e.cons().accept(this);
    Expr altr = e.alt().accept(this);

    if (cons.equals(altr))
      return cons;
    else
      return new IfExpr(e.cond(), cons, altr);
  }

  @Override
  public Expr transform (StoreExpr e)
  {
    if (getArray(e.array()).equals(target)) {
      return e.value().accept(new FixStores());
    }
    else if (getArray(e.array()) instanceof IncrPtrExpr) {
      return e.value().accept(new FixStores());
    }

    throw new RuntimeException("NYI. Heuristic generator found unexpected expression: " + e);
  }

  @Override
  public Expr transform (CallExpr e)
  {
    if (e.name().equals("HBuffer_Set")) {
      if (getArray(e.args().get(0)).equals(target)) {
        return e.args().get(e.args().size()-1).accept(new FixStores());
      }

      throw new RuntimeException("NYI. Heuristic generator found unexpected expression: " + e);
    }
    else
      return super.transform(e);
  }

  @Override
  public Expr transform (VarExpr e)
  {
    if (e.equals(target)) {
      if (target.type() instanceof PtrT)
        return new VarExpr("v1", ((PtrT) target.type()).elemT());
      else if (target.type() instanceof ArrayT)
        return new VarExpr("v1", ((ArrayT) target.type()).elemT());
      else if (target.type() instanceof ClassT) {
        ClassT clsT = (ClassT) target.type();
        if (clsT.name().equals("HBuffer"))
          return new VarExpr("v1", ((ArrayT) clsT.fields().get(0).type()).elemT());
        else
          throw new RuntimeException("NYI. Heuristic generator found unexpected expression: " + e);
      }
      else
        throw new RuntimeException("NYI. Heuristic generator found unexpected expression: " + e);
    }
    else
      throw new RuntimeException("NYI. Heuristic generator found unexpected expression: " + e);
  }

  class FixStores extends Transformer {
    @Override
    public Expr transform (SelectExpr e) {
      if (e.array() instanceof StoreExpr)
        return ((StoreExpr) e.array()).value();
      return null;
    }
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