/*
 * ExtractConstants.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef EXTRACTCONSTANTS_H_
#define EXTRACTCONSTANTS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

#include <set>

using namespace clang;

namespace Dexter {

class ExtractConstants : public RecursiveASTVisitor<ExtractConstants>
{
public:
  ExtractConstants(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl* f);
  bool VisitStmt(Stmt* s);

protected:
  void Extract(Stmt* o);

protected:
  Rewriter &rewriter;

  std::set<int> int_consts;
  std::set<double> float_consts;

  bool debug = false;
};

}

#endif /* EXTRACTCONSTANTS_H_ */