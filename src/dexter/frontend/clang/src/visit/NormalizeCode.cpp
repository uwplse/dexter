/*
 * NormalizeCode.cpp
 *
 *  Created on: Apr 29, 2017
 *      Author: maazsaf
 */

#include "visit/NormalizeCode.h"

bool Dexter::NormalizeCode::VisitFunctionDecl (FunctionDecl *f)
{
  ctx = &(f->getASTContext());

  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  NormalizeLoops (f->getBody());

  return true;
}

bool Dexter::NormalizeCode::VisitStmt (Stmt *s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::NormalizeCode::NormalizeLoops (Stmt *s)
{
  if (s == NULL)
    return;

  else if (isa<ForStmt>(s))
  {
    ForStmt *loop = cast<ForStmt>(s);

    int lno = rewriter.getSourceMgr().getSpellingLineNumber(loop->getSourceRange().getBegin());
    llvm::outs() << "Normalizing for-loop at line " << lno << "\n";

    if (this->debug)
      llvm::outs() << Util::print(loop) << "\n";

    clang::Expr* wcond = (loop->getCond() == NULL ? Dexter::ASTNodeFactory::BoolLit(true, ctx) : loop->getCond());
    Stmt* wbody = Dexter::ASTNodeFactory::CompoundStmts(loop->getBody(), loop->getInc(), ctx);
    WhileStmt* wloop = Dexter::ASTNodeFactory::WhileLoop(wcond, wbody, ctx);

    Stmt* nl = (loop->getInit() == NULL ? wloop : (Stmt*) Dexter::ASTNodeFactory::CompoundStmts(loop->getInit(), (Stmt*)wloop, ctx));

    if (this->debug)
      llvm::outs() << Util::print(nl) << "\n";

    Dexter::StmtExt* ol_ext = Dexter::StmtExt::Get(loop);
    Dexter::StmtExt* nl_ext = Dexter::StmtExt::Get(wloop);

    nl_ext->upperBound(ol_ext->upperBound());
    nl_ext->lowerBound(ol_ext->lowerBound());

    Dexter::ASTNodeFactory::SaveNormalizedNode(s, nl);
    // Dexter::ASTNodeFactory::SaveOrigNode(wloop, s);
  }

  Stmt::child_iterator it;
  for (it = s->child_begin(); it != s->child_end(); ++it)
    NormalizeLoops(*it);
}