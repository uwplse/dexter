package dexter.synthesis;

import dexter.Files;
import dexter.Preferences;
import dexter.ir.bool.Program;
import dexter.ir.codegen.SkPrinter;
import dexter.misc.ChooseIDInitializer;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

/**
 * Created by Maaz Ahmad on 8/7/19.
 */
public class Synthesizer {
  public static Program solve(Program p, String irFilePath, String skFilePath) throws IOException, InterruptedException {
    p.accept(new ChooseIDInitializer());

    Files.writeFile(irFilePath, p.toString());
    Files.writeFile(skFilePath, p.accept(new SkPrinter()));

    // Run sketch
    int err_code = runSketch(skFilePath);

    if (err_code == 0) {
      if (Preferences.Global.verbosity > 0)
        out.println("(Successful)");
    }
    else {
      out.println("(Failed)");
      System.exit(1);
    }

    // Patch output
    if (!Preferences.Sketch.expr_codegen) {
      p = (Program) p.accept(new Patcher(p.functions()));

      File skParse = new File("metalift_skParse.txt");
      skParse.delete();
    }

    return p;
  }

  private static int runSketch(String filepath) throws IOException, InterruptedException {
    ProcessBuilder pb;
    Map<String, String> env;

    File outF = new File(filepath + ".out");
    File errF = new File(filepath + ".err");
    File parseF = new File(filepath + ".skParse");
    File defParseF = new File("metalift_skParse.txt");

    if (outF.exists() && Preferences.Global.use_output_logs)
    {
      if (parseF.exists())
      {
        if (defParseF.exists())
          defParseF.delete();

        java.nio.file.Files.copy(Paths.get(filepath + ".skParse"), Paths.get("metalift_skParse.txt"));
        return 0;
      }
    }

    List<String> command = new ArrayList<>();
    command.add("sketch");
    command.add("-V" + Preferences.Sketch.verbosity);

    if (Preferences.Sketch.slv_parallel)
      command.add("--slv-parallel");
    if (Preferences.Sketch.slv_parallel) {
      command.add("--slv-p-cpus"); command.add(Integer.toString(Preferences.Sketch.cores));
    }

    command.add("--slv-simiters"); command.add(Integer.toString(Preferences.Sketch.sim_iters));
    command.add("--slv-seed"); command.add(Integer.toString(Preferences.Sketch.slv_seed));

    if (Preferences.Sketch.lightverif) command.add("--slv-lightverif");
    if (Preferences.Sketch.minisat) { command.add("--slv-synth"); command.add("MINI"); }

    command.add("--bnd-inline-amnt"); command.add(Integer.toString(Preferences.Sketch.inline_bnd));
    command.add("--bnd-unroll-amnt"); command.add(Integer.toString(Preferences.Sketch.unrolL_bnd));

    command.add("--bnd-inbits"); command.add(Integer.toString(Preferences.Sketch.inbits_bnd));
    command.add("--bnd-cbits"); command.add(Integer.toString(Preferences.Sketch.cbits_bnd));
    command.add("--bnd-int-range"); command.add(Integer.toString(Preferences.Sketch.int_range));
    command.add("--bnd-arr-size"); command.add(Integer.toString(Preferences.Sketch.arr_sz_bnd));

    String codeGen = Synthesizer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String exprCodeGen = new File(codeGen).getParent() + "/dexterExprCodegen.jar";
    command.add("--fe-custom-codegen"); command.add((Preferences.Sketch.expr_codegen ? exprCodeGen : codeGen));

    command.add(filepath);

    if (Preferences.Global.verbosity > 1)
      System.out.println(String.join(" ", command));

    pb = new ProcessBuilder(command);

    env = pb.environment();

    int code;

    if (Preferences.Global.log)
      code = pb.redirectOutput(outF).redirectError(errF).start().waitFor();
    else {
      Process p = pb.start();
      BufferedReader outS = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader errS = new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ( outS.readLine() != null || errS.readLine() != null);
      code = p.waitFor();
    }

    if (parseF.exists())
      parseF.delete();

    if (code != 0)
      return code;

    java.nio.file.Files.copy(Paths.get("metalift_skParse.txt"), Paths.get(filepath + ".skParse"));

    return code;
  }
}