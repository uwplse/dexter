package dexter.synthesis;

import sketch.compiler.ast.core.FEReplacer;
import sketch.compiler.ast.core.stmts.StmtAssign;
import sketch.util.annot.CodeGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by akcheung on 7/4/17.
 *
 * To run this parser with sketch, compile and jar this class in metalift.jar, then
 * run sketch with
 *
 * sketch --fe-custom-codegen metalift.jar <sketch input>
 *
 * This will generate an output file metalift_skParse.txt that records the value
 * for each choice variable.
 */

@CodeGenerator
public class OutputParser extends FEReplacer
{
  protected static final Pattern choose = Pattern.compile("choose(\\d+)__\\w+ = (\\d+)");
  protected static final Pattern choose_alt = Pattern.compile("choose(\\d+)_(\\d+)__\\w+ = (\\d+)");
  protected static final Pattern consts = Pattern.compile("const(\\d+)__\\w+ = (\\d+)");

  // sketch custom codegen doesn't allow passing arguments to ctor
  public static String outFilename = "metalift_skParse.txt";

  protected Map<String, Integer> chooseValues;
  protected Map<Integer, Integer> constValues;

  public OutputParser()
  {
    this.chooseValues = new HashMap<>();
    this.constValues = new HashMap<>();
    try { new PrintWriter(outFilename).close(); } // clear the file
    catch (IOException e) { throw new RuntimeException(e); }
  }

  @Override
  public Object visitStmtAssign(StmtAssign stmt)
  {
    Matcher choose_m = choose.matcher(stmt.toString());
    Matcher chooseAlt_m = choose_alt.matcher(stmt.toString());
    Matcher consts_m = consts.matcher(stmt.toString());

    if (chooseAlt_m.find())
    {
      int chID = Integer.parseInt(chooseAlt_m.group(1));
      int ceID = Integer.parseInt(chooseAlt_m.group(2));
      int v = Integer.parseInt(chooseAlt_m.group(3));
      System.out.println("#: " + chID + "_" + ceID + " val: " + v);

      if (chooseValues.containsKey(chID + "_" + ceID))
        throw new RuntimeException("got two values for choose var: " + stmt);

      chooseValues.put(chID + "_" + ceID, v);
      try
      {
        FileWriter out = new FileWriter(outFilename, true);
        out.write(chID + "_" + ceID + ":" + v + "\n");
        out.close();
      }
      catch (IOException e) { throw new RuntimeException(e); }
    }
    else if (choose_m.find())
    {
      //System.out.println(stmt);
      int n = Integer.parseInt(choose_m.group(1));
      int v = Integer.parseInt(choose_m.group(2));
      System.out.println("#: " + n + " val: " + v);

      if (chooseValues.containsKey(n))
        throw new RuntimeException("got two values for choose var: " + stmt);

      chooseValues.put(n + "", v);
      try
      {
        FileWriter out = new FileWriter(outFilename, true);
        out.write(n + ":" + v + "\n");
        out.close();
      }
      catch (IOException e) { throw new RuntimeException(e); }
    }
    else if (consts_m.find())
    {
      //System.out.println(stmt);
      int n = Integer.parseInt(consts_m.group(1));
      int v = Integer.parseInt(consts_m.group(2));
      System.out.println("#: " + n + " val: " + v);

      if (constValues.containsKey(n))
        throw new RuntimeException("got two values for choose var: " + stmt);

      constValues.put(n, v);
      try
      {
        FileWriter out = new FileWriter(outFilename, true);
        out.write(n + ":" + v + "\n");
        out.close();
      }
      catch (IOException e) { throw new RuntimeException(e); }
    }

    return super.visitStmtAssign(stmt);
  }
}