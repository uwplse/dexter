package dexter.analysis;

import dexter.ir.BaseVisitor;
import dexter.ir.Expr;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.VarExpr;
import scala.Tuple2;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class VCAnalyzer extends BaseVisitor {
  List<FuncDecl> fnDecls;
  VarExpr outVar;

  Set<Expr> expresions;
  Set<Expr> terms;
  Set<Tuple2<Expr, List<Expr>>> indexes;

  public VCAnalyzer(List<FuncDecl> fns, VarExpr v) {
    super();
    this.fnDecls = fns;
    this.outVar = v;
    this.expresions = new LinkedHashSet<>();
    this.terms = new LinkedHashSet<>();
    this.indexes = new LinkedHashSet<>();
  }

  public Set<Expr> getStencilExprs () { return expresions; }
  public Set<Expr> getStencilTerms () { return terms; }
  public Set<Tuple2<Expr, List<Expr>>> getStencilWriteIndexes () { return indexes; }

  @Override
  public void enter (CallExpr e)
  {
    // Find function declaration
    FuncDecl fnDecl = null;
    for (FuncDecl d : fnDecls)
      if (e.name().equals(d.name()))
        fnDecl = d;

    if (e.name().matches("inv[0-9]+"))
    {
      if (fnDecl == null)
        throw new RuntimeException("Function Declaration not found:" + e.name());

      for (int i=0; i<e.args().size(); i++)
      {
        VarExpr param = fnDecl.params().get(i);
        Expr arg = e.args().get(i);

        if (param.equals(outVar) && !(arg instanceof  VarExpr))
        {
          ExtractTerminals et = new ExtractTerminals();
          ExtractWriteIndexes ewi = new ExtractWriteIndexes(param);
          ExtractHeuristics eh = new ExtractHeuristics(param);

          arg.accept(et);
          arg.accept(ewi);

          this.expresions.add(arg.accept(eh));
          this.terms.addAll(et.terminals());
          this.indexes.addAll(ewi.idxs());
        }
      }
    }
  }
}