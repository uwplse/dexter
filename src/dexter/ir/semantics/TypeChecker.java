package dexter.ir.semantics;

import dexter.ir.Expr;
import dexter.ir.Printer;
import dexter.ir.Util;
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
import dexter.ir.type.*;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class TypeChecker implements Visitor<Type>
{
  protected Map<String, Type> varTypes;
  protected Map<String, FunctionT> funcDecls;
  protected Map<String, ClassT> classDecls;

  @Override
  public Type visit(VarExpr n)
  {
    if (!varTypes.containsKey(n.name()) && !funcDecls.containsKey(n.name()))
      throw new RuntimeException(n.name() + " is not declared");

    if (n.type() != null)
    {
      if (n.type() != varTypes.get(n.name()) && n.type() != classDecls.get(n.name()) && n.type() != funcDecls.get(n.name()))
        throw new RuntimeException(n.name() + " expected type: " + varTypes.get(n.name()) + " but got: " + n.type() +
                ": " + n);
    }
    else
    {
      if (varTypes.containsKey(n.name()))
        n.type(varTypes.get(n.name()));
      else if (classDecls.containsKey(n.name()))
        n.type(classDecls.get(n.name()));
      else
        n.type(funcDecls.get(n.name()));
    }

    return n.type();
  }

  @Override
  public Type visit(BoolLitExpr n)
  {
    assert n.type() == TypesFactory.Bool;
    return TypesFactory.Bool;
  }

  @Override
  public Type visit(NullExpr n)
  {
    return n.type();
  }

  @Override
  public Type visit(PtrExpr n) {
    if (!TypesFactory.isArrayT(n.data().accept(this)))
      throw new RuntimeException("Pointer data must be an array type: " + n);

    if (TypesFactory.Int != n.offset().accept(this))
      throw new RuntimeException("Pointer offset must be an integer type: " + n);

    n.data().accept(this);
    n.offset().accept(this);

    n.type(TypesFactory.ptrT(((ArrayT)n.data().type()).elemT()));

    return n.type();
  }

  @Override
  public Type visit(IncrPtrExpr e) {
    if (!TypesFactory.isPtrT(e.ptr().accept(this)))
      throw new RuntimeException("Icrement operator is only supported for pointer data type: " + e);

    if (TypesFactory.Int != e.incr().accept(this))
      throw new RuntimeException("Increment expression must be an integer type: " + e);

    e.ptr().accept(this);
    e.type(e.ptr().type());

    return e.type();
  }

  @Override
  public Type visit(DecrPtrExpr e) {
    if (!TypesFactory.isPtrT(e.ptr().accept(this)))
      throw new RuntimeException("Icrement operator is only supported for pointer data type: " + e);

    if (TypesFactory.Int != e.decr().accept(this))
      throw new RuntimeException("Decrement expression must be an integer type: " + e);

    e.ptr().accept(this);
    e.type(e.ptr().type());

    return e.type();
  }

  @Override
  public Type visit(CallExpr n)
  {
    String callee = n.name();
    FunctionT callT;

    // a constructor call
    if (this.classDecls.containsKey(callee))
    {
      callT = this.funcDecls.get(callee);
    }

    // a function variable
    else if (this.varTypes.containsKey(callee))
    {
      if (!TypesFactory.isFunctionT(this.varTypes.get(callee)))
        throw new RuntimeException(n.name() + " is not a function type: " + n);
      else
        callT = (FunctionT)this.varTypes.get(callee);
    }

    // a regular function call
    else
    {
      callT = this.funcDecls.get(callee);
      if (callT == null) // TODO: Remove the matching hack
        if (callee.matches("int_expr_grm"))
        {
          n.type(TypesFactory.Int);
          return n.type();
        }
        else if (callee.matches("float_expr_grm"))
        {
          n.type(TypesFactory.Float);
          return n.type();
        }
        else
          throw new RuntimeException(callee + " has not been declared: " + n);
    }

    // check arguments
    if (n.args().size() != callT.paramsT().size())
      throw new RuntimeException(callee + " needs to have " + callT.paramsT().size() + " params: " + n);

    for (int i = 0; i < n.args().size(); ++i)
    {
      if (!n.args().get(i).accept(this).equals(callT.paramsT().get(i)))
        if (!(n.args().get(i).type() instanceof IntT && callT.paramsT().get(i) instanceof IntT))
          throw new RuntimeException(n.args().get(i) + " should have type " + callT.paramsT().get(i) +
                " instead of " + n.args().get(i).accept(this) + ": " + n);
    }

    n.type(callT.returnT());
    return callT.returnT();
  }

  @Override
  public Type visit(BinaryExpr n)
  {
    if (n.left().accept(this) != (n.right().accept(this))) {
      if (!(n.left().type() instanceof IntT && n.right().type() instanceof IntT))
        throw new RuntimeException(n.left() + " evaluates to: " + n.left().accept(this) + " while " +
              n.right() + " evaluates to: " + n.right().accept(this) + " in " + n);
    }

    Type returnT;
    BinaryExpr.Op op = n.op();

    if (op == BinaryExpr.Op.AND) { returnT = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.OR) { returnT = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.EQ) { returnT = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.NEQ) { returnT = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.IMPLY) { returnT = TypesFactory.Bool; }

    else if (op == BinaryExpr.Op.LT) { returnT = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.LE) { returnT = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.GE) { returnT = TypesFactory.Bool; }
    else if (op == BinaryExpr.Op.GT) { returnT = TypesFactory.Bool; }

    // left and right should have the same type now
    else if (op == BinaryExpr.Op.PLUS) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.MINUS) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.MULT) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.DIV) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.MOD) { returnT = n.left().type(); }

    else if (op == BinaryExpr.Op.SHL) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.LSHR) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.ASHR) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.BAND) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.BOR) { returnT = n.left().type(); }
    else if (op == BinaryExpr.Op.BXOR) { returnT = n.left().type(); }

    else
      throw new RuntimeException("unknown binary operator: " + n.op() + ": " + n);

    n.type(returnT);
    return n.type();
  }

  @Override
  public Type visit(UnaryExpr n)
  {
    Type returnT;
    UnaryExpr.Op op = n.op();

    if (op == UnaryExpr.Op.NOT)
    {
      if (n.base().accept(this) != TypesFactory.Bool)
        throw new RuntimeException(n.base() + " does not evaluate to bool: " + n);

      returnT = TypesFactory.Bool;
    }
    else if (op == UnaryExpr.Op.PLUS || op == UnaryExpr.Op.MINUS)
    {
      if (n.base().accept(this) == TypesFactory.Int)
        returnT = TypesFactory.Int;
      else if (n.base().accept(this) == TypesFactory.Float)
        returnT = TypesFactory.Float;
      else
        throw new RuntimeException(n.base() + " does not evaluate to int or float");
    }
    else if (op == UnaryExpr.Op.BNOT)
    {
      if (n.base().accept(this) == TypesFactory.Int)
        returnT = TypesFactory.Int;
      else
        throw new RuntimeException(n.base() + " does not evaluate to int");
    }

    else
      throw new RuntimeException("unknown unary operator: " + n.op() + ": " + n);

    n.type(returnT);
    return n.type();
  }

  @Override
  public Type visit(FieldExpr e)
  {
    Type t = e.target().accept(this);

    if (t instanceof  ClassT)
    {
      ClassT classT = (ClassT)t;
      VarExpr field = null;
      for (VarExpr f : classT.fields())
      {
        if (f.name().equals(e.field()))
        {
          field = f;
          break;
        }
      }

      if (field == null)
        throw new RuntimeException(classT.name() + " does not have field " + e.field());

      e.type(field.type());
      return e.type();
    }
    else if (t instanceof PtrT)
    {
      PtrT ptrT = (PtrT) t;
      if (e.field().equals("data")) {
        Type retT = TypesFactory.arrayT(ptrT.dim(), ptrT.elemT());
        e.type(retT);
        return retT;
      }
      else if (e.field().equals("offset")) {
        e.type(TypesFactory.Int);
        return TypesFactory.Int;
      }
      else
        throw new RuntimeException("Pointer type does not have field " + e.field());
    }
    else if (t instanceof BufferT)
    {
      BufferT bufT = (BufferT) t;
      if (e.field().matches("(dim0|dim1|dim2|dim3)_extent")) {
        e.type(TypesFactory.Int);
        return TypesFactory.Int;
      }
      else
        throw new RuntimeException("Buffer type does not have field " + e.field());
    }
    else
      throw new RuntimeException("Unexpected target type: " + t);
  }

  @Override
  public Type visit(ForallExpr n)
  {
    if (varTypes.containsKey(n.var().name()))
        throw new RuntimeException("duplicated variable " + n.var().name() + " in forall " + n.accept(new Printer()));;

    n.var().type(TypesFactory.Int);
    varTypes.put(n.var().name(), n.var().type());


    /*
    if ( !(n.start() instanceof BinaryExpr) )
      throw new RuntimeException(Util.print(n) + " is not a binary expr");
    if ( !(n.end() instanceof BinaryExpr) )
      throw new RuntimeException(Util.print(n) + " is not a binary expr");

    VarExpr var = n.var();
    BinaryExpr s = (BinaryExpr)n.start();
    BinaryExpr e = (BinaryExpr)n.end();

    if (!(s.left() instanceof VarExpr && ((VarExpr)s.left()).name().equals(var.name()) &&
            s.op() == BinaryExpr.Op.EQ))
      throw new RuntimeException(Util.print(s) + " must be of the form " + var.name() + " = ...");

    if (!(e.left() instanceof VarExpr && ((VarExpr)s.left()).name().equals(var.name())))
      throw new RuntimeException(Util.print(s) + " must be of the form " + var.name() + " <op> ...");
    */

    Expr s = n.start();
    Expr e = n.end();

    //if (s.accept(this) != TypesFactory.Bool)
    if (s.accept(this) != n.var().type())
      throw new RuntimeException(s + " does not evaluate to " + n.var().type() + ": " + n);
    if (e.accept(this) != n.var().type())
      throw new RuntimeException(e + " does not evaluate to " + n.var().type() + ": " + n);

    if (n.body().accept(this) != TypesFactory.Bool)
      throw new RuntimeException(n.body() + " does not evaluate to bool: " + n);

    return n.type(n.body().type()).type();
  }

  @Override
  public Type visit(IfExpr e)
  {
    if (e.cond().accept(this) != TypesFactory.Bool)
      throw new RuntimeException(e.cond() + " does not evaluate to bool: " + e);

    if (e.alt() != null && e.cons().accept(this) != e.alt().accept(this))
      throw new RuntimeException(e.cons() + " has type: " + e.cons().type() +
                                 " but " + e.alt() + " has type: " + e.alt().type() + ": " + e);

    return e.type(e.cons().type()).type();
  }

  @Override
  public Type visit(ChooseIntExpr e) {
    return TypesFactory.Int;
  }

  @Override
  public Type visit(LetExpr n)
  {
    for (VarExpr v : n.vars())
      varTypes.put(v.name(), v.type());

    Type t = n.body().accept(this);

    if (t != TypesFactory.Bool)
      throw new RuntimeException(n + " does not evaluate to bool");

    if (n.assumptions() != null && n.assumptions().accept(this) != TypesFactory.Bool)
      throw new RuntimeException(n + " assumptions do not evaluate to bool");

    n.type(t);
    return t;
  }

  @Override
  public Type visit(FuncDecl n)
  {
    this.varTypes = new HashMap<>();
    for (VarExpr v : n.params())
      varTypes.put(v.name(), v.type());

    if (!n.isUnInterpreted() && n.body().accept(this) != n.retType())
      throw new RuntimeException(n + " evaluates to " + n.body().accept(this) + " rather than " + n.retType());

    return n.type();
  }

  @Override
  public Type visit(ClassDecl n)
  {
    List<Type> fieldsT = new ArrayList<>();
    for (VarExpr f : n.fields())
      fieldsT.add(f.type());

    // insert constructor call into function newDecls
    FunctionT ctorT = TypesFactory.functionT(n.name(), n.type(), fieldsT);
    this.funcDecls.put(n.name(), ctorT);

    return n.type();
  }

  @Override
  public Type visit(Program p)
  {
    this.classDecls = new HashMap<>();
    this.funcDecls = new HashMap<>();
    this.varTypes = new HashMap<>();

    for (ClassDecl c : p.classes())
    {
      this.classDecls.put(c.name(), (ClassT) c.type());
      c.type(c.accept(this));
    }

    // add to function newDecls list so that CallExprs can check whether the callee is declared
    for (FuncDecl d : p.functions())
    {
      d.type(TypesFactory.functionT(d.name(), d.retType(), d.params().stream().map(a -> a.type()).collect(Collectors.toList())));
      this.funcDecls.put(d.name(), (FunctionT) d.type());
    }

    for (FuncDecl d : p.functions())
    {
      if (!d.accept(this).equals(d.type()))
        throw new RuntimeException(d + " does not match its type: " + d.type() + " " + d.accept(this));
    }

    Type t = (p.body() != null) ? p.body().accept(this) : TypesFactory.Bool;

    if (t != TypesFactory.Bool)
      throw new RuntimeException(p.body() + " does not evaluate to boolean");

    p.type(t);
    return t;
  }

  @Override
  public Type visit(IntLitExpr n)
  {
    assert n.type() == TypesFactory.Int;
    return TypesFactory.Int;
  }

  @Override
  public Type visit(FloatLitExpr n)
  {
    assert n.type() == TypesFactory.Float;
    return TypesFactory.Float;
  }

  @Override
  public Type visit(UIExpr u) {
    return u.type();
  }

  @Override
  public Type visit(ChooseExpr e)
  {
    Type returnT = null;

    for (Expr arg :e.args())
    {
      Type argT = arg.accept(this);
      if (returnT == null)
        returnT = argT;
      else
      {
        if (!returnT.equals(argT))
          if (!(returnT instanceof IntT && argT instanceof IntT))
            throw new RuntimeException(arg + " in " + e + " evaluates to: " + argT + " rather than " + returnT);
      }
    }

    if (e.args().size() < 1)
      throw new RuntimeException(e + " should have at least 1 arguments");

    e.type(returnT);
    return e.type();
  }

  /* lists */
  @Override
  public Type visit(EmptyListExpr n)
  {
    return n.type();
  }

  @Override
  public Type visit(GetExpr e)
  {
    if (!TypesFactory.isListT(e.list().accept(this)))
      throw new RuntimeException(e.list() + " is not a list" );

    if (e.index().accept(this) != TypesFactory.Int)
      throw new RuntimeException(e.index() + " does not evaluate to int");

    e.type(((ListT)e.list().type()).elemT());
    return e.type();
  }

  @Override
  public Type visit(LengthExpr e)
  {
    if (!TypesFactory.isListT(e.list().accept(this)))
      throw new RuntimeException(e.list() + " is not a list" );

    e.type(TypesFactory.Int);
    return e.type();
  }

  @Override
  public Type visit(EqualsExpr e)
  {
    if (!TypesFactory.isListT(e.list1().accept(this)))
      throw new RuntimeException(e.list1() + " is a " + e.list1().type() + " not a list" );

    if (!TypesFactory.isListT(e.list2().accept(this)))
      throw new RuntimeException(e.list2() + " is a " + e.list2().type() + " not a list");

    if (!e.list1().type().equals(e.list2().type()))
      throw new RuntimeException("both args in " + e + " should have the same type rather than " +
                                 e.list1().type() + " and " + e.list2().type());

    e.type(TypesFactory.Bool);
    return e.type();
  }

  @Override
  public Type visit(ConcatExpr e)
  {
    if (!TypesFactory.isListT(e.list1().accept(this)))
      throw new RuntimeException(e.list1() + " is a " + e.list1().type() + " not a list" );

    if (!TypesFactory.isListT(e.list2().accept(this)))
      throw new RuntimeException(e.list2() + " is a " + e.list2().type() + " not a list");

    if (!e.list1().type().equals(e.list2().type()))
      throw new RuntimeException("both args in " + e + " should have the same type rather than " +
              e.list1().type() + " and " + e.list2().type());

    e.type(e.list1().type());
    return e.type();
  }

  @Override
  public Type visit(SliceExpr e)
  {
    if (!TypesFactory.isListT(e.list().accept(this)))
      throw new RuntimeException(e.list() + " is a " + e.list().type() + " not a list" );

    if (e.start().accept(this) != TypesFactory.Int)
      throw new RuntimeException(e.start() + " is a " + e.start().type() + " not an int");

    if (e.end().accept(this) != TypesFactory.Int)
      throw new RuntimeException(e.start() + " is a " + e.end().type() + " not an int");

    e.type(e.list().type());
    return e.type();
  }

  @Override
  public Type visit(ListCompExpr e)
  {
    // process each of the 'for x in y' construct and assign types to them

    for (Pair<VarExpr, Expr> iter : e.iters())
    {
      VarExpr v = iter.a;
      Expr l = iter.b;

      // set l's type
      if (!TypesFactory.isListT(l.accept(this)))
        throw new RuntimeException(l + " does not evaluate to a list");

      // set v's type and declare v in the current scope
      v.type(((ListT)l.type()).elemT());
      varTypes.put(v.name(), v.type());
    }

    // process the expression
    e.expr().type(e.expr().accept(this));

    // return type is a list of the expression's type
    e.type(TypesFactory.listT(e.expr().type()));
    return e.type();
  }

  @Override
  public Type visit(AppendExpr e)
  {
    if (!TypesFactory.isListT(e.list().accept(this)))
      throw new RuntimeException(e.list() + " is not a list");

    if (!e.elem().accept(this).equals(((ListT)e.list().type()).elemT()))
      throw new RuntimeException("can't append " + e.elem() + " to " + e.list().type());

    e.type(e.list().type());
    return e.type();
  }

  /* arrays */

  @Override
  public Type visit(SelectExpr e)
  {
    if (!(e.array().accept(this) instanceof CollectionT))
      throw new RuntimeException(e.array() + " is not an array" );

    for (Expr index : e.index())
    {
      if (index.accept(this) != TypesFactory.Int)
        throw new RuntimeException(index + " does not evaluate to int");
    }

    CollectionT type = (CollectionT)e.array().type();
    if (e.index().size() != type.dim())
      throw new RuntimeException(e + " should have " + type.dim() + " indices");

    e.type(type.elemT());
    return e.type();
  }

  @Override
  public Type visit(StoreExpr e)
  {
    if (!(e.array().accept(this) instanceof CollectionT))
      throw new RuntimeException(e.array() + " is not an array" );


    if (e.value().accept(this) != ((CollectionT)e.array().type()).elemT())
      throw new RuntimeException(e + " should store values of type " + ((CollectionT)e.array().type()).elemT());

    for (Expr index : e.index())
    {
      if (index.accept(this) != TypesFactory.Int)
        throw new RuntimeException(index + " does not evaluate to int");
    }

    CollectionT type = (CollectionT)e.array().type();
    if (e.index().size() != type.dim())
      throw new RuntimeException(e + " should have " + type.dim() + " indices" + e.index().size());

    e.type(e.array().type());
    return e.type();
  }

  @Override
  public Type visit(ForEach e) {
    Type vsT = e.getVarSet().accept(this);

    if (!(e.getVarSet() instanceof Variables))
      throw new RuntimeException(e.getVarSet() + " is not a variable-set" );

    Type elemT = e.getVarSet().type();

    if (varTypes.containsKey(e.getId()))
      throw new RuntimeException("duplicated variable " + e.getId() + " in macro " + e.accept(new Printer()));

    e.getId().type(elemT);
    varTypes.put(e.getId().name(), e.getId().type());

    if (TypesFactory.Bool != (e.getSubExpr().accept(this)))
      throw new RuntimeException(e.getSubExpr() + " is not a boolean expression");

    return e.getSubExpr().type();
  }

  @Override
  public Type visit(Variables e) {
    e.type(e.getVarT());
    return e.type();
  }

  @Override
  public Type visit(Bound e) {
    e.type(TypesFactory.Int);
    return e.type();
  }

  @Override
  public Type visit(Init e) {
    Type t = e.getExpr().accept(this);
    e.type(t);
    return t;
  }

  @Override
  public Type visit(Scope e) {
    Type t = e.getExpr().accept(this);
    e.type(t);
    return t;
  }

  @Override
  public Type visit(TupleExpr e) {
    for (Expr val : e.vals())
      val.accept(this);

    // TODO: Check all values are same type

    e.type(TypesFactory.tupleT(e.vals().get(0).type(), e.vals().size()));

    return e.type();
  }
}