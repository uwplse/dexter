package dexter.ir.codegen;

import dexter.frontend.CodeAnalysis;
import dexter.ir.Expr;
import dexter.ir.Substitutor;
import dexter.ir.Transformer;
import dexter.ir.bool.*;
import dexter.ir.codegen.util.ContainsConstant;
import dexter.ir.codegen.util.ContainsMacro;
import dexter.ir.codegen.util.EmptyChooseRemover;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.macro.*;
import dexter.ir.semantics.VarCounter;
import dexter.ir.tuple.TupleExpr;
import dexter.ir.type.ArrayT;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by maazsaf on 3/26/17.
 */
public class MacroExpander extends Transformer {
  protected CodeAnalysis analysis;
  protected List<FuncDecl> fns;
  protected List<ClassDecl> classes;
  protected Map<String, List<FuncDecl>> dynFns;

  protected Stack<String> parentFn;
  protected Stack<List<VarExpr>> scope;
  protected Stack<List<VarExpr>> args;
  protected Stack<List<VarExpr>> consts;

  protected boolean symbolicConstants;

  protected HashMap<String, List<VarExpr>> savedArgs;
  protected HashMap<String, List<VarExpr>> savedConsts;

  protected Set<Integer> handledInts;
  protected Set<Double> handledFloats;
  protected Expr assumptions;

  public MacroExpander(CodeAnalysis analysis, boolean symConsts) {
    super();

    this.analysis = analysis;
    this.fns = new ArrayList<>();
    this.classes = new ArrayList<>();
    this.dynFns = new HashMap<>();

    this.parentFn = new Stack<>();
    this.scope = new Stack<>();
    this.args = new Stack<>();
    this.consts = new Stack<>();

    this.savedArgs = new HashMap<>();
    this.savedConsts = new HashMap<>();

    this.handledInts = new HashSet<>();
    this.handledFloats = new HashSet<>();

    this.symbolicConstants = symConsts;
  }

  @Override
  public Expr transform(Program e) {
    fns = e.functions();
    classes = e.classes();

    Expr body = e.body().accept(this);

    List<FuncDecl> functions = e.functions().stream()
            .flatMap(fn -> {
              if (this.dynFns.containsKey(fn.name()))
                return this.dynFns.get(fn.name()).stream();
              else
                return Arrays.asList(fn).stream();
            }).collect(Collectors.toList());


    List<FuncDecl> fnsT = functions;

    functions = functions.stream()
            .filter(fn -> !(fn.accept(new ContainsMacro(fn.name(), fnsT)) || fn.accept(new ContainsConstant(fn.name(), fnsT))))
            .collect(Collectors.toList());

    return new Program(functions, e.classes(), body).accept(new EmptyChooseRemover());
  }

  @Override
  public Expr transform (LetExpr e)
  {
    Set<VarExpr> newVars = e.vars();

    this.consts.push(new ArrayList<>());

    Expr body = (e.body() == null ? e.body() : e.body().accept(this));
    Expr assumptions = (e.assumptions() == null ? e.assumptions() : e.assumptions().accept(this));

    if (assumptions == null)
      assumptions = this.assumptions;
    else if (this.assumptions != null)
      assumptions = new BinaryExpr(assumptions, BinaryExpr.Op.AND, this.assumptions);

    newVars.addAll(this.consts.pop());

    return new LetExpr(newVars, body, assumptions);
  }

  // We need to update CallExprs if the function being called has
  // a macro inside it.
  @Override
  public Expr transform (CallExpr e)
  {
    // Get function template
    FuncDecl fnDecl = null;
    for (FuncDecl fn : this.fns) {
      if (fn.name().equals(e.name())) {
        fnDecl = fn;
      }
    }

    // TODO: REMOVE
    if (e.name().matches("int_expr_grm|float_expr_grm"))
      return new CallExpr(e.name(), e.args().stream().map(arg->arg.accept(this)).collect(Collectors.toList()));

    // If function does not contain macro, don't do anything
    if (
          fnDecl.isUnInterpreted() ||
          (!fnDecl.body().accept(new ContainsMacro((this.parentFn.empty() ? "" : this.parentFn.peek()), fns)) &&
          !fnDecl.body().accept(new ContainsConstant((this.parentFn.empty() ? "" : this.parentFn.peek()), fns)))
       )
      return null;

    // If recursive call, do nothing (we fix these at the end)
    if (this.parentFn.contains(e.name()))
      return null;

    // Visit all children
    List<Expr> newArgs = e.args().stream()
                                 .map(arg -> arg.accept(this))
                                 .collect(Collectors.toList());

    if (fnDecl.isUnInterpreted())
      return new CallExpr(e.name(), new ArrayList<>(newArgs));

    // Have we already expanded this function for this scope?
    String fn_name = "";
    if (!this.parentFn.empty() && fnDecl.accept(new ContainsMacro(this.parentFn.peek(), fns)))
      fn_name = String.join("_", this.parentFn) + "_" + e.name();
    else
      fn_name = e.name();

    if (savedArgs.containsKey(fn_name))
    {
      newArgs.addAll(savedArgs.get(fn_name));
      if (this.symbolicConstants) newArgs.addAll(savedConsts.get(fn_name));
      if (!this.args.empty()) {
        this.args.peek().addAll(this.savedArgs.get(fn_name).stream()
                                   .filter(v -> !this.args.peek().contains(v))
                                   .collect(Collectors.toList()));

        this.consts.peek().addAll(this.savedConsts.get(fn_name).stream()
                .filter(v -> !this.consts.peek().contains(v))
                .collect(Collectors.toList()));
      }

      return new CallExpr(fn_name, new ArrayList<>(newArgs));
    }

    // We have to generate a new functionDecl by expanding macros
    // for the current scope
    Expr body = null;
    List<VarExpr> params = new ArrayList<>();

    // Get expanded function body
    this.args.push(new ArrayList<>());
    this.consts.push(new ArrayList<>());
    if (!fnDecl.isUnInterpreted()) {
      this.parentFn.push(e.name());

      // Special case
      if (e.name().equals("pc") || e.name().matches("inv[0-9]+"))
        this.scope.push(new ArrayList<>(fnDecl.params()));

      body = fnDecl.body().accept(this);

      if (e.name().equals("pc") || e.name().matches("inv[0-9]+"))
        this.scope.pop();

      this.parentFn.pop();
    }

    // Get the macro vars used inside function body
    List<VarExpr> macroVars = new ArrayList<>();
    List<VarExpr> symConsts = new ArrayList<>();
    params.addAll(fnDecl.params());

    macroVars.addAll(this.args.pop().stream().filter(mv -> !params.contains(mv)).collect(Collectors.toSet()));
    symConsts.addAll(this.consts.pop().stream().filter(sc -> !params.contains(sc)).collect(Collectors.toSet()));

    if (!this.args.empty()) {
      this.args.peek().addAll(macroVars.stream()
                                       .filter(v -> !this.args.peek().contains(v))
                                       .collect(Collectors.toList()));
    }

    if (!this.consts.empty()) {
      this.consts.peek().addAll(symConsts.stream()
              .filter(v -> !this.consts.peek().contains(v))
              .collect(Collectors.toList()));
    }

    // Special case
    if (e.name().equals("pc") || e.name().matches("inv[0-9]+"))
      macroVars.clear();

    newArgs.addAll(macroVars);
    if (this.symbolicConstants) newArgs.addAll(symConsts);

    // Save macroVars
    this.savedArgs.put(fn_name, macroVars);
    this.savedConsts.put(fn_name, symConsts);

    // Create dynamic function
    params.addAll(macroVars);
    params.addAll(symConsts);
    if (!this.dynFns.containsKey(fnDecl.name()))
      this.dynFns.put(fnDecl.name(), new ArrayList<>());

    // Change recursive calls
    List<VarExpr> argsToAdd = new ArrayList<>(macroVars);
    argsToAdd.addAll(symConsts);

    body = body.accept(new RecursiveCallUpdater(e.name(), fn_name, argsToAdd));
    this.dynFns.get(fnDecl.name()).add(new FuncDecl(fn_name, params, fnDecl.retType(), body));

    // Change CallExpr to have needed args
    return new CallExpr(fn_name, new ArrayList<>(newArgs));
  }

  @Override
  public Expr transform (IntLitExpr e)
  {
    if (!e.canModelAsVar() || e.value() <= 4)
      return e;

    VarExpr sym_var = new VarExpr("Const" + e.value(), e.type());
    if (!this.consts.empty() && !this.consts.peek().contains(sym_var))
      this.consts.peek().add(sym_var);

    if (handledInts.contains(e.value()))
      return sym_var;

    handledInts.add(e.value());

    if (this.assumptions == null)
      this.assumptions = new BinaryExpr(sym_var, BinaryExpr.Op.GT, new IntLitExpr(0));
    else
      this.assumptions = new BinaryExpr(this.assumptions, BinaryExpr.Op.AND, new BinaryExpr(sym_var, BinaryExpr.Op.GT, new IntLitExpr(0)));

    return sym_var;
  }

  @Override
  public Expr transform (FloatLitExpr e)
  {
    if (e.value() == 0.0)
      return e;

    VarExpr sym_var = new VarExpr("Const" + Double.toString(e.value()).replace('.', '_'), e.type());
    if (!this.consts.empty() && !this.consts.peek().contains(sym_var))
      this.consts.peek().add(sym_var);

    if (!this.consts.empty() && !this.consts.peek().contains(sym_var))
      this.consts.peek().add(sym_var);

    if (handledFloats.contains(e.value()))
      return sym_var;

    handledFloats.add(e.value());

    if (this.assumptions == null)
      this.assumptions = new BinaryExpr(sym_var, BinaryExpr.Op.GT, new FloatLitExpr(0));
    else
      this.assumptions = new BinaryExpr(this.assumptions, BinaryExpr.Op.AND, new BinaryExpr(sym_var, BinaryExpr.Op.GT, new FloatLitExpr(0)));

    return sym_var;
  }

  @Override
  public Expr transform (Init e)
  {
    assert e.getExpr() instanceof VarExpr;

    VarExpr initVar = this.scope.peek().stream().filter(v -> v.name().equals(((VarExpr) e.getExpr()).name() + "_init")).collect(Collectors.toList()).get(0);

    if (!this.args.empty() && !this.args.peek().contains(initVar))
      this.args.peek().add(initVar);

    return initVar;
  }

  @Override
  public Expr transform (Scope e)
  {
    assert e.getExpr() instanceof VarExpr;

    VarExpr var = this.scope.peek().stream().filter(v -> v.name().equals(((VarExpr) e.getExpr()).name())).collect(Collectors.toList()).get(0);

    if (!this.args.empty() && !this.args.peek().contains(var))
      this.args.peek().add(var);

    return var;
  }

  @Override
  public Expr transform (ForEach e)
  {
    Variables varSet = ((Variables) e.getVarSet());

    // Get all matching variables that are in scope
    Set<VarExpr> vars = new HashSet<>();
    for (String name : analysis.getVarNames(varSet.getCat(), varSet.getVarT())) {
      this.scope.peek().stream().filter(v -> v.name().equals(name)).forEach(v -> {
        vars.add(v);
        if (!this.args.empty() && !this.args.peek().contains(v))
          this.args.peek().add(v);
      });
    }

    Expr ret = new BoolLitExpr(true);

    for (Expr var : vars)
      ret = new BinaryExpr(ret, BinaryExpr.Op.AND, e.getSubExpr().accept(new Substitutor(e.getId(), var)));

    return ret.accept(this);
  }

  @Override
  public Expr transform (Variables e)
  {
    if (e.getCat() == Variables.CATEGORY.CONSTANTS)
      return new ChooseExpr(analysis.getConstants(e.getVarT())).accept(this);

    // Get all matching variables that are in scope
    Set<Expr> vars = new HashSet<>();
    for (String name : analysis.getVarNames(e.getCat(), e.getVarT())) {
      this.scope.peek().stream().filter(v -> v.name().equals(name)).forEach(v -> {
        vars.add(v);
        if (!this.args.empty() && !this.args.peek().contains(v))
          this.args.peek().add(v);
      });
    }

    if (vars.size() == 0)
      return defValue(e.getVarT());
    else
      return new ChooseExpr(new ArrayList<>(vars));
  }

  private Expr defValue(Type varT) {
    if (varT == TypesFactory.Int)
      return new IntLitExpr(1);
    else if (varT == TypesFactory.Float)
      return new FloatLitExpr(1.0);
    else if (varT == TypesFactory.Bool)
      return new BoolLitExpr(true);
    else if (varT instanceof ArrayT) {
      ArrayT arrT = (ArrayT) varT;
      if (arrT.elemT() == TypesFactory.Int)
        return new TupleExpr(Arrays.asList(new IntLitExpr(0))).type(TypesFactory.tupleT(arrT.elemT(), 1));

      throw new RuntimeException("NYI: Default value for type " + varT + ". Macro did not match any variables.");
    }
    else
      throw new RuntimeException("NYI: Default value for type " + varT + ". Macro did not match any variables.");
  }

  @Override
  public Expr transform (Bound e)
  {
    Expr bound = analysis.getBound(e.getCategory(), e.getIndex());

    List<VarExpr> varsUsed = new ArrayList<>(bound.accept(this).accept(new VarCounter(false, fns, classes)));

    if (!this.args.empty())
    {
      varsUsed.stream()
              .filter(v -> this.args.peek()
                                    .stream()
                                    .filter(arg -> arg.name().equals(v.name()))
                                    .collect(Collectors.toList())
                                    .isEmpty())
              .forEach(v -> this.scope.peek()
                                      .stream()
                                      .filter(var -> var.name().equals(v.name()))
                                      .forEach(var -> this.args.peek().add(var)));
    }

    return bound;
  }

  private class RecursiveCallUpdater extends Transformer {
    String old_name, new_name;
    List<VarExpr> argsToAdd;

    public RecursiveCallUpdater(String old_name, String new_name, List<VarExpr> argsToAdd) {
      this.argsToAdd = new ArrayList<>(argsToAdd);
      this.old_name = old_name;
      this.new_name = new_name;
    }

    @Override
    public CallExpr visit(CallExpr e) {
      List<Expr> updatedArgs = new ArrayList();
      for (Expr arg : e.args())
        updatedArgs.add(arg.accept(this));

      if (e.name().equals(old_name)) {
        updatedArgs.addAll(argsToAdd);
        return new CallExpr(new_name, updatedArgs);
      }

      return new CallExpr(e.name(), updatedArgs);
    }
  }
}