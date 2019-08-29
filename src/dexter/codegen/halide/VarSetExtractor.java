package dexter.codegen.halide;

import dexter.ir.BaseVisitor;
import dexter.ir.bool.CallExpr;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maaz Ahmad on 8/19/19.
 */
public class VarSetExtractor extends BaseVisitor {
  Set<String> vars = new HashSet<>();

  public Set<String> vars() { return vars; }

  @Override
  public void enter (CallExpr c) {
    if (c.name().equals("index2D")) {
      vars.add("x");
      vars.add("y");
    }
  }
}