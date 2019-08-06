/*
 * VarScopeAnalysis.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "visit/VarScopeAnalysis.h"

bool Dexter::VarScopeAnalysis::VisitFunctionDecl (FunctionDecl *f)
{
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  llvm::outs() << "Analyzing intentional code block `" << f->getNameAsString() << "`\n";

  std::set<ValueDecl *> scope;
  this->liveVars = std::stack<std::set<ValueDecl *>>();
  this->liveVars.push(scope);

  this->callees.clear();

  FunctionDecl::param_const_iterator pit;
  for (pit = f->param_begin(); pit != f->param_end(); ++pit)
  {
    if ((*pit)->getNameAsString() != ""){
      this->liveVars.top().insert(*pit);
    }
  }

  FindVarsInScope (f->getBody());

  // TODO: Fix this
  // Also run analysis for all functions called from this function
  std::set<FunctionDecl *>::iterator it;
  for (it = this->callees.begin(); it != this->callees.end(); ++it)
  {
    std::set<ValueDecl *> scope;
    this->liveVars = std::stack<std::set<ValueDecl *>>();
    this->liveVars.push(scope);

    FunctionDecl::param_const_iterator pit;
    for (pit = (*it)->param_begin(); pit != (*it)->param_end(); ++pit)
    {
      this->liveVars.top().insert(*pit);
    }

    FindVarsInScope((*it)->getBody());
  }

  return true;
}

bool Dexter::VarScopeAnalysis::VisitStmt (Stmt *s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::VarScopeAnalysis::FindVarsInScope (Stmt *o)
{
  if (o == NULL)
    return;

  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (this->processed.count(o) > 0)
    return;
  else
    this->processed.insert(o);

  if (isa<DeclStmt>(s))
  {
    DeclStmt * ds = cast<DeclStmt>(s);
    
    if (debug)
      llvm::outs() << "found decl: " << Util::print(ds);

    DeclStmt::const_decl_iterator it;
    for (it = ds->decl_begin(); it != ds->decl_end(); ++it)
    {
      this->liveVars.top().insert((ValueDecl*)*it);
    }
  }
  else if (isa<DeclRefExpr>(s))
  {
    DeclRefExpr * dre = cast<DeclRefExpr>(s);
    this->liveVars.top().insert(dre->getDecl());
  }
  else if (isa<CompoundStmt>(s))
  {
    this->liveVars.push(this->liveVars.top());

    CompoundStmt * cs = cast<CompoundStmt>(s);
    CompoundStmt::const_body_iterator it;
    for (it = cs->body_begin(); it != cs->body_end(); ++it)
      FindVarsInScope(*it);

    ext->liveVars(this->liveVars.top());

    this->liveVars.pop();
  }
  else if (isa<WhileStmt>(s))
  {
    // Must call explicitly due to loop normalization
    WhileStmt * ws = cast<WhileStmt>(s);
    FindVarsInScope(ws->getBody());

    ext->liveVars(this->liveVars.top());
  }
  else if (isa<clang::CallExpr>(s))
  {
    FunctionDecl * fnDecl = cast<clang::CallExpr>(s)->getDirectCallee();
    if (fnDecl)
      this->callees.insert(fnDecl);
    else
      Util::error(s, "Function declaration not found: ");
  }
  else
  {
    ext->liveVars(this->liveVars.top());
  }
}