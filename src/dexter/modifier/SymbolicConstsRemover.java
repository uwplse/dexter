package dexter.modifier;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.LetExpr;
import dexter.ir.bool.VarExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.type.TypesFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SymbolicConstsRemover extends Transformer {
  List<FuncDecl> functions;

  public SymbolicConstsRemover(List<FuncDecl> fns) {
    this.functions = fns;
  }

  // Necessary
  @Override
  public Expr transform (VarExpr e)
  {
    if (e.type() == TypesFactory.Int && e.name().matches("Const[0-9]+"))
      return new IntLitExpr(Integer.parseInt(e.name().substring(5)));

    return null;
  }

  // Transformations below are not necessary but nice to do

  @Override
  public Expr transform (CallExpr e)
  {
    FuncDecl fnDecl = null;
    for (FuncDecl fn : this.functions)
      if (fn.name().equals(e.name()))
        fnDecl = fn;

    if (TypesFactory.isClassT(e.name()))
      return new CallExpr(e.name(), e.args());

    if (fnDecl == null)
      throw new RuntimeException("Function declaration not found: " + e.name());

    List<Expr> args = new ArrayList<>();
    for (int i=0; i<e.args().size(); i++) {
      Expr arg = e.args().get(i);
      Expr param = fnDecl.params().get(i);
      if (!(((VarExpr) param).name().matches("Const[0-9]+")))
        args.add(arg.accept(this));
    }

    return new CallExpr(e.name(), args);
  }

  @Override
  public Expr transform (FuncDecl e)
  {
    return new FuncDecl(e.name(), e.params().stream().filter(param -> !param.name().matches("Const[0-9]+")).collect(Collectors.toList()), e.retType(), (e.isUnInterpreted() ? null : e.body().accept(this)), e.isGenerator());
  }

  @Override
  public Expr transform (LetExpr e)
  {
    return new LetExpr(e.vars().stream().filter(param -> !param.name().matches("Const[0-9]+")).collect(Collectors.toSet()), e.body().accept(this), (e.assumptions() == null ? null : e.assumptions().accept(this)));
  }
}