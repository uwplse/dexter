/*
 * ExtractInVars.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "visit/ExtractInVars.h"

bool Dexter::ExtractInVars::VisitFunctionDecl (FunctionDecl* f)
{
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  if (Dexter::Preferences::Verbosity > 0) {
    if (Dexter::Preferences::Mode == 0)
      llvm::outs() << "Analyzing intentional code block `" << f->getNameAsString() << "`\n";
    else
      llvm::outs() << "Analyzing legacy code block `" << f->getNameAsString() << "`\n";
  }

  Pipeline* pipeline = Dexter::DeclExt::Get(f)->DAG();
  std::set<Stage*> stages = pipeline->getAllStages();

  // Run analysis on each stage
  std::set<Stage*>::iterator stage;
  for (stage = stages.begin(); stage != stages.end(); ++stage)
  {
    std::set<ValueDecl *> in_vars, local_vars;
    std::vector<Stmt*> stmts = (*stage)->getStatements();

    std::vector<Stmt*>::iterator stmt;
    for (stmt = stmts.begin(); stmt != stmts.end(); ++stmt)
      Extract(*stmt, in_vars, local_vars);

    (*stage)->setInputVars(in_vars);
    (*stage)->setLocalVars(local_vars);

    if (debug) {
      llvm::outs() << "STAGE " << (*stage)->getId() << "\n";
      for (std::set<ValueDecl *>::const_iterator it = in_vars.begin(); it != in_vars.end(); ++it)
        llvm::outs() << (*it)->getNameAsString() << "\n";
      llvm::outs() << "\n\n";
    }
  }

  return true;
}

bool Dexter::ExtractInVars::VisitStmt (Stmt* s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::ExtractInVars::Extract (Stmt *o, std::set<ValueDecl *> & in_vars, std::set<ValueDecl *> & local_vars)
{
  if (o == NULL) return;

  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  if (debug)
  {
    llvm::outs() << Util::print(s) << "\n";
    s->dump();
  }

  if (isa<DeclRefExpr>(s))
  {
    if (!cast<DeclRefExpr>(s)->getType()->isFunctionType())
      in_vars.insert(cast<DeclRefExpr>(s)->getDecl());
  }
  else if (isa<DeclStmt>(s))
  {
    DeclStmt::const_decl_iterator it;
    for (it = cast<DeclStmt>(s)->decl_begin(); it != cast<DeclStmt>(s)->decl_end(); ++it)
    {
      VarDecl * d = (VarDecl *)*it;
      local_vars.insert(d);
    }
  }

  Stmt::child_iterator it;
  for (it = s->child_begin(); it != s->child_end(); ++it)
    Extract(*it, in_vars, local_vars);
}