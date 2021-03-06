package dexter.grammar;

import dexter.Files;
import dexter.frontend.CodeAnalysis;
import dexter.ir.Expr;
import dexter.ir.bool.CallExpr;
import dexter.ir.bool.FuncDecl;
import dexter.ir.bool.Program;
import dexter.ir.bool.VarExpr;
import dexter.ir.codegen.MacroExpander;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.tuple.TupleExpr;
import dexter.ir.type.*;
import dexter.modifier.StencilExprSetter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dexter.grammar.Grammar.Name.ROIGrammar;
import static dexter.grammar.Grammar.Name.TermGrammar;

/**
 * Created by Maaz Ahmad on 8/7/19.
 */
public class Grammar {
  public enum Name {
    ROIGrammar,
    TermGrammar
  }

  public static Program loadROIGrammar (Program p, CodeAnalysis analysis) throws IOException {
    // Read Grammar file
    Program grammar = Files.loadGrammarFile(ROIGrammar);

    // Stitch Grammar file
    p.stitch(grammar);

    // Expand Macros
    p = (Program) p.accept(new MacroExpander(analysis, true));

    return p;
  }

  public static Program loadTermGrammar (Program p, CodeAnalysis analysis, Type bufT, Type termT) throws IOException {
    // Read Grammar file
    Program grammar = Files.loadGrammarFile(TermGrammar);

    // Stitch Grammar file
    stitchPointsGrammar(p, grammar, bufT, termT);

    // Expand Macros
    p = (Program) p.accept(new MacroExpander(analysis, true));

    return p;
  }

  public static Program loadExprGrammar(Program p, CodeAnalysis analysis, List<Expr> opts, Type bufT) throws IOException {
    // Stitch stage 3 grammar for synthesizing expressions
    stitchExprGrammar(p, opts, bufT);

    // Expand Macros
    p = (Program) p.accept(new MacroExpander(analysis, true));

    return p;
  }

  private static void stitchPointsGrammar(Program p, Program grammar, Type bufT, Type termT) {
    Type genT;
    if (bufT instanceof CollectionT)
      genT = ((CollectionT) bufT).elemT();
    else
      throw new RuntimeException("Unexpected outVar type: " + bufT);

    List<FuncDecl> fns = new ArrayList<>();

    // Add generator functions from this grammar
    fns.addAll(grammar.functions());

    // Update stencil expressions with calls to the generator function
    for (FuncDecl fn : p.functions()) {
      if (fn.name().matches("buf_asn_1d(i|f)")) {
        Expr stencilExpr = generatePointGrammarCall(termT, 1);
        fns.add((FuncDecl) fn.accept(new StencilExprSetter(p.functions(), stencilExpr)));
      }
      else if (fn.name().matches("buf_asn_2d(i|f)|buf_asn_row_2d(i|f)")) {
        Expr stencilExpr = generatePointGrammarCall(termT, 2);
        fns.add((FuncDecl) fn.accept(new StencilExprSetter(p.functions(), stencilExpr)));
      }
      //else if (fn.name().equals("pc") || fn.name().matches("inv[0-9]+"))
        //fns.add((FuncDecl) fn.accept(new StencilExprSetter(p.functions(), stencilExpr)));
      else if (fn.name().equals("int_expr") || fn.name().equals("float_expr")) {
        VarExpr param = new VarExpr("val", termT);
        fn.params(Arrays.asList(param));
        if (genT == termT) {
          // faster
          fn.body(param);
          fn.isUnInterpreted(false);
        } else {
          fn.body(null);
          fn.isUnInterpreted(true);
        }
        fns.add(fn);
      } else if (!fns.contains(fn))
        fns.add(fn);
    }

    p.functions(fns);
  }

  private static CallExpr generatePointGrammarCall(Type t, int dim) {
    Expr idx_x = new VarExpr("idx_x", TypesFactory.Int);
    Expr idx_y = new VarExpr("idx_y", TypesFactory.Int);
    Expr idx_c = new VarExpr("idx_c", TypesFactory.Int);

    switch (dim) {
      case 1:
        if (t == TypesFactory.Int) {
          Expr termGenCall = new CallExpr("int_terms_1d", Arrays.asList(idx_x));
          return new CallExpr("int_expr", Arrays.asList(termGenCall));
        }
        else if (t == TypesFactory.Float) {
          Expr termGenCall = new CallExpr("float_terms_1d", Arrays.asList(idx_x));
          return new CallExpr("float_expr", Arrays.asList(termGenCall));
        }
        else
          throw new RuntimeException("Unexpected term type: " + t);

      case 2:
        if (t == TypesFactory.Int) {
          Expr termGenCall = new CallExpr("int_terms_2d", Arrays.asList(idx_x, idx_y));
          return new CallExpr("int_expr", Arrays.asList(termGenCall));
        }
        else if (t == TypesFactory.Float) {
          Expr termGenCall = new CallExpr("float_terms_2d", Arrays.asList(idx_x, idx_y));
          return new CallExpr("float_expr", Arrays.asList(termGenCall));
        }
        else
          throw new RuntimeException("Unexpected term type: " + t);

      case 3:
        if (t == TypesFactory.Int) {
          Expr termGenCall = new CallExpr("int_terms_3d", Arrays.asList(idx_x, idx_y, idx_c));
          return new CallExpr("int_expr", Arrays.asList(termGenCall));
        }
        else if (t == TypesFactory.Float) {
          Expr termGenCall = new CallExpr("float_terms_3d", Arrays.asList(idx_x, idx_y, idx_c));
          return new CallExpr("float_expr", Arrays.asList(termGenCall));
        }
        else
          throw new RuntimeException("Unexpected term type: " + t);

      default:
        throw new RuntimeException("NYI");
    }
  }

  private static void stitchExprGrammar(Program p, List<Expr> opts, Type outVarT) {
    // Prepare input array to grammar (including any heuristics)
    CallExpr genCall = generateExprGrammarCall(opts, outVarT);

    List<FuncDecl> fns = new ArrayList<>();

    // Add generator functions from this grammar
    VarExpr N = new VarExpr("N", TypesFactory.Int);
    VarExpr terminals = new VarExpr("terminals", genCall.args().get(1).type());

    FuncDecl intGen = new FuncDecl("int_expr_grm", Arrays.asList(N, terminals), TypesFactory.Int, null, false);
    FuncDecl floatGen = new FuncDecl("float_expr_grm", Arrays.asList(N, terminals), TypesFactory.Float, null, false);

    fns.add(intGen);
    fns.add(floatGen);

    // Convert ui_exprs in PCs to calls to the generator function
    for (FuncDecl fn : p.functions())
      if (fn.name().equals("int_expr") || fn.name().equals("float_expr"))
      {
        fn.body(genCall);
        fn.isUnInterpreted(false);
        fns.add(fn);
      }
      else if (!fns.contains(fn))
        fns.add(fn);

    p.functions(fns);
  }

  private static CallExpr generateExprGrammarCall(List<Expr> opts, Type bufT) {
    Type elemT ;
    if (bufT instanceof CollectionT)
      elemT = ((CollectionT) bufT).elemT();
    else
      throw new RuntimeException("NYI");

    Expr N = new IntLitExpr(opts.size(), false);
    Expr terminals = new TupleExpr(opts);
    terminals.type(TypesFactory.tupleT(opts.get(0).type(), opts.size()));

    List<Expr> args = Arrays.asList(N, terminals);

    if (elemT == TypesFactory.Int)
      return new CallExpr("int_expr_grm", args);
    else if (elemT == TypesFactory.Float)
      return new CallExpr("float_expr_grm", args);
    else
      throw new RuntimeException("Unexpected outVar elem type: " + bufT);
  }
}