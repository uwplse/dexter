/*
 * ExtractIndexVars.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "visit/ExtractIndexVars.h"

bool Dexter::ExtractIndexVars::VisitFunctionDecl (FunctionDecl *f)
{
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  llvm::outs() << "Analyzing intentional code block `" << f->getNameAsString() << "`\n";

  Pipeline* pipeline = Dexter::DeclExt::Get(f)->DAG();
  std::set<Stage*> stages = pipeline->getAllStages();

  // Run analysis on each stage
  std::set<Stage*>::iterator stage;
  for (stage = stages.begin(); stage != stages.end(); ++stage)
  {
    std::set<ValueDecl *> idx_vars;
    std::vector<Stmt*> stmts = (*stage)->getStatements();

    std::vector<Stmt*>::iterator stmt;
    for (stmt = stmts.begin(); stmt != stmts.end(); ++stmt)
      Extract(*stmt, idx_vars, true);

    (*stage)->setIndexVars(idx_vars);
  }

  return true;
}

bool Dexter::ExtractIndexVars::VisitStmt (Stmt* s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::ExtractIndexVars::Extract (Stmt *o, std::set<ValueDecl *> & idx_vars, bool ignore)
{
  if (o == NULL) return;

  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  if (debug)
  {
    llvm::outs() << Util::print(s) << "\n";
    s->dump();
  }

  if (isa<ArraySubscriptExpr>(s))
  {
    Extract(cast<ArraySubscriptExpr>(s)->getBase(), idx_vars, ignore);
    Extract(cast<ArraySubscriptExpr>(s)->getIdx(), idx_vars, false);
  }
  else if (isa<DeclRefExpr>(s))
  {
    if (!ignore)
      idx_vars.insert(cast<DeclRefExpr>(s)->getDecl());
  }
  else
  {
    Stmt::child_iterator it;
    for (it = s->child_begin(); it != s->child_end(); ++it)
      Extract(*it, idx_vars, ignore);
  }
}