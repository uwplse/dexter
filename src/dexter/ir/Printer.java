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
import dexter.ir.type.ClassT;
import dexter.ir.type.ListT;
import dexter.ir.type.TypesFactory;

import java.util.*;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Printer implements Visitor<String>
{
  /* Boolean */

  @Override
  public String visit (BinaryExpr e)
  {
    return "(" + e.left().accept(this) + " " + e.op() + " " + e.right().accept(this) + ")";
  }

  @Override
  public String visit(UnaryExpr e) {
    return e.op() + "(" + e.base().accept(this) + ")";
  }

  @Override
  public String visit (FieldExpr e)
  {
    if (e.target() instanceof VarExpr)
      return e.target().accept(this) + "." + e.field();
    else
      return "(" + e.target().accept(this) + ")." + e.field();
  }

  @Override
  public String visit (BoolLitExpr v)
  {
    return v.value() + "";
  }

  @Override
  public String visit (NullExpr v) { return "null(" + v.type() + ")"; }

  @Override
  public String visit(PtrExpr n) {
    return visit((CallExpr) n);
  }

  @Override
  public String visit(IncrPtrExpr e) {
    return visit((CallExpr) e);
  }

  @Override
  public String visit(DecrPtrExpr e) {
    return visit((CallExpr) e);
  }

  /* handles all function calls */
  @Override
  public String visit (CallExpr v)
  {
    StringBuffer sb = new StringBuffer();

    sb.append(v.name() + "(");

    Iterator<Expr> it = v.args().iterator();
    while (it.hasNext())
    {
      Expr e = it.next();
      sb.append(e.accept(this));
      if (it.hasNext())
        sb.append(", ");
    }

    sb.append(")");
    return sb.toString();
  }

  @Override
  public String visit (LetExpr n)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("let(");

    sb.append(printVarDecls(n.vars(), true));

    sb.append(") in ");
    sb.append(n.body().accept(this));

    if (n.assumptions() != null)
    {
      sb.append(" assume ");
      sb.append(n.assumptions().accept(this));
    }

    return sb.toString();
  }

  @Override
  public String visit (IfExpr e)
  {
    StringBuffer sb = new StringBuffer();

    sb.append("(if ");
    sb.append(e.cond().accept(this));
    sb.append(" then ");
    sb.append(e.cons().accept(this));
    if (e.alt() != null)
    {
      sb.append(" else ");
      sb.append(e.alt().accept(this));
    }
    sb.append(")");

    return sb.toString();
  }

  @Override
  public String visit(ChooseIntExpr e) {
    StringBuffer sb = new StringBuffer();
    sb.append("const(int, ");
    sb.append(e.getWidth());
    sb.append(")");
    return sb.toString();
  }

  @Override
  public String visit(FuncDecl n)
  {
    StringBuffer sb = new StringBuffer();

    sb.append(n.name() + "(");

    sb.append(printVarDecls(n.params(), false));

    sb.append(") : " + n.retType());

    if (!n.isUnInterpreted())
      sb.append(" -> " + n.body().accept(this));

    sb.append((n.name().equals("lambda") ? "" : "\n\n"));

    return sb.toString();
  }

  @Override
  public String visit(ClassDecl n)
  {
    return "class " + n.name() + "(" + printVarDecls(n.fields(), false) + ")\n";
  }

  protected String printVarDecls(Collection<VarExpr> vars, boolean shouldSort)
  {
    StringBuffer sb = new StringBuffer();

    // print by lexiographic order
    List<VarExpr> list = new ArrayList<>(vars);
    if (shouldSort)
      Collections.sort(list, Comparator.comparing((VarExpr x) -> x.name()));

    Iterator<VarExpr> it = list.iterator();

    while (it.hasNext())
    {
      VarExpr p = it.next();
      sb.append(p.accept(this));

      if (TypesFactory.isClassT(p.type()))
        sb.append(":" + ((ClassT)p.type()).name());
      else
        sb.append(":" + p.type());

      if (it.hasNext())
        sb.append(", ");
    }

    return sb.toString();
  }

  @Override
  public String visit(Program p)
  {
    StringBuffer sb = new StringBuffer();

    for (ClassDecl c : p.classes())
      sb.append(c.accept(this));

    for (FuncDecl f : p.functions())
      sb.append(f.accept(this));

    if (p.body() != null)
      sb.append(p.body().accept(this));

    return sb.toString();
  }

  @Override
  public String visit (VarExpr v)
  {
    return v.name();
  }


  /* Linear arithmetic */
  @Override
  public String visit (ForallExpr n)
  {
    StringBuffer sb = new StringBuffer();

    sb.append("forall(");
    sb.append(n.var().accept(this));
    sb.append(", ");
    sb.append(n.start().accept(this));
    sb.append(", ");
    sb.append(n.end().accept(this));
    sb.append(", ");
    sb.append(n.body().accept(this));
    sb.append(")");

    return sb.toString();
  }

  @Override
  public String visit (IntLitExpr s)
  {
    return s.value() + "";
  }

  @Override
  public String visit (FloatLitExpr s) { return s.value() + ""; }

  @Override
  public String visit(UIExpr ui) {
    return "uninterp(" + ui.type() + ")";
  }

  @Override public String visit (ChooseExpr e)  { return visit((CallExpr)e); }

  /* lists */
  @Override
  public String visit (EmptyListExpr n)
  {
    return "empty(" + ((ListT)n.type()).elemT() + ")";
  }

  @Override public String visit (EqualsExpr e) { return visit((CallExpr)e); }
  @Override public String visit (GetExpr e) { return visit((CallExpr)e); }
  @Override public String visit (LengthExpr e) { return visit((CallExpr)e);}
  @Override public String visit (ConcatExpr e) { return visit((CallExpr)e); }
  @Override public String visit (SliceExpr e) { return visit((CallExpr)e); }
  @Override public String visit (AppendExpr e) { return visit((CallExpr)e); }

  @Override public String visit (ListCompExpr e)
  {
    return e.expr().accept(this) + " " + String.join(" ",
            Util.map(e.iters(), p -> "(for " + p.a.accept(this) + " in " + p.b.accept(this) + ")"));
  }

  /* array */
  @Override public String visit (SelectExpr e) { return visit((CallExpr)e); }
  @Override public String visit (StoreExpr e) { return visit((CallExpr)e); }

  @Override
  public String visit(ForEach e) {
    return "@for ( " + e.getId().name() + " in " + e.getVarSet().accept(this) + " ) emit " + e.getSubExpr().accept(this);
  }

  @Override
  public String visit(Variables e) {
    switch (e.getCat())
    {
      case INPUT_VARIABLES:
        return "@in_vars(" + e.getVarT().toString() + ")";
      case OUTPUT_VARIABLES:
        return "@out_vars(" + e.getVarT().toString() + ")";
      case OUTPUT_ARRAY_VARIABLES:
        return "@out_arr_vars(" + e.getVarT().toString() + ")";
      case ALL_VARIABLES:
        return "@vars(" + e.getVarT().toString() + ")";
      case CONSTANTS:
        return "@consts(" + e.getVarT().toString() + ")";
      case INDEX_VARS:
        return "@idx_vars(" + e.getVarT().toString() + ")";
      default:
        throw new RuntimeException("Unrecognized macro category: " + e.getCat());
    }
  }

  @Override
  public String visit(Bound e) {
    switch (e.getCategory())
    {
      case LOWER_BOUND:
        return "@lb(" + e.getIndex() + ")";
      case UPPER_BOUND:
        return "@ub(" + e.getIndex() + ")";
      default:
        throw new RuntimeException("Unrecognized macro category: " + e.getCategory());
    }
  }

  @Override
  public String visit(Init e) {
    return "@init(" + e.getExpr().accept(this) + ")";
  }

  @Override
  public String visit(Scope e) {
    return "@scope(" + e.getExpr().accept(this) + ")";
  }

  @Override
  public String visit(TupleExpr t) {
    StringBuffer sb = new StringBuffer();

    sb.append("[");

    Iterator<Expr> it = t.vals().iterator();
    while (it.hasNext())
    {
      Expr e = it.next();
      sb.append(e.accept(this));
      if (it.hasNext())
        sb.append(", ");
    }

    sb.append("]");
    return sb.toString();
  }
}