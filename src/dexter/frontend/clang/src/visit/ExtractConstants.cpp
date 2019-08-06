/*
 * ExtractConstants.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "visit/ExtractConstants.h"

bool Dexter::ExtractConstants::VisitFunctionDecl (FunctionDecl* f)
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
    int_consts.clear();
    float_consts.clear();

    std::vector<Stmt*> stmts = (*stage)->getStatements();
    std::vector<Stmt*>::iterator stmt;
    for (stmt = stmts.begin(); stmt != stmts.end(); ++stmt)
      Extract(*stmt);

    (*stage)->setIntConsts(int_consts);
    (*stage)->setFloatConsts(float_consts);
  }

  return true;
}

bool Dexter::ExtractConstants::VisitStmt (Stmt* s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::ExtractConstants::Extract (Stmt * o)
{
  if (o == NULL) return;

  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  if (debug)
  {
    llvm::outs() << Util::print(s) << "\n";
    s->dump();
  }

  if (isa<IntegerLiteral>(s))
  {
    int_consts.insert(cast<IntegerLiteral>(s)->getValue().getLimitedValue());
  }
  else if (isa<FloatingLiteral>(s))
  {
    float_consts.insert(cast<FloatingLiteral>(s)->getValueAsApproximateDouble());
  }
  else
  {
    Stmt::child_iterator it;
    for (it = s->child_begin(); it != s->child_end(); ++it)
      Extract(*it);
  }
}