// Generated from DexterIR.g4 by ANTLR 4.7.2

package dexter.ir.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DexterIRParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DexterIRVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(DexterIRParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#uFnDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUFnDecl(DexterIRParser.UFnDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#fnDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFnDecl(DexterIRParser.FnDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#genDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenDecl(DexterIRParser.GenDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDecl(DexterIRParser.ClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(DexterIRParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassType(DexterIRParser.ClassTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(DexterIRParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pointerType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerType(DexterIRParser.PointerTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bufferType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBufferType(DexterIRParser.BufferTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tupleType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(DexterIRParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListType(DexterIRParser.ListTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fnType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFnType(DexterIRParser.FnTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitvectorType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitvectorType(DexterIRParser.BitvectorTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code baseType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseType(DexterIRParser.BaseTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#forIter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForIter(DexterIRParser.ForIterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code initMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitMacro(DexterIRParser.InitMacroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExpr(DexterIRParser.NullExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varsMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarsMacro(DexterIRParser.VarsMacroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scopeMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScopeMacro(DexterIRParser.ScopeMacroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(DexterIRParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code letExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExpr(DexterIRParser.LetExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ptrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtrExpr(DexterIRParser.PtrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(DexterIRParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boundsMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundsMacro(DexterIRParser.BoundsMacroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lambdaExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaExpr(DexterIRParser.LambdaExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forEachMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForEachMacro(DexterIRParser.ForEachMacroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpr(DexterIRParser.IfExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cIntLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCIntLitExpr(DexterIRParser.CIntLitExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExpr(DexterIRParser.CallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forallExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForallExpr(DexterIRParser.ForallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLitExpr(DexterIRParser.IntLitExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr(DexterIRParser.BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code incrPtrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncrPtrExpr(DexterIRParser.IncrPtrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyListExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyListExpr(DexterIRParser.EmptyListExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decrPtrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecrPtrExpr(DexterIRParser.DecrPtrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code floatLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatLitExpr(DexterIRParser.FloatLitExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code uninterpExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUninterpExpr(DexterIRParser.UninterpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fieldExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldExpr(DexterIRParser.FieldExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code implyExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplyExpr(DexterIRParser.ImplyExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listCompExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListCompExpr(DexterIRParser.ListCompExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarExpr(DexterIRParser.VarExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLitExpr(DexterIRParser.BoolLitExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code chooseExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChooseExpr(DexterIRParser.ChooseExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tupleExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleExpr(DexterIRParser.TupleExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#forr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForr(DexterIRParser.ForrContext ctx);
	/**
	 * Visit a parse tree produced by {@link DexterIRParser#inr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInr(DexterIRParser.InrContext ctx);
}