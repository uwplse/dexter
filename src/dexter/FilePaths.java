package dexter;

import java.io.File;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class FilePaths {
    private static String binDir;
    private static String cppFileDir;

    private static String benchmarkName;

    private static String cppFilePath;
    private static String dslFilePath;
    private static String outputFilePath;

    private static String vcFilePath;
    private static String analysisFilePath;
    private static String udfsFilePath;

    private static String clauseFilePath;
    private static String templateFilePath;
    private static String pointsFilePath;
    private static String exprsFilePath;

    private static String templateSketchFilePath;
    private static String pointsSketchFilePath;
    private static String exprSketchFilePath;

    private static String summaryFilePath;
    private static String z3FilePath;

    public static void init (CLIParser parser) {
        cppFilePath = parser.source().getAbsolutePath();
        dslFilePath = parser.dsl().getAbsolutePath();
        outputFilePath = parser.out().getAbsolutePath();

        File cppFile = new File(cppFilePath);

        benchmarkName = cppFile.getName().split("\\.")[0];

        cppFileDir = cppFile.getParent() + "/";
        binDir = cppFileDir + "/bin/";

        vcFilePath = cppFileDir + benchmarkName + "_stage_sid.prl";
        analysisFilePath = cppFileDir + benchmarkName + ".json";
        udfsFilePath = cppFileDir + benchmarkName + "_udfs.prl";

        clauseFilePath = binDir + benchmarkName + "_stage_sid_varname_summary.prl";
        templateFilePath = binDir + benchmarkName + "_stage_sid_varname_template.prl";
        pointsFilePath = binDir + benchmarkName + "_stage_sid_varname_points_pid.prl";
        exprsFilePath = binDir + benchmarkName + "_stage_sid_varname_expr.prl";

        templateSketchFilePath = binDir + benchmarkName + "_stage_sid_varname_template.sk";
        pointsSketchFilePath = binDir + benchmarkName + "_stage_sid_varname_points_pid.sk";
        exprSketchFilePath = binDir + benchmarkName + "_stage_sid_varname_expr.sk";

        summaryFilePath = binDir + benchmarkName + "_stage_sid_summary.prl";
        z3FilePath = binDir + benchmarkName + "_stage_sid.z3";
    }

    public static String benchmarkName () { return benchmarkName; }

    public static String stageSummary(int id) {
        return summaryFilePath.replace("sid", Integer.toString(id));
    }

    public static String cppFile() {
        return cppFilePath;
    }

    public static String dslFile() {
        return dslFilePath;
    }

    public static String analysisFile() {
        return analysisFilePath;
    }

    public static String binDir() {
        return binDir;
    }

    public static String clauseFile(int id, String var) {
        return clauseFilePath
                .replace("sid", Integer.toString(id))
                .replace("varname", var);
    }

    public static String vcFile(int id) {
        return vcFilePath.replace("sid", Integer.toString(id));
    }

    public static String udfsFile() {
        return udfsFilePath;
    }

    public static String templateFile(int id, String var) {
        return templateFilePath
                .replace("sid", Integer.toString(id))
                .replace("varname", var);
    }

    public static String templateSketchFile(int id, String var) {
        return templateSketchFilePath
                .replace("sid", Integer.toString(id))
                .replace("varname", var);
    }

    public static String pointsFile(int id, String var, int pid) {
        return pointsFilePath
                .replace("sid", Integer.toString(id))
                .replace("varname", var)
                .replace("pid", Integer.toString(pid));
    }

    public static String pointsSketchFile(int id, String var, int pid) {
        return pointsSketchFilePath
                .replace("sid", Integer.toString(id))
                .replace("varname", var)
                .replace("pid", Integer.toString(pid));
    }

    public static String exprsFile(int id, String var) {
        return exprsFilePath
                .replace("sid", Integer.toString(id))
                .replace("varname", var);
    }

    public static String exprSketchFile(int id, String var) {
        return exprSketchFilePath
                .replace("sid", Integer.toString(id))
                .replace("varname", var);
    }

    public static String z3File(int id) {
        return z3FilePath.replace("sid", Integer.toString(id));
    }
}