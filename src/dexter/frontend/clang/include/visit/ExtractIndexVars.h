/*
 * ExtractIndexVars.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef EXTRACTINDEXVARS_H_
#define EXTRACTINDEXVARS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

#include <set>

using namespace clang;

namespace Dexter {

class ExtractIndexVars : public RecursiveASTVisitor<ExtractIndexVars>
{
public:
  ExtractIndexVars(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl* f);
  bool VisitStmt(Stmt* s);

protected:
  void Extract (Stmt *o, std::set<ValueDecl *> & idx, bool ignore);

protected:
  Rewriter &rewriter;

  bool debug = false;
};

}

#endif /* EXTRACTINDEXVARS_H_ */