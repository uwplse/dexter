package dexter.misc;

import dexter.ir.BaseVisitor;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.ChooseExpr;
import dexter.ir.bool.ChooseIntExpr;

/**
 * Created by Maaz Ahmad on 8/7/19.
 */
public class ChooseIDInitializer extends BaseVisitor {
  private int nextChooseID = -1;
  private int nextCallExprID = -1;

  @Override
  public void enter (ChooseExpr e) {
    e.setId(++this.nextChooseID);
  }

  @Override
  public void enter (ChooseIntExpr e) {
    e.setId(++this.nextChooseID);
  }

  @Override
  public void enter (CallExpr e) {
    e.setId(++this.nextCallExprID);
  }
}