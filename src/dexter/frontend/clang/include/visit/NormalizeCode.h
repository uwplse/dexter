/*
 * NormalizeCode.h
 *
 *  Created on: Apr 29, 2018
 *      Author: maazsaf
 */

#ifndef NORMALIZECODE_H_
#define NORMALIZECODE_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

using namespace clang;

namespace Dexter {

class NormalizeCode : public RecursiveASTVisitor<NormalizeCode>
{
public:
  NormalizeCode(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl *f);
  bool VisitStmt(Stmt *s);

protected:
  void NormalizeLoops (Stmt *s);

protected:
  Rewriter &rewriter;
  ASTContext *ctx;

  bool debug = false;
};

}

#endif /* NORMALIZECODE_H_ */