package dexter;

import dexter.grammar.Grammar;
import dexter.ir.bool.Program;
import dexter.ir.parser.Util;

import java.io.*;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Files {
  // Inputs to the compiler
  private static String cppFrontendDir;

  private static String inputFilePath;
  private static String outputFilePath;

  private static String coreDslFilePath;
  private static String userDslFilePath;

  // Directories to save intermediate files
  private static String tmpDir;
  private static String binDir;

  private static String analysisFilePath;
  private static String vcFilePath;
  private static String udfsFilePath;

  private static String clauseFilePath;
  private static String roiFilePath;
  private static String pointsFilePath;
  private static String exprsFilePath;

  private static String roiSketchFilePath;
  private static String pointsSketchFilePath;
  private static String exprSketchFilePath;

  private static String summaryFilePath;
  private static String z3FilePath;

  public static void init (CLIParser parser) {
    // Inputs to compiler
    cppFrontendDir = parser.frontend().getAbsolutePath();

    inputFilePath = parser.source().getAbsolutePath();
    outputFilePath = parser.out().getAbsolutePath();

    coreDslFilePath = cppFrontendDir + "/DSL.ir";
    userDslFilePath = (parser.dsl() == null ? "" : parser.dsl().getAbsolutePath());

    // Directories to save intermediate files
    tmpDir = ParentDir(inputFilePath) + "/" + FileName(inputFilePath) + "_dx_tmp/";
    binDir = tmpDir + "%s_stage_%d_bin/";

    analysisFilePath = tmpDir + "%s.json";
    vcFilePath = tmpDir + "%s_stage_%d.prl";
    udfsFilePath = tmpDir + "%s_stage_%d_udfs.prl";

    clauseFilePath = binDir + "%s_stage_%d_%s_clause.prl";
    roiFilePath = binDir + "%s_stage_%d_%s_roi.prl";
//    pointsFilePath = binDir + benchmarkName + "_stage_sid_varname_points_pid.prl";
//    exprsFilePath = binDir + benchmarkName + "_stage_sid_varname_expr.prl";

    roiSketchFilePath = binDir + "%s_stage_%d_%s_roi.sk";
//    pointsSketchFilePath = binDir + benchmarkName + "_stage_sid_varname_points_pid.sk";
//    exprSketchFilePath = binDir + benchmarkName + "_stage_sid_varname_expr.sk";

    summaryFilePath = binDir + "summary.prl";
//    z3FilePath = binDir + benchmarkName + "_stage_sid.z3";
  }

  // Path getters
  public static String cppFrontendDirPath() { return cppFrontendDir; }
  public static String tmpDirPath() { return tmpDir; }
  public static String binDirPath(String bn, int sid) { return String.format(binDir, bn, sid); }

  public static String cppFilePath() { return inputFilePath; }
  public static String coreDslFilePath() { return coreDslFilePath; }
  public static String userDslFilePath() { return userDslFilePath; }

  public static String analysisFilePath(String bn) { return String.format(analysisFilePath, bn); }
  public static String vcFilePath(String bn, int sid) { return String.format(vcFilePath, bn, sid); }
  public static String udfsFilePath(String bn, int sid) { return String.format(udfsFilePath, bn, sid); }

  public static String summaryFilePath(String bn, int sid) { return String.format(summaryFilePath, bn, sid); }
  public static String clauseFilePath(String bn, int sid, String var) { return String.format(clauseFilePath, bn, sid, bn, sid, var); }
  public static String roiFilePath(String bn, int sid, String var) { return String.format(roiFilePath, bn, sid, bn, sid, var); }

  public static String roiSketchFilePath(String bn, int sid, String var) { return String.format(roiSketchFilePath, bn, sid, bn, sid, var); }

  // File getters
  public static File cppFrontendDir() { return new File(cppFrontendDirPath()); }
  public static File tempDir() { return new File(tmpDirPath()); }
  public static File binDir(String bn, int sid) {
    return new File(binDirPath(bn, sid));
  }

  public static File cppFile() { return new File(cppFilePath()); }
  public static File coreDslFile() { return new File(coreDslFilePath()); }
  public static File userDslFile() { return new File(userDslFilePath()); }

  public static File analysisFile(String bn) { return new File(analysisFilePath(bn)); }
  public static File vcFile(String bn, int sid) { return new File(vcFilePath(bn, sid)); }
  public static File udfsFile(String bn, int sid) { return new File(udfsFilePath(bn, sid)); }

  public static File summaryFile(String bn, int sid) { return new File(summaryFilePath(bn, sid)); }
  public static File clauseFile(String bm, int sid, String var) { return new File(clauseFilePath(bm, sid, var)); }
  public static File roiFile(String bm, int sid, String var) { return new File(roiFilePath(bm, sid, var)); }

  public static File roiSketchFile(String bm, int sid, String var) { return new File(roiSketchFilePath(bm, sid, var)); }


  // Old
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

  public static String grammarFilePath (Grammar.Name name) {
    switch (name) {
      case ROIGrammar:
        return "src/dexter/grammar/ROIGrammar.prl";
      default:
        throw new RuntimeException("Grammar not found.");
    }
  }

  public static File grammarFile (Grammar.Name name) {
    return new File(grammarFilePath(name));
  }

  public static String ParentDir(String path) { return new File(path).getParent(); }

  public static String FileName(String path) { return new File(path).getName().split("\\.")[0]; }

  public static Program loadIRFile(File file) throws IOException {
    if (!file.exists())
      return null;

    FileInputStream fis = new FileInputStream(file);
    Program p = (Program) Util.parse(fis, false);
    fis.close();
    return p;
  }

  public static void writeFile(String filepath, String content) throws IOException {
    PrintWriter out = new PrintWriter(filepath);
    out.write(content);
    out.close();
  }
}