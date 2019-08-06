package dexter.ir.semantics;

import dexter.ir.Expr;
import dexter.ir.Printer;
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
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

/**
 * Created by Maaz Ahmad on 6/25/19.
 *
 * Checks whether variables and functions used are declared
 */
public class VarChecker implements Visitor<Boolean>
{
  protected Set<String> vars;
  protected Map<String, FuncDecl> functions;
  protected Map<String, ClassDecl> classes;
  protected boolean shouldThrow;

  public VarChecker (boolean shouldThrow)
  {
    this.vars = Collections.emptySet();
    this.functions = Collections.emptyMap();
    this.classes = Collections.emptyMap();
    this.shouldThrow = shouldThrow;
  }

  @Override
  public Boolean visit(VarExpr n)
  {
    if (shouldThrow && !vars.contains(n.name()) && !this.functions.containsKey(n.name()))
      throw new RuntimeException("variable not declared: " + n.name());
    else
      return vars.contains(n.name());
  }

  @Override
  public Boolean visit(BoolLitExpr n)
  {
    return true;
  }

  @Override
  public Boolean visit(NullExpr n) { return true; }

  @Override
  public Boolean visit(PtrExpr n) {
    return visit((CallExpr) n);
  }

  @Override
  public Boolean visit(IncrPtrExpr e) {
    return visit((CallExpr) e);
  }

  @Override
  public Boolean visit(DecrPtrExpr e) {
    return visit((CallExpr) e);
  }

  @Override
  public Boolean visit(CallExpr n)
  {
    for (Expr e : n.args())
      e.accept(this);

    return true;
  }

  @Override
  public Boolean visit(BinaryExpr n)
  {
    return n.left().accept(this) && n.right().accept(this);
  }

  @Override
  public Boolean visit(UnaryExpr n)
  {
    return n.base().accept(this);
  }

  @Override
  public Boolean visit(FieldExpr n) { return n.target().accept(this); }

  @Override
  public Boolean visit(ForallExpr n)
  {
    if (!this.vars.add(n.var().name()) && shouldThrow)
      throw new RuntimeException("duplicated variable " + n.var().name() + " in forall " + n.accept(new Printer()));

    return n.start().accept(this) && n.end().accept(this) && n.body().accept(this);
  }

  @Override
  public Boolean visit(IfExpr e)
  {
    return e.alt() != null ?
            e.cond().accept(this) && e.cons().accept(this) && e.alt().accept(this) :
            e.cond().accept(this) && e.cons().accept(this);
  }

  @Override
  public Boolean visit(ChooseIntExpr e) {
    return true;
  }

  @Override
  public Boolean visit(LetExpr n)
  {
    for (VarExpr v : n.vars())
    {
      if (!this.vars.add(v.name()) && shouldThrow)
        throw new RuntimeException("duplicated variable: " + v.name());
    }

    return n.body().accept(this) && (n.assumptions() == null ? true : n.assumptions().accept(this));
  }

  @Override
  public Boolean visit(Program p)
  {
    this.classes = new HashMap<>();

    for (ClassDecl c : p.classes())
    {
      if (this.classes.put(c.name(), c) != null && shouldThrow)
        throw new RuntimeException("duplicated class decl: " + c.name());

      if (!c.accept(this))
        return false;
    }

    this.functions = new HashMap<>();
    for (FuncDecl f : p.functions())
    {
      if (this.functions.put(f.name(), f) != null && shouldThrow)
        throw new RuntimeException("duplicated function decl: " + f.name());

      if (!f.accept(this))
        return false;
    }

    this.vars = new HashSet<>();
    if (p.body() != null)
      return p.body().accept(this);
    else
      return true;
  }

  @Override
  public Boolean visit(FuncDecl f)
  {
    this.vars = new HashSet<>();
    for (VarExpr v : f.params())
    {
      if (!this.vars.add(v.name()) && shouldThrow)
        throw new RuntimeException("duplicated variable " + v.name() + " in function " + f.name());
    }

    return (f.isUnInterpreted() ? true : f.body().accept(this));
  }

  @Override
  public Boolean visit(ClassDecl c)
  {
    return true;
  }

  @Override
  public Boolean visit(IntLitExpr n)
  {
    return true;
  }

  @Override
  public Boolean visit(FloatLitExpr n)
  {
    return true;
  }

  @Override
  public Boolean visit(UIExpr uiExpr) {
    return true;
  }

  @Override
  public Boolean visit(ChooseExpr e)
  {
    for (Expr arg : e.args())
      arg.accept(this);

    return true;
  }

  /* lists */

  @Override
  public Boolean visit(EmptyListExpr n)
  {
    return true;
  }

  @Override
  public Boolean visit(GetExpr e)
  {
    return e.list().accept(this) && e.index().accept(this);
  }

  @Override
  public Boolean visit(LengthExpr e)
  {
    return e.list().accept(this);
  }

  @Override
  public Boolean visit(EqualsExpr e)
  {
    return e.list1().accept(this) && e.list2().accept(this);
  }

  @Override
  public Boolean visit(AppendExpr e)
  {
    return e.list().accept(this) && e.elem().accept(this);
  }

  @Override
  public Boolean visit(SliceExpr e) { return e.list().accept(this) && e.start().accept(this) && e.end().accept(this); }

  @Override
  public Boolean visit(ConcatExpr e) { return e.list1().accept(this) && e.list2().accept(this); }

  @Override
  public Boolean visit(ListCompExpr e)
  {
    for (Pair<VarExpr, Expr> iter : e.iters())
    {
      if (!vars.add(iter.a.name()) && shouldThrow)
        throw new RuntimeException("duplicated variable " + iter.a.name() + " in " + e.accept(new Printer()));

      if (!iter.b.accept(this))
        return false;
    }

    return e.expr().accept(this);
  }

  /* arrays */

  @Override
  public Boolean visit(SelectExpr e)
  {
    if (!e.array().accept(this))
      return false;

    for (Expr param : e.index())
    {
      if (!param.accept(this))
        return false;
    }
    return true;
  }

  @Override
  public Boolean visit(StoreExpr e)
  {
    if (!e.array().accept(this))
      return false;

    if (!e.value().accept(this))
      return false;

    for (Expr param : e.index())
    {
      if (!param.accept(this))
        return false;
    }
    return true;
  }

  @Override
  public Boolean visit(ForEach e) {
    if (!this.vars.add(e.getId().name()) && shouldThrow)
      throw new RuntimeException("duplicated variable " + e.getId().name() + " in forall " + e.accept(new Printer()));

    return e.getSubExpr().accept(this);
  }

  @Override
  public Boolean visit(Variables e) {
    return true;
  }

  @Override
  public Boolean visit(Bound e) {
    return true;
  }

  @Override
  public Boolean visit(Init e) {
    return e.getExpr().accept(this);
  }

  @Override
  public Boolean visit(Scope e) {
    return e.getExpr().accept(this);
  }

  @Override
  public Boolean visit(TupleExpr e) {
    return e.vals().stream().map(v -> v.accept(this)).reduce(true, (v1,v2) -> v1 || v2);
  }
}