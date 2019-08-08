package dexter.analysis;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExtractLiveFunctionSet extends Transformer {
  protected Set<FuncDecl> liveFns;
  protected List<FuncDecl> fnDecls;

  public Set<FuncDecl> liveFns () { return liveFns; }

  public ExtractLiveFunctionSet(List<FuncDecl> functions) {
    this.liveFns = new HashSet<>();
    this.fnDecls = functions;
  }

  @Override
  public Expr transform (CallExpr e) {
    if (TypesFactory.isClassT(e.name()))
      return null;

    for (FuncDecl fnDecl : fnDecls)
      if (fnDecl.name().equals(e.name())) {
        this.liveFns.add(fnDecl);

        if (!fnDecl.isUnInterpreted())
          fnDecl.body().accept(this);

        return null;
      }

    if (e.name().matches("int_expr_grm|float_expr_grm"))
      return null;

    throw new RuntimeException("Function declaration not found: " + e.name());
  }
}