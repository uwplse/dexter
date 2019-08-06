/*
 * GenerateDAG.h
 *
 *  Created on: Apr 29, 2018
 *      Author: maazsaf
 */

#ifndef GENERATEDAG_H_
#define GENERATEDAG_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "dag/Pipeline.h"
#include "Util.h"

using namespace clang;

namespace Dexter {

class GenerateDAG : public RecursiveASTVisitor<GenerateDAG>
{
public:
  GenerateDAG(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl *f);
  bool VisitStmt(Stmt *s);

protected:
  void AssignStmt (Stmt *o, Dexter::Pipeline *pipeline);

protected:
  Rewriter &rewriter;

  bool debug = false;
};

}

#endif /* GENERATEDAG_H_ */