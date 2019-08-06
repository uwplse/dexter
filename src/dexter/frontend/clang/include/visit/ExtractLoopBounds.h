/*
 * ExtractLoopBounds.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef EXTRACTLOOPBOUNDS_H_
#define EXTRACTLOOPBOUNDS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

using namespace clang;

namespace Dexter {

class ExtractLoopBounds : public RecursiveASTVisitor<ExtractLoopBounds>
{
public:
  ExtractLoopBounds(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl* f);
  bool VisitStmt(Stmt *s);

protected:
  void Extract (Stmt* s);

  bool forTemplate1 (ForStmt * s);
  bool forTemplate2 (ForStmt * s);
  bool forTemplate3 (ForStmt * s);
  bool forTemplate4 (ForStmt * s);

  bool whileTemplate1 (WhileStmt * s);

protected:
  Rewriter &rewriter;
  ASTContext *ctx;

  bool debug = false;
};

}

#endif /* EXTRACTLOOPBOUNDS_H_ */