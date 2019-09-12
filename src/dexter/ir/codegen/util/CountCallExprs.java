package dexter.ir.codegen.util;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.VarExpr;

import java.util.*;
import java.util.stream.Collectors;

public class CountCallExprs extends Transformer {
  Map<CallExpr, List<Integer>> counts;
  static Map<CallExpr, Integer> Ids = new HashMap<>();
  static int nextID = 0;

  public static Map<CallExpr, Integer> getIds () { return Ids; }
  public static void reset () { Ids.clear(); nextID = 0; }

  public CountCallExprs()
  {
    this.counts = new LinkedHashMap<>();
  }

  public Map<CallExpr, List<Integer>> getCount()
  {
    return this.counts;
  }

  @Override
  public Expr transform(CallExpr c)
  {
    if (!c.name().endsWith("_gen"))
      return null;

    CallExpr ce = new CallExpr(c.name(), c.args().stream().map(a -> a.accept(this)).collect(Collectors.toList()), c.getId());
    ce.type(c.type());

    if (!this.counts.containsKey(ce))
      this.counts.put(ce, new ArrayList<>());

    this.counts.get(ce).add(ce.getId());

    if (!this.Ids.containsKey(ce)) {
      this.Ids.put(ce, nextID++);
    }

    return new VarExpr(ce.name() + "_" + this.Ids.get(ce) + "_" + (this.counts.get(ce).size()-1), ce.type());
  }
}