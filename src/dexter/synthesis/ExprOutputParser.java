package dexter.synthesis;

import sketch.compiler.ast.core.FEReplacer;
import sketch.compiler.ast.core.stmts.StmtAssign;
import sketch.util.annot.CodeGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by maazsaf on 11/14/18.
 *
 * To run this parser with sketch, compile and jar this class in metalift.jar, then
 * run sketch with
 *
 * sketch --fe-custom-codegen metalift.jar <sketch input>
 *
 * This will generate an output file metalift_skParse.txt that records the value
 * for each choice variable.
 */

public class ExprOutputParser extends FEReplacer
{
  // Dexter specific outputs
  protected static final Pattern expr_idx = Pattern.compile("expr_idx__\\w+ = (\\d+)");
  protected static final Pattern expr_sz = Pattern.compile("expr_sz__\\w+ = (\\d+)");
  protected static final Pattern steps = Pattern.compile("(idxL|idxR|idxCL|idxCR|op|bop|opc)__\\w+ = \\{(.*?)\\}");

  // sketch custom codegen doesn't allow passing arguments to ctor
  public static String outFilename = "metalift_skParse.txt";

  public ExprOutputParser()
  {
    try { new PrintWriter(outFilename).close(); } // clear the file
    catch (IOException e) { throw new RuntimeException(e); }
  }

  @Override
  public Object visitStmtAssign(StmtAssign stmt)
  {
    Matcher expr_idx_m = expr_idx.matcher(stmt.toString());
    Matcher expr_sz_m = expr_sz.matcher(stmt.toString());
    Matcher steps_m = steps.matcher(stmt.toString());

    if (expr_idx_m.find())
    {
      //System.out.println(stmt);
      int v = Integer.parseInt(expr_idx_m.group(1));
      System.out.println("expr_idx: " + v);

      try
      {
        FileWriter out = new FileWriter(outFilename, true);
        out.write("expr_idx:" + v + "\n");
        out.close();
      }
      catch (IOException e) { throw new RuntimeException(e); }
    }
    else if (expr_sz_m.find())
    {
      //System.out.println(stmt);
      int v = Integer.parseInt(expr_sz_m.group(1));
      System.out.println("expr_sz: " + v);

      try
      {
        FileWriter out = new FileWriter(outFilename, true);
        out.write("expr_sz:" + v + "\n");
        out.close();
      }
      catch (IOException e) { throw new RuntimeException(e); }
    }
    else if (steps_m.find())
    {
      //System.out.println(stmt);
      String n = steps_m.group(1);
      List<Integer> v = new ArrayList<>(Arrays.asList(steps_m.group(2).split("\\s*,\\s*"))).stream().map(v_str -> Integer.parseInt(v_str)).collect(Collectors.toList());
      System.out.println(n + ": " + v);

      try
      {
        FileWriter out = new FileWriter(outFilename, true);
        out.write(n +":" + v + "\n");
        out.close();
      }
      catch (IOException e) { throw new RuntimeException(e); }
    }

    return super.visitStmtAssign(stmt);
  }
}