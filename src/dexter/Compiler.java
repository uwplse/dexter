package dexter;

import dexter.frontend.CppFrontend;

/**
 * Created by Maaz Ahmad on 6/26/19.
 */
public class Compiler {
  public static void run () {
    // Run Cpp Frontend
    if ( Preferences.Global.run_frontend )
      CppFrontend.run();

    // Load intention code blocks


    // Read DAG
    //Pipeline pipeline = CppFrontend.loadDAG();

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
}