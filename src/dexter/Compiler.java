package dexter;

import dexter.analysis.VCAnalyzer;
import dexter.dag.Pipeline;
import dexter.dag.Stage;
import dexter.frontend.CodeAnalysis;
import dexter.frontend.CodeBlock;
import dexter.frontend.CppFrontend;
import dexter.ir.Expr;
import dexter.ir.array.DecrPtrExpr;
import dexter.ir.array.IncrPtrExpr;
import dexter.ir.array.SelectExpr;
import dexter.ir.array.StoreExpr;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.Program;
import dexter.ir.bool.VarExpr;
import dexter.ir.codegen.MacroExpander;
import dexter.ir.semantics.TypeChecker;
import dexter.ir.semantics.VarChecker;
import dexter.ir.type.*;
import dexter.modifier.VCWritesUpdator;
import dexter.synthesis.Synthesizer;
import scala.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static dexter.grammar.Grammar.Name.ROIGrammar;

/**
 * Created by Maaz Ahmad on 6/26/19.
 */
public class Compiler {

  public static void run () throws IOException, InterruptedException {
    // Run Cpp Frontend
    if (Preferences.Global.run_frontend)
      CppFrontend.run();

    // Initialize TypesFactory
    TypesFactory.loadDSL(Files.coreDslFilePath());
    TypesFactory.loadDSL(Files.userDslFilePath());

    // Load intention code blocks
    Set<CodeBlock> intentionalBlocks = CppFrontend.getIntentionalBlocks();

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
            Program template = synthesizeROI(cb, stage, buffer);

            // Stage 2: Synthesize points and scalars used in the clause stencil
            //Program template_points = synthesizeStencilTerminals(stage, outVar, template, termsMapping);

            // Stage 3: Synthesize stencil expression
            //Program clause = synthesizeStencilExpr(stage, outVar, template_points, termsMapping);

            // Stage 4: Verify synthesized clause
            //boolean verifies = true;//verifySummary(stage, clause);

            //clauses.add(clause);
          }

          // Restore outvars set
          stage.analysis().restoreOutputBuffers();
        }
      }
    }

    // Solve each stage
    /*for (Stage stage : pipeline.stages())
    {
        // Clear previously defined functions and classes
        TypesFactory.reset();

        if (stage.isEmpty() || !stage.containsLoop())
            continue;

        System.out.println("\nSynthesizing PC for Stage " + stage.id() + "\n");

        // Check if this stage was previously compiled
        File stageSummary = new File(filepaths.stageSummary(stage.id()));
        if (stageSummary.exists() && !force_rerun)
            continue;

        // Get set of output variables
        Set<VarExpr> outArrVars = stage.getOutputArrVars();

        // Synthesize PC for each output variable individually
        Map<Expr, Expr> termsMapping = new LinkedHashMap<>();
        List<Program> clauses = new ArrayList<>();
        for (VarExpr outVar : outArrVars) {
            System.out.println("Synthesizing PC clause for: " + outVar.name() + "\n");

            // Filter outvars set
            stage.analysis().filterOutputArrVars(outVar);

            File summaryClause = new File(filepaths.clauseFile(stage.id(), outVar.name()));
            if (summaryClause.exists() && !force_rerun) {
                Program clause = loadClause(summaryClause);
                clauses.add(clause);
            }
            else {
                // Stage 1: Synthesize clause template
                Program template = synthesizeClauseTemplate(stage, outVar);

                // Stage 2: Synthesize points and scalars used in the clause stencil
                Program template_points = synthesizeStencilTerminals(stage, outVar, template, termsMapping);

                // Stage 3: Synthesize stencil expression
                Program clause = synthesizeStencilExpr(stage, outVar, template_points, termsMapping);

                // Stage 4: Verify synthesized clause
                boolean verifies = true;//verifySummary(stage, clause);

                clauses.add(clause);
            }

            // Restore outvars set
            stage.analysis().restoreOutputArrVars();
        }

        // Stitch clauses to generate Summary/*
        Program summary = generateSummary(clauses);

        // Write summary to file for debugging
        writeFile(filepaths.stageSummary(stage.id()), summary.accept(new Printer()), "Writing Summary ...");
    }

    // Generate Halide code
    //String halideCode = generateHalideCode(summary);

    // Write output
    //writeFile(outputFilePath, halideCode, "Writing Output ...");

    // Delete intermediate fiels
    cleanup(pipeline.stages());*/
  }

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

    // Extract terms from array writes
    VCAnalyzer vca = new VCAnalyzer(p.functions(), buffer);
    p.accept(vca);

    // Load program analysis
    CodeAnalysis analysis = stage.analysis();

    // Important: Convert all array writes to buffer into uninterpreted exprs
    Expr ui_expr = genUIExpr(buffer, vca.getStencilWriteIndexes(), new ArrayList<>());
    p = (Program) p.accept(new VCWritesUpdator(p.functions(), analysis.getOutputBuffers(), ui_expr));

    // Read Grammar file
    Program grammar = Files.loadIRFile(Files.grammarFile(ROIGrammar));

    // Stitch Grammar file
    p.stitch(grammar);

    // Expand Macros
    p = (Program) p.accept(new MacroExpander(analysis, true));

    if (Preferences.Global.debug)
      p.print();

    // Check semantics
    checkSemantics(p);

    System.out.println("   Running the synthesizer...");

    Preferences.Sketch.expr_codegen = false;
    Preferences.Sketch.lightverif = Preferences.Sketch.ROI.lightverif;
    Preferences.Sketch.minisat = Preferences.Sketch.ROI.minisat;
    p = Synthesizer.solve(cb, stage, buffer, p);

    // Init ChooseIDs -- needed since macro expansion dynamically generates new ChooseExpr
    /*

    // Remove generators and unused functions
    removeUnusedFunctions(p, false);
*/
    return p;
  }

  private static void checkSemantics(Program p) {
    p.accept(new VarChecker(true));
    p.accept(new TypeChecker());
  }

  private static Expr genUIExpr(VarExpr target, Set<Tuple2<Expr, List<Expr>>> idxs, List<Expr> params) {
    Expr ui_expr = target;

    Type elemT ;
    if (target.type() instanceof ArrayT)
      elemT = ((ArrayT) target.type()).elemT();
    else if (target.type() instanceof PtrT)
      elemT = ((PtrT) target.type()).elemT();
    else if (target.type() instanceof ClassT) {
      ClassT clsT = (ClassT) target.type();
      if (clsT.name().equals("HBuffer"))
        elemT = ((ArrayT) clsT.fields().get(0).type()).elemT();
      else
        throw new RuntimeException("NYI");
    }
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
}