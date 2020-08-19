package dexter.synthesis;

import dexter.ir.Expr;
import dexter.ir.Printer;
import dexter.ir.Substitutor;
import dexter.ir.Transformer;
import dexter.ir.bool.*;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.parser.Util;
import dexter.ir.semantics.TypeChecker;

import java.io.*;
import java.util.*;

/**
 * Created by akcheung on 7/5/17.
 *
 * Replace choose expressions based on values stored in the input file
 */

public class Patcher extends Transformer
{
  protected boolean debug = false;
  protected Map<String, Integer> vals;
  protected List<FuncDecl> fns;

  //protected int currentNum;
  protected Stack<Integer> ceID;

  public static void main (String[] args) throws IOException {
    FileInputStream fis = new FileInputStream(new File(args[0]));
    Program p = (Program) Util.parse(fis, true);
    fis.close();
    p = (Program) p.accept(new Patcher(p.functions()));
    p.accept(new TypeChecker());
    System.out.println(p.accept(new Printer()));
  }

  public Patcher(List<FuncDecl> functions) throws IOException
  {
    this.vals = new HashMap<>();
    this.fns = functions;

    //this.currentNum = 0;
    this.ceID = new Stack<>();
    this.ceID.push(-1);

    BufferedReader in = new BufferedReader(new FileReader(OutputParser.outFilename));

    String l;
    while ((l = in.readLine()) != null)
    {
      String [] s = l.split(":");
      vals.put(s[0], Integer.parseInt(s[1]));
    }

    if (debug) System.out.println(vals);

    in.close();
  }

  @Override
  public Expr transform (ChooseExpr e)
  {
    int chID = e.getId();

    if (debug)
      System.err.println(e.accept(new Printer()));

    if (vals.containsKey(chID + "")) {
      if (debug)
        System.out.println(e.args().get(vals.get(chID + "")).accept(new Printer()));

      if (vals.get(chID + "") < e.args().size())
        return e.args().get(vals.get(chID + "")).accept(this);
      else
        return e.args().get(e.args().size()-1).accept(this);
    }
    else if (vals.containsKey(chID + "_" + ceID.peek())) {
      if (debug)
        System.out.println(e.args().get(vals.get(chID + "_" + ceID.peek())).accept(new Printer()));

      if (vals.get(chID + "_" + ceID.peek()) < e.args().size())
        return e.args().get(vals.get(chID + "_" + ceID.peek())).accept(this);
      else
        return e.args().get(e.args().size()-1).accept(this);
    }
    else {
      if (debug)
        System.out.println("VALS NOT FOUND: " + chID + ", " + chID + "_" + ceID.peek());

      return null;
    }
  }

  @Override
  public Expr transform (CallExpr e)
  {
    for (FuncDecl fnDecl : this.fns)
      if (fnDecl.name().matches(e.name()))
        if (!fnDecl.isGenerator())
          return null;

    if (debug) System.err.println(">>> " + e.accept(new Printer()) + "   :   " + e.getId());

    List<Expr> args = new ArrayList<>();
    for (Expr arg : e.args())
      args.add(arg.accept(this));

    for (FuncDecl fn : this.fns) {
      if (fn.name().equals(e.name()))
      {
        this.ceID.push(e.getId());
        Expr rVal = fn.body().accept(this);
        this.ceID.pop();
        for (int i=0; i<fn.params().size(); i++)
          rVal = rVal.accept(new Substitutor(fn.params().get(i), args.get(i)));
        return rVal;
      }
    }

    return null;
  }

  /*@Override
  public Expr transform (ChooseExpr e)
  {
    int base = e.getId();
    int offset = 0;

    if (debug) System.err.println(e.accept(new Printer()));

    while (offset < e.args().size()-1)
    {
      if(debug)
        System.err.println("["+(base + offset) + " : " + vals.get(base + offset)+"] " + e.args().get(offset).accept(new Printer()));

      if (vals.get(base + offset) == 1) {
        Expr arg = e.args().get(offset);
        return arg.accept(this);
      }

      offset++;
    }

    if(debug)
      System.err.println("["+(base + offset) + " : " + vals.get(base + offset)+"] " + e.args().get(offset).accept(new Printer()));

    return e.args().get(offset).accept(this);
  }*/

  @Override
  public Expr transform (ChooseIntExpr e)
  {
    Integer id = e.getId();
    return new IntLitExpr(vals.get(id + ""));
  }

}