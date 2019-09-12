package dexter.ir.parser;

import dexter.ir.Expr;
import dexter.ir.Printer;
import dexter.ir.array.*;
import dexter.ir.bool.*;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.ForallExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.integer.UIExpr;
import dexter.ir.list.*;
import dexter.ir.macro.*;
import dexter.ir.tuple.TupleExpr;
import dexter.ir.type.FunctionT;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;
import org.antlr.v4.runtime.misc.Pair;

import java.util.*;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class BuildASTVisitor extends DexterIRBaseVisitor<Expr>
{
  public int chooseID;

  public BuildASTVisitor() {
    chooseID = 0;
  }

  protected Type parseType (DexterIRParser.TypeContext ctx)
  {
    if (ctx instanceof DexterIRParser.ClassTypeContext)
    {
      return TypesFactory.lookupClassT(ctx.getText());
    }
    else if (ctx instanceof DexterIRParser.ArrayTypeContext)
    {
      DexterIRParser.ArrayTypeContext c = (DexterIRParser.ArrayTypeContext)ctx;
      return TypesFactory.arrayT(Integer.parseInt(c.dim.getText()), parseType(c.elemT));
    }
    else if (ctx instanceof DexterIRParser.BufferTypeContext)
    {
      DexterIRParser.BufferTypeContext c = (DexterIRParser.BufferTypeContext)ctx;
      return TypesFactory.bufferT(parseType(c.elemT), Integer.parseInt(c.dim.getText()));
    }
    else if (ctx instanceof DexterIRParser.PointerTypeContext)
    {
      DexterIRParser.PointerTypeContext c = (DexterIRParser.PointerTypeContext)ctx;
      return TypesFactory.ptrT(parseType(c.elemT));
    }
    else if (ctx instanceof DexterIRParser.ListTypeContext)
    {
      return TypesFactory.listT(parseType(((DexterIRParser.ListTypeContext) ctx).elemT));
    }
    else if (ctx instanceof DexterIRParser.TupleTypeContext)
    {
      DexterIRParser.TupleTypeContext c = (DexterIRParser.TupleTypeContext) ctx;
      return TypesFactory.tupleT(parseType(c.elemT), Integer.parseInt(((DexterIRParser.TupleTypeContext) ctx).sz.getText()));
    }
    else if (ctx instanceof DexterIRParser.FnTypeContext)
    {
      List<Type> parsedTypes = new ArrayList<>();

      for (DexterIRParser.TypeContext c : ((DexterIRParser.FnTypeContext) ctx).type())
        parsedTypes.add(parseType(c));

      if (parsedTypes.size() == 0)
        throw new RuntimeException("function types need to at least contain the return type");

      return TypesFactory.functionT("lambda", parsedTypes.get(parsedTypes.size() - 1),
              parsedTypes.subList(0, parsedTypes.size() - 1));
    }
    else if (ctx instanceof DexterIRParser.BitvectorTypeContext)
    {
      return TypesFactory.bitvectorT(Integer.parseInt(((DexterIRParser.BitvectorTypeContext) ctx).w.getText()));
    }
    else if (ctx instanceof DexterIRParser.BaseTypeContext)
    {
      String name = ctx.getText();
      if (name.equals("int"))
        return TypesFactory.Int;
      else if (name.equals("int"))
        return TypesFactory.Int;
      else if (name.equals("int8_t"))
        return TypesFactory.Int8;
      else if (name.equals("int16_t"))
        return TypesFactory.Int16;
      else if (name.equals("int32_t"))
        return TypesFactory.Int32;
      else if (name.equals("uint8_t"))
        return TypesFactory.UInt8;
      else if (name.equals("uint16_t"))
        return TypesFactory.UInt16;
      else if (name.equals("uint32_t"))
        return TypesFactory.UInt32;
      else if (name.equals("bool"))
        return TypesFactory.Bool;
      else if (name.equals("float"))
        return TypesFactory.Float;
      else
        throw new RuntimeException("Unknown base type: " + ctx.getText());
    }
    else
      throw new RuntimeException("Unknown type: " + ctx.getText());
  }

  @Override
  public Expr visitProgram(DexterIRParser.ProgramContext ctx)
  {
    // parse class newDecls first as function newDecls might refer to them
    List<ClassDecl> classDecls = new ArrayList<>();

    for (DexterIRParser.ClassDeclContext c : ctx.classDecl())
    {
      ClassDecl cd = (ClassDecl) visit(c);
      if (classDecls.contains(cd))
        throw new RuntimeException("duplicate class declaration: " + cd.name());

      classDecls.add(cd);
    }

    List<FuncDecl> fnDecls = new ArrayList<>();

    for (DexterIRParser.FnDeclContext c : ctx.fnDecl())
    {
      FuncDecl f = (FuncDecl) visit(c);
      if (fnDecls.contains(f))
        throw new RuntimeException("duplicate declaration of: " + f.name());

      fnDecls.add(f);
    }

    for (DexterIRParser.UFnDeclContext c : ctx.uFnDecl())
    {
      FuncDecl f = (FuncDecl) visit(c);
      if (fnDecls.contains(f))
        throw new RuntimeException("duplicate declaration of: " + f.name());

      fnDecls.add(f);
    }

    return ctx.expr() == null ? new Program(fnDecls, classDecls, null) : new Program(fnDecls, classDecls, visit(ctx.expr()));
  }

  @Override
  public Expr visitClassDecl (DexterIRParser.ClassDeclContext ctx)
  {
    List<VarExpr> fields = new ArrayList<>();

    for (DexterIRParser.VarDeclContext c : ctx.varDecl())
    {
      VarExpr f = (VarExpr)visit(c);
      if (fields.contains(f))
        throw new RuntimeException("duplicate field declaration: " + f.name());

      fields.add(f);
    }

    Expr r = new ClassDecl(ctx.name.getText(), fields);
    r.type(TypesFactory.classT(ctx.name.getText(), fields));

    return r;
  }

  @Override
  public Expr visitUFnDecl(DexterIRParser.UFnDeclContext ctx)
  {
    List<VarExpr> params = new ArrayList<>();
    List<Type> paramsT = new ArrayList<>();

    for (DexterIRParser.VarDeclContext c : ctx.varDecl())
    {
      VarExpr p = (VarExpr)visit(c);
      if (params.contains(p))
        throw new RuntimeException("duplicate declaration of: " + p.name());

      params.add(p);
      paramsT.add(p.type());
    }

    Type returnT = parseType(ctx.retType);
    Expr r = new FuncDecl(ctx.name.getText(), params, returnT, null);
    r.type(TypesFactory.functionT(ctx.name.getText(), returnT, paramsT));

    return r;
  }

  @Override
  public Expr visitFnDecl(DexterIRParser.FnDeclContext ctx)
  {
    List<VarExpr> params = new ArrayList<>();
    List<Type> paramsT = new ArrayList<>();

    for (DexterIRParser.VarDeclContext c : ctx.varDecl())
    {
      VarExpr p = (VarExpr)visit(c);
      if (params.contains(p))
        throw new RuntimeException("duplicate declaration of: " + p.name());

      params.add(p);
      paramsT.add(p.type());
    }

    Type returnT = parseType(ctx.retType);
    Expr r = new FuncDecl(ctx.name.getText(), params, returnT, visit(ctx.body));
    r.type(TypesFactory.functionT(ctx.name.getText(), returnT, paramsT));

    return r;
  }

  @Override
  public Expr visitLetExpr(DexterIRParser.LetExprContext ctx)
  {
    Set<VarExpr> m = new HashSet<>();

    for (DexterIRParser.VarDeclContext v : ctx.varDecl())
    {
      VarExpr var = (VarExpr)visit(v);
      var.type(parseType(v.type()));
      if (m.contains(var))
        throw new RuntimeException("duplicate declaration of: " + v.getText());
      else
        m.add(var);
    }

    if (ctx.as == null)
      return new LetExpr(m, visit(ctx.body));
    else
      return new LetExpr(m, visit(ctx.body), visit(ctx.as));
  }

  @Override
  public Expr visitFieldExpr (DexterIRParser.FieldExprContext ctx)
  {
    return new FieldExpr(visit(ctx.obj), ctx.field.getText());
  }

  @Override
  public Expr visitVarDecl(DexterIRParser.VarDeclContext ctx)
  {
    return new VarExpr(ctx.name.getText(), parseType(ctx.type()));
  }

  @Override
  public Expr visitListCompExpr(DexterIRParser.ListCompExprContext ctx)
  {
    return new ListCompExpr(visit(ctx.e),
                dexter.ir.Util.map(ctx.forIter(), c -> new Pair<>(new VarExpr(c.ID().getText()), visit(c.expr()))));
  }

  @Override
  public Expr visitForallExpr(DexterIRParser.ForallExprContext ctx)
  {
    return new ForallExpr(new VarExpr(ctx.ID().getText()), visit(ctx.start),
                          visit(ctx.end), visit(ctx.body));
  }

  @Override
  public Expr visitIfExpr(DexterIRParser.IfExprContext ctx)
  {
    return ctx.alt != null ?
            new IfExpr(visit(ctx.cond), visit(ctx.cons), visit(ctx.alt)) :
            new IfExpr(visit(ctx.cond), visit(ctx.cons), null);
  }

  @Override
  public Expr visitParenExpr(DexterIRParser.ParenExprContext ctx)
  {
    return visit(ctx.expr());
  }

  @Override
  public Expr visitImplyExpr(DexterIRParser.ImplyExprContext ctx)
  {
    // rewrite P->Q to neg(P) or Q

    Expr antecedent = visit(ctx.ls);
    Expr consequent = visit(ctx.rs);
    return new BinaryExpr(antecedent, BinaryExpr.Op.IMPLY, consequent);
  }

  @Override
  public Expr visitUnaryExpr(DexterIRParser.UnaryExprContext ctx)
  {
    UnaryExpr.Op op;

    switch (ctx.op.getType())
    {
      case DexterIRParser.NOT: { op = UnaryExpr.Op.NOT; break; }
      case DexterIRParser.BNOT: { op = UnaryExpr.Op.BNOT; break; }
      case DexterIRParser.PLUS: { op =  UnaryExpr.Op.PLUS; break; }
      case DexterIRParser.MINUS: { op = UnaryExpr.Op.MINUS; break; }

      default: throw new RuntimeException("unknown unary op: " + ctx.op);
    }

    if (op == UnaryExpr.Op.PLUS) // Ignore preceding "+"
      return visit(ctx.b);

    return new UnaryExpr(op, visit(ctx.b));
  }

  @Override
  public Expr visitBinaryExpr(DexterIRParser.BinaryExprContext ctx)
  {
    BinaryExpr.Op op;

    switch (ctx.op.getType())
    {
      case DexterIRParser.AND: { op = BinaryExpr.Op.AND; break; }
      case DexterIRParser.OR: { op = BinaryExpr.Op.OR; break; }
      case DexterIRParser.EQ: { op =  BinaryExpr.Op.EQ; break; }
      case DexterIRParser.NEQ: { op = BinaryExpr.Op.NEQ; break; }

      case DexterIRParser.PLUS: { op = BinaryExpr.Op.PLUS; break; }
      case DexterIRParser.MINUS: { op = BinaryExpr.Op.MINUS; break; }
      case DexterIRParser.MULT: { op = BinaryExpr.Op.MULT; break; }
      case DexterIRParser.DIV: { op = BinaryExpr.Op.DIV; break; }
      case DexterIRParser.MOD: { op = BinaryExpr.Op.MOD; break; }

      case DexterIRParser.LT: { op = BinaryExpr.Op.LT; break; }
      case DexterIRParser.LE: { op = BinaryExpr.Op.LE; break; }
      case DexterIRParser.GE: { op = BinaryExpr.Op.GE; break; }
      case DexterIRParser.GT: { op = BinaryExpr.Op.GT; break; }

      case DexterIRParser.SHL: { op = BinaryExpr.Op.SHL; break; }
      case DexterIRParser.LSHR: { op = BinaryExpr.Op.LSHR; break; }
      case DexterIRParser.ASHR: { op = BinaryExpr.Op.ASHR; break; }
      case DexterIRParser.BAND: { op = BinaryExpr.Op.BAND; break; }
      case DexterIRParser.BOR: { op = BinaryExpr.Op.BOR; break; }
      case DexterIRParser.BXOR: { op = BinaryExpr.Op.BXOR; break; }

      default: throw new RuntimeException("unknown binary op: " + ctx.op);
    }

    return new BinaryExpr(visit(ctx.l), op, visit(ctx.r));
  }

  @Override
  public Expr visitVarExpr(DexterIRParser.VarExprContext ctx)
  {
    return new VarExpr(ctx.ID().getText());
  }

  @Override
  public Expr visitBoolLitExpr(DexterIRParser.BoolLitExprContext ctx)
  {
    Expr e = new BoolLitExpr(ctx.getText().equals("true"));
    e.type(TypesFactory.Bool);
    return e;
  }

  @Override
  public Expr visitChooseExpr (DexterIRParser.ChooseExprContext ctx)
  {
    int id = chooseID;
    chooseID += 1;

    if (!ctx.t.getText().equals("int"))
      throw new RuntimeException("choose can only be applied to int types");

    return new ChooseIntExpr(id, Integer.parseInt(ctx.bw.getText())).type(TypesFactory.Int);
  }

  @Override
  public Expr visitNullExpr (DexterIRParser.NullExprContext ctx)
  {
    Type t = this.parseType(ctx.t);
    if (!(TypesFactory.isClassT(t) || TypesFactory.isListT(t)))
      throw new RuntimeException("null can only be applied to class or list types");

    Expr r = new NullExpr();
    r.type(t);
    return r;
  }

  private void checkArgs (List<Expr> args, int numExpected, String fnName)
  {
    if (args.size() != numExpected)
      throw new RuntimeException("incorrect argument passed to " + fnName + ":" +
              Arrays.toString(args.stream().map(a -> a.accept(new Printer())).toArray()));
  }

  @Override
  public Expr visitCallExpr(DexterIRParser.CallExprContext ctx) {
    List<Expr> args = new ArrayList<>();
    for (DexterIRParser.ExprContext e : ctx.expr())
      args.add(visit(e));

    String name = ctx.name.getText();
    if (name.equals("choose")) {
      int id = chooseID;
      chooseID += args.size();
      return new ChooseExpr(args, id);

    }

    /* list functions */
    else if (name.equals("equals")) {
      checkArgs(args, 2, "equals");
      return new EqualsExpr(args.get(0), args.get(1));
    } else if (name.equals("get")) {
      checkArgs(args, 2, "get");
      return new GetExpr(args.get(0), args.get(1));
    } else if (name.equals("len")) {
      checkArgs(args, 1, "len");
      return new LengthExpr(args.get(0));
    } else if (name.equals("concat")) {
      checkArgs(args, 2, "concat");
      return new ConcatExpr(args.get(0), args.get(1));
    } else if (name.equals("slice")) {
      checkArgs(args, 3, "slice");
      return new SliceExpr(args.get(0), args.get(1), args.get(2));
    }

    /* ordered relations functions */
    else if (name.equals("append")) {
      checkArgs(args, 2, "append");
      return new AppendExpr(args.get(0), args.get(1));
    }


    /* array functions */
    else if (name.equals("store")) {
      if (args.size() < 3)
        throw new RuntimeException("incorrect argument passed to store:" +
                Arrays.toString(args.stream().map(a -> a.accept(new Printer())).toArray()));
      return new StoreExpr(args.get(0), args.get(1), args.subList(2, args.size()));
    } else if (name.equals("select")) {
      if (args.size() < 2)
        throw new RuntimeException("incorrect argument passed to select:" +
                Arrays.toString(args.stream().map(a -> a.accept(new Printer())).toArray()));
      return new SelectExpr(args.get(0), args.subList(1, args.size()));
    }

    /* uninterpreted fn call */
    else
      return new CallExpr(ctx.name.getText(), args);
  }

  @Override
  public Expr visitIntLitExpr (DexterIRParser.IntLitExprContext ctx)
  {
    Expr e = new IntLitExpr(Integer.parseInt(ctx.NUMBER().getText()));
    e.type(TypesFactory.Int);
    return e;
  }

  @Override
  public Expr visitFloatLitExpr (DexterIRParser.FloatLitExprContext ctx)
  {
    Expr e = new FloatLitExpr(Double.parseDouble(ctx.FLOATNUM().getText()));
    e.type(TypesFactory.Float);
    return e;
  }

  @Override
  public Expr visitEmptyListExpr (DexterIRParser.EmptyListExprContext ctx)
  {
    Type elemT = this.parseType(ctx.elemT);
    Expr r = new EmptyListExpr();
    r.type(TypesFactory.listT(elemT));
    return r;
  }

  @Override
  public Expr visitVarsMacro(DexterIRParser.VarsMacroContext ctx)
  {
    switch (ctx.m.getText())
    {
      case "out_vars":
        return new Variables(Variables.CATEGORY.OUTPUT_VARIABLES, parseType(ctx.t));
      case "out_arr_vars":
        return new Variables(Variables.CATEGORY.OUTPUT_ARRAY_VARIABLES, parseType(ctx.t));
      case "in_vars":
        return new Variables(Variables.CATEGORY.INPUT_VARIABLES, parseType(ctx.t));
      case "idx_vars":
        return new Variables(Variables.CATEGORY.INDEX_VARS, parseType(ctx.t));
      case "vars":
        return new Variables(Variables.CATEGORY.ALL_VARIABLES, parseType(ctx.t));
      case "consts":
        return new Variables(Variables.CATEGORY.CONSTANTS, parseType(ctx.t));
      default:
        throw new RuntimeException("unknown macro: " + ctx.m.getText());
    }
  }

  @Override
  public Expr visitForEachMacro(DexterIRParser.ForEachMacroContext ctx)
  {
    return new ForEach(new VarExpr(ctx.v.getText()), visit(ctx.m), visit(ctx.body));
  }

  @Override
  public Expr visitBoundsMacro(DexterIRParser.BoundsMacroContext ctx)
  {
    switch (ctx.m.getText()) {
      case "ub":
       return new Bound(Bound.CATEGORY.UPPER_BOUND, Integer.parseInt(ctx.idx.getText()));
      case "lb":
        return new Bound(Bound.CATEGORY.LOWER_BOUND, Integer.parseInt(ctx.idx.getText()));
      default:
        throw new RuntimeException("unknown macro: " + ctx.m.getText());
    }
  }

  @Override
  public Expr visitLambdaExpr(DexterIRParser.LambdaExprContext ctx)
  {
    List<VarExpr> params = new ArrayList<>();
    List<Type> paramsT = new ArrayList<>();

    for (DexterIRParser.VarDeclContext c : ctx.varDecl())
    {
      VarExpr p = (VarExpr)visit(c);
      if (params.contains(p))
        throw new RuntimeException("duplicate declaration of: " + p.name());

      params.add(p);
      paramsT.add(p.type());
    }

    Type returnT = parseType(ctx.retType);
    Expr r = new FuncDecl("lambda", params, returnT, visit(ctx.body));
    r.type(new FunctionT("lambda", paramsT, returnT));

    return r;
  }

  @Override
  public Expr visitCIntLitExpr(DexterIRParser.CIntLitExprContext ctx)
  {
    Expr e = new IntLitExpr(Integer.parseInt(ctx.v.getText()), false);
    e.type(TypesFactory.Int);
    return e;
  }

  @Override
  public Expr visitInitMacro(DexterIRParser.InitMacroContext ctx) {
    return new Init(visit(ctx.v));
  }

  @Override
  public Expr visitUninterpExpr(DexterIRParser.UninterpExprContext ctx) {
    switch(ctx.t.getText())
    {
      case "int":
        return new UIExpr(TypesFactory.Int);
      case "float":
        return new UIExpr(TypesFactory.Float);
      default:
        throw new RuntimeException("could not parse uninterp expr");
    }
  }

  @Override
  public Expr visitScopeMacro(DexterIRParser.ScopeMacroContext ctx) { return new Scope(visit(ctx.v)); }

  @Override
  public Expr visitTupleExpr(DexterIRParser.TupleExprContext ctx) {
    List<Expr> args = new ArrayList<>();
    for (DexterIRParser.ExprContext e : ctx.expr())
      args.add(visit(e));

    return new TupleExpr(args);
  }

  @Override
  public Expr visitPtrExpr(DexterIRParser.PtrExprContext ctx)
  {
    return new PtrExpr(visit(ctx.d), visit(ctx.o));
  }

  @Override
  public Expr visitIncrPtrExpr(DexterIRParser.IncrPtrExprContext ctx) {
    return new IncrPtrExpr(visit(ctx.p), visit(ctx.o));
  }

  @Override
  public Expr visitDecrPtrExpr(DexterIRParser.DecrPtrExprContext ctx) {
    return new DecrPtrExpr(visit(ctx.p), visit(ctx.o));
  }
}