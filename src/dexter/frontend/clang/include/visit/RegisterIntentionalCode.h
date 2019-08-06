/*
 * RegisterIntentionalCode.h
 *
 *  Created on: Apr 29, 2018
 *      Author: maazsaf
 */

#ifndef REGISTERINTENTIONALCODE_H_
#define REGISTERINTENTIONALCODE_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

#include <map>
#include <regex>

using namespace clang;

namespace Dexter {

class RegisterIntentionalCode : public RecursiveASTVisitor<RegisterIntentionalCode>
{
public:
  RegisterIntentionalCode (Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl *f);

  bool VisitDecl(Decl *d);
  bool VisitStmt(Stmt *s);

protected:
  Rewriter &rewriter;

  std::map<std::string, FunctionDecl*> fnDecls;

  bool debug = false;
};

}

#endif /* REGISTERINTENTIONALCODE_H_ */