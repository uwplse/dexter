package dexter.ir.parser;

import dexter.ir.Expr;
import dexter.ir.Printer;
import dexter.ir.semantics.TypeChecker;
import dexter.ir.semantics.VarChecker;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Util
{
  private static boolean debug = false;

  public static Expr parse (FileInputStream fis, boolean checkSemantics) throws IOException
  {
    CharStream input = CharStreams.fromStream(fis);
    return parse(input, checkSemantics);
  }

  public static Expr parse (String p, boolean checkSemantics)
  {
    CharStream input = CharStreams.fromString(p);
    return parse(input, checkSemantics);
  }

  private static Expr parse (CharStream input, boolean checkSemantics)
  {
    DexterIRLexer lexer = new DexterIRLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    DexterIRParser parser = new DexterIRParser(tokens);

    parser.addErrorListener(new BaseErrorListener()
      {
        public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line,
                                int charPositionInLine, String msg, RecognitionException e)
        { throw new RuntimeException(e); }
      });
    ParseTree tree = parser.program();

    // show tree in text form
    if (debug) System.out.println("parse tree:" + tree.toStringTree(parser));

    Expr t = new BuildASTVisitor().visit(tree);

    if (t == null)
      throw new RuntimeException("Error in parsing");
    else if (debug)
      System.out.println("AST: " + t.accept(new Printer()));

    if (checkSemantics)
    {
      t.accept(new VarChecker(true));

      t.accept(new TypeChecker());
    }

    return t;
  }
}