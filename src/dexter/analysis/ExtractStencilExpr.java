package dexter.analysis;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;

import java.util.List;

public class ExtractStencilExpr extends Transformer {
  protected List<FuncDecl> fnDecls;
  protected Expr expr;

  public ExtractStencilExpr(List<FuncDecl> functions) {
    this.fnDecls = functions;
  }

  public Expr getExpr () { return expr; }

  @Override
  public Expr transform (CallExpr e)
  {
    if (e.name().equals("int_expr"))
      expr = e;
    else if (e.name().equals("float_expr"))
      expr = e;
    else
    {
      for (FuncDecl fn : fnDecls)
        if (fn.name().equals(e.name()))
          fn.body(fn.body().accept(this));
    }

    return null;
  }
}