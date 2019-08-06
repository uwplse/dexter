/*
 * ASTNodeFactory.h
 *
 *  Created on: Mar 5, 2018
 *      Author: maazsaf
 */

#ifndef ASTNODEFACTORY_H_
#define ASTNODEFACTORY_H_

#include "clang/AST/AST.h"

using namespace clang;

namespace Dexter {

class ASTNodeFactory
{
public:
  static void SaveNormalizedNode(Stmt* o, Stmt* n);
  static Stmt * GetNormalizedNode(Stmt* o);

  static BinaryOperator * BinaryOp(clang::Expr * lhs, clang::Expr * rhs, BinaryOperator::Opcode opc, ASTContext * ctx);
  static CompoundStmt * CompoundStmts(Stmt * s1, Stmt * s2, ASTContext * ctx);
  static CXXBoolLiteralExpr * BoolLit(bool val, ASTContext * ctx);
  static IntegerLiteral * IntegerLit(int val, ASTContext * ctx);
  static WhileStmt * WhileLoop(clang::Expr * cond, Stmt * body, ASTContext * ctx);

protected:
  static const SourceLocation nopos;
  static std::map<Stmt*,Stmt*>  ast;
};

}

#endif /* ASTNODEFACTORY_H_ */