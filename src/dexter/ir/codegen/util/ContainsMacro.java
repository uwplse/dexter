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
import dexter.ir.tuple.TupleExpr;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;
import java.util.Stack;

public class ContainsMacro implements Visitor<Boolean> {
  Stack<String> trace = new Stack<>();
  List<FuncDecl> fns;

  public ContainsMacro(String name, List<FuncDecl> fns) {
    this.trace.push(name);
    this.fns = fns;
  }

  @Override
  public Boolean visit(CallExpr e) {
    boolean a = false;
    for (Expr arg : e.args())
      a = a || arg.accept(this);

    if (this.trace.contains(e.name()))
      return a;

    this.trace.push(e.name());

    FuncDecl fnDecl = null;
    for (FuncDecl fn : fns)
      if (fn.name().equals(e.name()))
        fnDecl = fn;

    boolean b = (fnDecl == null ? false : fnDecl.accept(this));

    this.trace.pop();

    return a || b;
  }

  @Override
  public Boolean visit(BinaryExpr e) {
    return e.left().accept(this) || e.right().accept(this);
  }

  @Override
  public Boolean visit(UnaryExpr e) {
    return e.base().accept(this);
  }

  @Override
  public Boolean visit(IfExpr e) {
    return e.cond().accept(this) || e.cons().accept(this) || e.alt().accept(this);
  }

  @Override
  public Boolean visit(ChooseExpr e) {
    return e.args().stream().map(expr -> expr.accept(this)).reduce(false, (v1,v2) -> v1 || v2);
  }

  @Override
  public Boolean visit(ForallExpr e) {
    return e.start().accept(this) || e.end().accept(this) || e.body().accept(this);
  }

  @Override
  public Boolean visit(LetExpr e) {
    return e.body().accept(this);
  }

  @Override
  public Boolean visit(FuncDecl n) {
    return (n.isUnInterpreted() ? false : n.body().accept(this));
  }

  @Override
  public Boolean visit(Program p) {
    throw new RuntimeException("NYI");
  }

  @Override
  public Boolean visit(EqualsExpr e) {
    return e.list1().accept(this) || e.list2().accept(this);
  }

  @Override
  public Boolean visit(GetExpr e) {
    return e.list().accept(this) || e.index().accept(this);
  }

  @Override
  public Boolean visit(LengthExpr e) {
    return e.list().accept(this);
  }

  @Override
  public Boolean visit(ListCompExpr e) {e.iters();
    boolean b = false;
    for (Pair<VarExpr, Expr> p : e.iters())
      b = b || p.a.accept(this) || p.b.accept(this);

    return e.expr().accept(this) || b;
  }

  @Override
  public Boolean visit(ConcatExpr e) {
    return e.list1().accept(this) || e.list2().accept(this);
  }

  @Override
  public Boolean visit(SliceExpr e) {
    return e.list().accept(this) || e.start().accept(this) || e.end().accept(this);
  }

  @Override
  public Boolean visit(AppendExpr e) {
    return e.list().accept(this) || e.elem().accept(this);
  }

  @Override
  public Boolean visit(SelectExpr e) {
    return e.array().accept(this) || e.index().get(0).accept(this);
  }

  @Override
  public Boolean visit(StoreExpr e) {
    return e.array().accept(this) || e.value().accept(this) || e.index().get(0).accept(this);
  }

  @Override
  public Boolean visit(PtrExpr n) {
    return n.data().accept(this) || n.offset().accept(this);
  }

  @Override
  public Boolean visit(IncrPtrExpr e) {
    return e.ptr().accept(this) || e.incr().accept(this);
  }

  @Override
  public Boolean visit(DecrPtrExpr e) {
    return e.ptr().accept(this) || e.decr().accept(this);
  }

  @Override
  public Boolean visit(ForEach e) {
    return true;
  }

  @Override
  public Boolean visit(Variables e) {
    return true;
  }

  @Override
  public Boolean visit(Bound bound) { return true; }

  @Override
  public Boolean visit(Init init) {
    return true;
  }

  @Override
  public Boolean visit(Scope e) {
    return true;
  }

  @Override
  public Boolean visit(TupleExpr e) {
    return e.vals().stream().map(v -> v.accept(this)).reduce(false, (v1, v2) -> v1 || v2);
  }

  @Override
  public Boolean visit(EmptyListExpr e) {
    return false;
  }

  @Override
  public Boolean visit(VarExpr e) {
    return false;
  }

  @Override
  public Boolean visit(BoolLitExpr e) {
    return false;
  }

  @Override
  public Boolean visit(NullExpr e) {
    return false;
  }

  @Override
  public Boolean visit(FieldExpr e) {
    return false;
  }

  @Override
  public Boolean visit(ChooseIntExpr e) {
    return false;
  }

  @Override
  public Boolean visit(ClassDecl n) {
    return false;
  }

  @Override
  public Boolean visit(IntLitExpr e) {
    return false;
  }

  @Override
  public Boolean visit(FloatLitExpr e) {
    return false;
  }

  @Override
  public Boolean visit(UIExpr e) {
    return false;
  }
}