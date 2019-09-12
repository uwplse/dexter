package dexter;

import dexter.analysis.ExtractLiveFunctionSet;
import dexter.analysis.ExtractStencilExpr;
import dexter.analysis.VCAnalyzer;
import dexter.codegen.halide.CodeGen;
import dexter.dag.Pipeline;
import dexter.dag.Stage;
import dexter.frontend.CodeAnalysis;
import dexter.frontend.CodeBlock;
import dexter.frontend.CppFrontend;
import dexter.grammar.Grammar;
import dexter.ir.Expr;
import dexter.ir.array.DecrPtrExpr;
import dexter.ir.array.IncrPtrExpr;
import dexter.ir.array.SelectExpr;
import dexter.ir.array.StoreExpr;
import dexter.ir.bool.*;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.semantics.TypeChecker;
import dexter.ir.semantics.VarChecker;
import dexter.ir.type.*;
import dexter.modifier.*;
import dexter.synthesis.ExprPatcher;
import dexter.synthesis.Synthesizer;
import scala.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Maaz Ahmad on 6/26/19.
 */
public class Compiler {

  public static void run () throws Exception {
    // Run Cpp Frontend
    if (Preferences.Global.run_frontend)
      CppFrontend.run();

    // Load intention code blocks
    Set<CodeBlock> intentionalBlocks = CppFrontend.getIntentionalBlocks();

    if (intentionalBlocks.size() == 0)
      return;

    // Initialize TypesFactory
    TypesFactory.loadDSL(Files.coreDslFilePath());
    TypesFactory.loadDSL(Files.userDslFilePath());

    for (CodeBlock cb : intentionalBlocks) {
      // Get DAG of operations in code block
      Pipeline pipeline = cb.asDAG();

      // Lift each stage of the pipeline using 3-stage synth algorithm
      for (Stage stage : pipeline.stages()) {
        if (stage.isEmpty() || !stage.containsLoop())
          continue;

        System.out.println("\n## Lifting code block `" + cb.name() + "` (Stage " + stage.id() + ")\n");

        // If execution logs available from prior runs, re-use them
        File stageSummary = Files.summaryFile(cb.name(), stage.id());
        if (stageSummary.exists() && Preferences.Global.use_output_logs)
          continue;

        // Get the set of output buffers
        Set<VarExpr> outputBuffers = stage.getOutputBuffers();

        // Synthesize PC for each output variable individually
        Map<Expr, Expr> termsMapping = new LinkedHashMap<>();
        List<Program> clauses = new ArrayList<>();
        for (VarExpr buffer : outputBuffers) {
          System.out.println("Synthesizing post-condition clause for buffer: " + buffer.name());

          // Filter outvars set
          stage.analysis().filterOutputBuffers(buffer);

          File clause = Files.clauseFile(cb.name(), stage.id(), buffer.name());
          if (clause.exists() && Preferences.Global.use_output_logs) {
            Program c = Files.loadIRFile(clause);
            clauses.add(c);
          }
          else {
            // Stage 1: Synthesize clause template
            Program roi = synthesizeROI(cb, stage, buffer);

            // Stage 2: Synthesize points and scalars used in the clause stencil
            Program roi_points = synthesizeTerminals(cb, stage, buffer, roi, termsMapping);

            // Stage 3: Synthesize stencil expression
            Program roi_points_expr = synthesizeStencilExpr(cb, stage, buffer, roi_points, termsMapping);

            // Stage 4: Verify synthesized clause
            // TODO

            clauses.add(roi_points_expr);
          }

          // Restore outvars set
          stage.analysis().restoreOutputBuffers();
        }

        // Stitch clauses together to generate stage summary
        stage.summary(generateSummary(clauses));
      }

      // Use synthesized summary to generate Halide Code
      String halideCode = CodeGen.asHalideGenerator(cb);

      System.out.println("\n\n" + halideCode);
    }
  }

  // 3 synthesis stages
  private static Program synthesizeROI(CodeBlock cb, Stage stage, VarExpr buffer) throws IOException, InterruptedException {
    System.out.println("\nStep 1: Infer output ROI");

    if (!Files.binDir(cb.name(), stage.id()).exists()) Files.binDir(cb.name(), stage.id()).mkdir();

    System.out.println("   Preparing synthesis problem...");

    // Load VC
    Program p = Files.loadIRFile(Files.vcFile(cb.name(), stage.id()));

    // Load UDFs
    Program udfs = Files.loadIRFile(Files.udfsFile(cb.name(), stage.id()));

    // Load DSL
    Program coreDSL = Files.loadIRFile(Files.coreDslFile());
    Program userDSL = Files.loadIRFile(Files.userDslFile());

    // Stitch VC program with the UDFs
    p.prepend(udfs);

    // Stitch VC program with the DSLs
    p.prepend(userDSL);
    p.prepend(coreDSL);

    // Check semantics
    checkSemantics(p);

    // Analyze array writes
    VCAnalyzer vca = new VCAnalyzer(p.functions(), buffer);
    p.accept(vca);

    // Load program analysis
    CodeAnalysis analysis = stage.analysis();

    // Important: Convert all array writes to buffer into uninterpreted exprs
    Expr ui_expr = genUIExpr(buffer, vca.getStencilWriteIndexes(), new ArrayList<>());
    p = (Program) p.accept(new VCWritesUpdator(p.functions(), analysis.getOutputBuffers(), ui_expr));

    // Load grammar
    System.out.println("   Loading ROI grammar... ");
    p = Grammar.loadROIGrammar(p, analysis);

    // Check semantics
    checkSemantics(p);

    // Run synthesizer
    System.out.print("   Running the synthesizer... ");

    Preferences.Sketch.expr_codegen = false;
    Preferences.Sketch.lightverif = Preferences.Sketch.ROI.lightverif;
    Preferences.Sketch.minisat = Preferences.Sketch.ROI.minisat;
    p = Synthesizer.solve(
        p,
        Files.roiFilePath(cb.name(), stage.id(), buffer.name()),
        Files.roiSketchFilePath(cb.name(), stage.id(), buffer.name())
    );

    // Remove generators and unused functions
    removeUnusedFunctions(p, false);

    if (Preferences.Global.verbosity > 2)
      p.print();

    return p;
  }

  private static Program synthesizeTerminals(CodeBlock cb, Stage stage, VarExpr buffer, Program p, Map<Expr, Expr> termMapping) throws IOException, InterruptedException {
    System.out.println("\nStep 2: Infer terminals");

    System.out.println("   Preparing synthesis problem...");

    // Load UDFs
    Program udfs = Files.loadIRFile(Files.udfsFile(cb.name(), stage.id()));

    // Load DSL
    Program coreDSL = Files.loadIRFile(Files.coreDslFile());
    Program userDSL = Files.loadIRFile(Files.userDslFile());

    // Stitch VC program with the UDFs
    p.prepend(udfs);

    // Stitch VC program with the DSLs
    p.prepend(userDSL);
    p.prepend(coreDSL);

    // Load unmodified VC
    Program vc = Files.loadIRFile(Files.vcFile(cb.name(), stage.id()));
    vc.prepend(udfs);
    vc.prepend(userDSL);
    vc.prepend(coreDSL);

    checkSemantics(vc);

    // Analyze array writes
    VCAnalyzer vca = new VCAnalyzer(p.functions(), buffer);
    vc.accept(vca);

    // Incrementally synthesize point mappings
    int tid = 0;
    Program tm = null;
    for (Expr term : vca.getStencilTerms())
    {
      if (term instanceof VarExpr || term instanceof IntLitExpr || term instanceof BoolLitExpr || term instanceof FloatLitExpr)
      {
        // Scalars and literals maps to themselves
        termMapping.put(term, term);

        continue;
      }
      if (termMapping.containsKey(term))
        continue;

      System.out.println("\n   Mapping terminal: " + term);

      // Load program analysis
      CodeAnalysis analysis = stage.analysis();

      // Generate uninterpreted expression
      Expr ui_expr  = genUIExpr(buffer, vca.getStencilWriteIndexes(), Arrays.asList(term));

      // Important: Change all array writes to uninterpreted exprs
      VCWritesUpdator vcwa = new VCWritesUpdator(p.functions(), analysis.getOutputBuffers(), ui_expr);
      tm = new Program(p.functions(), p.classes(), ((Program) vc.accept(vcwa)).body());

      // Load grammar
      System.out.println("      Loading terminal grammar... ");
      tm = Grammar.loadTermGrammar(tm, analysis, buffer.type(), term.type());

      // Check semantics
      checkSemantics(tm);

      // Run synthesizer
      System.out.print("      Running the synthesizer... ");

      Preferences.Sketch.expr_codegen = false;
      Preferences.Sketch.lightverif = Preferences.Sketch.Term.lightverif;
      Preferences.Sketch.minisat = Preferences.Sketch.Term.minisat;
      tm = Synthesizer.solve(
          tm,
          Files.termFilePath(cb.name(), stage.id(), buffer.name(), tid),
          Files.termSketchFilePath(cb.name(), stage.id(), buffer.name(), tid)
      );

      checkSemantics(tm);

      // Extract term mapping
      Expr point = extractSynthesizedPoint(tm);

      if (Preferences.Global.verbosity > 2)
        point.print();

      // Save term mapping
      termMapping.put(term, point);

      tid++;
    }

    p = tm;

    // Remove generators and unused functions
    removeUnusedFunctions(p, false);

    // Add dyn constants to let statement
    LetExpr currBody = (LetExpr) p.body();
    p.body(new LetExpr(currBody.vars(), currBody.body(), currBody.assumptions()));

    // Load program analysis
    CodeAnalysis analysis = stage.analysis();

    // Update postconditions
    updateStencilExpression(p, termMapping, buffer.type());

    if (Preferences.Global.verbosity > 1)
      p.print();

    return p;
  }

  private static Program synthesizeStencilExpr(CodeBlock cb, Stage stage, VarExpr buffer, Program p, Map<Expr, Expr> termsMapping) throws IOException, InterruptedException {
    System.out.println("\nStep 3: Infer stencil expr");

    System.out.println("   Preparing synthesis problem...");

    // Load UDFs
    Program udfs = Files.loadIRFile(Files.udfsFile(cb.name(), stage.id()));

    // Load DSL
    Program coreDSL = Files.loadIRFile(Files.coreDslFile());
    Program userDSL = Files.loadIRFile(Files.userDslFile());

    // Stitch VC program with the UDFs
    p.prepend(udfs);

    // Stitch VC program with the DSLs
    p.prepend(userDSL);
    p.prepend(coreDSL);

    // Load unmodified VC
    Program vc = Files.loadIRFile(Files.vcFile(cb.name(), stage.id()));
    vc.prepend(udfs);
    vc.prepend(userDSL);
    vc.prepend(coreDSL);
    checkSemantics(vc);

    // Extract terms from array writes
    VCAnalyzer vca = new VCAnalyzer(vc.functions(), buffer);
    vc.accept(vca);

    // Restore original VCs for stage 3
    vc = (Program) vc.accept(new SymbolicConstArgsGenerator(p.functions()));
    p.body(vc.body());

    // Stitch stage 3 grammar for synthesizing expressions
    System.out.println("   Loading Expr grammar... ");
    List<Expr> opts = genExprSynthOpts(termsMapping, vca.getStencilExprs(), buffer.type());
    p = Grammar.loadExprGrammar(p, stage.analysis(), opts, buffer.type());

    // Check semantics
    checkSemantics(p);

    // Run synthesizer
    System.out.print("   Running the synthesizer... ");

    Preferences.Sketch.expr_codegen = true;
    Preferences.Sketch.lightverif = Preferences.Sketch.Expr.lightverif;
    Preferences.Sketch.minisat = Preferences.Sketch.Expr.minisat;
    p = Synthesizer.solve(
        p,
        Files.exprFilePath(cb.name(), stage.id(), buffer.name()),
        Files.exprSketchFilePath(cb.name(), stage.id(), buffer.name())
    );

    // Patch
    p = (Program) p.accept(new ExprPatcher(p.functions(), opts));
    File skParse = new File("metalift_skParse.txt");
    skParse.delete();

    // Remove generators and unused functions
    removeUnusedFunctions(p, true);

    // Remove symbolic constants
    p = (Program) p.accept(new SymbolicConstsRemover(p.functions()));

    // Write clause to file for debugging
    Files.writeFile(Files.clauseFilePath(cb.name(), stage.id(), buffer.name()), p.toString());

    return p;
  }

  private static Program generateSummary(List<Program> clauses) {
    assert clauses.size() > 0;

    Program s = clauses.get(0);

    s = (Program) s.accept(new PostfixFnNamesWithID(s.functions(), 0));

    for (int i=1; i<clauses.size(); i++)
    {
      Program clause = clauses.get(i);
      clause = (Program) clause.accept(new PostfixFnNamesWithID(clause.functions(), i));

      for (FuncDecl s_fn : s.functions())
        for (FuncDecl c_fn : clause.functions())
          if (s_fn.name().equals("pc") && c_fn.name().equals("pc"))
            s_fn.body(new BinaryExpr(s_fn.body(), BinaryExpr.Op.AND, c_fn.body()));

      for (FuncDecl fn : clause.functions())
        if (!fn.name().equals("pc") && s.functions().stream().filter(fnD -> fnD.name().equals(fn.name())).count() == 0)
          s.functions().add(fn);
    }

    for (FuncDecl fn : s.functions())
      if (fn.name().equals("index2D"))
        fn.body(new BinaryExpr(new BinaryExpr(new VarExpr("y", TypesFactory.Int), BinaryExpr.Op.MULT, new VarExpr("width", TypesFactory.Int)), BinaryExpr.Op.PLUS, new VarExpr("x", TypesFactory.Int)));

    return s;
  }

  // Util functions
  private static void checkSemantics(Program p) {
    p.accept(new VarChecker(true));
    p.accept(new TypeChecker());
  }

  private static Expr genUIExpr(VarExpr target, Set<Tuple2<Expr, List<Expr>>> idxs, List<Expr> params) {
    Expr ui_expr = target;

    Type elemT ;
    if (target.type() instanceof CollectionT)
      elemT = ((CollectionT) target.type()).elemT();
    else
      throw new RuntimeException("NYI");

    List<Tuple2<Expr, List<Expr>>> idxsL = new ArrayList<>(idxs);

    for (int i=idxsL.size()-1; i>=0; i--) {
      Tuple2<Expr,List<Expr>> idx = idxsL.get(i);

      List<Expr> idxLst = idx._2;

      Expr inp;
      if (target.type() instanceof ClassT) {
        List<Expr> args = new ArrayList<>();
        args.add(target);
        args.addAll(idxLst);
        inp = new CallExpr("HBuffer_Get", args);
      }
      else
        inp = new SelectExpr(target, idxLst);

      List<Expr> inpLst = new ArrayList<>();
      if (params.isEmpty())
        inpLst.add(inp);
      else
        inpLst.addAll(params);

      if (elemT == TypesFactory.Int)
        if (target.type() instanceof ClassT) {
          List<Expr> args = new ArrayList<>();
          args.add(ui_expr);
          args.addAll(idxLst);
          args.add(new CallExpr("int_expr", inpLst));
          ui_expr = new CallExpr("HBuffer_Set", args);
        }
        else
          ui_expr = new StoreExpr(ui_expr, new CallExpr("int_expr", inpLst), idxLst);
      else if (elemT == TypesFactory.Float)
        if (target.type() instanceof ClassT) {
          List<Expr> args = new ArrayList<>();
          args.add(ui_expr);
          args.addAll(idxLst);
          args.add(new CallExpr("float_expr", inpLst));
          ui_expr = new CallExpr("HBuffer_Set", args);
        }
        else
          ui_expr = new StoreExpr(ui_expr, new CallExpr("float_expr", inpLst), idxLst);
      else
        throw new RuntimeException("Unexpected outvar elem type: " + target.type());

      // Does the ptr need to be updated before this write?
      if (idx._1 != null) {
        if (idx._1 instanceof IncrPtrExpr)
          ui_expr = new IncrPtrExpr(ui_expr, ((IncrPtrExpr)idx._1).incr());
        else if (idx._1 instanceof DecrPtrExpr)
          ui_expr = new DecrPtrExpr(ui_expr, ((DecrPtrExpr)idx._1).decr());
        else
          throw new RuntimeException("NYI");
      }
    }

    return ui_expr;
  }

  private static void removeUnusedFunctions (Program p, boolean all) {
    // Get set of functions used
    Set<String> liveFns = new HashSet<>();

    if (all) {
      ExtractLiveFunctionSet fse = new ExtractLiveFunctionSet(p.functions());
      p.body().accept(fse);
      liveFns.addAll(fse.liveFns().stream().map(fnDecl -> fnDecl.name()).collect(Collectors.toSet()));
    }
    else {
      for (FuncDecl fn : p.functions()) {
        if (fn.name().equals("pc") || fn.name().matches("inv[0-9]+")) {
          ExtractLiveFunctionSet fse = new ExtractLiveFunctionSet(p.functions());
          fn.body().accept(fse);
          liveFns.addAll(fse.liveFns().stream().map(fnDecl -> fnDecl.name()).collect(Collectors.toSet()));
        }
      }
    }

    p.functions(p.functions()
        .stream()
        .filter(fn -> fn.name().equals("pc") || fn.name().matches("inv[0-9]+") || liveFns.contains(fn.name()))
        .collect(Collectors.toList()));
  }

  private static Expr extractSynthesizedPoint(Program p) {
    for (FuncDecl fn : p.functions())
      if (fn.name().equals("pc")) {
        ExtractStencilExpr pe = new ExtractStencilExpr(p.functions());
        fn.accept(pe);
        CallExpr stencil_expr = (CallExpr) pe.getExpr();
        return stencil_expr.args().get(0);
      }

    // Should never reach this point
    throw new RuntimeException("PC function not found.");
  }

  private static void updateStencilExpression(Program p, Map<Expr, Expr> termMapping, Type bufT) {
    List<FuncDecl> fns = new ArrayList<>();

    Type elemT ;
    if (bufT instanceof ArrayT)
      elemT = ((ArrayT) bufT).elemT();
    else if (bufT instanceof PtrT)
      elemT = ((PtrT) bufT).elemT();
    else if (bufT instanceof ClassT) {
      ClassT clsT = (ClassT) bufT;
      if (clsT.name().equals("HBuffer"))
        elemT = ((ArrayT) clsT.fields().get(0).type()).elemT();
      else
        throw new RuntimeException("NYI");
    }
    else
      throw new RuntimeException("NYI");

    // Outvar_init value
    List<Expr> idx = Arrays.asList(
            new VarExpr("idx_x", TypesFactory.Int),
            new VarExpr("idx_y", TypesFactory.Int)
    );

    Expr e;
    if (bufT instanceof ClassT) {
      List<Expr> idx2 = new ArrayList<>();
      idx2.add(new VarExpr("out_init", bufT));
      idx2.addAll(idx);
      e = new CallExpr("HBuffer_Get", idx2);
    }
    else
      e = new SelectExpr(new VarExpr("out_init", bufT), idx);

    e.type(elemT);

    List<Expr> terms = new ArrayList<>();
    terms.add(e);
    terms.addAll(termMapping.values().stream().map(v -> v.accept(new VarExprToScopeMacroConvertor())).collect(Collectors.toList()));

    Expr genCall = null;

    if (elemT == TypesFactory.Int)
      genCall = new CallExpr("int_expr", terms);
    else if (elemT == TypesFactory.Float)
      genCall = new CallExpr("float_expr", terms);
    else
      throw new RuntimeException("Unexpected outVar type: " + bufT);

    // Update stencil expressions with calls to the generator function
    for (FuncDecl fn : p.functions())
      if (fn.name().equals("pc") || fn.name().matches("inv[0-9]+"))
        fns.add((FuncDecl) fn.accept(new StencilExprSetter(p.functions(), genCall)));
      else if (fn.name().equals("int_expr") || fn.name().equals("float_expr"))
      {
        List<VarExpr> params = new ArrayList<>();
        int i = 1;
        for (Expr term : terms)
          params.add(new VarExpr("v" + i++, term.type()));

        fn.params(params);
        fn.body(null);
        fn.isUnInterpreted(true);
        fns.add(fn);
      }
      else if (!fns.contains(fn))
        fns.add(fn);

    p.functions(fns);
  }

  private static List<Expr> genExprSynthOpts (Map<Expr, Expr> termsMapping, Set<Expr> stencilExprs, Type bufT) throws IOException {
    List<Expr> opts = new ArrayList<>();
    Map<Expr,Expr> localMapping = new LinkedHashMap<>();

    int i = 1;

    Type elemT;
    if (bufT instanceof ArrayT)
      elemT = ((ArrayT) bufT).elemT();
    else if (bufT instanceof PtrT)
      elemT = ((PtrT) bufT).elemT();
    else if (bufT instanceof ClassT)
    {
      ClassT clsT = (ClassT) bufT;
      if (clsT.name().equals("HBuffer"))
        elemT = ((ArrayT) clsT.fields().get(0).type()).elemT();
      else
        throw new RuntimeException("Unexpected outVar type: " + bufT);
    }
    else
      throw new RuntimeException("Unexpected outVar type: " + bufT);

    if (elemT == null)
      throw new RuntimeException("Unexpected outvar type: " + bufT);

    opts.add(new VarExpr("v" + i++, elemT));

    for (Expr e : termsMapping.values())
    {
      VarExpr lv = new VarExpr("v" + i++, e.type());
      if (elemT == lv.type()) opts.add(lv);
      localMapping.put(e, lv);
    }

    for (Expr e : stencilExprs)
    {
      for (Map.Entry<Expr,Expr> m : termsMapping.entrySet()){
        Expr from = m.getKey();
        Expr to = localMapping.get(m.getValue());
        e = e.accept(new TermSubstitutor(from, to));
      }

      opts.add(e);
    }

    return opts;
  }
}