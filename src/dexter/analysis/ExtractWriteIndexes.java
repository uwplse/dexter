package dexter.analysis;

import dexter.ir.BaseVisitor;
import dexter.ir.Expr;
import dexter.ir.Printer;
import dexter.ir.array.DecrPtrExpr;
import dexter.ir.array.IncrPtrExpr;
import dexter.ir.array.StoreExpr;
import dexter.ir.bool.BinaryExpr;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.VarExpr;
import dexter.ir.integer.IntLitExpr;
import scala.Tuple2;

import java.util.*;

/**
 * Created by Maaz Ahmad on 8/7/19.
 */
public class ExtractWriteIndexes extends BaseVisitor {
  VarExpr target;
  Set<Tuple2<Expr, List<Expr>>> idxs;

  Expr ptrOp;

  public ExtractWriteIndexes(VarExpr t)
  {
    super();
    this.target = t;
    this.ptrOp = null;
    this.idxs = new LinkedHashSet<>();
  }

  public Set<Tuple2<Expr, List<Expr>>> idxs() { return idxs; }

  @Override
  public void enter (StoreExpr e)
  {
    if (getArray(e.array()).equals(target)) {
      idxs.add(new Tuple2<>(this.ptrOp, e.index()));
      this.ptrOp = null;
    }
    else if (getArray(e.array()) instanceof IncrPtrExpr) {
      idxs.add(new Tuple2<>(this.ptrOp, Arrays.asList(new BinaryExpr(((IncrPtrExpr) getArray(e.array())).incr(), BinaryExpr.Op.PLUS, e.index().get(0)))));
      this.ptrOp = null;
    }
  }

  @Override
  public void enter (CallExpr e)
  {
    if (e.name().equals("HBuffer_Set") && e.args().get(0).equals(target)) {
      List<Expr> idx = new ArrayList<>();
      for (int i=1; i<e.args().size()-1; ++i)
        idx.add(e.args().get(i));
      idxs.add(new Tuple2<>(null, idx));
    }
  }

  @Override
  public Expr visit (IncrPtrExpr e)
  {
    Expr oldOp = this.ptrOp;
    this.ptrOp = new IncrPtrExpr(new IntLitExpr(0), e.incr());
    Expr r = new IncrPtrExpr(e.ptr().accept(this), e.incr().accept(this));
    this.ptrOp = oldOp;
    return r;
  }

  @Override
  public Expr visit (DecrPtrExpr e) {
    Expr oldOp = this.ptrOp;
    this.ptrOp = new DecrPtrExpr(new IntLitExpr(0), e.decr());
    Expr r = new DecrPtrExpr(e.ptr().accept(this), e.decr().accept(this));
    this.ptrOp = oldOp;
    return r;
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