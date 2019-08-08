package dexter.ir.codegen.util;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.array.*;
import dexter.ir.bool.*;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.ForallExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.integer.UIExpr;
import dexter.ir.list.*;
import dexter.ir.macro.*;
import dexter.ir.semantics.VarCounter;
import dexter.ir.tuple.TupleExpr;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static dexter.ir.Util.map;

/**
 * Created by akcheung on 4/22/17.
 *
 * Lift a ML expression into a function
 */
public class ExprLifter implements Visitor<Expr>
{
  protected List<FuncDecl> newDecls;
  protected List<FuncDecl> existingDecls;
  protected List<ClassDecl> classes;
  protected Function<Expr, Boolean> shouldLift;

  protected int callNum = 0;

  public ExprLifter (Function<Expr, Boolean> shouldLift)
  {
    this.existingDecls = Collections.emptyList();
    this.classes = Collections.emptyList();
    this.newDecls = new ArrayList<>();
    this.shouldLift = shouldLift;
  }

  protected String fnName ()
  {
    return "lift_fn" + (callNum++);
  }

  // x & y --> fn(x, y)
  protected CallExpr lift (Expr e)
  {
    // convert to list in case multiple iterators of the same set return in different order
    List<FuncDecl> decls = new ArrayList<>(existingDecls);
    decls.addAll(this.newDecls);
    List<VarExpr> varsUsed = new ArrayList<>(e.accept(new VarCounter(false, decls, classes)));
    List<VarExpr> uniqueVarsUsed = new ArrayList<>();
    varsUsed.sort(Comparator.comparing((VarExpr x) -> x.name()));

    // regrettable that we can't use a Set here, but we need to uniquify the
    // Vars used.  we'll at least take advantage of the fact that varsUsed is
    // sorted on name.
    if (varsUsed.size() > 0) {
      uniqueVarsUsed.add(varsUsed.get(0));
      for (int i = 1; i < varsUsed.size(); i++) {
        if (!varsUsed.get(i).name().equals(
                uniqueVarsUsed.get(uniqueVarsUsed.size()-1).name())) {
          uniqueVarsUsed.add(varsUsed.get(i));
        }
      }
    }

    String fnName = fnName();
    FuncDecl newDecl = (FuncDecl)new FuncDecl(fnName, uniqueVarsUsed, e.type(), e).type(e.type());
    this.newDecls.add(newDecl);

    return (CallExpr)new CallExpr(fnName, uniqueVarsUsed).type(e.type());
  }

  protected Expr defaultVisit (Expr e)
  {
    return shouldLift.apply(e) ? lift(e) : e;
  }

  @Override
  public Expr visit(VarExpr e)
  {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(BoolLitExpr e)
  {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(NullExpr e) { return defaultVisit(e); }

  @Override
  public Expr visit(PtrExpr e) {
    e = (PtrExpr) new PtrExpr(e.data().accept(this), e.offset()).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(IncrPtrExpr e) {
    e = (IncrPtrExpr) new IncrPtrExpr(e.ptr().accept(this), e.incr()).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(DecrPtrExpr e) {
    e = (DecrPtrExpr) new DecrPtrExpr(e.ptr().accept(this), e.decr()).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(CallExpr e)
  {
    List<Expr> args = new ArrayList<>();
    for (Expr arg : e.args())
      args.add(arg.accept(this));

    e = (CallExpr)new CallExpr(e.name(), args, e.getId()).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(BinaryExpr e)
  {
    e = (BinaryExpr)new BinaryExpr(e.left().accept(this), e.op(), e.right().accept(this)).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(UnaryExpr e) {
    e = (UnaryExpr)new UnaryExpr(e.op(), e.base().accept(this)).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(FieldExpr e)
  {
    e = (FieldExpr)new FieldExpr(e.target().accept(this), e.field()).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(ChooseExpr e)
  {
    List<Expr> args = new ArrayList<>();
    for (Expr arg : e.args())
      args.add(arg.accept(this));

    ChooseExpr e2 = (ChooseExpr)new ChooseExpr(args, e.getId()).type(e.type());
    return defaultVisit(e2);
  }

  @Override
  public Expr visit(ForallExpr e)
  {
    e = (ForallExpr)new ForallExpr(e.var(), e.start().accept(this), e.end().accept(this), e.body().accept(this))
            .type(e.type());

    return defaultVisit(e);
  }

  @Override
  public Expr visit(IfExpr e)
  {
    e = (e.alt() != null) ?
        (IfExpr)new IfExpr(e.cond().accept(this), e.cons().accept(this), e.alt().accept(this)).type(e.type()) :
        (IfExpr)new IfExpr(e.cond().accept(this), e.cons().accept(this), null).type(e.type());

    return defaultVisit(e);
  }

  @Override
  public Expr visit(ChooseIntExpr e) {
    return e;
  }

  @Override
  public Expr visit(LetExpr e)
  {
    e = (LetExpr)new LetExpr(e.vars(), e.body().accept(this), (e.assumptions() == null ? e.assumptions() : e.assumptions().accept(this))).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(FuncDecl f)
  {
    return new FuncDecl(f.name(), f.params(), f.retType(), (f.isUnInterpreted() ? f.body() : f.body().accept(this).type(f.retType())));
  }

  @Override
  public Expr visit(ClassDecl c) { return defaultVisit(c); }

  @Override
  public Expr visit(Program p)
  {
    // store this in a class field so that we can look up for declared functions when processing the body
    this.existingDecls = p.functions();
    this.classes = p.classes();

    List<FuncDecl> fns = new ArrayList<>();

    for (FuncDecl fn : p.functions())
      fns.add((FuncDecl) fn.accept(this));

    Expr body = (p.body() == null ? null : p.body().accept(this));

    //System.out.println("Expr lifter: " + this.newDecls.size() + " exprs lifted to fns");
    fns.addAll(newDecls);

    return new Program(fns, p.classes(), body).type(p.type());
  }

  @Override
  public Expr visit(IntLitExpr e)
  {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(FloatLitExpr e)
  {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(UIExpr e) {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(EqualsExpr e)
  {
    e = (EqualsExpr)new EqualsExpr(e.list1().accept(this), e.list2().accept(this)).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(EmptyListExpr e)
  {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(GetExpr e)
  {
    e = (GetExpr)new GetExpr(e.list().accept(this), e.index().accept(this)).type(e.type());
    return defaultVisit(e);
  }


  @Override
  public Expr visit(LengthExpr e)
  {
    e = (LengthExpr)new LengthExpr(e.list().accept(this)).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(AppendExpr e)
  {
    e = (AppendExpr)new AppendExpr(e.list().accept(this), e.elem().accept(this)).type(e.type());
    return e;
  }

  @Override
  public Expr visit(SliceExpr e)
  {
    e = (SliceExpr)new SliceExpr(e.list().accept(this), e.start().accept(this), e.end().accept(this)).type(e.type());
    return e;
  }

  @Override
  public Expr visit(ConcatExpr e)
  {
    e = (ConcatExpr)new ConcatExpr(e.list1().accept(this), e.list2().accept(this)).type(e.type());
    return e;
  }

  @Override
  public Expr visit(ListCompExpr e)
  {
    e = (ListCompExpr)new ListCompExpr(e.expr().accept(this),
            map(e.iters(), p -> new Pair<>((VarExpr)p.a.accept(this), p.b.accept(this)))).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(SelectExpr e)
  {
    List<Expr> indices = new ArrayList<>();
    for (Expr index : e.index())
      indices.add(index.accept(this));
    e = (SelectExpr)new SelectExpr(e.array().accept(this), indices).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(StoreExpr e)
  {
    List<Expr> indices = new ArrayList<>();
    for (Expr index : e.index())
      indices.add(index.accept(this));
    e = (StoreExpr)new StoreExpr(e.array().accept(this), e.value().accept(this), indices).type(e.type());
    return defaultVisit(e);
  }

  @Override
  public Expr visit(ForEach e) {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(Variables e) {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(Bound e) {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(Init e) {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(Scope e) {
    return defaultVisit(e);
  }

  @Override
  public Expr visit(TupleExpr e) {
    return defaultVisit(e);
  }
}
