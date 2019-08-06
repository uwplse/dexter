package dexter.ir.integer;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.Type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class UIExpr extends Expr {

  public UIExpr(Type t) {
    this.type = t;
  }

  @Override
  public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}