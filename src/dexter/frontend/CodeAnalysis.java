package dexter.frontend;

import dexter.ir.Expr;
import dexter.ir.bool.*;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.macro.Bound;
import dexter.ir.macro.Variables;
import dexter.ir.parser.Util;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;
import scala.Tuple2;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by maazsaf on 3/26/17.
 */
public class CodeAnalysis {
  // Operator analysis
  protected Set<BinaryExpr.Op> binaryOps;
  protected Set<UnaryExpr.Op> unaryOps;
  protected Set<String> fns;

  // Variable analysis
  protected Set<VarExpr> input_vars_set;
  protected Set<VarExpr> output_vars_set;
  protected Set<VarExpr> output_arr_vars_set;
  protected Set<VarExpr> output_buffers_set;
  protected Set<VarExpr> unfiltered_output_arr_vars_set;
  protected Set<VarExpr> unfiltered_output_buf_vars_set;
  protected Set<VarExpr> scope_vars_set;
  protected Set<VarExpr> local_vars_set;
  protected Set<Expr> consts_set;

  protected Map<String, Set<String>> vars_map;
  protected Map<Type, Set<Expr>> consts_map;
  protected List<Tuple2<Expr, Expr>> loopBounds;

  protected Map<String, String> cTypes;

  // Constructors
  public CodeAnalysis() {
    this.binaryOps = new HashSet<>();
    this.unaryOps = new HashSet<>();
    this.fns = new HashSet<>();

    this.output_vars_set = new HashSet<>();
    this.output_arr_vars_set = new HashSet<>();
    this.output_buffers_set = new HashSet<>();
    this.unfiltered_output_arr_vars_set = new HashSet<>();;
    this.input_vars_set = new HashSet<>();
    this.local_vars_set = new HashSet<>();
    this.scope_vars_set = new HashSet<>();
    this.consts_set = new HashSet<>();

    this.vars_map = new HashMap<>();
    this.consts_map = new HashMap<>();
  }

  public CodeAnalysis(JsonObject analysis) {
    JsonObject variables = analysis.getJsonObject("Variables");
    parseVariables(variables);

    JsonObject constants = analysis.getJsonObject("Constants");
    parseConstants(constants);

    JsonArray loopBounds = analysis.getJsonArray("Bounds");
    parseLoopBounds(loopBounds);
  }

  // Setter / Getter functions
  public Set<BinaryExpr.Op> getBinaryOps() { return binaryOps; }
  public Set<UnaryExpr.Op> getUnaryOps() { return unaryOps; }

  public Set<VarExpr> getOutputVars() { return output_vars_set; }
  public Set<VarExpr> getOutputArrVars() { return output_arr_vars_set; }
  public Set<VarExpr> getOutputBuffers() { return output_buffers_set; }
  public Set<VarExpr> getInputVars() { return input_vars_set; }
  public Set<VarExpr> getLocalVars() { return local_vars_set; }
  public Set<VarExpr> getScopeVars() { return scope_vars_set; }
  public String getCppType(String varName) { return this.cTypes.get(varName); }

  public int numBounds () { return loopBounds.size(); }

  public void filterOutputArrVars(VarExpr v) {
    this.unfiltered_output_arr_vars_set = new HashSet<>(this.output_vars_set);
    this.output_arr_vars_set = output_arr_vars_set.stream()
                                  .filter(var -> (!TypesFactory.isArrayT(var.type()) && !TypesFactory.isPtrT(var.type())) || var.equals(v))
                                  .collect(Collectors.toSet());
  }

  public void filterOutputBuffers(VarExpr v) {
    this.unfiltered_output_buf_vars_set = new HashSet<>(this.output_buffers_set);
    this.output_buffers_set = output_buffers_set.stream()
        .filter(var -> (!TypesFactory.isArrayT(var.type()) && !TypesFactory.isPtrT(var.type())) || var.equals(v))
        .collect(Collectors.toSet());
  }

  public void restoreOutputArrVars() {
    this.output_arr_vars_set = new HashSet<>(this.unfiltered_output_arr_vars_set);
  }

  public void restoreOutputBuffers() {
    this.output_buffers_set = new HashSet<>(this.unfiltered_output_buf_vars_set);
  }

  public Set<String> getVarNames(Variables.CATEGORY c, Type t)
  {
    switch (c)
    {
      case ALL_VARIABLES:
        Set<String> vars = new HashSet<>();
        if (vars_map.containsKey("Input<" + t + ">")) vars.addAll(vars_map.get("Input<" + t + ">"));
        if (vars_map.containsKey("Output<" + t + ">")) vars.addAll(vars_map.get("Output<" + t + ">"));
        return vars;
      case INPUT_VARIABLES:
        return (vars_map.containsKey("Input<" + t + ">") ? vars_map.get("Input<" + t + ">") : new HashSet<>());
      case OUTPUT_VARIABLES:
        return this.output_vars_set.stream().filter(v -> v.type().equals(t)).map(v -> v.name()).collect(Collectors.toSet());
      case OUTPUT_ARRAY_VARIABLES:
        return this.output_arr_vars_set.stream().filter(v -> v.type().equals(t)).map(v -> v.name()).collect(Collectors.toSet());
      case INDEX_VARS:
        return (vars_map.containsKey("Index<" + t + ">") ? vars_map.get("Index<" + t + ">") : new HashSet<>());
      case CONSTANTS:
        return (vars_map.containsKey("Output<" + t + ">") ? vars_map.get("Output<" + t + ">") : new HashSet<>());
      default:
        throw new RuntimeException ("Unknown variables macro type");
    }
  }

  public List<Expr> getConstants(Type constT) {
    List<Expr> choices = new ArrayList<>(this.consts_map.get(constT));
    return choices;
  }

  public Expr getBound(Bound.CATEGORY c, int index)
  {
    switch (c)
    {
      case LOWER_BOUND:
        return loopBounds.get(index)._1;
      case UPPER_BOUND:
        return loopBounds.get(index)._2;
      default:
        throw new RuntimeException ("Unknown bound macro type");
    }
  }

  // Load from JSON
  private void parseVariables(JsonObject variables) {
    this.vars_map = new HashMap<>();
    this.cTypes = new HashMap<>();
    this.output_vars_set = new HashSet<>();
    this.output_arr_vars_set = new HashSet<>();
    this.output_buffers_set = new HashSet<>();
    this.input_vars_set = new HashSet<>();
    this.local_vars_set = new HashSet<>();

    variables.forEach((s, v) -> {
      Set<String> var_set = new HashSet<>();

      for (int i=0; i< ((JsonArray)v).size(); i++) {
        String varName = ((JsonArray) v).getJsonArray(i).getString(0);
        String varCType = ((JsonArray) v).getJsonArray(i).getString(1);
        var_set.add(varName);
        cTypes.put(varName, varCType);
      }

      vars_map.put(s, var_set);

      if (s.startsWith("Output<")) {
        Program p = (Program) Util.parse("let(var: " + s.substring(7, s.length()-1) + ") in true", false);
        Type varsT = ((LetExpr) p.body()).vars().iterator().next().type();
        for (String name : var_set)
          this.output_vars_set.add(new VarExpr(name, varsT));
      }
      else if (s.startsWith("OutputArr<")) {
        Program p = (Program) Util.parse("let(var: " + s.substring(10, s.length()-1) + ") in true", false);
        Type varsT = ((LetExpr) p.body()).vars().iterator().next().type();
        for (String name : var_set)
          this.output_arr_vars_set.add(new VarExpr(name, varsT));
      }
      else if (s.startsWith("OutputBuf<")) {
        Program p = (Program) Util.parse("let(var: " + s.substring(10, s.length()-1) + ") in true", false);
        Type varsT = ((LetExpr) p.body()).vars().iterator().next().type();
        for (String name : var_set)
          this.output_buffers_set.add(new VarExpr(name, varsT));
      }
      else if (s.startsWith("Input<")) {
        Program p = (Program) Util.parse("let(var: " + s.substring(6, s.length()-1) + ") in true", false);
        Type varsT = ((LetExpr) p.body()).vars().iterator().next().type();
        for (String name : var_set)
          this.input_vars_set.add(new VarExpr(name, varsT));
      }
      else if (s.startsWith("Local<")) {
        Program p = (Program) Util.parse("let(var: " + s.substring(6, s.length()-1) + ") in true", false);
        Type varsT = ((LetExpr) p.body()).vars().iterator().next().type();
        for (String name : var_set)
          this.local_vars_set.add(new VarExpr(name, varsT));
      }
    });
  }

  private void parseConstants(JsonObject constants) {
    this.consts_map = new HashMap<>();

    constants.forEach((t, v) -> {
      switch (t) {
        case "bool":
          consts_map.put(TypesFactory.Bool, new HashSet<>());
          for (int i=0; i< ((JsonArray)v).size(); i++)
            consts_map.get(TypesFactory.Bool).add(new BoolLitExpr(Boolean.parseBoolean(((JsonArray)v).getString(i))));
          break;
        case "int":
          consts_map.put(TypesFactory.Int, new HashSet<>());
          for (int i=0; i< ((JsonArray)v).size(); i++)
            consts_map.get(TypesFactory.Int).add(new IntLitExpr(Integer.parseInt(((JsonArray)v).getString(i))));
          break;
        case "float":
          consts_map.put(TypesFactory.Float, new HashSet<>());
          for (int i=0; i< ((JsonArray)v).size(); i++)
            consts_map.get(TypesFactory.Float).add(new FloatLitExpr(Float.parseFloat(((JsonArray)v).getString(i))));
          break;
        default:
          throw new RuntimeException ("Unknown constant type");
      }
    });
  }

  private void parseLoopBounds(JsonArray loopBounds) {
    this.loopBounds = new ArrayList<>();

    loopBounds.forEach(p -> {
      JsonObject pair = (JsonObject) p;

      Expr lb = Util.parse(pair.getString("lb"), false);
      if (lb instanceof Program)
        lb = ((Program) lb).body();

      Expr ub = Util.parse(pair.getString("ub"), false);
      if (ub instanceof Program)
        ub = ((Program) ub).body();

      Tuple2<Expr, Expr> bounds = new Tuple2<Expr, Expr>(lb, ub);
      this.loopBounds.add(bounds);
    });
  }
}