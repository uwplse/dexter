/*
 * ExtractOutVars.h
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#ifndef EXTRACTOUTVARS_H_
#define EXTRACTOUTVARS_H_

#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ast/ASTNodeFactory.h"
#include "ast/DeclExt.h"
#include "ast/StmtExt.h"
#include "Util.h"

#include <set>

using namespace clang;

namespace Dexter {

class ExtractOutVars : public RecursiveASTVisitor<ExtractOutVars>
{
public:
  ExtractOutVars(Rewriter &r) : rewriter(r) {}

  bool VisitFunctionDecl(FunctionDecl *f);

protected:
  void Extract(Stmt *o);
  void Extract(clang::Expr* e);
  void ExtractArray(clang::Expr* e);

public:

  class LexicalScope
  {
  public:
  	void outputVar(ValueDecl * v) { output_vars.insert(v); }
    void outputArrVar(ValueDecl * v) { output_arr_vars.insert(v); }
    void outputVars(std::set<ValueDecl *> vars) { this->output_vars.insert(vars.begin(), vars.end()); }
    void outputArrVars(std::set<ValueDecl *> vars) { this->output_arr_vars.insert(vars.begin(), vars.end()); }
    std::set<ValueDecl *> & outVars() { return output_vars; }
    std::set<ValueDecl *> & outArrVars() { return output_arr_vars; }

  protected:
    std::set<ValueDecl *> output_vars;
    std::set<ValueDecl *> output_arr_vars;
  };

protected:
  Rewriter &rewriter;

  std::stack<LexicalScope *> scopes;

  bool debug = false;
};

}

#endif /* EXTRACTOUTVARS_H_ */