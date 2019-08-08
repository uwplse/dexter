package dexter.ir.codegen.util;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.VarExpr;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CountCallExprs extends Transformer {
  Map<CallExpr, Integer> exprs;
  static Map<CallExpr, Integer> Ids = new HashMap<>();
  static int nextID = 0;

  public static Map<CallExpr, Integer> getIds () { return Ids; }
  public static void reset () { Ids.clear(); nextID = 0; }

  public CountCallExprs()
  {
    this.exprs = new LinkedHashMap<>();
  }

  public Map<CallExpr, Integer> getCount()
  {
    return this.exprs;
  }

  @Override
  public Expr transform(CallExpr c)
  {
    if (!c.name().endsWith("_gen"))
      return null;

    CallExpr ce = new CallExpr(c.name(), c.args().stream().map(a -> a.accept(this)).collect(Collectors.toList()), c.getId());
    ce.type(c.type());

    this.exprs.put(ce, this.exprs.getOrDefault(ce, 0) + 1);

    if (!this.Ids.containsKey(ce)) {
      this.Ids.put(ce, nextID++);
    }

    return new VarExpr(ce.name() + "_" + this.Ids.get(ce) + "_" + (this.exprs.get(ce)-1), ce.type());
  }
}