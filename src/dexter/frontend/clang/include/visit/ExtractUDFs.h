/*
 * ExtractUDFs.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef EXTRACTUDFS_H_
#define EXTRACTUDFS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "ir/bool/FuncDecl.h"
#include "ClangToIRParser.h"
#include "Util.h"

#include <set>

using namespace clang;

namespace Dexter {

class ExtractUDFs : public RecursiveASTVisitor<ExtractUDFs>
{
public:
  ExtractUDFs(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl* f);
  bool VisitStmt(Stmt* s);

protected:
  void Extract(Stmt* o);
  void GenerateUDF (FunctionDecl * fnDecl);
  void GenerateUDF (clang::Expr * container, CXXMethodDecl * mDecl);

protected:
  Rewriter &rewriter;

  std::set<FuncDecl*> udfs;

  bool debug = false;
};

}

#endif /* EXTRACTUDFS_H_ */