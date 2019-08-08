package dexter.ir.codegen.util;

/**
 * Created by akcheung on 8/30/17.
 */

import dexter.ir.Expr;
import dexter.ir.Util;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.VarExpr;
import dexter.ir.list.ListCompExpr;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

/**
 * custom expression lifter that lifts both the expr in list comprehension (if it is not already lifted)
 * and the list comprehension expression itself. Example:
 *
 * (x+k) (for x in append(l1, l2)) --> f(x, k) (for x in g(l1, l2))
 */
public class ListCompExprLifter extends ExprLifter
{
  public ListCompExprLifter (List<FuncDecl> fns)
  {
    super((Expr e) -> false);
  }

  @Override
  public Expr visit(ListCompExpr e)
  {
    Expr expr = e.expr();
    if (!(expr instanceof CallExpr))
      expr = lift(e.expr());

    e = (ListCompExpr)new ListCompExpr(expr,
            Util.map(e.iters(), p -> new Pair<>((VarExpr)p.a.accept(this), p.b.accept(this))))
            .type(e.type());
    return lift(e);
  }
}