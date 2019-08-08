package dexter.synthesis;

import dexter.ir.Expr;
import dexter.ir.Transformer;
import dexter.ir.bool.BinaryExpr;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.IfExpr;
import dexter.ir.type.Type;
import dexter.ir.type.TypesFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by maazsaf on 11/14/17.
 *
 * Replace int_expr_grm and float_expr_grammar with synthesized expression
 */

public class ExprPatcher extends Transformer
{
  protected boolean debug = false;

  protected List<FuncDecl> fns;
  protected List<Expr> opts;

  protected int expr_idx;
  protected int expr_size;
  protected Map<String, List<Integer>> expr_steps;

  protected List<String> dexterKeywords = Arrays.asList("idxL", "idxR", "idxCL", "idxCR", "op", "bop", "opc", "expr_sz", "expr_idx");

  //protected int currentNum;
  protected int ceID;

  public ExprPatcher(List<FuncDecl> functions, List<Expr> opts) throws IOException
  {
    this.expr_idx = 0;
    this.expr_size = 0;
    this.expr_steps = new HashMap<>();

    this.fns = functions;
    this.opts = opts;

    BufferedReader in = new BufferedReader(new FileReader(ExprOutputParser.outFilename));

    String l;
    while ((l = in.readLine()) != null)
    {
      String [] s = l.split(":");

      if (dexterKeywords.contains(s[0]))
        if (s[0].equals("expr_sz"))
          expr_size = Integer.parseInt(s[1]);
        else if (s[0].equals("expr_idx"))
          expr_idx = Integer.parseInt(s[1]);
        else
          expr_steps.put(s[0], new ArrayList<>(Arrays.asList(s[1].substring(1,s[1].length()-1).split("\\s*,\\s*"))).stream().map(v_str -> Integer.parseInt(v_str)).collect(Collectors.toList()));
      else
        throw new RuntimeException("Keyword not recognized: " + s[0]);
    }

    in.close();
  }

  private Expr  do_op_float(int c, Expr v1, Expr v2)
  {
    if (c == 0)       return new BinaryExpr(v1, BinaryExpr.Op.PLUS, v2);
    else if (c == 1)  return new BinaryExpr(v1, BinaryExpr.Op.MINUS, v2);
    else if (c == 2)  return new BinaryExpr(v1, BinaryExpr.Op.MULT, v2);
    else              return new BinaryExpr(v1, BinaryExpr.Op.DIV, v2);
  }

  private Expr  do_op_int(int c, Expr v1, Expr v2)
  {
    if (c == 0)       return new BinaryExpr(v1, BinaryExpr.Op.LSHR, v2);
    else if (c == 1)  return new BinaryExpr(v1, BinaryExpr.Op.SHL, v2);
    else if (c == 2)  return new BinaryExpr(v1, BinaryExpr.Op.PLUS, v2);
    else if (c == 3)  return new BinaryExpr(v1, BinaryExpr.Op.MINUS, v2);
    else if (c == 4)  return new BinaryExpr(v1, BinaryExpr.Op.MULT, v2);
    else              return new BinaryExpr(v1, BinaryExpr.Op.DIV, v2);
  }

  private Expr  do_bop(int c, Expr v1, Expr v2)
  {
    if (c == 0)       return v1;
    else if (c == 1)  return new BinaryExpr(v1, BinaryExpr.Op.GT, v2);
    else if (c == 2)  return new BinaryExpr(v1, BinaryExpr.Op.EQ, v2);
    else              return new BinaryExpr(v1, BinaryExpr.Op.NEQ, v2);
  }

  private Expr patchExprGrammar(CallExpr e, Type exprT)
  {
    if (expr_idx < opts.size())
      return opts.get(expr_idx);
    else {
      // Re-trace the synthesis algorithm from the log
      int step = 0;

      while (step < expr_size) {
        int idxL = expr_steps.get("idxL").get(step);
        int idxR = expr_steps.get("idxR").get(step);
        int idxCL = expr_steps.get("idxCL").get(step);
        int idxCR = expr_steps.get("idxCR").get(step);
        int op = expr_steps.get("op").get(step);
        int bop = expr_steps.get("bop").get(step);
        int opc = expr_steps.get("opc").get(step);

        if (opc == 1)
          opts.add((exprT == TypesFactory.Int ? do_op_int(op, opts.get(idxL), opts.get(idxR)) : do_op_float(op, opts.get(idxL), opts.get(idxR))));
        else
          opts.add(new IfExpr(do_bop(bop, opts.get(idxCL), opts.get(idxCR)), opts.get(idxL), opts.get(idxR)));

        step++;
      }

      return opts.get(opts.size()-1);
    }
  }

  @Override
  public Expr transform (CallExpr e)
  {
    // Special handling for grammar
    if (e.name().equals("int_expr_grm"))
      return patchExprGrammar(e, TypesFactory.Int);
    else if (e.name().equals("float_expr_grm"))
      return patchExprGrammar(e, TypesFactory.Float);
    else
      return null;
  }
}