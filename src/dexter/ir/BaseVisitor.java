package dexter.ir;

import dexter.ir.array.*;
import dexter.ir.bool.*;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.ForallExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.integer.UIExpr;
import dexter.ir.list.*;
import dexter.ir.macro.*;
import dexter.ir.tuple.TupleExpr;

/**
 * Created by Maaz Ahmad on 6/25/19.
 *
 * Iterates the AST and calls the visit method that can be overwritten by subclasses to implement
 * various analysis passes.
 */
public class BaseVisitor implements Visitor<Expr>
{
  public void enter (VarExpr n) { }
  public void exit (VarExpr n) { }

  @Override
  public Expr visit(VarExpr n)
  {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (BoolLitExpr n) { }
  public void exit (BoolLitExpr n) { }

  @Override
  public Expr visit(BoolLitExpr n)
  {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (NullExpr n) { }
  public void exit (NullExpr n) { }

  @Override
  public Expr visit (NullExpr n)
  {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (PtrExpr n) { }
  public void exit (PtrExpr n) { }

  @Override
  public Expr visit (PtrExpr n) {
    enter(n);
    n.data().accept(this);
    n.offset().accept(this);
    exit(n);
    return n;
  }

  public void enter (IncrPtrExpr n) { }
  public void exit (IncrPtrExpr n) { }

  @Override
  public Expr visit(IncrPtrExpr n) {
    enter(n);
    n.ptr().accept(this);
    n.incr().accept(this);
    exit(n);
    return n;
  }

  public void enter (DecrPtrExpr n) { }
  public void exit (DecrPtrExpr n) { }

  @Override
  public Expr visit(DecrPtrExpr n) {
    enter(n);
    n.ptr().accept(this);
    n.decr().accept(this);
    exit(n);
    return n;
  }

  public void enter (CallExpr n) { }
  public void exit (CallExpr n) { }

  @Override
  public Expr visit(CallExpr n)
  {
    enter(n);
    n.args().forEach(a -> a.accept(this));
    exit(n);
    return n;
  }

  public void enter (FieldExpr n) { }
  public void exit (FieldExpr n) { }

  @Override
  public Expr visit(FieldExpr n)
  {
    enter(n);
    n.target().accept(this);
    exit(n);
    return n;
  }

  public void enter (BinaryExpr n) { }
  public void exit (BinaryExpr n) { }

  @Override
  public Expr visit(BinaryExpr n)
  {
    enter(n);
    n.left().accept(this);
    n.right().accept(this);
    exit(n);
    return n;
  }

  public void enter (UnaryExpr n) { }
  public void exit (UnaryExpr n) { }

  @Override
  public Expr visit(UnaryExpr n)
  {
    enter(n);
    n.base().accept(this);
    exit(n);
    return n;
  }

  public void enter (IfExpr n) { }
  public void exit (IfExpr n) { }

  @Override
  public Expr visit(IfExpr n)
  {
    enter(n);
    n.cond().accept(this);
    n.cons().accept(this);
    n.alt().accept(this);
    exit(n);
    return n;
  }

  public void enter (ChooseIntExpr n) { }
  public void exit (ChooseIntExpr n) { }

  @Override
  public Expr visit(ChooseIntExpr n)
  {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (ChooseExpr n) { }
  public void exit (ChooseExpr n) { }

  @Override
  public Expr visit(ChooseExpr n)
  {
    enter(n);
    n.args().forEach(a -> a.accept(this));
    exit(n);
    return n;
  }

  public void enter (ForallExpr n) { }
  public void exit (ForallExpr n) { }

  @Override
  public Expr visit(ForallExpr n)
  {
    enter(n);
    n.var().accept(this);
    n.start().accept(this);
    n.end().accept(this);
    n.body().accept(this);
    exit(n);
    return n;
  }

  public void enter (LetExpr n) { }
  public void exit (LetExpr n) { }

  @Override
  public Expr visit(LetExpr n)
  {
    enter(n);
    n.vars().forEach(v -> v.accept(this));
    n.body().accept(this);
    if (n.assumptions() != null) n.assumptions().accept(this);
    exit(n);
    return n;
  }

  public void enter (FuncDecl n) { }
  public void exit (FuncDecl n) { }

  @Override
  public Expr visit(FuncDecl n)
  {
    enter(n);
    n.params().forEach(p -> p.accept(this));
    if (!n.isUnInterpreted()) n.body().accept(this);
    exit(n);
    return n;
  }

  public void enter (ClassDecl n) { }
  public void exit (ClassDecl n) { }

  @Override
  public Expr visit(ClassDecl n)
  {
    enter(n);
    n.fields().forEach(f -> f.accept(this));
    exit(n);
    return n;
  }

  public void enter (Program p) { }
  public void exit (Program p) { }

  @Override
  public Expr visit(Program n)
  {
    enter(n);
    n.functions().forEach(f -> f.accept(this));
    n.classes().forEach(f -> f.accept(this));
    n.body().accept(this);
    exit(n);
    return n;
  }

  public void enter (IntLitExpr n) { }
  public void exit (IntLitExpr n) { }

  @Override
  public Expr visit(IntLitExpr n)
  {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (FloatLitExpr n) { }
  public void exit (FloatLitExpr n) { }

  @Override
  public Expr visit(FloatLitExpr n)
  {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (UIExpr n) { }
  public void exit (UIExpr n) { }

  @Override
  public Expr visit(UIExpr n) {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (EqualsExpr n) { }
  public void exit (EqualsExpr n) { }

  @Override
  public Expr visit(EqualsExpr n)
  {
    enter(n);
    n.list1().accept(this);
    n.list2().accept(this);
    exit(n);
    return n;
  }

  public void enter (EmptyListExpr n) { }
  public void exit (EmptyListExpr n) { }

  @Override
  public Expr visit(EmptyListExpr n)
  {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (GetExpr n) { }
  public void exit (GetExpr n) { }

  @Override
  public Expr visit(GetExpr n)
  {
    enter(n);
    n.list().accept(this);
    n.index().accept(this);
    exit(n);
    return n;
  }

  public void enter (LengthExpr n) { }
  public void exit (LengthExpr n) { }

  @Override
  public Expr visit(LengthExpr n)
  {
    enter(n);
    n.list().accept(this);
    exit(n);
    return n;
  }

  public void enter (AppendExpr n) { }
  public void exit (AppendExpr n) { }

  @Override
  public Expr visit(AppendExpr n)
  {
    enter(n);
    n.list().accept(this);
    n.elem().accept(this);
    exit(n);
    return n;
  }

  public void enter (SliceExpr n) { }
  public void exit (SliceExpr n) { }

  @Override
  public Expr visit(SliceExpr n)
  {
    enter(n);
    n.list().accept(this);
    n.start().accept(this);
    n.end().accept(this);
    exit(n);
    return n;
  }

  public void enter (ConcatExpr n) { }
  public void exit (ConcatExpr n) { }

  @Override
  public Expr visit(ConcatExpr n)
  {
    enter(n);
    n.list1().accept(this);
    n.list2().accept(this);
    exit(n);
    return n;
  }

  public void enter (ListCompExpr n) { }
  public void exit (ListCompExpr n) { }

  @Override
  public Expr visit (ListCompExpr n)
  {
    enter(n);
    n.expr().accept(this);
    n.iters().forEach(p -> { p.a.accept(this); p.b.accept(this); });
    exit(n);
    return n;
  }

  public void enter (SelectExpr n) { }
  public void exit (SelectExpr n) { }

  @Override
  public Expr visit(SelectExpr n)
  {
    enter(n);
    n.array().accept(this);
    n.index().forEach(i -> i.accept(this));
    exit(n);
    return n;

  }

  public void enter (StoreExpr n) { }
  public void exit (StoreExpr n) { }

  @Override
  public Expr visit(StoreExpr n)
  {
    enter(n);
    n.array().accept(this);
    n.value().accept(this);
    n.index().forEach(i -> i.accept(this));
    exit(n);
    return n;
  }

  public void enter (ForEach n) { }
  public void exit (ForEach n) { }

  @Override
  public Expr visit(ForEach n) {
    enter(n);
    n.getId().accept(this);
    n.getVarSet().accept(this);
    n.getSubExpr().accept(this);
    exit(n);
    return n;
  }

  public void enter (Variables n) { }
  public void exit (Variables n) { }

  @Override
  public Expr visit(Variables n) {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (Bound n) { }
  public void exit (Bound n) { }

  @Override
  public Expr visit(Bound n) {
    enter(n);
    exit(n);
    return n;
  }

  public void enter (Init n) { }
  public void exit (Init n) { }

  @Override
  public Expr visit(Init n) {
    enter(n);
    n.getExpr().accept(this);
    exit(n);
    return n;
  }

  public void enter (Scope n) { }
  public void exit (Scope n) { }

  @Override
  public Expr visit(Scope n) {
    enter(n);
    n.getExpr().accept(this);
    exit(n);
    return n;
  }

  public void enter (TupleExpr n) { }
  public void exit (TupleExpr n) { }

  @Override
  public Expr visit(TupleExpr n) {
    enter(n);
    n.vals().forEach(v -> v.accept(this));
    exit(n);
    return n;
  }
}