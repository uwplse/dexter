package dexter.ir.list;

import dexter.ir.Visitor;
import dexter.ir.bool.CallExpr;

import java.util.Collections;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class EmptyListExpr extends CallExpr
{
  public EmptyListExpr()
  {
    super("empty", Collections.emptyList());
  }

  @Override
  public <T> T accept(Visitor<T> p)
  {
    return p.visit(this);
  }

  @Override
  public boolean equals (Object o)
  {
    return o instanceof EmptyListExpr;
  }
}