/*
 * VarScopeAnalysis.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef VARSCOPEANALYSIS_H_
#define VARSCOPEANALYSIS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

#include <set>

using namespace clang;

namespace Dexter {

class VarScopeAnalysis : public RecursiveASTVisitor<VarScopeAnalysis>
{
public:
  VarScopeAnalysis(Rewriter &r) : rewriter(r) {}

  bool VisitStmt(Stmt *s);
  bool VisitFunctionDecl(FunctionDecl *f);

protected:
  void FindVarsInScope(Stmt* o);

protected:
  Rewriter &rewriter;
  std::stack<std::set<ValueDecl *>> liveVars;
  std::set<FunctionDecl *> callees;
  std::set<Stmt *> processed;

  bool debug = false;
};

}

#endif /* VARSCOPEANALYSIS_H_ */