package dexter.ir.bool;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class FuncDecl extends Expr
{
  protected String name;
  protected List<VarExpr> params;
  protected Expr body;
  protected Type retType;
  protected boolean isUnInterpreted;
  protected boolean isGenerator;

  public FuncDecl (String name, List<VarExpr> params, Type retType, Expr body, boolean isGen)
  {
    this.name = name;
    this.params = new ArrayList<> (params);
    this.body = body;
    this.isUnInterpreted = (body == null ? true : false);
    this.retType = retType;
    this.isGenerator = isGen;
  }

  public String name () { return name; }
  public List<VarExpr> params () { return params; }
  public Expr body () { return body; }
  public Type retType () { return retType; }
  public boolean isUnInterpreted () { return isUnInterpreted; }
  public void isUnInterpreted (boolean isU) { isUnInterpreted = isU; }
  public boolean isGenerator () { return isGenerator; }
  public void isGenerator (boolean isG) { isGenerator = isG; }

  public void body (Expr body) { this.body = body; }
  public void params (List<VarExpr> params) { this.params = params; }

  @Override
  public <T> T accept(Visitor<T> v)
  {
    return v.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    if (o instanceof FuncDecl)
    {
      FuncDecl other = (FuncDecl)o;
      return other.name.equals(this.name) && other.params.equals(this.params) &&
              other.isUnInterpreted == this.isUnInterpreted && (other.isUnInterpreted || other.body.equals(this.body));
    }
    else
      return false;
  }
}