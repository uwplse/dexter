package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.TypesFactory;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class UnaryExpr extends Expr {

  public static final class Op
  {
    private String symbol;

    private Op(String symbol) { this.symbol = symbol; }

    public static final Op NOT = new Op("!");
    public static final Op BNOT = new Op("~");
    public static final Op MINUS = new Op("-");
    public static final Op PLUS = new Op("+");

    @Override public String toString () { return symbol; }
  }

  protected Op op;
  protected Expr base;

  public UnaryExpr(Op op, Expr base)
  {
    this.op = op;
    this.base = base;

    if (op == UnaryExpr.Op.NOT) { this.type = TypesFactory.Bool; }
    else if (op == UnaryExpr.Op.BNOT) { this.type = base.type(); }
    else if (op == UnaryExpr.Op.PLUS) { this.type = base.type(); }
    else if (op == UnaryExpr.Op.MINUS) { this.type = base.type(); }
  }

  public Op op () { return op; }
  public Expr base () { return base; }

  @Override
  public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof UnaryExpr)
      return ((UnaryExpr)o).op.equals(op) && ((UnaryExpr)o).base.equals(base);
    else
      return false;
  }
}