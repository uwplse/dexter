package dexter.ir;

import dexter.ir.array.*;
import dexter.ir.bool.*;
import dexter.ir.integer.*;
import dexter.ir.list.*;
import dexter.ir.macro.*;
import dexter.ir.tuple.*;

/**
 * Created by Maaz Ahmad on 6/25/19.
 */
public interface Visitor<T>
{
  /* boolean */
  T visit(VarExpr e);
  T visit(BoolLitExpr e);
  T visit(NullExpr e);

  T visit(PtrExpr e);
  T visit(IncrPtrExpr e);
  T visit(DecrPtrExpr e);

  T visit(CallExpr e);
  T visit(FieldExpr e);

  T visit(BinaryExpr e);
  T visit(UnaryExpr e);
  T visit(IfExpr e);

  T visit(ChooseIntExpr e);
  T visit(ChooseExpr e);
  T visit(ForallExpr e);
  T visit(LetExpr e);
  T visit(FuncDecl e);
  T visit(ClassDecl e);
  T visit(Program e);

  /* integer */
  T visit(IntLitExpr e);
  T visit(FloatLitExpr e);
  T visit(UIExpr e);

  /* list */
  T visit(EqualsExpr e);
  T visit(EmptyListExpr e);
  T visit(GetExpr e);
  T visit(LengthExpr e);
  T visit(ListCompExpr e);
  T visit(ConcatExpr e);
  T visit(SliceExpr e);
  T visit(AppendExpr e);

  /* array */
  T visit(SelectExpr e);
  T visit(StoreExpr e);

  /* macros */
  T visit(ForEach e);
  T visit(Variables e);
  T visit(Bound e);
  T visit(Init e);
  T visit(Scope e);

  /* tuple */
  T visit(TupleExpr e);
}