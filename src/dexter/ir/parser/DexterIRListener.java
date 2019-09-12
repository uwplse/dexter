// Generated from DexterIR.g4 by ANTLR 4.7.2

package dexter.ir.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DexterIRParser}.
 */
public interface DexterIRListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(DexterIRParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(DexterIRParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#uFnDecl}.
	 * @param ctx the parse tree
	 */
	void enterUFnDecl(DexterIRParser.UFnDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#uFnDecl}.
	 * @param ctx the parse tree
	 */
	void exitUFnDecl(DexterIRParser.UFnDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#fnDecl}.
	 * @param ctx the parse tree
	 */
	void enterFnDecl(DexterIRParser.FnDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#fnDecl}.
	 * @param ctx the parse tree
	 */
	void exitFnDecl(DexterIRParser.FnDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void enterClassDecl(DexterIRParser.ClassDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void exitClassDecl(DexterIRParser.ClassDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(DexterIRParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(DexterIRParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterClassType(DexterIRParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitClassType(DexterIRParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(DexterIRParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(DexterIRParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pointerType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterPointerType(DexterIRParser.PointerTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pointerType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitPointerType(DexterIRParser.PointerTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bufferType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterBufferType(DexterIRParser.BufferTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bufferType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitBufferType(DexterIRParser.BufferTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tupleType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterTupleType(DexterIRParser.TupleTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tupleType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitTupleType(DexterIRParser.TupleTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterListType(DexterIRParser.ListTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitListType(DexterIRParser.ListTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fnType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterFnType(DexterIRParser.FnTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fnType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitFnType(DexterIRParser.FnTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitvectorType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterBitvectorType(DexterIRParser.BitvectorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitvectorType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitBitvectorType(DexterIRParser.BitvectorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code baseType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(DexterIRParser.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code baseType}
	 * labeled alternative in {@link DexterIRParser#type}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(DexterIRParser.BaseTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#forIter}.
	 * @param ctx the parse tree
	 */
	void enterForIter(DexterIRParser.ForIterContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#forIter}.
	 * @param ctx the parse tree
	 */
	void exitForIter(DexterIRParser.ForIterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code initMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInitMacro(DexterIRParser.InitMacroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code initMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInitMacro(DexterIRParser.InitMacroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNullExpr(DexterIRParser.NullExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNullExpr(DexterIRParser.NullExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varsMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVarsMacro(DexterIRParser.VarsMacroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varsMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVarsMacro(DexterIRParser.VarsMacroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scopeMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterScopeMacro(DexterIRParser.ScopeMacroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scopeMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitScopeMacro(DexterIRParser.ScopeMacroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(DexterIRParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(DexterIRParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code letExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLetExpr(DexterIRParser.LetExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code letExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLetExpr(DexterIRParser.LetExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ptrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPtrExpr(DexterIRParser.PtrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ptrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPtrExpr(DexterIRParser.PtrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(DexterIRParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(DexterIRParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boundsMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoundsMacro(DexterIRParser.BoundsMacroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boundsMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoundsMacro(DexterIRParser.BoundsMacroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lambdaExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpr(DexterIRParser.LambdaExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lambdaExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpr(DexterIRParser.LambdaExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forEachMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterForEachMacro(DexterIRParser.ForEachMacroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forEachMacro}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitForEachMacro(DexterIRParser.ForEachMacroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIfExpr(DexterIRParser.IfExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIfExpr(DexterIRParser.IfExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cIntLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCIntLitExpr(DexterIRParser.CIntLitExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cIntLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCIntLitExpr(DexterIRParser.CIntLitExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(DexterIRParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(DexterIRParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forallExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterForallExpr(DexterIRParser.ForallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forallExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitForallExpr(DexterIRParser.ForallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntLitExpr(DexterIRParser.IntLitExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntLitExpr(DexterIRParser.IntLitExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(DexterIRParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(DexterIRParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code incrPtrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIncrPtrExpr(DexterIRParser.IncrPtrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code incrPtrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIncrPtrExpr(DexterIRParser.IncrPtrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyListExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEmptyListExpr(DexterIRParser.EmptyListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyListExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEmptyListExpr(DexterIRParser.EmptyListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decrPtrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDecrPtrExpr(DexterIRParser.DecrPtrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decrPtrExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDecrPtrExpr(DexterIRParser.DecrPtrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code floatLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFloatLitExpr(DexterIRParser.FloatLitExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code floatLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFloatLitExpr(DexterIRParser.FloatLitExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code uninterpExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUninterpExpr(DexterIRParser.UninterpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code uninterpExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUninterpExpr(DexterIRParser.UninterpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fieldExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFieldExpr(DexterIRParser.FieldExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fieldExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFieldExpr(DexterIRParser.FieldExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code implyExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterImplyExpr(DexterIRParser.ImplyExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code implyExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitImplyExpr(DexterIRParser.ImplyExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listCompExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterListCompExpr(DexterIRParser.ListCompExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listCompExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitListCompExpr(DexterIRParser.ListCompExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVarExpr(DexterIRParser.VarExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVarExpr(DexterIRParser.VarExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolLitExpr(DexterIRParser.BoolLitExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolLitExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolLitExpr(DexterIRParser.BoolLitExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code chooseExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterChooseExpr(DexterIRParser.ChooseExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code chooseExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitChooseExpr(DexterIRParser.ChooseExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tupleExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTupleExpr(DexterIRParser.TupleExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tupleExpr}
	 * labeled alternative in {@link DexterIRParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTupleExpr(DexterIRParser.TupleExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#forr}.
	 * @param ctx the parse tree
	 */
	void enterForr(DexterIRParser.ForrContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#forr}.
	 * @param ctx the parse tree
	 */
	void exitForr(DexterIRParser.ForrContext ctx);
	/**
	 * Enter a parse tree produced by {@link DexterIRParser#inr}.
	 * @param ctx the parse tree
	 */
	void enterInr(DexterIRParser.InrContext ctx);
	/**
	 * Exit a parse tree produced by {@link DexterIRParser#inr}.
	 * @param ctx the parse tree
	 */
	void exitInr(DexterIRParser.InrContext ctx);
}