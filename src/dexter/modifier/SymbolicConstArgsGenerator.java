package dexter.modifier;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.LetExpr;
import dexter.ir.bool.VarExpr;
import dexter.ir.type.TypesFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SymbolicConstArgsGenerator extends Transformer {
  List<FuncDecl> fnDecls;
  Set<VarExpr> vars;

  public SymbolicConstArgsGenerator(List<FuncDecl> functions) {
    fnDecls = functions;
  }

  @Override
  public Expr transform (CallExpr e) {
    //if (e.name().equals("pc") || e.name().matches("inv[0-9]+")) {
    FuncDecl fnDecl = null;
    for (FuncDecl fn : fnDecls)
      if (fn.name().equals(e.name()))
        fnDecl = fn;

    if (TypesFactory.isClassT(e.name()))
      return new CallExpr(e.name(), e.args().stream().map(arg -> arg.accept(this)).collect(Collectors.toList()));

    if (fnDecl == null)
      throw new RuntimeException("Function Declaration not found");

    List<Expr> newArgs = new ArrayList<>(e.args());
    for (int i=e.args().size(); i<fnDecl.params().size(); i++) {
      newArgs.add(fnDecl.params().get(i));
      vars.add(fnDecl.params().get(i));
    }
    //}

    return new CallExpr(e.name(), newArgs.stream().map(arg -> arg.accept(this)).collect(Collectors.toList()));
  }

  @Override
  public Expr transform (LetExpr e) {
    vars = new HashSet<>();

    Expr body = e.body().accept(this);
    Expr assumptions = (e.assumptions() == null ? null : e.assumptions().accept(this));

    Set<VarExpr> newVars = new HashSet<>(e.vars());
    newVars.addAll(vars);
    return new LetExpr(newVars, body, assumptions).type(e.type());
  }
}