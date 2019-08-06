/*
 * GenerateDAG.cpp
 *
 *  Created on: Apr 29, 2017
 *      Author: maazsaf
 */

#include "visit/GenerateDAG.h"

bool Dexter::GenerateDAG::VisitFunctionDecl (FunctionDecl *f)
{
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  Dexter::Stage::NextId = 0;
  Dexter::Pipeline * pipeline = new Dexter::Pipeline();
  AssignStmt(f->getBody(), pipeline);

  if (debug)
    pipeline->print();

  pipeline->simplify();

  if (debug)
    pipeline->print();

  ext->DAG(pipeline);

  llvm::outs() << "Intentional code block `" << f->getNameAsString() << "` converted to a DAG with " << pipeline->getAllStages().size() << " stages.\n";

  return true;
}

bool Dexter::GenerateDAG::VisitStmt (Stmt *s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::GenerateDAG::AssignStmt (Stmt * o, Dexter::Pipeline * pipeline)
{
  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  if (debug) {
    llvm::outs() << Dexter::Util::print(s) << "\n";
  }

  if (isa<CompoundStmt>(s))
  {
    CompoundStmt * cs = cast<CompoundStmt>(s);
    CompoundStmt::const_body_iterator it;
    for (it = cs->body_begin(); it != cs->body_end(); ++it)
    {
      AssignStmt(*it, pipeline);
    }
  }
  else if (isa<WhileStmt>(s))
  {
    pipeline->getCurrStage()->addStatement(s);
    pipeline->getCurrStage()->containsLoop(true);
    pipeline->createNewStage();
  }
  else if (isa<IfStmt>(s))
  {
    IfStmt * is = cast<IfStmt>(s);

    assert (is->getInit() == NULL);

    Dexter::Pipeline * cons = new Dexter::Pipeline();
    AssignStmt(is->getThen(), cons);

    Dexter::Pipeline * altr = new Dexter::Pipeline();
    AssignStmt(is->getElse(), altr);

    pipeline->getCurrStage()->addStatement(is->getCond());
    pipeline->createFork(is->getCond(), cons, altr);
  }
  else if (isa<clang::CallExpr>(s))
  {
    pipeline->getCurrStage()->addStatement(s);
  }
  else{
    pipeline->getCurrStage()->addStatement(s);
  }
}