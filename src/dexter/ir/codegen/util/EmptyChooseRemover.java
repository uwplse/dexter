package dexter.ir.codegen.util;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.ChooseExpr;

import java.util.ArrayList;
import java.util.List;

public class EmptyChooseRemover extends Transformer {

  @Override
  public Expr visit(ChooseExpr e) {
    // Flatten all nested choose exprs
    List<Expr> updatedArgs = new ArrayList();
    for (Expr arg : e.args())
    {
      if (arg instanceof ChooseExpr && ((ChooseExpr) arg).args().isEmpty())
        // Ignore empty nested choose expr
        ;
      else if (arg instanceof ChooseExpr)
        // Inline non-empty nested choose
        updatedArgs.addAll(((ChooseExpr) arg.accept(this)).args());
      else
        updatedArgs.add(arg.accept(this));
    }
    return new ChooseExpr(updatedArgs, e.getId());
  }
}