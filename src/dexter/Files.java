package dexter;

import dexter.grammar.Grammar;
import dexter.ir.bool.Program;
import dexter.ir.parser.Util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

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

  private static String origFuncFilePath;
  private static String analysisFilePath;
  private static String vcFilePath;
  private static String udfsFilePath;

  private static String clauseFilePath;
  private static String roiFilePath;
  private static String termFilePath;
  private static String exprFilePath;

  private static String roiSketchFilePath;
  private static String termSketchFilePath;
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

    origFuncFilePath = tmpDir + "%s.cpp";
    analysisFilePath = tmpDir + "%s.json";
    vcFilePath = tmpDir + "%s_stage_%d.ir";
    udfsFilePath = tmpDir + "%s_stage_%d_udfs.ir";

    clauseFilePath = binDir + "%s_clause.ir";
    roiFilePath = binDir + "%s_roi.ir";
    termFilePath = binDir + "%s_term_%d.ir";
    exprFilePath = binDir + "%s_expr.ir";

    roiSketchFilePath = binDir + "%s_roi.sk";
    termSketchFilePath = binDir + "%s_term_%d.sk";
    exprSketchFilePath = binDir + "%s_expr.sk";

    summaryFilePath = binDir + "summary.ir";
//    z3FilePath = binDir + benchmarkName + "_stage_sid.z3";
  }

  // Path getters
  public static String cppFrontendDirPath() { return cppFrontendDir; }
  public static String tmpDirPath() { return tmpDir; }
  public static String binDirPath(String bn, int sid) { return String.format(binDir, bn, sid); }

  public static String cppFilePath() { return inputFilePath; }
  public static String coreDslFilePath() { return coreDslFilePath; }
  public static String userDslFilePath() { return userDslFilePath; }

  public static String origFuncFilePath(String bn) { return String.format(origFuncFilePath, bn); }
  public static String analysisFilePath(String bn) { return String.format(analysisFilePath, bn); }
  public static String vcFilePath(String bn, int sid) { return String.format(vcFilePath, bn, sid); }
  public static String udfsFilePath(String bn, int sid) { return String.format(udfsFilePath, bn, sid); }

  public static String summaryFilePath(String bn, int sid) { return String.format(summaryFilePath, bn, sid); }
  public static String clauseFilePath(String bn, int sid, String var) { return String.format(clauseFilePath, bn, sid, var); }
  public static String roiFilePath(String bn, int sid, String var) { return String.format(roiFilePath, bn, sid, var); }
  public static String termFilePath(String bn, int sid, String var, int tid) { return String.format(termFilePath, bn, sid, var, tid); }
  public static String exprFilePath(String bn, int sid, String var) { return String.format(exprFilePath, bn, sid, var); }

  public static String roiSketchFilePath(String bn, int sid, String var) { return String.format(roiSketchFilePath, bn, sid, var); }
  public static String termSketchFilePath(String bn, int sid, String var, int tid) { return String.format(termSketchFilePath, bn, sid, var, tid); }
  public static String exprSketchFilePath(String bn, int sid, String var) { return String.format(exprSketchFilePath, bn, sid, var); }

  public static String outputFilePath () { return outputFilePath; }

  // File getters
  public static File cppFrontendDir() { return new File(cppFrontendDirPath()); }
  public static File tempDir() { return new File(tmpDirPath()); }
  public static File binDir(String bn, int sid) {
    return new File(binDirPath(bn, sid));
  }

  public static File cppFile() { return new File(cppFilePath()); }
  public static File coreDslFile() { return new File(coreDslFilePath()); }
  public static File userDslFile() { return new File(userDslFilePath()); }

  public static File origFuncFile(String bn) { return new File(origFuncFilePath(bn)); }
  public static File analysisFile(String bn) { return new File(analysisFilePath(bn)); }
  public static File vcFile(String bn, int sid) { return new File(vcFilePath(bn, sid)); }
  public static File udfsFile(String bn, int sid) { return new File(udfsFilePath(bn, sid)); }

  public static File summaryFile(String bn, int sid) { return new File(summaryFilePath(bn, sid)); }
  public static File clauseFile(String bm, int sid, String var) { return new File(clauseFilePath(bm, sid, var)); }
  public static File roiFile(String bm, int sid, String var) { return new File(roiFilePath(bm, sid, var)); }
  public static File termFile(String bm, int sid, String var, int tid) { return new File(termFilePath(bm, sid, var, tid)); }
  public static File exprFile(String bm, int sid, String var) { return new File(exprFilePath(bm, sid, var)); }

  public static File roiSketchFile(String bm, int sid, String var) { return new File(roiSketchFilePath(bm, sid, var)); }
  public static File termSketchFile(String bm, int sid, String var, int tid) { return new File(termSketchFilePath(bm, sid, var, tid)); }
  public static File exprSketchFile(String bn, int sid, String var) { return new File(exprSketchFilePath(bn, sid, var)); }

  public static File outputFile () { return new File(outputFilePath()); }

  // Misc
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

  public static Program loadGrammarFile(Grammar.Name grammar) throws IOException {
    InputStream in;
    switch (grammar) {
      case ROIGrammar:
        in = Grammar.class.getResourceAsStream("ROIGrammar.ir");
        break;
      case TermGrammar:
        in = Grammar.class.getResourceAsStream("TermGrammar.ir");
        break;
      default:
        throw new RuntimeException("Grammar not found.");
    }
    Program p = (Program) Util.parse(convert(in, Charset.defaultCharset()), false);
    return p;
  }

  public static String convert(InputStream inputStream, Charset charset) throws IOException {

    try (Scanner scanner = new Scanner(inputStream, charset.name())) {
      return scanner.useDelimiter("\\A").next();
    }
  }

  public static void writeFile(String filepath, String content) throws IOException {
    PrintWriter out = new PrintWriter(filepath);
    out.write(content);
    out.close();
  }

  // Old
  public static String z3File(int id) {
    return z3FilePath.replace("sid", Integer.toString(id));
  }
}