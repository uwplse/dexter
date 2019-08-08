package dexter.modifier;

import dexter.ir.BaseVisitor;
import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.array.StoreExpr;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.VarExpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VCWritesUpdator extends Transformer {
  List<FuncDecl> fnDecls;
  Set<VarExpr> outVars;
  Expr ui_expr;

  public VCWritesUpdator(List<FuncDecl> fns, Set<VarExpr> ov, Expr e) {
    super();
    this.fnDecls = fns;
    this.outVars = ov;
    this.ui_expr = e;
  }

  @Override
  public Expr transform (CallExpr e)
  {
    // Find function declaration
    FuncDecl fnDecl = null;
    for (FuncDecl d : fnDecls)
      if (e.name().equals(d.name()))
        fnDecl = d;

    // Only update the function calls to the invariants
    // TODO: Check if it should be invariants + pc?
    if (e.name().matches("inv[0-9]+"))
    {
      if (fnDecl == null)
        throw new RuntimeException("Function Declaration not found");

      List<Expr> newArgs = new ArrayList<>();

      for (int i=0; i<e.args().size(); i++)
      {
        VarExpr param = fnDecl.params().get(i);
        Expr arg = e.args().get(i);

        ContainsStore cs = new ContainsStore();
        arg.accept(cs);

        // Only update writes to outArrVars
        if (outVars.contains(param) && cs.containsStore)
          newArgs.add(ui_expr);
        else
          newArgs.add(arg);
      }

      return new CallExpr(e.name(), newArgs);
    }

    return null;
  }

  private class ContainsStore extends BaseVisitor {
    boolean containsStore = false;
    @Override
    public void enter (StoreExpr e)
    {
      this.containsStore = true;
    }

    @Override
    public void enter (CallExpr e)
    {
      if (e.name().equals("HBuffer_Set"))
        this.containsStore = true;
    }
  }
}