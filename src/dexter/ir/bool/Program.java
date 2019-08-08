package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Program extends Expr
{
  protected List<FuncDecl> functions;
  protected List<ClassDecl> classes;
  protected Expr body;

  public Program (List<FuncDecl> functions, List<ClassDecl> classes, Expr body)
  {
    this.functions = new ArrayList<>(functions);
    this.classes = new ArrayList<>(classes);
    this.body = body;
  }

  public List<FuncDecl> functions () { return functions; }
  public List<ClassDecl> classes () { return classes; }
  public Expr body () { return body; }

  public void functions (List<FuncDecl> fns) { functions = new ArrayList<>(fns); }
  public void classes (List<ClassDecl> cls) { classes = new ArrayList<>(cls); }
  public void body (Expr bdy) { body = bdy; }

  public void prepend(Program dsl)
  {
    if (dsl == null)
      return;

    classes.addAll(dsl.classes);
    List<FuncDecl> fns = new ArrayList<>();
    for (FuncDecl fn : dsl.functions)
      if (!functions.contains(fn))
        fns.add(fn);
    fns.addAll(functions);
    functions = new ArrayList<>(fns);
  }

  public void stitch(Program grammar)
  {
    List<FuncDecl> fns = new ArrayList<>();

    for (FuncDecl fn : this.functions)
    {
      if (fn.name().matches("pc"));
      else if (fn.name().matches("inv[0-9]+"));
      else fns.add(fn);
    }

    for (FuncDecl fn : grammar.functions)
    {
      if (fn.name().matches("pc"));
      else if (fn.name().matches("inv"));
      else fns.add(fn);
    }

    for (FuncDecl fn : this.functions)
    {
      if (fn.name().matches("pc")) {
        for (FuncDecl grm_fn : grammar.functions)
          if (grm_fn.name().matches("pc"))
            fns.add(new FuncDecl(fn.name, fn.params, fn.retType, grm_fn.body));
      }
      else if (fn.name().matches("inv[0-9]+")) {
        for (FuncDecl grm_fn : grammar.functions)
          if (grm_fn.name().matches("inv"))
            fns.add(new FuncDecl(fn.name, fn.params, fn.retType, grm_fn.body));
      }
    }

    this.functions = new ArrayList<>(fns);
  }

  public FuncDecl getPC()
  {
    for (FuncDecl fn : functions)
    {
      if (fn.name().matches("pc"));
        return fn;
    }

    throw new RuntimeException("Post-condition not found.");
  }

  @Override
  public <T> T accept(Visitor<T> v)
  {
    return v.visit(this);
  }
}