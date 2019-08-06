package dexter.ir;

import dexter.ir.array.*;
import dexter.ir.bool.*;
import dexter.ir.integer.FloatLitExpr;
import dexter.ir.integer.ForallExpr;
import dexter.ir.integer.IntLitExpr;
import dexter.ir.integer.UIExpr;
import dexter.ir.list.*;
import dexter.ir.macro.*;
import dexter.ir.tuple.TupleExpr;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public class Substitutor extends Transformer
{
  protected Map<Expr,Expr> replaceSet;

  public Substitutor (Expr from, Expr to)
  {
    this.replaceSet = new HashMap<>();
    this.replaceSet.put(from, to);
  }

  public Substitutor(Map<Expr, Expr> replaceSet)
  {
    this.replaceSet = replaceSet;
  }

  protected Expr visitDefault (Expr n)
  {
    if (replaceSet.containsKey(n))
      return replaceSet.get(n);
    else
      return null;
  }

  /* boolean */
  @Override public Expr transform (VarExpr e) { return visitDefault(e); }
  @Override public Expr transform (BoolLitExpr e) { return visitDefault(e); }
  @Override public Expr transform (NullExpr e) { return visitDefault(e); }

  @Override public Expr transform (PtrExpr e) { return visitDefault(e); }
  @Override public Expr transform (IncrPtrExpr e) { return visitDefault(e); }
  @Override public Expr transform (DecrPtrExpr e) { return visitDefault(e); }

  @Override public Expr transform (CallExpr e) { return visitDefault(e); }
  @Override public Expr transform (FieldExpr e) { return visitDefault(e); }

  @Override public Expr transform (BinaryExpr e) { return visitDefault(e); }
  @Override public Expr transform (UnaryExpr e) { return visitDefault(e); }
  @Override public Expr transform (IfExpr e) { return visitDefault(e); }

  @Override public Expr transform (ChooseIntExpr e) { return visitDefault(e); }
  @Override public Expr transform (ChooseExpr e) { return visitDefault(e); }
  @Override public Expr transform (ForallExpr e) { return visitDefault(e); }
  @Override public Expr transform (LetExpr e) { return visitDefault(e); }
  @Override public Expr transform (FuncDecl e) { return visitDefault(e); }
  @Override public Expr transform (ClassDecl e) { return visitDefault(e); }
  @Override public Expr transform (Program e) { return visitDefault(e); }

  /* integer */
  @Override public Expr transform (IntLitExpr e) { return visitDefault(e); }
  @Override public Expr transform (FloatLitExpr e) { return visitDefault(e); }
  @Override public Expr transform (UIExpr e) { return visitDefault(e); }

  /* list */
  @Override public Expr transform (EqualsExpr e) { return visitDefault(e); }
  @Override public Expr transform (EmptyListExpr e) { return visitDefault(e); }
  @Override public Expr transform (GetExpr e) { return visitDefault(e); }
  @Override public Expr transform (LengthExpr e) { return visitDefault(e); }
  @Override public Expr transform (ListCompExpr e) { return visitDefault(e); }
  @Override public Expr transform (ConcatExpr e) { return visitDefault(e); }
  @Override public Expr transform (SliceExpr e) { return visitDefault(e); }
  @Override public Expr transform (AppendExpr e) { return visitDefault(e); }

  /* array */
  @Override public Expr transform (SelectExpr e) { return visitDefault(e); }
  @Override public Expr transform (StoreExpr e) { return visitDefault(e); }

  /* macros */
  @Override public Expr transform (ForEach e) { return visitDefault(e); }
  @Override public Expr transform (Variables e) { return visitDefault(e); }
  @Override public Expr transform (Bound e) { return visitDefault(e); }
  @Override public Expr transform (Init e) { return visitDefault(e); }
  @Override public Expr transform (Scope e) { return visitDefault(e); }

  /* tuple */
  @Override public Expr transform (TupleExpr e) { return visitDefault(e); }
}