/*
 * Util.cpp
 *
 *  Created on: Mar 5, 2018
 *      Author: maazsaf
 */

#include "ast/ASTNodeFactory.h"

const SourceLocation Dexter::ASTNodeFactory::nopos;
std::map<Stmt *, Stmt*> Dexter::ASTNodeFactory::ast;

void Dexter::ASTNodeFactory::SaveNormalizedNode(Stmt* o, Stmt* n)
{
  ast.insert(std::pair<Stmt*,Stmt*>(o, n));
}

Stmt* Dexter::ASTNodeFactory::GetNormalizedNode(Stmt* o)
{
  if (ast.count(o))
    return ast[o];
  else
    return o;
}

BinaryOperator* Dexter::ASTNodeFactory::BinaryOp(clang::Expr * lhs, clang::Expr * rhs, BinaryOperator::Opcode opc, ASTContext * ctx)
{
  if (opc >= BO_MulAssign && opc <= BO_OrAssign)
  {
    return new(*ctx)CompoundAssignOperator(lhs, rhs, opc, lhs->getType(), VK_RValue, OK_Ordinary, lhs->getType(), lhs->getType(), nopos, FPOptions());
  }

  QualType resultType =
    (BinaryOperator::isComparisonOp(opc) || BinaryOperator::isLogicalOp(opc)) ?
    ctx->getBOOLType() : lhs->getType();

  return new(*ctx)BinaryOperator(lhs, rhs, opc, resultType, VK_RValue, OK_Ordinary, nopos, FPOptions());
}

CompoundStmt* Dexter::ASTNodeFactory::CompoundStmts(Stmt * s1, Stmt * s2, ASTContext * ctx)
{
  std::vector<Stmt*> stmts;

  if (isa<CompoundStmt>(s1))
  {
    CompoundStmt::const_body_iterator it = static_cast<CompoundStmt*>(s1)->body_begin();
    while (it != static_cast<CompoundStmt*>(s1)->body_end())
    {
      stmts.push_back(*it);
      it++;
    }
  }
  else if (s1 != NULL)
  {
    stmts.push_back(s1);
  }

  if (isa<CompoundStmt>(s2))
  {
    CompoundStmt::const_body_iterator it = static_cast<CompoundStmt*>(s2)->body_begin();
    while (it != static_cast<CompoundStmt*>(s2)->body_end())
    {
      stmts.push_back(*it);
      it++;
    }
  }
  else if (s2 != NULL) {
    stmts.push_back(s2);
  }

  return CompoundStmt::Create(*ctx, llvm::makeArrayRef(stmts.data(), stmts.size()), nopos, nopos);
}

CXXBoolLiteralExpr* Dexter::ASTNodeFactory::BoolLit(bool val, ASTContext * ctx)
{
  return new(*ctx)CXXBoolLiteralExpr(true, ctx->getBOOLType(), nopos);
}

IntegerLiteral * Dexter::ASTNodeFactory::IntegerLit(int val, ASTContext * ctx)
{
  return IntegerLiteral::Create(*ctx, llvm::APInt(ctx->getIntWidth(ctx->IntTy), 0, true), ctx->IntTy, nopos);
}

WhileStmt* Dexter::ASTNodeFactory::WhileLoop(clang::Expr* cond, Stmt* body, ASTContext * ctx)
{
  return new(*ctx)WhileStmt(*ctx, NULL, cond, body, nopos);
}