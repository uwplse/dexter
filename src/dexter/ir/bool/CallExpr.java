package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 *
 * Call to (interpreted or uninterpreted) functions
 */
public class CallExpr extends Expr
{
  protected String name;
  protected List<Expr> args;
  protected int ID;

  public CallExpr(String name)
  {
    this.name = name;
    this.args = new ArrayList<>();
  }

  public CallExpr (String name, List<? extends Expr> args)
  {
    this.name = name;
    this.args = new ArrayList<>(args);
    this.ID = 0;
  }

  public CallExpr (String name, List<? extends Expr> args, int id)
  {
    this.name = name;
    this.args = new ArrayList<>(args);
    this.ID = id;
  }

  public String name () { return  name; }
  public List<Expr> args () { return args; }

  public int getId() { return this.ID; }
  public void setId(int id) { this.ID = id; }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof CallExpr)
    {
      CallExpr other = (CallExpr)o;
      if (!other.name.equals(this.name) || other.args.size() != this.args.size())
        return false;

      for (int i = 0; i < this.args.size(); ++i)
      {
        if (!(this.args.get(i).equals(other.args.get(i))))
          return false;
      }

      return true;
    }
    else
      return false;
  }

  @Override
  public int hashCode(){
    return 0;
  }
}