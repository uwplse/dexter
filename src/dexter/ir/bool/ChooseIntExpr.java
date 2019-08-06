package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class ChooseIntExpr extends Expr {

  protected int id;
  protected int width;

  public ChooseIntExpr(int id, int width) {
    this.id = id;
    this.width = width;
  }

  public int getId() { return id; }
  public void setId(int i) { this.id = i; }

  public int getWidth() { return width; }

  @Override
  public <T> T accept(Visitor<T> p) {
    return p.visit(this);
  }
}