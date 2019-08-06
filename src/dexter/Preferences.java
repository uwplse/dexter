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
        public static boolean log = false;
        public static boolean debug = false;
        public static boolean use_output_logs = false;
        public static boolean skip_frontend = false;
        public static boolean generate_intermediate_files = false;
    }

    public static void init (CLIParser parser) {
        Global.log = parser.log();
        Global.debug = parser.debug();
        Global.use_output_logs = parser.use_output_logs();
        Global.run_frontend = !parser.skip_frontend();
        Global.generate_intermediate_files = parser.generate_intermediate_files();
    }
}