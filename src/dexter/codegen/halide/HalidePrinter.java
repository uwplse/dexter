package dexter.codegen.halide;

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
import dexter.ir.type.PtrT;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mahmad on 11/15/2018.
 */
public class HalidePrinter implements Visitor<String>
{
  Map<VarExpr, Expr> termMapping = new HashMap<>();

  public HalidePrinter () {}

  public HalidePrinter (Map<VarExpr, Expr> m) { termMapping.putAll(m); }

  String toHalideType (Type t) {
    if (TypesFactory.Int == t)
      return "Halide::Expr";

    if (TypesFactory.Float == t)
      return "Halide::Expr";

    if (TypesFactory.Bool == t)
      return "Halide::Expr";

    if (TypesFactory.isPtrT(t))
      return "Halide::Buffer<" + ((PtrT)t).elemT() + ">";

    throw new RuntimeException("NYI. Type converstion to halide for: " + t.toString());
  }

  protected String printExprList (Iterable<? extends Expr> l, String start, String delim, String end, boolean printType) {
    StringBuffer sb = new StringBuffer();

    sb.append(start);

    Iterator<? extends Expr> it = l.iterator();
    while (it.hasNext())
    {
      Expr e = it.next();
      if (printType)
        sb.append(toHalideType(e.type()) + " ");

      sb.append(e.accept(this));

      if (it.hasNext())
        sb.append(delim);
    }
    sb.append(end);

    return sb.toString();
  }

  @Override
  public String visit(FuncDecl n) {
    StringBuffer sb = new StringBuffer();

    Pattern p = Pattern.compile("cast_(int64|uint8|uint16|uint32|int8|int16|int32|float|double)_(int64|double|uint8|uint16|uint32|int8|int16|int32|float)");
    Matcher m = p.matcher(n.name());

    if (m.matches()) {
      return "";
    }

    // Function signature
    sb.append(toHalideType(n.retType()) + " " + n.name() + " " + printExprList(n.params(), "(", ", ", ")", true) + " {\n");
    sb.append("  return " + n.body().accept(this) + ";");
    sb.append("\n}\n");

    return sb.toString();
  }

  @Override
  public String visit(VarExpr e) {
    if (termMapping.containsKey(e)) {
      return termMapping.get(e).accept(this);
    }

    else if (e.name().equals("idx_x"))
      return "x";

    else if (e.name().equals("idx_y"))
      return "y";

    //else if (e.name().equals("idx"))
      //return new VarExpr("i", e.type());

    //else if (e.name().equals("curr_cn"))
      //return new VarExpr("cn", e.type());

    //else if (e.name().startsWith("Const")) {
      // return the part without "Const" and replace _ with .
    //  return e.name().substring(5, e.name().length()).replace("_", ".") + "f";
    //}

    return e.name();
  }

  @Override
  public String visit(BoolLitExpr e) {
    return Boolean.toString(e.value());
  }

  @Override
  public String visit(IntLitExpr e) {
    return Integer.toString(e.value());
  }

  @Override
  public String visit(FloatLitExpr e) {
    return e.value() + "f";
  }

  @Override
  public String visit(CallExpr e) {
    Pattern p = Pattern.compile("cast_(int64|uint8|uint16|uint32|int8|int16|int32|float|double)_(int64|double|uint8|uint16|uint32|int8|int16|int32|float)");
    Matcher m = p.matcher(e.name());

    if (m.matches()) {
      String tp = m.group(2);
      if (!tp.equals("float")) {
        // for int types, we need to append to make the type name correct
        tp += "_t";
      }
      return "Halide::cast<" + tp + ">" + printExprList(e.args(), "(", ", ", ")", false);
    }

    else if (e.name().matches("HBuffer_Get"))
    {
      return e.args().get(0) + printExprList(e.args().subList(1, e.args().size()), "(", ", ", ")", false);
    }

    return e.name() + printExprList(e.args(), "(", ", ", ")", false);
  }

  @Override
  public String visit(IfExpr e) {
    return "select(" + e.cond().accept(this) + ", " + e.cons().accept(this) + ", " + e.alt().accept(this) + ")";
  }

  @Override
  public String visit(UnaryExpr e) {
    return "(" + e.op() + e.base().accept(this) + ")";
  }

  @Override
  public String visit(BinaryExpr e) {
    // C++ doesn't have ">>>", so not sure what to do here.
    String op = e.op() == BinaryExpr.Op.ASHR ? ">>" : e.op().toString();
    // binary equals is == in C++
    op = e.op() == BinaryExpr.Op.EQ ? "==" : op;
    return "(" + e.left().accept(this) + " " + op + " " + e.right().accept(this) + ")";
  }

  @Override
  public String visit(SelectExpr e) {
    assert (e.index().size() == 1);

    Expr idx = e.index().get(0);

    if (idx instanceof IntLitExpr || idx instanceof VarExpr)
      return e.array().accept(this) + "(" + idx.accept(this) + ")";

    if (idx instanceof SelectExpr)
      return e.array().accept(this) + "(" + idx.accept(this) + ")";

    if (idx instanceof CallExpr)
    {
      CallExpr c = (CallExpr) idx;

      if (c.name().equals("index1D")) {
        String cn = c.args().get(0).accept(this);
        String i = c.args().get(1).accept(this);
        return e.array().accept(this) + "(" + i + ", " + cn + ")";
      }
      else if (c.name().equals("index2D")) {
        String x = c.args().get(0).accept(this);
        String y = c.args().get(1).accept(this);
        return e.array().accept(this) + "(" + x + ", " + y + ")";
      }
    }

    return e.array().accept(this) + "(Halide::cast<int32_t>(" + e.index().get(0).accept(this) + "))";
    //return NYI();
  }

  @Override
  public String visit(PtrExpr e) {
    return NYI();
  }

  @Override
  public String visit(IncrPtrExpr e) {
    return NYI();
  }

  @Override
  public String visit(DecrPtrExpr e) {
    return NYI();
  }

  @Override
  public String visit(NullExpr e) {
    return NYI();
  }

  @Override
  public String visit(FieldExpr e) {
    return NYI();
  }

  @Override
  public String visit(ChooseIntExpr e) {
    return NYI();
  }

  @Override
  public String visit(ChooseExpr e) {
    return NYI();
  }

  @Override
  public String visit(ForallExpr e) {
    return NYI();
  }

  @Override
  public String visit(LetExpr e) {
    return NYI();
  }

  @Override
  public String visit(ClassDecl n) {
    return NYI();
  }

  @Override
  public String visit(Program e) {
    return NYI();
  }

  @Override
  public String visit(UIExpr uiExpr) {
    return NYI();
  }

  @Override
  public String visit(EqualsExpr e) {
    return NYI();
  }

  @Override
  public String visit(EmptyListExpr e) {
    return NYI();
  }

  @Override
  public String visit(GetExpr e) {
    return NYI();
  }

  @Override
  public String visit(LengthExpr e) {
    return NYI();
  }

  @Override
  public String visit(ListCompExpr e) {
    return NYI();
  }

  @Override
  public String visit(ConcatExpr e) {
    return NYI();
  }

  @Override
  public String visit(SliceExpr e) {
    return NYI();
  }

  @Override
  public String visit(AppendExpr e) {
    return NYI();
  }

  @Override
  public String visit(StoreExpr e) {
    return NYI();
  }

  @Override
  public String visit(ForEach e) {
    return NYI();
  }

  @Override
  public String visit(Variables e) {
    return NYI();
  }

  @Override
  public String visit(Bound e) {
    return NYI();
  }

  @Override
  public String visit(Init e) {
    return NYI();
  }

  @Override
  public String visit(Scope e) {
    return NYI();
  }

  @Override
  public String visit(TupleExpr e) {
    return NYI();
  }

  private static String NYI() {
    throw new RuntimeException("NYI.");
  }
}