package dexter.codegen.halide;

import dexter.ir.BaseVisitor;
import dexter.ir.array.SelectExpr;
import dexter.ir.bool.CallExpr;
import dexter.ir.type.BufferT;
import dexter.ir.type.TypesFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maaz Ahmad on 8/19/19.
 */
public class VarSetExtractor extends BaseVisitor {
  Set<String> vars = new HashSet<>();

  public Set<String> vars() { return vars; }

  @Override
  public void enter (SelectExpr c) {
    if (TypesFactory.isBufferT(c.array().type())) {
      BufferT bufT = (BufferT) c.array().type();
      if (bufT.dim() == 1) {
        vars.add("x");
      }
      else if (bufT.dim() == 2) {
        vars.add("x");
        vars.add("y");
      }
      else
        throw new RuntimeException("NYI");
    }
    else
      throw new RuntimeException("NYI");
  }
}