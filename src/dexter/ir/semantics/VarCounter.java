package dexter.ir.semantics;

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
import dexter.ir.tuple.TupleExpr;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

/**
 * Created by Maaz Ahmad on 6/25/19.
 *
 * Utility class that returns the variables that used in an expression.
 */
public class VarCounter implements Visitor<Set<VarExpr>>
{
  // should x in  f(x) (for x in l)  count as a variable?
  protected boolean countListCompVar;
  protected List<FuncDecl> fns;
  protected List<ClassDecl> classes;

  public VarCounter (boolean countListCompVar, List<FuncDecl> fns, List<ClassDecl> classes)
  {
    this.countListCompVar = countListCompVar;
    this.fns = fns;
    this.classes = classes;
  }

  @Override
  public Set<VarExpr> visit(VarExpr e) {
    return Collections.singleton(e);
  }

  @Override
  public Set<VarExpr> visit(BoolLitExpr e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(NullExpr e) { return Collections.emptySet(); }

  @Override
  public Set<VarExpr> visit(PtrExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(IncrPtrExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(DecrPtrExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(CallExpr e)
  {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));

    boolean found = false;

    for (FuncDecl fn : fns)
    {
      if (fn.name().equals(e.name()))
        found = true;
    }

    if (!found)
    {
      // is this a class constructor?
      for (ClassDecl c : classes)
      {
        if (c.name().equals(e.name()))
          found = true;
      }
    }

    if (!found)
    {
      List<Type> argsT = new ArrayList<>();
      for (Expr arg : e.args())
        argsT.add(arg.type());

      r.add(new VarExpr(e.name(), TypesFactory.functionT(e.name(), e.type(), argsT)));
    }

    return r;
  }

  @Override
  public Set<VarExpr> visit(BinaryExpr e)
  {
    Set<VarExpr> r = new HashSet<>();
    r.addAll(e.left().accept(this));
    r.addAll(e.right().accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(UnaryExpr e) {
    return e.base().accept(this);
  }

  @Override
  public Set<VarExpr> visit(FieldExpr e) { return e.target().accept(this); }

  @Override
  public Set<VarExpr> visit(ChooseExpr e)
  {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(ForallExpr e)
  {
    Set<VarExpr> r = new HashSet<>();
    r.addAll(e.start().accept(this));
    r.addAll(e.end().accept(this));
    r.addAll(e.body().accept(this));
    r.remove(e.var());
    return r;
  }

  @Override
  public Set<VarExpr> visit(IfExpr e)
  {
    Set<VarExpr> r = new HashSet<>();
    r.addAll(e.cond().accept(this));
    r.addAll(e.cons().accept(this));

    if (e.alt() != null)
      r.addAll(e.alt().accept(this));

    return r;
  }

  @Override
  public Set<VarExpr> visit(ChooseIntExpr e) {
    Set<VarExpr> r = new HashSet<>();
    return r;
  }

  @Override
  public Set<VarExpr> visit(LetExpr e) {
    return e.body().accept(this);
  }

  @Override
  public Set<VarExpr> visit(FuncDecl n) {
    return n.body().accept(this);
  }

  @Override
  public Set<VarExpr> visit(ClassDecl n) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(Program p) {
    return p.body().accept(this);
  }

  @Override
  public Set<VarExpr> visit(IntLitExpr e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(FloatLitExpr e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(UIExpr e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(EqualsExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(EmptyListExpr e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(GetExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(LengthExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(AppendExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(SliceExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(ConcatExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.args())
      r.addAll(arg.accept(this));
    return r;
  }

  @Override
  public Set<VarExpr> visit(ListCompExpr e)
  {
    Set<VarExpr> r = new HashSet<>();
    r.addAll(e.expr().accept(this));

    for (Pair<VarExpr, Expr> iter : e.iters())
    {
      r.addAll(iter.a.accept(this));
      r.addAll(iter.b.accept(this));
    }

    // remove the iteration variable x in 'for x in l' since otherwise
    // the lifter will make x one of the input parameters to the lifted function
    if (!countListCompVar)
    {
      for (Pair<VarExpr, Expr> iter : e.iters())
        r.remove(iter.a); // need to remove as e.expr would also reference the list element variable
    }

    return r;
  }

  @Override
  public Set<VarExpr> visit(SelectExpr e) {
    Set<VarExpr> vars = new HashSet<VarExpr>();
    vars.addAll(e.array().accept(this));
    for(Expr index : e.index())
      vars.addAll(index.accept(this));

    return vars;
  }

  @Override
  public Set<VarExpr> visit(StoreExpr e) {
    Set<VarExpr> vars = new HashSet<VarExpr>();
    vars.addAll(e.array().accept(this));
    for(Expr index : e.index())
      vars.addAll(index.accept(this));

    vars.addAll(e.value().accept(this));
    return vars;
  }

  @Override
  public Set<VarExpr> visit(ForEach e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(Variables e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(Bound e) {
    return Collections.emptySet();
  }

  @Override
  public Set<VarExpr> visit(Init e) {
    return e.getExpr().accept(this);
  }

  @Override
  public Set<VarExpr> visit(Scope e) {
    return e.getExpr().accept(this);
  }

  @Override
  public Set<VarExpr> visit(TupleExpr e) {
    Set<VarExpr> r = new HashSet<>();
    for (Expr arg : e.vals())
      r.addAll(arg.accept(this));
    return r;
  }
}