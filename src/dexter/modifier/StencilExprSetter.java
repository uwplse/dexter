package dexter.modifier;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.VarExpr;
import dexter.ir.type.TypesFactory;

import java.util.ArrayList;
import java.util.List;

public class StencilExprSetter extends Transformer {
  protected Expr stencil_expr;
  protected List<FuncDecl> fnDecls;

  public StencilExprSetter(List<FuncDecl> functions, Expr e) {
    this.stencil_expr = e;
    this.fnDecls = functions;
  }

  @Override
  public Expr transform (CallExpr e)
  {
    List<Expr> idxGenParams = new ArrayList<>();
    idxGenParams.add(new VarExpr("x", TypesFactory.Int));
    idxGenParams.add(new VarExpr("y", TypesFactory.Int));
    idxGenParams.add(new CallExpr("width_gen", new ArrayList<>()));

    List<Expr> termsGenParams = new ArrayList<>();
    termsGenParams.add(new CallExpr("idx_gen", idxGenParams));

    if (e.name().equals("int_expr") || e.name().equals("float_expr"))
      return stencil_expr;
    else
    {
      for (FuncDecl fn : fnDecls)
        if (fn.name().equals(e.name()))
          fn.body(fn.body().accept(this));
      return null;
    }
  }
}