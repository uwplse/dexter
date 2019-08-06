package dexter.ir.macro;

import dexter.ir.Expr;
import dexter.ir.Visitor;
import dexter.ir.type.Type;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Variables extends Expr
{
  public enum CATEGORY {
    OUTPUT_VARIABLES,
    INPUT_VARIABLES,
    ALL_VARIABLES,
    CONSTANTS,
    INDEX_VARS,
    OUTPUT_ARRAY_VARIABLES;
  }

  public CATEGORY getCat() {
    return cat;
  }

  public Type getVarT() {
    return varT;
  }

  protected CATEGORY cat;
  protected Type varT;

  public Variables(CATEGORY cat, Type varT)
  {
    this.cat = cat;
    this.varT = varT;
  }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }
}