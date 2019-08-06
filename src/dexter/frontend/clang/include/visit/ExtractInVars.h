/*
 * ExtractInVars.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef EXTRACTINVARS_H_
#define EXTRACTINVARS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

#include <set>

using namespace clang;

namespace Dexter {

class ExtractInVars : public RecursiveASTVisitor<ExtractInVars>
{
public:
  ExtractInVars(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl *f);
  bool VisitStmt(Stmt* s);

protected:
  void Extract(Stmt *o, std::set<ValueDecl *> & in_vars, std::set<ValueDecl *> & local_vars);

protected:
  Rewriter &rewriter;

  bool debug = false;
};

}

#endif /* EXTRACTINVARS_H_ */