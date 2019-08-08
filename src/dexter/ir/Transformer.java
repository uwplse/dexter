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
import dexter.ir.type.TypesFactory;
import org.antlr.v4.runtime.misc.Pair;

import java.util.stream.Collectors;

/**
 * Created by Maaz Ahmad on 6/25/19.
 *
 * Iterates the AST and calls the transform method that can be overwritten by subclasses.
 * transform should return null if traversal of the original AST is to be continued, otherwise
 * a non-null Expr should be returned.
 */
public class Transformer implements Visitor<Expr>
{
  public Expr transform (VarExpr e) { return null; }

  @Override
  public Expr visit(VarExpr o)
  {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (BoolLitExpr e) { return null; }

  @Override
  public Expr visit(BoolLitExpr o)
  {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (NullExpr e) { return null; }

  @Override
  public Expr visit (NullExpr o)
  {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (PtrExpr e) { return null; }

  @Override
  public Expr visit(PtrExpr o) {
    Expr n = transform(o);
    return n == null ? new PtrExpr(o.data().accept(this), o.offset().accept(this)) : n;
  }

  public Expr transform (IncrPtrExpr e) { return null; }

  @Override
  public Expr visit(IncrPtrExpr o) {
    Expr n = transform(o);
    return n == null ? new IncrPtrExpr(o.ptr().accept(this), o.incr().accept(this)) : n;
  }

  public Expr transform (DecrPtrExpr e) { return null; }

  @Override
  public Expr visit(DecrPtrExpr o) {
    Expr n = transform(o);
    return n == null ? new IncrPtrExpr(o.ptr().accept(this), o.decr().accept(this)) : n;
  }

  public Expr transform (CallExpr e) { return null; }

  @Override
  public Expr visit(CallExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new CallExpr(o.name(), o.args().stream().map(a -> a.accept(this)).collect(Collectors.toList()), o.getId())
            .type(o.type())
      : n;
  }

  public Expr transform (FieldExpr e) { return null; }

  @Override
  public Expr visit(FieldExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new FieldExpr(o.target().accept(this), o.field()).type(o.type())
      : n;
  }

  public Expr transform (BinaryExpr e) { return null; }

  @Override
  public Expr visit(BinaryExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new BinaryExpr(o.left().accept(this), o.op(), o.right().accept(this)).type(o.type())
      : n;
  }

  public Expr transform(UnaryExpr e) { return null; }

  @Override
  public Expr visit(UnaryExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new UnaryExpr(o.op(), o.base().accept(this)).type(o.type())
      : n;
  }

  public Expr transform (IfExpr e) { return null; }

  @Override
  public Expr visit(IfExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new IfExpr(o.cond().accept(this), o.cons().accept(this), o.alt().accept(this))
            .type(o.type())
      : n;
  }

  public Expr transform (ChooseIntExpr e) {
    return null;
  }

  @Override
  public Expr visit(ChooseIntExpr o)
  {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (ChooseExpr e) { return null; }

  @Override
  public Expr visit(ChooseExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new ChooseExpr(o.args().stream().map(a -> a.accept(this)).collect(Collectors.toList()), o.getId())
            .type(o.type())
        : n;
  }

  public Expr transform (ForallExpr e) { return null; }

  @Override
  public Expr visit(ForallExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new ForallExpr((VarExpr)o.var().accept(this),
                     o.start().accept(this), o.end().accept(this), o.body().accept(this))
            .type(o.type())
        : n;
  }

  public Expr transform (LetExpr e) { return null; }

  @Override
  public Expr visit(LetExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new LetExpr(o.vars().stream().map(v -> (VarExpr)v.accept(this)).collect(Collectors.toSet()),
                  o.body().accept(this), (o.assumptions() == null ? null : o.assumptions().accept(this))).type(o.type())
        : n;
  }

  public Expr transform (FuncDecl e) { return null; }

  @Override
  public Expr visit(FuncDecl o)
  {
    Expr n = transform(o);
    if (n == null)
    {
      FuncDecl f = new FuncDecl(o.name(),
              o.params().stream().map(p -> (VarExpr)p.accept(this)).collect(Collectors.toList()),
              o.retType(), (o.isUnInterpreted() ? null : o.body().accept(this)));

      f = (FuncDecl)f.type(TypesFactory.functionT(f.name(), f.retType(), f.params().stream().map(p -> p.type()).collect(Collectors.toList())));
      return f;
    }
    else
      return n;
  }

  public Expr transform (ClassDecl e) { return null; }

  @Override
  public Expr visit(ClassDecl o)
  {
    Expr n = transform(o);

    if (n == null)
    {
      ClassDecl c = new ClassDecl(o.name(), o.fields().stream().map(f -> (VarExpr)f.accept(this)).collect(Collectors.toList()));

      c = (ClassDecl)c.type(TypesFactory.classT(c.name(), o.fields()));
      return c;
    }
    else
      return n;
  }

  public Expr transform (Program p) { return null; }

  @Override
  public Expr visit(Program o)
  {
    // not sure what the type of a Program is
    Expr n = transform(o);
    return n == null ?
      new Program(o.functions().stream().map(f -> (FuncDecl)f.accept(this)).collect(Collectors.toList()),
                  o.classes().stream().map(c -> (ClassDecl)c.accept(this)).collect(Collectors.toList()),
                  o.body().accept(this)).type(o.type()) : n;
  }

  public Expr transform (IntLitExpr e) { return null; }

  @Override
  public Expr visit(IntLitExpr o)
  {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (FloatLitExpr e) { return null; }

  @Override
  public Expr visit(FloatLitExpr o)
  {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (UIExpr e) { return null; }

  @Override
  public Expr visit(UIExpr o) {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (EqualsExpr e) { return null; }

  @Override
  public Expr visit(EqualsExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new EqualsExpr(o.list1().accept(this), o.list2().accept(this)).type(o.type()) : n;
  }

  public Expr transform (EmptyListExpr e) { return null; }

  @Override
  public Expr visit(EmptyListExpr o)
  {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (GetExpr e) { return null; }

  @Override
  public Expr visit(GetExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new GetExpr(o.list().accept(this), o.index().accept(this)).type(o.type()) : n;
  }

  public Expr transform (LengthExpr e) { return null; }

  @Override
  public Expr visit(LengthExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new LengthExpr(o.list().accept(this)).type(o.type()) : n;
  }

  public Expr transform (AppendExpr e) { return null; }

  @Override
  public Expr visit(AppendExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new AppendExpr(o.list().accept(this), o.elem().accept(this)).type(o.type()) : n;
  }

  public Expr transform (SliceExpr e) { return null; }

  @Override
  public Expr visit(SliceExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new SliceExpr(o.list().accept(this), o.start().accept(this), o.end().accept(this))
            .type(o.type()): n;
  }

  public Expr transform (ConcatExpr e) { return null; }

  @Override
  public Expr visit(ConcatExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new ConcatExpr(o.list1().accept(this), o.list2().accept(this)).type(o.type()) : n;
  }

  public Expr transform (ListCompExpr e) { return null; }

  @Override
  public Expr visit (ListCompExpr o)
  {
    Expr n = transform(o);
    return n == null ?
      new ListCompExpr(o.expr().accept(this),
            Util.map(o.iters(), p -> new Pair<>((VarExpr)p.a.accept(this), p.b.accept(this))))
          .type(o.type()) : n;
  }

  public Expr transform (SelectExpr e) { return null; }

  @Override
  public Expr visit(SelectExpr o)
  {
    Expr n = transform(o);
    return n == null ? new SelectExpr(o.array().accept(this),
                                      o.index().stream().map(i -> i.accept(this)).collect(Collectors.toList()))
                        .type(o.type()) : n;

  }

  public Expr transform (StoreExpr e) { return null; }

  @Override
  public Expr visit(StoreExpr o)
  {
    Expr n = transform(o);
    return n == null ? new StoreExpr(o.array().accept(this), o.value().accept(this),
                                     o.index().stream().map(i -> i.accept(this)).collect(Collectors.toList()))
                        .type(o.type()) : n;
  }

  public Expr transform (ForEach e) { return null; }

  @Override
  public Expr visit(ForEach o) {
    Expr n = transform(o);
    return n == null ? new ForEach((VarExpr) o.getId().accept(this), o.getVarSet().accept(this), o.getSubExpr().accept(this)) : n;
  }

  public Expr transform (Variables e) { return null; }

  @Override
  public Expr visit(Variables o) {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (Bound e) { return null; }

  @Override
  public Expr visit(Bound o) {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (Init e) { return null; }

  @Override
  public Expr visit(Init o) {
    Expr n = transform(o);
    return n == null ? new Init(o.getExpr().accept(this)) : n;
  }

  public Expr transform (Scope e) { return null; }

  @Override
  public Expr visit(Scope o) {
    Expr n = transform(o);
    return n == null ? o : n;
  }

  public Expr transform (TupleExpr e) { return null; }

  @Override
  public Expr visit(TupleExpr o) {
    Expr n = transform(o);
    return n == null ? new TupleExpr(o.vals().stream().map(v -> v.accept(this)).collect(Collectors.toList())).type(o.type) : n;
  }
}