package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.TypesFactory;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class BinaryExpr extends Expr
{
  public static final class Op
  {
    private String symbol;

    private Op(String symbol) { this.symbol = symbol; }

    public static final Op AND = new Op("&&");
    public static final Op OR = new Op("||");
    public static final Op EQ = new Op("=");
    public static final Op NEQ = new Op("!=");

    public static final Op LT = new Op("<");
    public static final Op LE = new Op("<=");
    public static final Op GE = new Op(">=");
    public static final Op GT = new Op(">");

    public static final Op PLUS = new Op("+");
    public static final Op MINUS = new Op("-");
    public static final Op MULT = new Op("*");
    public static final Op DIV = new Op("/");
    public static final Op MOD = new Op("%");

    public static final Op SHL = new Op("<<");
    public static final Op LSHR = new Op(">>");
    public static final Op ASHR = new Op(">>>");
    public static final Op BAND = new Op("&");
    public static final Op BOR = new Op("|");
    public static final Op BXOR = new Op("^");

    public static final Op IMPLY = new Op("-->");

    @Override public String toString () { return symbol; }
  }

  protected Expr left;
  protected Op op;
  protected Expr right;

  public BinaryExpr(Expr left, Op op, Expr right)
  {
    this.left = left;
    this.op = op;
    this.right = right;

    if (op == BinaryExpr.Op.AND) { this.type = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.OR) { this.type = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.EQ) { this.type = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.NEQ) { this.type = TypesFactory.Bool; }

    else if (op == BinaryExpr.Op.LT) { this.type = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.LE) { this.type = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.GE) { this.type = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.GT) { this.type = TypesFactory.Bool; }

    else if (op == BinaryExpr.Op.IMPLY) { this.type = TypesFactory.Bool; }

    // left and right should have the same type now
    else if (op == BinaryExpr.Op.PLUS) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.MINUS) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.MULT) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.DIV) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.MOD) { this.type = left().type(); }

    else if (op == BinaryExpr.Op.SHL) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.LSHR) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.ASHR) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.BAND) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.BOR) { this.type = left().type(); }
    else if (op == BinaryExpr.Op.BXOR) { this.type = left().type(); }
  }

  public Expr left () { return left; }
  public Op op () { return op; }
  public Expr right () { return right; }


  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof BinaryExpr)
      return ((BinaryExpr)o).left.equals(left) &&
              ((BinaryExpr)o).op.equals(op) &&
              ((BinaryExpr)o).right.equals(right);
    else
      return false;
  }
}