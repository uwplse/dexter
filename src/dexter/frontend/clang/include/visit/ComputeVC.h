/*
 * ComputeVC.h
 *
 *  Created on: Oct 13, 2017
 *      Author: maazsaf
 */

#ifndef COMPUTEVC_H_
#define COMPUTEVC_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "ir/array/StoreExpr.h"
#include "Util.h"
#include "ClangToIRParser.h"

using namespace clang;

namespace Dexter {

class ComputeVC : public RecursiveASTVisitor<ComputeVC>
{
public:
  ComputeVC(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl* f);
  bool VisitStmt(Stmt* s);

protected:
  void vc (UnaryOperator * s, Dexter::VCGenerator * vcGen);
  void vc (clang::CallExpr * s, Dexter::VCGenerator * vcGen);
  void vc (ConditionalOperator * s, Dexter::VCGenerator * vcGen);
  void vc (IfStmt * s, Dexter::VCGenerator * vcGen);
  void vc (WhileStmt * s, Dexter::VCGenerator * vcGen);
  void vc (CompoundStmt * s, Dexter::VCGenerator * vcGen);
  void vc (DeclStmt * s, Dexter::VCGenerator * vcGen);
  void vc (Stmt * s, Dexter::VCGenerator * vcGen);
  void vc (clang::Expr * from, Dexter::Expr * to, Dexter::VCGenerator * vcGen, bool replace = true);

  Dexter::Expr * vc (BinaryOperator * s, Dexter::VCGenerator * vcGen);

  clang::Expr * getArray (clang::Expr * e);

  DeclRefExpr * makeDeclRefExpr(const VarDecl *D, bool RefersToEnclosingVariableOrCapture);

protected:
  Rewriter &rewriter;
  ASTContext *ctx;

  std::map<NamedDecl *, Dexter::Expr *> vars;

  std::vector<Dexter::VarExpr *> irOutVars;
  std::vector<Dexter::VarExpr *> irInVars;

  bool debug = false;
};

}

#endif /* COMPUTEVC_H_ */