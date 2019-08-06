package dexter.ir.array;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Arrays;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class PtrExpr extends CallExpr
{
  public PtrExpr(Expr data, Expr offset)
  {
    super("ptr", Arrays.asList(data, offset));
  }

  public Expr data () { return this.args.get(0); }
  public Expr offset  () { return this.args.get(1); }

  @Override public <T> T accept(Visitor<T> p) { return p.visit(this); }
}