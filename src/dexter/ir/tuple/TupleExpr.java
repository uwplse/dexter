package dexter.ir.tuple;

import dexter.ir.Expr;
import dexter.ir.Visitor;

import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class TupleExpr extends Expr {
  List<Expr> vals;

  public TupleExpr(List<Expr> v) {
    this.vals = v;
  }

  public List<Expr> vals() { return this.vals; }

  @Override
  public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}