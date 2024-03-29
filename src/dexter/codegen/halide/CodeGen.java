package dexter.codegen.halide;

import dexter.analysis.ExtractLiveFunctionSet;
import dexter.dag.Pipeline;
import dexter.dag.Stage;
import dexter.frontend.CodeBlock;
import dexter.ir.Expr;
import dexter.ir.array.SelectExpr;
import dexter.ir.bool.*;
import dexter.ir.integer.ForallExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.type.BufferT;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;

import java.nio.Buffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maaz Ahmad on 8/19/19.
 */
public class CodeGen {
  public static String asHalideGenerator(CodeBlock cb) throws Exception {
    StringBuffer code = new StringBuffer();

    Pipeline pipeline = cb.asDAG();

    String name = cb.name();
    String inputDecls = extractInputDecls(pipeline);
    String outputDecls = extractOutputDecls(pipeline);
    String varsDecls = extractVarDecls(pipeline);
    String localDecls = extractLocalDecls(pipeline);
    String fnDecls = extractUDFDecls(pipeline);
    String stmts = extractStatements(pipeline);
    String schedule = extractSchedule(pipeline);

    code.append(
      "class Gen_$FUNC_NAME$ : public Halide::Generator<Gen_$FUNC_NAME$> {\n" +
      "public:\n" +
      "  $INP_DECLS$" +
        "$OUT_DECLS$\n" +
      "  $VAR_DECLS$\n" +
      "  $LOCAL_DECLS$\n" +
      "  $FN_DECLS$" +
      "void generate() {\n" +
      "    $HALIDE_STATEMENTS$\n" +
      "  }\n" +
      "  void schedule() {\n" +
      "    $SCHEDULE$\n" +
      "  }\n" +
      "};\n" +
      "HALIDE_REGISTER_GENERATOR(Gen_$FUNC_NAME$, $FUNC_NAME$)"
    );

    return code.toString()
        .replaceAll("\\$FUNC_NAME\\$", name)
        .replaceAll("\\$INP_DECLS\\$", inputDecls)
        .replaceAll("\\$OUT_DECLS\\$", outputDecls)
        .replaceAll("\\$VAR_DECLS\\$", varsDecls)
        .replaceAll("\\$FN_DECLS\\$", fnDecls)
        .replaceAll("\\$LOCAL_DECLS\\$", localDecls)
        .replaceAll("\\$HALIDE_STATEMENTS\\$", stmts)
        .replaceAll("\\$SCHEDULE\\$", schedule)
        ;
  }

  private static String extractInputDecls(Pipeline pipeline) {
    Set<VarExpr> inpVars = new LinkedHashSet<>();
    Map<String,String> cppTypes = new HashMap<>();

    getInputVars(pipeline, inpVars, cppTypes);

    StringBuffer paramDecls = new StringBuffer();
    for (VarExpr var: inpVars) {
      if (TypesFactory.isArrayT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isPtrT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isBufferT(var.type())) {
        String bufType = cppTypes.get(var.name());

        Pattern p = Pattern.compile("class Halide::Runtime::Buffer<(.*?), (.*?)>");
        Matcher m = p.matcher(bufType);
        if (!m.matches())
          throw new RuntimeException("Unexpected buffer type: " + bufType);

        String elemT = m.group(1);
        String dim = m.group(2);

        paramDecls.append("Input<Halide::Buffer<" + elemT + ">> "
            + var.name() + "{\"" + var.name() + "\", " + dim + "};\n  ");
      }
      else {
        String tp = cppTypes.get(var.name());
        if (tp.startsWith("const")) {
          tp = tp.replace("const ", "");
        }
        paramDecls.append("Input<" + tp + "> " +
            var.name() + "{\"" + var.name() + "\"};\n  ");
      }
    }

    return paramDecls.toString();
  }

  private static String extractVarDecls(Pipeline pipeline) {
    VarSetExtractor vse = new VarSetExtractor();

    for (Stage stage : pipeline.stages()) {
      if (stage.isEmpty() || !stage.containsLoop())
        continue;

      stage.summary().accept(vse);
    }

    StringBuffer varDecls = new StringBuffer();
    for (String name : vse.vars())
      varDecls.append("Halide::Var " + name + "{\"" + name + "\"};\n  ");

    return varDecls.toString();
  }

  private static String extractOutputDecls(Pipeline pipeline) {
    Set<VarExpr> outpVars = new LinkedHashSet<>();
    Map<String,String> cppTypes = new HashMap<>();

    getOutputVars(pipeline, outpVars, cppTypes);

    StringBuffer paramDecls = new StringBuffer();
    for (VarExpr var: outpVars) {
      if (TypesFactory.isArrayT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isPtrT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isBufferT(var.type())) {
        String bufType = cppTypes.get(var.name());

        Pattern p = Pattern.compile("class Halide::Runtime::Buffer<(.*?), (.*?)>");
        Matcher m = p.matcher(bufType);
        if (!m.matches())
          throw new RuntimeException("Unexpected buffer type: " + bufType);

        String elemT = m.group(1);
        String dim = m.group(2);

        paramDecls.append("Output<Halide::Buffer<" + elemT + ">> "
            + var.name() + "{\"" + var.name() + "\", " + dim + "};\n  ");
      }
      else {
        throw new RuntimeException("NYI.");
      }
    }

    return paramDecls.toString();
  }

  private static String extractUDFDecls(Pipeline pipeline) {
    Set<FuncDecl> liveFns = new LinkedHashSet<>();

    for (Stage stage : pipeline.stages()) {
      if (stage.isEmpty())
        continue;

      if (!stage.containsLoop())
        continue;

      FuncDecl pc = null;
      for (FuncDecl fnDecl : stage.summary().functions()) {
        if (fnDecl.name().matches("int_expr_.*")) {
          ExtractLiveFunctionSet efs = new ExtractLiveFunctionSet(stage.summary().functions());
          fnDecl.body().accept(efs);
          liveFns.addAll(efs.liveFns());
        }
      }
    }

    StringBuffer fnDecls = new StringBuffer();
    for (FuncDecl fn : liveFns) {
      String fnDecl = fn.accept(new HalidePrinter());
      fnDecls.append((fnDecl.equals("") ? "" : fnDecl + "\n  "));
    }

    return fnDecls.toString();
  }

  private static String extractLocalDecls(Pipeline pipeline) throws Exception {
    Stage head = pipeline.stage(1);
    Stack<Stage> stages = new Stack<>();
    stages.push(head);

    Set<VarExpr> localVars = new LinkedHashSet<>();
    Map<VarExpr,Integer> versions = new HashMap<>();

    while (!stages.isEmpty()) {
      Stage stage = stages.pop();
      if (stage.isEmpty() || !stage.containsLoop())
        continue;

      localVars.addAll(stage.analysis().getLocalVars());

      for (VarExpr v : stage.analysis().getOutputBuffers()) {
        if (versions.containsValue(v))
          versions.put(v, versions.get(v) + 1);
        else {
          if (localVars.contains(v))
            versions.put(v, 1);
          else
            versions.put(v, 0);
        }
      }


      if (stage.forks())
      {
        stages.push(pipeline.stage(stage.consStageId()));
        stages.push(pipeline.stage(stage.altrStageId()));
      }
      else if (stage.nextStageId() != -1)
      {
        stages.push(pipeline.stage(stage.nextStageId()));
      }
    }

    StringBuffer localDecls = new StringBuffer();
    for (VarExpr var: versions.keySet()) {
      int count = versions.get(var);

      if (TypesFactory.isArrayT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isPtrT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isBufferT(var.type())) {
        for (int i=0; i<count; i++)
          if (i==0)
            localDecls.append("Halide::Func " + var.name() + ";\n  ");
          else
            localDecls.append("Halide::Func " + var.name() + "_" + i + ";\n  ");
      }
      else {
        throw new RuntimeException("NYI.");
      }
    }

    return localDecls.toString();
  }

  private static String extractStatements(Pipeline pipeline) throws Exception {
    StringBuffer statements = new StringBuffer();

    Stage curr_stage;
    curr_stage = pipeline.stage(1);

    while (true) {
      if (!curr_stage.isEmpty()) {
        if (curr_stage.containsLoop()) {
          FuncDecl pc = null;

          for (FuncDecl fnDecl : curr_stage.summary().functions()) {
            if (fnDecl.name().equals("pc"))
              pc = fnDecl;
          }

          if (pc == null)
            throw new RuntimeException("Postcondition function not found");

          List<Expr> stmts = getStatements(pc);

          for (Expr stmt : stmts) {
            CallExpr c = (CallExpr) stmt;

            if (c.name().matches(".*_buf_asn_1d(i|f)(_[0-9]+)?$")) {
              statements.append(gen1DFuncAssign(c, curr_stage.summary().functions()) + "\n    ");
            }
            else if (c.name().matches(".*_buf_asn_2d(i|f)(_[0-9]+)?$")) {
              statements.append(gen2DFuncAssign(c, curr_stage.summary().functions()) + "\n    ");
            }
            else {
              NYI();
            }
          }
        }

        //if (curr_stage.forks()) {
          //Stage cons_stage = pipeline.stage(curr_stage.consStageId());
          //Stage altr_stage = pipeline.stage(curr_stage.altrStageId());
          //statements.append(genSelectAssign(curr_stage.cond(),cons_stage.getOutputArrVars(),altr_stage.getOutputArrVars()));
        //}
      }

      if (curr_stage.nextStageId() == -1)
        break;
      else {
        curr_stage = pipeline.stage(curr_stage.nextStageId());
      }
    }

    return statements.toString();
  }

  private static String genSelectAssign(Expr out_var, Expr cond, List<VarExpr> cons, String altr) {
    String lhs = "";
    String rhs = "";
    if (TypesFactory.isBufferT(out_var.type())) {
      BufferT bufT = (BufferT) out_var.type();
      if (bufT.dim() == 1)
         lhs = out_var.accept(new HalidePrinter()) + "(x)";
      else if (bufT.dim() == 2)
        lhs = out_var.accept(new HalidePrinter()) + "(x, y)";
      else
        NYI();

      rhs = "select(" + cond.accept(new HalidePrinter()) + ", " + cons + ", " + altr + ");";
    }

    return lhs + " = " + rhs + ";";
  }

  private static String gen1DFuncAssign(CallExpr c, List<FuncDecl> functions) {
    String ret = "";

    VarExpr out_var = (VarExpr) c.args().get(2);
    VarExpr out_var_init = (VarExpr) c.args().get(3);

    FuncDecl fnDecl = null;
    for (FuncDecl fn : functions)
      if (fn.name().equals(c.name()))
        fnDecl = fn;

    // Discard the for-loops
    Expr e = ((ForallExpr) fnDecl.body()).body();

    Expr rhs_e = ((BinaryExpr) e).right();

    CallExpr v1 = new CallExpr(
            out_var_init.name(),
            Arrays.asList(new VarExpr("x", TypesFactory.Int))
    );

    String lhs = out_var.accept(new HalidePrinter()) + "(x)";
    String rhs = genStencilExpr(rhs_e, v1, functions);

    ret += lhs + " = " + rhs + ";";
    return ret;
  }

  private static String gen2DFuncAssign(CallExpr c, List<FuncDecl> functions) {
    String ret = "";

    Expr lb_y = c.args().get(0);
    Expr ub_y = c.args().get(1);
    Expr lb_x = c.args().get(2);
    Expr ub_x = c.args().get(3);
    VarExpr out_var = (VarExpr) c.args().get(4);
    VarExpr out_var_init = (VarExpr) c.args().get(5);

    FuncDecl fnDecl = null;
    for (FuncDecl fn : functions)
      if (fn.name().equals(c.name()))
        fnDecl = fn;

    // Discard the for-loops
    Expr e = ((ForallExpr) fnDecl.body()).body();
    e = ((ForallExpr) e).body();

    Expr rhs_e = ((BinaryExpr) e).right();

    CallExpr v1 = new CallExpr(
        out_var_init.name(),
        Arrays.asList(new VarExpr("x", TypesFactory.Int), new VarExpr("y", TypesFactory.Int))
    );

    String lhs = out_var.accept(new HalidePrinter()) + "(x, y)";
    String rhs = genStencilExpr(rhs_e, v1, functions);

//    // For now, always output an undef pure definition
//    String bufType = this.analysis.getCppType(((VarExpr)out_var).name());
//    bufType = bufType.substring(0, bufType.length()-2);
//    ret += lhs + " = Halide::undef<" + /*((PtrT)out_var.type()).elemT()*/
//            bufType + ">();\n  ";

    ret += lhs + " = " + rhs + ";";
    return ret;
  }

  private static String genStencilExpr(Expr rhs_e, Expr v1, List<FuncDecl> functions) {
    assert rhs_e instanceof CallExpr;

    CallExpr rhs = (CallExpr) rhs_e;

    FuncDecl exprFnDecl = null;
    for (FuncDecl fn : functions)
      if (fn.name().equals(rhs.name()))
        exprFnDecl = fn;

    assert exprFnDecl != null;

    Map<VarExpr,Expr> termMapping = new HashMap<>();
    termMapping.put(exprFnDecl.params().get(0), v1);
    for (int i=1; i<exprFnDecl.params().size(); i++) {
      VarExpr param = exprFnDecl.params().get(i);
      Expr arg = rhs.args().get(i);
      termMapping.put(param, arg);
    }

    return exprFnDecl.body().accept(new HalidePrinter(termMapping));
  }

  private static String NYI() {
    throw new RuntimeException("NYI.");
  }

  private static String extractSchedule(Pipeline pipeline) {
    Set<VarExpr> inpVars = new LinkedHashSet<>();
    Set<VarExpr> outVars = new LinkedHashSet<>();
    Map<String,String> cppTypes = new HashMap<>();

    getInputVars(pipeline, inpVars, cppTypes);
    getOutputVars(pipeline, outVars, cppTypes);

    StringBuffer schedule = new StringBuffer();
    for (VarExpr var: inpVars) {
      if (TypesFactory.isArrayT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isPtrT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isBufferT(var.type())) {
        String bufType = cppTypes.get(var.name());

        Pattern p = Pattern.compile("class Halide::Runtime::Buffer<(.*?), (.*?)>");
        Matcher m = p.matcher(bufType);
        if (!m.matches())
          throw new RuntimeException("Unexpected buffer type: " + bufType);

        int dims = Integer.parseInt(m.group(2));

        for (int dim = 0; dim < dims; ++dim)
          schedule.append(var.name() + ".dim("+dim+").set_bounds_estimate(0, 1024);\n    ");
      }
      else {
        String tp = cppTypes.get(var.name());
        // TODO make type specific
        schedule.append(var.name() + ".offset.set_estimate(16383);\n    ");
      }
    }

    for (VarExpr var: outVars) {
      if (TypesFactory.isArrayT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isPtrT(var.type())) {
        throw new RuntimeException("NYI.");
      }
      else if (TypesFactory.isBufferT(var.type())) {
        String bufType = cppTypes.get(var.name());

        Pattern p = Pattern.compile("class Halide::Runtime::Buffer<(.*?), (.*?)>");
        Matcher m = p.matcher(bufType);
        if (!m.matches())
          throw new RuntimeException("Unexpected buffer type: " + bufType);

        int dims = Integer.parseInt(m.group(2));

        if (dims == 1)
          schedule.append(var.name() + ".estimate(i, 0, 1024);\n    ");
        else if (dims == 2) {
          schedule.append(var.name() + ".estimate(x, 0, 1024);\n    ");
          schedule.append(var.name() + ".estimate(y, 0, 1024);\n    ");
        }
        else if (dims == 3) {
          schedule.append(var.name() + ".estimate(x, 0, 1024);\n    ");
          schedule.append(var.name() + ".estimate(y, 0, 1024);\n    ");
          schedule.append(var.name() + ".estimate(c, 0, 4);\n    ");
        }
        else {
          for (int dim = 0; dim < dims; ++dim)
            schedule.append(var.name() + ".estimate(d" + dim + ", 0, 1024);\n    ");
        }
      }
      else {
        NYI();
      }
    }

    return schedule.toString();
  }

  private static void getInputVars(Pipeline pipeline, Set<VarExpr> inpVars, Map<String, String> cppTypes) {
    Set<VarExpr> localVars = new LinkedHashSet<>();

    for (Stage stage : pipeline.stages()) {
      if (stage.isEmpty() || !stage.containsLoop())
        continue;

      localVars.addAll(stage.analysis().getLocalVars());

      for (VarExpr v : stage.analysis().getInputVars())
        if (!localVars.contains(v) && varIsUsed(v, stage))
          inpVars.add(v);

      cppTypes.putAll(stage.analysis().cppTypes());
    }
  }

  private static boolean varIsUsed(VarExpr v, Stage stage) {
    FuncDecl pc = null;

    for (FuncDecl fnDecl : stage.summary().functions()) {
      if (fnDecl.name().equals("pc"))
        pc = fnDecl;
    }

    if (pc == null)
      throw new RuntimeException("Postcondition function not found");

    List<Expr> stmts = getStatements(pc);

    for (Expr stmt : stmts) {
      CallExpr c = (CallExpr) stmt;
      if (c.name().matches(".*_buf_asn_2d(i|f)(_[0-9]+)?$")) {
        // Find Decl
        FuncDecl fnDecl = null;
        for (FuncDecl fn :stage.summary().functions())
          if (fn.name().equals(c.name()))
            fnDecl = fn;

        // Discard the for-loops
        Expr e = ((ForallExpr) fnDecl.body()).body();
        e = ((ForallExpr) e).body();

        // Get expr
        CallExpr rhs = (CallExpr) ((BinaryExpr) e).right();
        FuncDecl exprFnDecl = null;
        for (FuncDecl fn : stage.summary().functions())
          if (fn.name().equals(rhs.name()))
            exprFnDecl = fn;

        assert exprFnDecl != null;

        CallExpr v1 = new CallExpr(
            ((VarExpr) c.args().get(4)).name(),
            Arrays.asList(new VarExpr("x", TypesFactory.Int), new VarExpr("y", TypesFactory.Int))
        );

        Map<VarExpr,Expr> termMapping = new HashMap<>();
        termMapping.put(exprFnDecl.params().get(0), v1);
        for (int i=1; i<exprFnDecl.params().size(); i++) {
          VarExpr param = exprFnDecl.params().get(i);
          Expr arg = rhs.args().get(i);
          termMapping.put(param, arg);
        }

        ExtractLiveVars elv = new ExtractLiveVars(termMapping);
        exprFnDecl.body().accept(elv);
        if (elv.contains(v))
          return true;
      }
      else if (c.name().matches(".*_buf_asn_1d(i|f)(_[0-9]+)?$")) {
        // Find Decl
        FuncDecl fnDecl = null;
        for (FuncDecl fn :stage.summary().functions())
          if (fn.name().equals(c.name()))
            fnDecl = fn;

        // Discard the for-loop
        Expr e = ((ForallExpr) fnDecl.body()).body();

        // Get expr
        CallExpr rhs = (CallExpr) ((BinaryExpr) e).right();
        FuncDecl exprFnDecl = null;
        for (FuncDecl fn : stage.summary().functions())
          if (fn.name().equals(rhs.name()))
            exprFnDecl = fn;

        assert exprFnDecl != null;

        CallExpr v1 = new CallExpr(
                ((VarExpr) c.args().get(2)).name(),
                Arrays.asList(new VarExpr("x", TypesFactory.Int))
        );

        Map<VarExpr,Expr> termMapping = new HashMap<>();
        termMapping.put(exprFnDecl.params().get(0), v1);
        for (int i=1; i<exprFnDecl.params().size(); i++) {
          VarExpr param = exprFnDecl.params().get(i);
          Expr arg = rhs.args().get(i);
          termMapping.put(param, arg);
        }

        ExtractLiveVars elv = new ExtractLiveVars(termMapping);
        exprFnDecl.body().accept(elv);
        if (elv.contains(v))
          return true;
      }
      else
        NYI();
    }

    return false;
  }

  private static List<Expr> getStatements(FuncDecl pc) {
    List<Expr> stmts = new ArrayList<Expr>();
    Stack<Expr> stack = new Stack<>();
    stack.push(pc.body());

    while (!stack.isEmpty()) {
      Expr stmt = stack.pop();
      if (stmt instanceof CallExpr)
        stmts.add(stmt);
      else if (stmt instanceof BinaryExpr)
      {
        stack.push(((BinaryExpr) stmt).left());
        stack.push(((BinaryExpr) stmt).right());
      }
      else if (stmt instanceof BoolLitExpr)
        ;
      else
        NYI();
    }

    return stmts;
  }

  private static void getOutputVars(Pipeline pipeline, Set<VarExpr> outpVars, Map<String, String> cppTypes) {
    Set<VarExpr> localVars = new LinkedHashSet<>();
    for (Stage stage : pipeline.stages()) {
      if (stage.isEmpty() || !stage.containsLoop())
        continue;

      localVars.addAll(stage.analysis().getLocalVars());

      for (VarExpr v : stage.analysis().getOutputVars())
        if (!localVars.contains(v))
          outpVars.add(v);

      cppTypes.putAll(stage.analysis().cppTypes());
    }
  }
}