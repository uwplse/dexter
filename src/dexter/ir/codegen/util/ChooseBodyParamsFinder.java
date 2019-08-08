package dexter.ir.codegen.util;

import dexter.ir.BaseVisitor;
import dexter.ir.bool.ChooseExpr;
import scala.Tuple2;

import java.util.HashSet;
import java.util.Set;

public class ChooseBodyParamsFinder extends BaseVisitor {
  public Set<Tuple2<Integer, Integer>> IDs = new HashSet<>();

  @Override
  public void enter (ChooseExpr e)
  {
    this.IDs.add(new Tuple2<>(e.getId(), e.args().size()));
  }
}