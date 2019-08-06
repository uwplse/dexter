package dexter.ir.semantics;

import dexter.ir.*;
import dexter.ir.array.IncrPtrExpr;
import dexter.ir.array.PtrExpr;
import dexter.ir.array.SelectExpr;
import dexter.ir.bool.*;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class VCGenerator
{
  protected Expr vc;
  protected Expr prior;
  protected Expr assumptions;
  protected List<FuncDecl> decls;
  protected Map<Expr,Expr> replaceSet;
  protected Map<VarExpr, Pair<Expr,Expr>> bounds;

  protected static int invariantNum = 0;

  public VCGenerator ()
  {
    this.decls = new ArrayList<>();
    this.replaceSet = new HashMap<>();
    this.bounds = new HashMap<>();
    this.vc = null;
    this.assumptions = null;
    invariantNum = 0;
  }

  public VCGenerator (VCGenerator other)
  {
    this.decls = new ArrayList<>(other.decls);
    this.vc = other.vc;
    this.assumptions = other.assumptions;
    this.replaceSet = other.replaceSet;
  }

  public Expr vc () { return vc; }
  public void vc (Expr vc) { this.vc = vc; }

  public void mergeDecls (VCGenerator other)
  {
    for (FuncDecl d : other.decls)
    {
      if (!this.decls.contains(d))
        this.decls.add(d);
    }
  }

  public void addAssumption (Expr e)
  {
    if (this.assumptions == null)
      this.assumptions = e;
    else
      this.assumptions = new BinaryExpr(this.assumptions, BinaryExpr.Op.AND, e);
  }

  public void registerBounds (VarExpr v, Expr w, Expr h)
  {
    this.bounds.put(v, new Pair<>(w, h));
  }

  public void mergePrior(VCGenerator other)
  {
    if (this.prior == null)
      this.prior = other.prior;
    else if (other.prior != null)
      this.prior = new BinaryExpr(this.prior, BinaryExpr.Op.AND, other.prior);
  }

  /**
   * Reset invariant count. Only used for JUnit tests.
   */
  public static void resetInvariantNum () { invariantNum = 0; }

  public Program createProgram (Set<VarExpr> vars, Set<VarExpr> outVars)
  {
    // outvar_init == outvar before the loop
    for (VarExpr outVar : outVars)
      this.replace(new VarExpr(outVar.name()+"_init", outVar.type()), outVar);

    Set<VarExpr> allVars = new HashSet<>(vars);
    for (VarExpr outVar : outVars)
      allVars.add(new VarExpr(outVar.name()+"_init", outVar.type()));

    LetExpr le = (this.prior == null) ? new LetExpr(allVars, this.vc, this.assumptions) :
                  new LetExpr(allVars, new BinaryExpr(this.prior, BinaryExpr.Op.AND, this.vc), this.assumptions);

    return new Program(decls, Collections.emptyList(), le);
  }

  public void pc (List<VarExpr> vars, Set<VarExpr> outVars)
  {
    if (vc != null)
      throw new RuntimeException("current VC is non-null: " + vc);

    List<VarExpr> args = new ArrayList<VarExpr>(vars);
    for (VarExpr outVar : outVars)
      args.add(new VarExpr(outVar.name()+"_init", outVar.type()));

    this.vc = new CallExpr("pc", args);
    this.decls.add(new FuncDecl("pc", args, TypesFactory.Bool, new BoolLitExpr(true)));
  }

  public void conditional (Expr cond, VCGenerator consequentVC, VCGenerator altVC)
  {
    // merge the decls
    for (FuncDecl d : consequentVC.decls)
    {
      if (!this.decls.contains(d))
        this.decls.add(d);
    }

    for (FuncDecl d : altVC.decls)
    {
      if (!this.decls.contains(d))
        this.decls.add(d);
    }

    // Merge priors
    mergePrior(consequentVC);
    mergePrior(altVC);

    // For c++ support
    Type condT = cond.type();
    if (condT == TypesFactory.Int) {
      cond = new BinaryExpr(cond, BinaryExpr.Op.NEQ, new IntLitExpr(0));
    }

    // Testing:
    if (
        consequentVC.vc instanceof CallExpr &&
        altVC.vc instanceof CallExpr &&
        ((CallExpr) consequentVC.vc).name().equals(((CallExpr) altVC.vc).name())
    ) {
      List<Expr> consArgs = ((CallExpr) consequentVC.vc).args();
      List<Expr> altArgs = ((CallExpr) altVC.vc).args();
      List<Expr> newArgs = new ArrayList<>();
      for (int i=0; i< consArgs.size(); i++) {
        if (!consArgs.get(i).equals(altArgs.get(i))){
          newArgs.add(new IfExpr(cond, consArgs.get(i), altArgs.get(i)));
        }
        else {
          newArgs.add(consArgs.get(i));
        }
      }

      this.vc = new CallExpr(((CallExpr) consequentVC.vc).name(), newArgs);
    }
    else
     this.vc = new IfExpr(cond, consequentVC.vc, altVC.vc);
  }

  public CallExpr invariant (List<VarExpr> vars, Set<VarExpr> outVars)
  {
    String invName = "inv" + invariantNum++;

    List<VarExpr> args = new ArrayList<VarExpr>(vars);
    for (VarExpr outVar : outVars)
      if (vars.contains(outVar))
        args.add(new VarExpr(outVar.name()+"_init", outVar.type()));

    CallExpr inv = new CallExpr(invName, args);
    this.decls.add(new FuncDecl(invName, args, TypesFactory.Bool, new BoolLitExpr(true)));

    return inv;
  }

  public void loop(Expr cond, Expr invariant, Expr continuation, Set<VarExpr> outputVars)
  {
    // hack for in-place updates in dexter
    continuation = continuation.accept(new FixArrayReads(outputVars));

    // !cond && invariant -> vc => !(!cond && invariant) || vc
    Expr pred = new BinaryExpr(invariant, BinaryExpr.Op.AND, new UnaryExpr(UnaryExpr.Op.NOT, cond));
    Expr termination = new BinaryExpr(pred, BinaryExpr.Op.IMPLY, this.vc);

    // cond && invariant -> continuation => !(cond && invariant) || continuation
    pred = new BinaryExpr(invariant, BinaryExpr.Op.AND, cond);
    Expr cont = new BinaryExpr(pred, BinaryExpr.Op.IMPLY, continuation);

    // true -> invariant
    this.prior = (this.prior == null) ?
      new BinaryExpr(cont, BinaryExpr.Op.AND, termination) :
      new BinaryExpr(this.prior, BinaryExpr.Op.AND, new BinaryExpr(cont, BinaryExpr.Op.AND, termination));

    this.vc = invariant;
  }

  public void ploop(Expr cond, Expr invariant, Expr continuation, Set<VarExpr> outputVars, Expr outerInv)
  {
    // !cond && outerInv && invariant -> vc => !(!cond && invariant) || vc
    Expr invs = (outerInv == null ? invariant : new BinaryExpr(invariant, BinaryExpr.Op.AND, outerInv));
    Expr pred = new UnaryExpr(UnaryExpr.Op.NOT, new BinaryExpr(invs, BinaryExpr.Op.AND, new UnaryExpr(UnaryExpr.Op.NOT, cond)));
    Expr termination = new BinaryExpr(pred, BinaryExpr.Op.OR, this.vc);

    // cond && invariant -> continuation => !(cond && invariant) || continuation
    pred = new UnaryExpr(UnaryExpr.Op.NOT, new BinaryExpr(invariant, BinaryExpr.Op.AND, cond));
    Expr cont = new BinaryExpr(pred, BinaryExpr.Op.OR, continuation);

    // true -> invariant
    this.prior = (this.prior == null) ?
            new BinaryExpr(cont, BinaryExpr.Op.AND, termination) :
            new BinaryExpr(this.prior, BinaryExpr.Op.AND, new BinaryExpr(cont, BinaryExpr.Op.AND, termination));

    this.vc = invariant;

    // outvar_init == outvar before the loop
    for (VarExpr outVar : outputVars)
      this.replace(new VarExpr(outVar.name()+"_init", outVar.type()), outVar);
  }

  public void replace (Expr from, Expr to)
  {
    this.vc = this.vc.accept(new Substitutor(from, to));
  }

  // Below: Hack for function inlining
  public void queueReplace (Expr from, Expr to)
  {
    this.replaceSet.put(from, to);
  }

  public void runQueue ()
  {
    this.vc = this.vc.accept(new Substitutor(this.replaceSet));
    this.replaceSet.clear();
  }

  public class FixArrayReads extends Transformer {
    Set<VarExpr> outputVars;

    public FixArrayReads(Set<VarExpr> outputVars) {
      this.outputVars = new HashSet<>(outputVars);
    }

    @Override
    public Expr transform (SelectExpr e)
    {
      if (e.array() instanceof VarExpr)
      {
        if (!this.outputVars.contains(e.array()))
          return null;
        else
        {
          if (TypesFactory.isPtrT(e.array().type())) {
            FieldExpr arr = new FieldExpr(new VarExpr(e.array().accept(new Printer()) + "_init", e.array().type()), "data");
            FieldExpr offset = new FieldExpr(e.array(), "offset");
            return new SelectExpr(new PtrExpr(arr, offset), e.index().stream().map(idx -> idx.accept(this)).collect(Collectors.toList()));
          }
          else
            return new SelectExpr(new VarExpr(e.array().accept(new Printer()) + "_init", e.array().type()), e.index().stream().map(idx -> idx.accept(this)).collect(Collectors.toList()));
        }
      }
      else if (e.array() instanceof IncrPtrExpr)
      {
        Expr ptr = ((IncrPtrExpr) e.array()).ptr();
        Expr incr = ((IncrPtrExpr) e.array()).incr();

        assert (ptr instanceof VarExpr);

        if (!this.outputVars.contains(ptr))
          return null;
        else
        {
          FieldExpr arr = new FieldExpr(new VarExpr(ptr.accept(new Printer()) + "_init", ptr.type()), "data");
          FieldExpr offset = new FieldExpr(ptr, "offset");
          return new SelectExpr(new IncrPtrExpr(new PtrExpr(arr, offset), incr), e.index().stream().map(idx -> idx.accept(this)).collect(Collectors.toList()));
        }
      }
      else {
        return new SelectExpr(e.array().accept(this), e.index().stream().map(idx -> idx.accept(this)).collect(Collectors.toList()));
      }
    }
  }
}