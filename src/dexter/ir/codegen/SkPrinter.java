package dexter.ir.codegen;

import dexter.Files;
import dexter.Preferences;
import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.array.*;
import dexter.ir.bool.*;
import dexter.ir.codegen.util.ChooseBodyParamsFinder;
import dexter.ir.codegen.util.CountCallExprs;
import dexter.ir.codegen.util.ExprLifter;
import dexter.ir.codegen.util.ListCompExprLifter;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.ForallExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.integer.UIExpr;
import dexter.ir.list.*;
import dexter.ir.macro.*;
import dexter.ir.tuple.TupleExpr;
import dexter.ir.type.*;
import dexter.synthesis.Synthesizer;
import org.antlr.v4.runtime.misc.Pair;
import scala.Tuple2;
import scala.annotation.meta.field;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by maazsaf on 4/3/17.
 */
public class SkPrinter implements Visitor<String>
{
  protected int indent;
  protected List<Pair<Integer,Integer>> consts;

  protected Map<String, Integer> chooseIDs;
  protected List<FuncDecl> fns;
  protected String prefix = "choose";

  public SkPrinter()
  {
    this.indent = 0;
    this.chooseIDs = new HashMap<>();
    this.consts = new ArrayList<>();
  }

  protected String indent ()
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < indent; ++i)
      sb.append("  ");

    return sb.toString();
  }


  protected String toSkType(Type t)
  {
    if (t instanceof BoolT)
      return "bit";
    else if (t instanceof IntT)
      return "int";
    else if (t instanceof FloatT)
      return "int";
    else if (t instanceof PtrT)
      return "Ptr_" + toSkType(((PtrT) t).elemT());
    else if (t instanceof BufferT)
      return "Buffer" + ((BufferT) t).dim() + "D<" + toSkType(((BufferT) t).elemT()) + ">";
    else if (t instanceof ArrayT)
    {
      ArrayT aT = (ArrayT)t;
      StringBuffer s = new StringBuffer();
      s.append(toSkType(aT.elemT()));
      for (int i = 0; i < aT.dim(); ++i)
        s.append("[ARRAY_LEN]");

      return s.toString();
    }
    else if (t instanceof ListT)
      return toSkType(((ListT)t).elemT()) + "_list";
    else if (t instanceof FunctionT)
      return "fun";
    else if (t instanceof ClassT)
      return ((ClassT)t).name();
    else if (t instanceof TupleT)
      return ((TupleT) t).elemT().toString() + "[" + ((TupleT) t).size() + "]";

    else
      throw new RuntimeException("don't know how to translate " + t + " to sketch type");
  }

  protected String printExprList (Iterable<? extends Expr> l,
                                  String start, String delim, String end, boolean printType)
  {
    StringBuffer sb = new StringBuffer();

    sb.append(start);

    Iterator<? extends Expr> it = l.iterator();
    while (it.hasNext())
    {
      Expr e = it.next();
      if (printType)
        sb.append(toSkType(e.type()) + " ");

      sb.append(e.accept(this));

      if (it.hasNext())
        sb.append(delim);
    }
    sb.append(end);

    return sb.toString();
  }

  protected String printWithParen (Expr e)
  {
    return (e instanceof VarExpr || e instanceof CallExpr ||  e instanceof BoolLitExpr || e instanceof IntLitExpr) ?
      e.accept(this) : "(" + e.accept(this) + ")";
  }


  /* booleans */

  @Override
  public String visit(VarExpr e)
  {
    return e.name();
  }

  @Override
  public String visit(BoolLitExpr e)
  {
    return e.value() ? "true" : "false";
  }

  @Override
  public String visit(NullExpr e) { return "null"; }

  @Override
  public String visit(PtrExpr n) {
    return "new " + toSkType(n.type()) + "(data=("+n.data().accept(this)+"), offset=("+n.offset().accept(this)+"))";
  }

  @Override
  public String visit(IncrPtrExpr e) {
    StringBuffer sb = new StringBuffer();

    String skArrayType = toSkType(e.ptr().type());

    sb.append(skArrayType + " ptrExpr = " + e.ptr().accept(this) + ";\n\t");
    sb.append("return new " + toSkType(e.type()) + "(data=(ptrExpr.data), offset=(ptrExpr.offset + " + e.incr().accept(this)+"))");

    return sb.toString();
  }

  @Override
  public String visit(DecrPtrExpr e) {
    return "new " + toSkType(e.type()) + "(data=("+e.ptr().accept(this)+".data), offset=(" + e.ptr().accept(this) + ".offset - " + e.decr().accept(this)+"))";
  }

  public String visit(CallExpr e, int id)
  {
    if (TypesFactory.isClassT(e.name()))
    {
      return e.accept(this);
    }
    else {
      // If calling a generator function
      if (e.name().endsWith("_gen")) {
        List<Expr> newArgs = new ArrayList<>(e.args());

        // Pass choose vars
        for (FuncDecl fnDecl : this.fns) {
          if (fnDecl.name().equals(e.name())) {
            ChooseBodyParamsFinder f = new ChooseBodyParamsFinder();
            fnDecl.body().accept(f);

            for (Tuple2<Integer,Integer> ID : f.IDs) {
              newArgs.add(new VarExpr("choose" + ID._1 + "_" + id, TypesFactory.Int));
              chooseIDs.put(ID._1 + "_" + id, (int) Math.ceil(Math.log(ID._2) / Math.log(2)));
            }

            break;
          }
        }

        return e.name() + printExprList(newArgs, "(", ", ", ")", false);
      }
      else
        return e.accept(this);
    }
  }

  @Override
  public String visit(CallExpr e)
  {
    //if (TypesFactory.isClassT(e.type()))// is ctor call
    if (TypesFactory.isClassT(e.name()))
    {
      StringBuffer sb = new StringBuffer();
      sb.append("new " + e.name() + "(");

      ClassT classT = (ClassT)e.type();

      for (int i = 0; i < classT.fields().size(); ++i)
      {
        VarExpr field = classT.fields().get(i);
        Expr param = e.args().get(i);
        sb.append(field.accept(this) + "=" + param.accept(this));

        if (i < classT.fields().size() - 1)
          sb.append(", ");
      }

      sb.append(")");
      return sb.toString();
    }
    else {
      // If calling a generator function
      if (e.name().endsWith("_gen")) {
        List<Expr> newArgs = new ArrayList<>(e.args());

        // Pass choose vars
        for (FuncDecl fnDecl : this.fns) {
          if (fnDecl.name().equals(e.name())) {
            ChooseBodyParamsFinder f = new ChooseBodyParamsFinder();
            fnDecl.body().accept(f);

            for (Tuple2<Integer,Integer> ID : f.IDs) {
              newArgs.add(new VarExpr("choose" + ID._1 + "_" + e.getId(), TypesFactory.Int));
              chooseIDs.put(ID._1 + "_" + e.getId(), (int) Math.ceil(Math.log(ID._2) / Math.log(2)));
            }

            break;
          }
        }

        return e.name() + printExprList(newArgs, "(", ", ", ")", false);
      }
      else
        return e.name() + printExprList(e.args(), "(", ", ", ")", false);
    }
  }

  @Override
  public String visit(BinaryExpr e)
  {
    if (BinaryExpr.Op.EQ == e.op()) {
      if (TypesFactory.isClassT(e.left().type()))
        return e.left().type().toString() + "_equals(" + printWithParen(e.left()) + ", " + printWithParen(e.right()) + ")";
      else
        return printWithParen(e.left()) + " == " + printWithParen(e.right());
    }
    else if (BinaryExpr.Op.ASHR == e.op()) {
      return "ashr(" + printWithParen(e.left()) + ", " + printWithParen(e.right()) + ")";
    }
    else if (BinaryExpr.Op.LSHR == e.op()) {
      return "lshr(" + printWithParen(e.left()) + ", " + printWithParen(e.right()) + ")";
    }
    else if (BinaryExpr.Op.SHL == e.op()) {
      return "shl(" + printWithParen(e.left()) + ", " + printWithParen(e.right()) + ")";
    }
    else if (BinaryExpr.Op.BXOR == e.op()) {
      return "xor(" + printWithParen(e.left()) + ", " + printWithParen(e.right()) + ")";
    }
    else if (BinaryExpr.Op.BAND == e.op()) {
      return "band(" + printWithParen(e.left()) + ", " + printWithParen(e.right()) + ")";
    }
    else if (BinaryExpr.Op.BOR == e.op()) {
      return "bor(" + printWithParen(e.left()) + ", " + printWithParen(e.right()) + ")";
    }
    else if (BinaryExpr.Op.IMPLY == e.op()) {
      return "(!" + printWithParen(e.left()) + ") || " + printWithParen(e.right());
    }
    else
      return printWithParen(e.left()) + " " + e.op() + " " + printWithParen(e.right());
  }

  @Override
  public String visit(UnaryExpr e)
  {
    if (e.op() == UnaryExpr.Op.BNOT)
      return "bnot(" + e.base().accept(this) + ")";
    else
      return e.op() + "(" + e.base().accept(this) + ")";
  }

  @Override
  public String visit(ChooseIntExpr e) {
    StringBuffer sb = new StringBuffer();

    int idNum = e.getId();
    consts.add(new Pair(idNum,e.getWidth()));

    sb.append("const" + (idNum++));

    return sb.toString();
  }

  private String printOptimizedChoose(ChooseExpr ch)
  {
    StringBuffer sb = new StringBuffer();

    // reset
    CountCallExprs.reset();

    Map<CallExpr, List<Integer>> genCalls = new HashMap<>();
    List<Expr> newArgs = new ArrayList<>();
    for (Expr choice : ch.args()){
      CountCallExprs cce = new CountCallExprs();
      newArgs.add(choice.accept(cce));
      cce.getCount().forEach((c, ids) -> {
        if (!genCalls.containsKey(c) || genCalls.get(c).size() < ids.size())
          genCalls.put(c, ids);
      });
    }

    // re-use calls to generators inside the body
    for (CallExpr c : genCalls.keySet()){
      for (int i=0; i<genCalls.get(c).size(); i++)
        sb.append(toSkType(c.type()) + " " + c.name() + "_" + CountCallExprs.getIds().get(c) + "_" + i + " = " + this.visit(c, genCalls.get(c).get(i)) + ";\n" + indent());
    }

    sb.append("return ");
    sb.append(new ChooseExpr(newArgs, ch.getId()).accept(this));

    sb.append(";\n");

    return sb.toString();
  }

  public String visit(ChooseExpr e)
  {
    StringBuffer sb = new StringBuffer();

    int chID = e.getId();
    if (prefix.equals("choose"))
      chooseIDs.put(chID+"", (int) Math.ceil(Math.log(e.args().size()) / Math.log(2)));

    if (e.args().size() == 1) {
      sb.append("(" + prefix + (chID) + " == 0 ? " + e.args().get(0).accept(this) + " : " + e.args().get(0).accept(this) + ")");
      return sb.toString();
    }

    sb.append("(" + prefix + (chID) + " == 0 ? " + e.args().get(0).accept(this));

    sb.append(" : ");

    for (int i = 1; i < e.args().size(); ++i)
    {
      // create a new choose var for every choose expressions except the last
      if (i < e.args().size() - 1) {
        sb.append(prefix + (chID) + " == " + i + " ? ");
      }

      sb.append(e.args().get(i).accept(this));

      if (TypesFactory.isFunctionT(e.type()))
      {
        sb.append("(");
        for (int j = 0; j < ((FunctionT)e.type()).paramsT().size(); ++j)
        {
          sb.append("p" + j);
          if (j != ((FunctionT) e.type()).paramsT().size() - 1)
            sb.append(", ");
        }
        sb.append(")");
      }

      if (i != e.args().size() - 1)
        sb.append(" : ");
    }

    sb.append(")");
    //return printExprList(e.args(), "{| ", " | ", " |}", false);

    return sb.toString();
  }

  @Override
  public String visit(ForallExpr e)
  {
    StringBuffer sb = new StringBuffer();

    String var = e.var().accept(this);
    sb.append("bit ret_val = true;\n" +
        indent() + "for (int " + var + "=" + e.start().accept(this) + "; " +
        var + "<" + e.end().accept(this) + "; " +
        "++" + var + ") {\n");

    ++this.indent;
    sb.append(indent() + "ret_val = ret_val && (" + e.body().accept(this) + ");\n");
    --this.indent;

    sb.append(indent() + "}\n" +
        indent() + "return ret_val");

    /*
    sb.append("return !(" + e.start().accept(this) + " && " + e.end().accept(this) + ") || " +
              e.body().accept(this));
              */

    return sb.toString();
  }

  @Override
  public String visit(LetExpr e)
  {
    StringBuffer sb = new StringBuffer();

    // print harness
    sb.append("harness void sketch");
    List<VarExpr> sorted = new ArrayList<>(e.vars());
    // TODO: Check if we really need to sort
    sorted.sort(Comparator.comparing((VarExpr x) -> x.name()));

    sb.append("(");
    for (Iterator<VarExpr> it = sorted.iterator(); it.hasNext();)
    {
      sb.append(serializeToHarness(it.next(), ""));
      if (it.hasNext())
        sb.append(", ");
    }
    sb.append(")");

    sb.append(" {\n");

    ++this.indent;

    this.listNum = 0;
    for (VarExpr v : sorted)
      sb.append(deserializeFromHarness(v, v.name(), ""));

    if (e.assumptions() != null)
      sb.append(indent() + "assume " + e.assumptions().accept(this) + ";\n");
    sb.append(indent() + "assert " + e.body().accept(this) + ";\n");
    --this.indent;

    sb.append("}\n");

    return sb.toString();
  }

  protected int listNum = 0;
  protected String listSize()
  {
    return "sz" + (listNum++);
  }

  protected int varId = 0;
  protected String id() { return "_t" + (varId++); }

  /**
   * v is the target that the deserialized variable should be assigned to
   */
  protected String deserializeFromHarness (VarExpr v, String src, String enclosedArray)
  {
    Type t = v.type();
    String n = v.name();

    if (TypesFactory.isPrimitiveT(t))
      return "";

    else if (TypesFactory.isListT(t))
    {
      Type elemT = ((ListT)t).elemT();

      if (TypesFactory.isPrimitiveT(elemT))
      {
        String ls = listSize();
        src = src + "_contents" + enclosedArray;
        return indent() + toSkType(t) + " " + n + " = new " + toSkType(t) + "(size=" + ls + ", contents=" + src + ");\n";
      }

      else if (TypesFactory.isListT(elemT) || TypesFactory.isClassT(elemT))
      {
        String ls = listSize();
        String it = ls + "_i"; // iterator variable
        String target = id();

        StringBuffer sb = new StringBuffer();

        sb.append(
          indent() + toSkType(t) + " " + n + " = new " + toSkType(t) + "(size=" + ls + ");\n" +
          indent() + "for (int " + it + " = 0; " + it + " < " + ls + "; ++" + it + ") {\n");

        ++indent;

        sb.append(
          deserializeFromHarness(new VarExpr(target, elemT), src, enclosedArray + "[" + it + "]") +
          indent() + n + ".contents[" + it + "] = " + target + ";\n");

        --indent;

        sb.append(indent() + "}\n");

        return sb.toString();
      }

      else
        throw new RuntimeException("don't know how to translate: " + v.name() + " type: " + t);
    }

    else if (TypesFactory.isPtrT(t))
    {
      PtrT ptrT = (PtrT)t;

      StringBuffer sb = new StringBuffer();

      if (TypesFactory.isPrimitiveT(ptrT.elemT()))
        sb.append(indent() + toSkType(ptrT) + " " + n + " = new " + toSkType(ptrT) + "(data="+n+"_data, offset=" + (v.name().endsWith("_init") ? "0" : v.name()+"_offset_1 + " + v.name()+"_offset_2") + ");\n");
      else
        throw new RuntimeException("don't know how to serialize array type: " + v.name());

      return sb.toString();
    }

    else if (TypesFactory.isBufferT(t))
    {
      BufferT bufT = (BufferT)t;

      StringBuffer sb = new StringBuffer();

      if (TypesFactory.isPrimitiveT(bufT.elemT())) {
        sb.append(indent() + toSkType(bufT) + " " + n + " = new " + toSkType(bufT) + "();\n");
        sb.append(indent() + n + ".data = " + n + "_data;\n");
        for (int dim=0; dim<bufT.dim(); dim++)
          sb.append(indent() + n + ".dim" + dim + "_extent = " + v.name().replace("_init","")+"_dim" + dim + ";\n");
      }
      else
        throw new RuntimeException("don't know how to serialize array type: " + v.name());

      return sb.toString();
    }

    // TODO: fix this once Shoaib decides how arrays should be encoded
    else if (TypesFactory.isArrayT(t))
    {
      ArrayT arrayT = (ArrayT)t;
      if (TypesFactory.isPrimitiveT(arrayT.elemT()))
        return "";
      else
        throw new RuntimeException("don't know how to serialize array type: " + v.name());
    }

    else if (TypesFactory.isClassT(v.type()))
    {
      ClassT classT = (ClassT)v.type();

      StringBuffer sb = new StringBuffer();
      sb.append(indent() + toSkType(classT) + " " + n + " = new " + toSkType(classT) + "();\n");

      for (VarExpr field : classT.fields())
      {
        String target = id();
        String serializedFieldName = src + "_" + field.name();
        Type fieldT = field.type();

        if (TypesFactory.isClassT(fieldT))
          sb.append(deserializeFromHarness(new VarExpr(target, field.type()), serializedFieldName, enclosedArray));
        else if (TypesFactory.isListT(fieldT))
          sb.append(deserializeFromHarness(new VarExpr(target, field.type()), serializedFieldName, enclosedArray));
        else
          sb.append(indent() + toSkType(fieldT) + " " + target + " = " + serializedFieldName + enclosedArray + ";\n");

        sb.append(indent() + n + "." + field.name() + " = " + target + ";\n");
      }

      return sb.toString();
    }

    else
      throw new RuntimeException("don't know how to deserialize " + v.name() + " from sketch harness, type: " + v.type());
  }


  protected String serializeToHarness (VarExpr v, String enclosedArray)
  {
    Type t = v.type();

    if (TypesFactory.isPrimitiveT(t))
    {
      if (enclosedArray == "")
        return toSkType(t) + " " + v.name();
      else
        return toSkType(t) + " " + enclosedArray + "] " + v.name();
    }

    else if (TypesFactory.isListT(t))
    {
      Type elemT = ((ListT) t).elemT();

      if (TypesFactory.isPrimitiveT(elemT))
      {
        String ls = listSize();

        if (enclosedArray == "")
          enclosedArray = "[" + ls + "]";
        else
          enclosedArray = enclosedArray + ", " + ls + "]";

        return "int " + ls + ", " + toSkType(elemT) + enclosedArray + " " + v.name() + "_contents";
      }

      else if (TypesFactory.isListT(elemT) || TypesFactory.isClassT(elemT))
      {
        String listSize = listSize();
        if (enclosedArray == "")
          enclosedArray = "[" + listSize;
        else
          enclosedArray = enclosedArray + ", " + listSize;

        return "int " + listSize + ", "  +
                serializeToHarness(new VarExpr(v.name(), elemT), enclosedArray);
      }

      else
        throw new RuntimeException("don't know how to translate: " + v.name() + " type: " + t);
    }

    else if (TypesFactory.isPtrT(t))
    {
      PtrT arrayT = (PtrT)t;
      if (TypesFactory.isPrimitiveT(arrayT.elemT()))
        return (v.name().endsWith("_init") ? "" : "int " + v.name() + "_offset_1, int " + v.name() + "_offset_2, ")  + toSkType(arrayT.elemT()) + "[ARRAY_LEN] " + v.name() + "_data";
      else
        throw new RuntimeException("don't know how to serialize array type: " + v.name());
    }

    // TODO: fix this once Shoaib decides how arrays should be encoded
    else if (TypesFactory.isArrayT(t))
    {
      ArrayT arrayT = (ArrayT)t;
      if (TypesFactory.isPrimitiveT(arrayT.elemT()))
        return toSkType(t) + " " + v.name();
      else
        throw new RuntimeException("don't know how to serialize array type: " + v.name());
    }

    else if (TypesFactory.isBufferT(t))
    {
      BufferT bufT = (BufferT)t;

      List<String> s = new ArrayList<>();

      if (TypesFactory.isPrimitiveT(bufT.elemT())) {
        if (v.name().endsWith("_init"))
          return toSkType(bufT.elemT()) + "[ARRAY_LEN] " + v.name() + "_data";
        else
          s.add (toSkType(bufT.elemT()) + "[ARRAY_LEN] " + v.name() + "_data");

        for (int dim=0; dim<bufT.dim(); dim++)
          s.add("int " + v.name() + "_dim" + dim);

        return String.join(", ", s);
      }
      else
        throw new RuntimeException("don't know how to serialize array type: " + v.name());
    }
    else if (TypesFactory.isClassT(t))
    {
      List<String> s = new ArrayList<>();

      for (VarExpr field : ((ClassT)v.type()).fields())
        s.add(serializeToHarness(new VarExpr(v.name() + "_" + field.name(), field.type()), enclosedArray));

      return String.join(", ", s);
    }

    else
      throw new RuntimeException("don't know how to serialize " + v.name() + " of type: " + v.type() + " to sketch harness");
  }

  @Override
  public String visit (IfExpr e)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(printWithParen(e.cond()) + " ? \n");

    ++this.indent;
    sb.append(indent() + printWithParen(e.cons()) + " : \n");
    sb.append(indent() + printWithParen(e.alt()));
    --this.indent;

    return sb.toString();
  }

  @Override
  public String visit(FuncDecl n)
  {
    if (n.isUnInterpreted())
    {
      if (n.name().equals("int_expr_grm") || n.name().equals("float_expr_grm"))
        return "";

      StringBuffer sb = new StringBuffer();

      sb.append(toSkType(n.retType()) + " " + n.name());
      sb.append(printExprList(n.params(), "(", ", ", ")", true));
      sb.append(";\n\n");

      return sb.toString();
    }
    else if (n.name().endsWith("_gen"))
    {
      StringBuffer sb = new StringBuffer();

      if (n.params().contains(new VarExpr("depth", TypesFactory.Int))) {
        // Function type
        sb.append("generator " + toSkType(n.retType()) + " " + n.name());
        sb.append(printExprList(n.params(), "(", ", ", ")", true));
        sb.append("{\n");

        ++this.indent;
        sb.append(this.indent());

        // Function body
        assert n.body() instanceof IfExpr;
        assert ((IfExpr) n.body()).cons() instanceof ChooseExpr;
        assert ((IfExpr) n.body()).alt() instanceof ChooseExpr;

        sb.append("if (" + ((IfExpr) n.body()).cond().accept(this) + ") {\n");

        ++this.indent;
        sb.append(this.indent());

        sb.append(printOptimizedChoose((ChooseExpr) ((IfExpr) n.body()).cons()));

        --this.indent;
        sb.append(this.indent());

        sb.append("} else {\n");

        ++this.indent;
        sb.append(this.indent());

        sb.append(printOptimizedChoose((ChooseExpr) ((IfExpr) n.body()).alt()));

        --this.indent;
        sb.append(this.indent());

        sb.append("}\n");
      }
      else {
        ChooseBodyParamsFinder f = new ChooseBodyParamsFinder();
        n.body().accept(f);

        List<VarExpr> fnParams = new ArrayList<>(n.params());
        for (Tuple2<Integer,Integer> ID : f.IDs)
          fnParams.add(new VarExpr("ch" + ID._1, TypesFactory.Int));

        sb.append("generator " + toSkType(n.retType()) + " " + n.name());
        sb.append(printExprList(fnParams, "(", ", ", ")", true));
        sb.append("{\n");

        ++this.indent;
        sb.append(this.indent());

        this.prefix = "ch";

        if (n.body() instanceof ChooseExpr) {
          sb.append(printOptimizedChoose((ChooseExpr) n.body()));
        }
        else {
          sb.append("return ");
          sb.append(n.body().accept(this));
          sb.append(";\n");
        }

        this.prefix = "choose";
      }

      --this.indent;
      sb.append("}\n\n");

      return sb.toString();
    }
    else
    {
      if (n.name().equals("lambda"))
      {
        StringBuffer sb = new StringBuffer();

        sb.append("(");
        sb.append(printExprList(n.params(), "(", ", ", ")", false));
        sb.append(" -> ");
        sb.append(n.body().accept(this));
        sb.append(")");

        return sb.toString();
      }
      else {
        StringBuffer sb = new StringBuffer();

        sb.append(toSkType(n.retType()) + " " + n.name());
        sb.append(printExprList(n.params(), "(", ", ", ")", true));
        sb.append("{\n");

        ++this.indent;
        sb.append(this.indent());
        // these special exprs generate their own return statement
        if (n.body() instanceof StoreExpr || n.body() instanceof ForallExpr ||
            n.body() instanceof ListCompExpr || n.body() instanceof IncrPtrExpr || n.body() instanceof DecrPtrExpr)
          sb.append(n.body().accept(this));
        else {
          sb.append("return ");
          sb.append(n.body().accept(this));
        }

        sb.append(";\n");

        --this.indent;
        sb.append("}\n\n");

        return sb.toString();
      }
    }
  }

  @Override
  public String visit(ClassDecl n)
  {
    String skName = toSkType(n.type());
    StringBuffer sb = new StringBuffer();

    sb.append("struct " + skName + "{\n");

    ++this.indent;
    for (VarExpr f : n.fields())
    {
      sb.append(this.indent());
      sb.append(toSkType(f.type()) + " ");
      sb.append(f.accept(this));
      sb.append(";\n");
    }
    --this.indent;
    sb.append("}\n\n");

    // class equality function

    sb.append("bit " + skName + "_equals(" + skName + " o1, " + skName + " o2) {\n");
    for (VarExpr f : n.fields())
    {
      Type fieldT = f.type();
      if (TypesFactory.isPrimitiveT(fieldT))
        sb.append("  if (o1." + f.name() + " != o2." + f.name() + ") return false;\n");
      else if (TypesFactory.isClassT(fieldT))
        sb.append("  if (" + ((ClassT)fieldT).name() + "_equals(o1." + f.name() + ", o2." + f.name() + ")) return false;\n");
      else if (TypesFactory.isListT(fieldT))
        sb.append("  if (" + toSkListFn("equals", (ListT)fieldT) + "(o1." + f.name() + ", o2." + f.name() + ")) return false;\n");
      else if (TypesFactory.isArrayT(fieldT))
        sb.append("  if (o1." + f.name() + " != o2." + f.name() + ") return false;\n");
    }
    sb.append("  return true;\n}\n\n");

    return sb.toString();
  }

  @Override
  public String visit(FieldExpr n)
  {
    if (n.target() instanceof VarExpr)
      return n.target().accept(this) + "." + n.field();
    else
      return "(" + n.target().accept(this) + ")." + n.field();
  }

  @Override
  public String visit(Program p)
  {
  /*
      overall structure:
      struct newDecls
      function newDecls
      list struct newDecls
      harness
   */

    // convert all array stores and forall exprs to function calls
    p = (Program)p.accept(new ExprLifter((Expr e) -> e instanceof StoreExpr || e instanceof ForallExpr || e instanceof IncrPtrExpr || e instanceof DecrPtrExpr));
    p = (Program)p.accept(new ListCompExprLifter(p.functions()));

    StringBuffer sb = new StringBuffer();

    // Include grammar file
    try {
      sb.append(Files.convert(Synthesizer.class.getResourceAsStream("grammar.skh"), Charset.defaultCharset()) + "\n\n");
    }
    catch (Exception e) { /* should never happen */ }

    // Ultimately, I suppose this should come from some config file or user param.
    sb.append("int ARRAY_LEN = " + Preferences.Sketch.arr_sz_bnd + ";\n");

    // All array select and stores are padding with offset
    sb.append("int OFFSET = " + Preferences.Sketch.arr_sz_bnd / 2 + ";\n\n");

    for (ClassDecl c : p.classes())
      sb.append(c.accept(this));

    fns = p.functions();
    for (FuncDecl f : p.functions())
      sb.append(f.accept(this));

    if (p.body() instanceof LetExpr)
      sb.append(p.body().accept(this));

    else
    {
      // print harness directly
      sb.append("harness void sketch() {\n");
      sb.append("  assert " + p.body().accept(this) + ";\n");
      sb.append("}\n");
    }

    // print the global choose vars
    for (String id : chooseIDs.keySet())
      sb.append("int choose" + id + " = ??(" + chooseIDs.get(id) + ");\n");

    // print the global const vars
    for (Pair<Integer, Integer> c : consts)
      sb.append("int const" + c.a + " = ??(" + c.b + ");\n");

    sb.append(printListFunctions());

    sb.append(printPtrFunctions());

    sb.append(printBufferFunctions());

    sb.append(printOpFunctions());

    return sb.toString();
  }

  /* integers */
  @Override
  public String visit(IntLitExpr e)
  {
    return e.value() + "";
  }

  @Override
  public String visit(FloatLitExpr e) { return Math.round(e.value()) + ""; }

  @Override
  public String visit(UIExpr uiExpr) {
    return "2";
  }

  protected String printPtrFunctions() {
    if (TypesFactory.ptrTypes().size() == 0)
      return "";

    StringBuffer sb = new StringBuffer();
    sb.append("\n\n/*** Ptr declarations ***/\n\n");

    // make list printing deterministic for testing purposes
    List<PtrT> ptrs = new ArrayList<>(TypesFactory.ptrTypes());
    ptrs.sort(Comparator.comparing((PtrT t) -> t.toString()));

    List<String> handled = new ArrayList<>();

    for (PtrT t : ptrs)
    {
      String skPtrT = toSkType(t);
      String skElemType = toSkType(t.elemT());

      if (handled.contains(skPtrT))
        continue;

      handled.add(skPtrT);

      /* declaration
         struct PtrT <T> { int offset; T [size] data; }
       */
      sb.append("struct " + skPtrT + " {\n" +
                "  int offset;\n" +
                "  " + skElemType + "[ARRAY_LEN] data;\n" +
                "}\n\n");
    }

    return sb.toString();
  }

  protected String printBufferFunctions() {
    if (TypesFactory.bufferTypes().size() == 0)
      return "";

    StringBuffer sb = new StringBuffer();
    sb.append("\n\n/*** Buffer declarations ***/\n\n");

    // make buffer printing deterministic for testing purposes
    List<BufferT> buffers = new ArrayList<>(TypesFactory.bufferTypes());
    buffers.sort(Comparator.comparing((BufferT t) -> t.toString()));

    List<String> handled = new ArrayList<>();

    for (BufferT t : buffers)
    {
      String skBufferT = "Buffer" + t.dim() + "D <T>";

      if (handled.contains(skBufferT))
        continue;

      handled.add(skBufferT);

      /* declaration
         struct Buffer <T> { T [size] data; int dim0_extent; int dim1_extent; ... }
       */
      sb.append("struct " + skBufferT + " {\n" +
          "  T [ARRAY_LEN] data;\n");

      for (int dim=0; dim<t.dim(); dim++)
          sb.append("  int dim" + dim + "_extent;\n");

      sb.append("}\n\n");
    }

    return sb.toString();
  }

  /* lists */
  protected String printListFunctions ()
  {
    if (TypesFactory.listTypes().size() == 0)
      return "";

    StringBuffer sb = new StringBuffer();
    sb.append("\n\n/*** List declarations and functions **/\n\n");

    // make list printing deterministic for testing purposes
    List<ListT> lists = new ArrayList<>(TypesFactory.listTypes());
    lists.sort(Comparator.comparing((ListT t) -> t.toString()));

    for (ListT t : lists)
    {
      String skListT = toSkType(t);
      String skElemType = toSkType(t.elemT());

      /* declaration
         struct ListT { int size; T [size] contents; }
       */
      sb.append("struct " + skListT + " {\n" +
              "  int size;\n" +
              "  " + skElemType + "[size] contents;\n" +
              "}\n\n");

      /* get
         T ListT_list_get(ListT l, int index) {
           if (index < l.size) return l.contents[index];
           else assert false;
         }
       */
      sb.append(skElemType + " " + toSkListFn("get", t) + "(" + skListT + " l, int index) {\n" +
              "  if (index < l.size) return l.contents[index];\n" +
              "  else assert false;\n" +
              "}\n\n");

      /* length
         int ListT_len(ListT l) { return l.size; }
       */
      sb.append("int " + toSkListFn("len", t) + "(" + skListT + " l) {\n" +
              "  return l.size;\n" +
              "}\n\n");

      /* set
         ListT ListT_list_set (ListT l, T v, int index) {
           if (index < l.size) {
             ListT r = l;
             r.contents[index] = v;
             return l;
           }
           else assert false;
         }
       */
      sb.append(skListT + " " + toSkListFn("set", t) + "(" + skListT + " l, " + skElemType + " v, int index) {\n" +
              "  if (index < l.size) {\n" +
              "    " + skListT + " r = l;\n" +
              "    r.contents[index] = v;\n" +
              "    return l;\n" +
              "  }\n" +
              "  else assert false;\n" +
              "}\n\n");

      /* equals
         bit ListT_equals (ListT l1, ListT l2) {
           if (l1.size != l2.size) return false;
           for (int i = 0; i < l1.size; ++i) {
             if (l1.contents[i].f1 != l2.contents[i].f1) return false;
             ...
           }
           return true;
         }
       */
      String eqDef = "";
      if (TypesFactory.isPrimitiveT(t.elemT()))
        eqDef = "    if (l1.contents[i] != l2.contents[i]) return false;\n";
      else if (TypesFactory.isClassT(t.elemT()))
      {
        ClassT classT = (ClassT)t.elemT();
        eqDef += "    if (!" + classT.name() + "_equals(l1.contents[i], l2.contents[i])) return false;\n";
      }
      else if (TypesFactory.isListT(t.elemT()))
        eqDef += "    if (!" + toSkListFn("equals", (ListT)t.elemT()) + "(l1.contents[i], l2.contents[i])) return false;\n";
      else
        throw new RuntimeException("don't know how to define equality for: " + t);

      sb.append("bit " + toSkListFn("equals", t) + "(" + skListT + " l1, " + skListT + " l2) {\n" +
              "  if (l1.size != l2.size) return false;\n" +
              "  for (int i = 0; i < l1.size; ++i) {\n" +
              eqDef +
              "  }\n" +
              "  return true;\n" +
              "}\n\n");

      /* append
         ListT ListT_append(ListT l, T v) {
           ListT r = new ListT(size = l.size + 1, contents = l.contents);
           r.contents[r.size - 1] = v;
           return r;
         }
       */
      sb.append(skListT + " " + toSkListFn("append", t) + "(" + skListT + " l, " + skElemType + " v) {\n" +
              "  " + skListT + " r = new " + skListT + "(size = l.size + 1, contents = l.contents);\n" +
              "  r.contents[r.size - 1] = v;\n" +
              "  return r;\n" +
              /*
              "  if (l.size == ARRAY_LEN) assert false;\n" +
              "  " + skListT + " r = new " + skListT + "();\n" +
              "  r.contents = l.contents;\n" +
              "  r.contents[l.size] = v;\n" +
              "  r.size = l.size + 1;\n" +
              "  return r;\n" +
              */
              "}\n\n");

      /* concat
         ListT ListT_concat(ListT l1, ListT l2) {
           if (l2.size == 0) return l1;
           else
           {
             ListT out = new ListT(size=l1.size + l2.size);
             for (int i = 0; i < l1.size; ++i)
               out.contents[i] = l1.contents[i];

             for (int i = 0; i < l2.size; ++i)
               out.contents[i+l1.size] = l2.contents[i];

             return out;
           }
         }
       */
      sb.append(skListT + " " + toSkListFn("concat", t) + "(" + skListT + " l1, " + skListT + " l2) {\n" +
              "  if (l2.size == 0) return l1;\n" +
              "  else\n" +
              "  {\n" +
              "     " + skListT + " out = new " + skListT + "(size=l1.size + l2.size);\n" +
              "     for (int i = 0; i < l1.size; ++i)\n" +
              "       out.contents[i] = l1.contents[i];\n" +
              "     for (int i = 0; i < l2.size; ++i)\n" +
              "       out.contents[i+l1.size] = l2.contents[i];\n" +
              "     return out;\n" +
              "  }\n" +
              "}\n\n");

      /* slice: [start, end)
         ListT slice (ListT l, int start, int end) {
           if (start < 0 || end < start)
             return emptyList;
           else
             return new ListT(size=(end-start), contents=l.contents[start::(end-start)]);
         }
       */
      sb.append(skListT + " " + toSkListFn("slice", t) + "(" + skListT + " l, int start, int end) {\n" +
              "  if (start < 0 || end < start) return new " + skListT + "(size=0);\n" +
              "  else if (end > l.size) return l;\n" +
              "  else\n" +
              "    return new " + skListT + "(size=(end-start), contents=l.contents[start::(end-start)]);\n" +
              "}\n\n");

      /* empty
         ListT ListT_empty() { return new ListT(size = 0); }
       */
      sb.append(skListT + " " + toSkListFn("empty", t) + "() {\n" +
              "  return new " + skListT + "(size = 0);\n" +
              "}\n\n");
    }

    return sb.toString();
  }

  protected String toSkListFn(String origName, ListT t)
  {
    return toSkType(t) + "_" + origName;
  }

  @Override
  public String visit(EqualsExpr e)
  {
    return visit(new CallExpr(toSkListFn(e.name(), (ListT)e.list1().type()), e.args(), e.getId()));
  }

  @Override
  public String visit(GetExpr e)
  {
    return visit(new CallExpr(toSkListFn(e.name(), (ListT)e.list().type()), e.args(), e.getId()));
  }

  @Override
  public String visit(LengthExpr e)
  {
    return visit(new CallExpr(toSkListFn(e.name(), (ListT)e.list().type()), e.args(), e.getId()));
  }

  @Override
  public String visit(EmptyListExpr e)
  {
    return visit(new CallExpr(toSkListFn(e.name(), (ListT)e.type()), e.args(), e.getId()));
  }

  @Override
  public String visit(AppendExpr e) { return visit(new CallExpr(toSkListFn(e.name(), (ListT)e.type()), e.args(), e.getId())); }

  @Override
  public String visit(SliceExpr e) { return visit(new CallExpr(toSkListFn(e.name(), (ListT)e.type()), e.args(), e.getId())); }

  @Override
  public String visit(ConcatExpr e) { return visit(new CallExpr(toSkListFn(e.name(), (ListT)e.type()), e.args(), e.getId())); }

  /**
   *  e (for t1 in l1) (for t2 in t1)  -->

     T listCompFn1 (T e, arg1, arg2, ...) { return [[ p(e, arg1, arg2) ]] }

     List<T> listComp1 (List l1, fun p, arg1, arg2, ...)
     {
       int outSize = 0;
       for (int i = 0; i < l1.size; ++i)
       {
         t1 = l1.contents[i];
         for (int j = 0; j < t1.size; ++j) // replace t1.size with 0 if empty list
         {
           t2 = t1.contents[j];
           T r = listCompFn1(t2, p, arg1, arg2, ...);
           if (r != null) ++outSize;
         }
       }

       List<T> out = new List<T>(size=outSize);
       int j = 0;
       for (int i = 0; i < l.size; ++i)
       {
         t1 = l1.contents[i];
         for (int j = 0; j < t1.size; ++j) // replace t1.size with 0 if empty list
         {
           t2 = t1.contents[j];
           T r = listCompFn1(t2, p, arg1, arg2, ...);
           if (r != null) { out.contents[j++] = r; }
         }
       }
       return out;
     }
   */
  @Override
  public String visit(ListCompExpr e)
  {
    String outElemTStr = toSkType(e.expr().type());
    String outTStr = toSkType(e.type());

    StringBuffer sb = new StringBuffer();

    sb.append("int _outSize = 0;\n");

    for (Pair<VarExpr, Expr> it : e.iters())
    {
      Expr list = it.b;
      String listStr;
      if (list instanceof VarExpr)
        listStr = list.accept(this);
      else
      {
        listStr = id();
        sb.append(indent() + toSkType(list.type()) + " " + listStr + " = " + list.accept(this) + ";\n");
      }

      VarExpr var = it.a;
      String varStr = var.accept(this);
      String varSkT = toSkType(var.type());

      String i = id();

      String listSize = list instanceof EmptyListExpr ? "0" : listStr + ".size";

      sb.append(indent() + "for (int " + i + " = 0; " + i + " < " + listSize + "; ++" + i + ") {\n");
      ++indent;
      sb.append(indent() + varSkT + " " + varStr + " = " + listStr + ".contents[" + i + "];\n");
    }

    sb.append(indent() + outElemTStr + " _r = " + e.expr().accept(this) + ";\n");
    if (!TypesFactory.isPrimitiveT(e.expr().type()))
      sb.append(indent() + "if (_r != null) ++_outSize;\n");
    else
      sb.append(indent() + "++_outSize;\n");

    for (int i = 0; i < e.iters().size(); ++i)
    {
      --indent;
      sb.append(indent() + "}\n");
    }


    sb.append(indent() + outTStr + " _out = new " + outTStr + "(size=_outSize);\n" +
              indent() + "int _outIndex = 0;\n");

    for (Pair<VarExpr, Expr> it : e.iters())
    {
      Expr list = it.b;
      String listStr;
      if (list instanceof VarExpr)
        listStr = list.accept(this);
      else
      {
        listStr = id();
        sb.append(indent() + toSkType(list.type()) + " " + listStr + " = " + list.accept(this) + ";\n");
      }

      VarExpr var = it.a;
      String varStr = var.accept(this);
      String varSkT = toSkType(var.type());

      String i = id();

      String listSize = list instanceof EmptyListExpr ? "0" : listStr + ".size";

      sb.append(indent() + "for (int " + i + " = 0; " + i + " < " + listSize + "; ++" + i + ") {\n");
      ++indent;
      sb.append(indent() + varSkT + " " + varStr + " = " + listStr + ".contents[" + i + "];\n");
    }

    sb.append(indent() + outElemTStr + " _r = " + e.expr().accept(this) + ";\n");
    if (!TypesFactory.isPrimitiveT(e.expr().type()))
      sb.append(indent() + "if (_r != null) { _out.contents[_outIndex++] = _r; }\n");
    else
      sb.append(indent() + "_out.contents[_outIndex++] = _r;\n");

    for (int i = 0; i < e.iters().size(); ++i)
    {
      --indent;
      sb.append(indent() + "}\n");
    }

    sb.append(indent() + "return _out");

    return sb.toString();
  }

  /* arrays */

  @Override
  public String visit(SelectExpr e)
  {
    if  (e.array().type() instanceof PtrT)
    {
      return e.array().accept(this) + ".data[OFFSET + " + e.array().accept(this) + ".offset + (" + e.index().get(0).accept(this) + ")]";
    }
    else if  (e.array().type() instanceof BufferT)
    {
      // assume planar mem layout
      // assume 2D
      assert (((BufferT) e.array().type()).dim() == 2);
      return e.array().accept(this) + ".data[OFFSET + (" + e.index().get(0).accept(this) + ") + (" + e.index().get(1).accept(this) + ") * " + e.array().accept(this) + ".dim0_extent]";
    }
    else
    {
      // Temporary hack for STNG
      List<Expr> indexes = new ArrayList<>();
      for(Expr index : e.index())
        indexes.add(new BinaryExpr(new VarExpr("OFFSET", TypesFactory.Int),BinaryExpr.Op.PLUS,index));

      return e.array().accept(this) + printExprList(indexes, "[", "][", "]", false);
    }
  }

  @Override
  public String visit(StoreExpr e)
  {
    StringBuffer sb = new StringBuffer();
    String skArrayType = toSkType(e.array().type());

    // Temporary hack for STNG
    List<Expr> indexes = new ArrayList<>();
    for(Expr index : e.index())
      indexes.add(new BinaryExpr(new VarExpr("OFFSET", TypesFactory.Int),BinaryExpr.Op.PLUS,index));

    if  (e.array().type() instanceof PtrT)
      sb.append(skArrayType + " r = new " + skArrayType + "(data=(" + e.array().accept(this) + ".data), offset=(" + e.array().accept(this) + ".offset));\n" +
          indent() + "r.data[OFFSET + r.offset + (" + e.index().get(0).accept(this) + ")] = " + e.value().accept(this) + ";\n" +
          indent() + "return r");
    else if (e.array().type() instanceof BufferT) {
      assert (((BufferT) e.array().type()).dim() == 2);
      sb.append(skArrayType + " r = new " + skArrayType + "(data=" + e.array().accept(this) + ".data, dim0_extent=" + e.array().accept(this) + ".dim0_extent, dim1_extent=" + e.array().accept(this) + ".dim1_extent);\n" +
          indent() + "r.data[OFFSET + (" + e.index().get(0).accept(this) + ") + (" + e.index().get(1).accept(this) + ") * " + e.array().accept(this) + ".dim0_extent] = " + e.value().accept(this) + ";\n" +
          indent() + "return r");
    }
    else
      sb.append(skArrayType + " r = " + e.array().accept(this) + ";\n" +
          indent() +"r" + printExprList(indexes, "[", "][", "]", false) + " = " + e.value().accept(this) + ";\n" +
          indent() +"return r");

    return sb.toString();
  }

  @Override
  public String visit(ForEach e) {
    throw new RuntimeException("don't know how to translate ForEach macro to sketch");
  }

  @Override
  public String visit(Variables e) {
    throw new RuntimeException("don't know how to translate Variables macro to sketch");
  }

  @Override
  public String visit(Bound bound) {
    throw new RuntimeException("don't know how to translate Bounds macro to sketch");
  }

  @Override
  public String visit(Init init) {
    throw new RuntimeException("don't know how to translate init macro to sketch");
  }

  @Override
  public String visit(Scope e) {
    throw new RuntimeException("don't know how to translate var macro to sketch");
  }

  @Override
  public String visit(TupleExpr e) {
    return printExprList(e.vals(), "{", ",", "}", false);
  }

  private String printOpFunctions()
  {
    String funcs = "";

    funcs += "\nint ashr (int v1, int v2);\n\n";

    funcs += "int xor (int v1, int v2);\n\n";

    funcs += "int band (int v1, int v2);\n\n";

    funcs += "int bor (int v1, int v2);\n\n";

    funcs += "int bnot (int v1);\n\n";

    funcs += "int lshr (int v1, int v2) { \n" +
        "  if (v2 == 0) return v1;\n" +
        "  if (v2 == 1) return v1 / 2;\n" +
        "  if (v2 == 2) return v1 / 4;\n" +
        "  if (v2 == 3) return v1 / 8;\n" +
        "  return -100;\n" +
        "}\n\n";

    funcs += "int shl (int v1, int v2) { \n" +
        "  if (v2 == 0) return v1;\n" +
        "  if (v2 == 1) return v1 * 2;\n" +
        "  if (v2 == 2) return v1 * 4;\n" +
        "  if (v2 == 3) return v1 * 8;\n" +
        "  return -100;\n" +
        "}";

    return funcs;
  }
}