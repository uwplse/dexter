package dexter;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Preferences {
  public static class Global {
    public static boolean log = false;
    public static boolean debug = false;
    public static boolean use_output_logs = false;
    public static boolean run_frontend = false;
    public static boolean generate_intermediate_files = false;
  }

  public static class Sketch {
    public static class ROI {
      public static boolean lightverif = true;
      public static boolean minisat = false;
    }

    public static boolean expr_codegen = false;
    public static boolean lightverif = false;
    public static boolean minisat = false;
    public static boolean slv_parallel = true;
    public static int verbosity = 6;
    public static int cores = 8;
    public static int sim_iters = 1500;
    public static int slv_seed = 1;
    public static int inline_bnd = 5;
    public static int unrolL_bnd = 4;
    public static int inbits_bnd = 2;
    public static int cbits_bnd = 1;
    public static int int_range = 35;
    public static int arr_sz_bnd = 50;

  }

  public static void init (CLIParser parser) {
    Global.log = parser.log();
    Global.debug = parser.debug();
    Global.use_output_logs = parser.use_output_logs();
    Global.run_frontend = !parser.skip_frontend();
    Global.generate_intermediate_files = parser.generate_intermediate_files();
  }
}