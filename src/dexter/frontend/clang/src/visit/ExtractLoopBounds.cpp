/*
 * ExtractLoopBounds.cpp
 *
 *  Created on: Apr 29, 2017
 *      Author: maazsaf
 */

#include "visit/ExtractLoopBounds.h"

bool Dexter::ExtractLoopBounds::VisitFunctionDecl (FunctionDecl *f)
{
  ctx = &(f->getASTContext());

  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  Extract (f->getBody());

  return true;
}

bool Dexter::ExtractLoopBounds::VisitStmt (Stmt *s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::ExtractLoopBounds::Extract (Stmt *s)
{
  if (s == NULL)
    return;

  if (this->debug) {
    printf ("Statement found: %s\n", s->getStmtClassName());
  }

  if (isa<ForStmt>(s))
  {
    ForStmt * loop = cast<ForStmt>(s);

    if (this->debug)
      llvm::outs() << Util::print(loop) << "\n";

    if (forTemplate1(loop) || forTemplate2(loop) || forTemplate3(loop) || forTemplate4(loop))
    {
      int lno = rewriter.getSourceMgr().getSpellingLineNumber(loop->getSourceRange().getBegin());
      if (Dexter::Preferences::Verbosity > 0)
        llvm::outs() << "Extracting bounds for for-loop at line " << lno << "\n";
    }
    else
      Util::error(loop, "For-Loop does not match any bound extraction templates: ");
  }
  else if (isa<WhileStmt>(s))
  {
    WhileStmt * loop = cast<WhileStmt>(s);

    if (this->debug)
      llvm::outs() << Util::print(loop) << "\n";

    if (whileTemplate1(loop))
    {
      int lno = rewriter.getSourceMgr().getSpellingLineNumber(loop->getSourceRange().getBegin());
      llvm::outs() << "Extracting bounds for while-loop at line " << lno << ".\n";
    }
    else
      Util::error(loop, "While-Loop does not match any bound extraction templates: ");
  }

  Stmt::child_iterator it;
  for (it = s->child_begin(); it != s->child_end(); ++it)
    Extract(*it);
}

// for (i=n; i<m; i++) or for(int i=n; i<m; i++)
bool Dexter::ExtractLoopBounds::forTemplate1 (ForStmt * loop)
{
  clang::Stmt* init = loop->getInit();
  clang::Expr* cond = loop->getCond();

  StmtExt* ext = Dexter::StmtExt::Get(loop);

  if (init == NULL || cond == NULL) return false;

  if (
    isa<BinaryOperator>(init) &&
    static_cast<BinaryOperator*>(init)->isAssignmentOp() &&
    isa<DeclRefExpr>(static_cast<BinaryOperator*>(init)->getLHS()) &&
    static_cast<BinaryOperator*>(init)->getLHS()->getType()->isIntegerType()
  )
  {
    ext->lowerBound(static_cast<BinaryOperator*>(init)->getRHS());
  }

  else if (
    isa<DeclStmt>(init) &&
    static_cast<DeclStmt*>(init)->isSingleDecl() &&
    isa<VarDecl>(static_cast<DeclStmt*>(init)->getSingleDecl()) &&
    static_cast<VarDecl*>(static_cast<DeclStmt*>(init)->getSingleDecl())->hasInit() &&
    static_cast<VarDecl*>(static_cast<DeclStmt*>(init)->getSingleDecl())->getInit()->getType()->isIntegerType()
  )
  {
    ext->lowerBound(static_cast<VarDecl*>(static_cast<DeclStmt*>(init)->getSingleDecl())->getInit());
  }

  else
    return false;

  if (
    isa<BinaryOperator>(cond) &&
    static_cast<BinaryOperator*>(cond)->getOpcode() == BO_LT
  )
  {
    BinaryOperator * bop = static_cast<BinaryOperator*>(cond);
    clang::Expr * lhs = bop->getLHS();
    clang::Expr * rhs = bop->getRHS();

    while (isa<CastExpr>(lhs))
      lhs = static_cast<CastExpr*>(lhs)->getSubExpr();

    while (isa<CastExpr>(rhs))
      rhs = static_cast<CastExpr*>(rhs)->getSubExpr();

    if (isa<DeclRefExpr>(lhs))
      ext->upperBound(static_cast<CastExpr*>(static_cast<BinaryOperator*>(cond)->getRHS()));
    else
      return false;
  }
  else
    return false;

  return true;
}

// for (; i<m; i++)
bool Dexter::ExtractLoopBounds::forTemplate2 (ForStmt * loop)
{
  clang::Stmt* init = loop->getInit();
  clang::Expr* cond = loop->getCond();

  StmtExt* ext = Dexter::StmtExt::Get(loop);

  if (init != NULL || cond == NULL) return false;

  if (
    isa<BinaryOperator>(cond) &&
    cast<BinaryOperator>(cond)->getOpcode() == BO_LT
  )
  {
    BinaryOperator * bop = cast<BinaryOperator>(cond);
    clang::Expr * lhs = bop->getLHS();
    clang::Expr * rhs = bop->getRHS();

    while (isa<CastExpr>(lhs))
      lhs = cast<CastExpr>(lhs)->getSubExpr();

    while (isa<CastExpr>(rhs))
      rhs = cast<CastExpr>(rhs)->getSubExpr();

    if (isa<DeclRefExpr>(lhs))
      ext->upperBound(cast<CastExpr>(cast<BinaryOperator>(cond)->getRHS())->getSubExpr());
    else
      return false;
  }
  else
    return false;

  ext->lowerBound(Dexter::ASTNodeFactory::IntegerLit(0, ctx));

  return true;
}

// for (endPtr = srcPtr + cols; srcPtr < endPtr; srcPtr++)
bool Dexter::ExtractLoopBounds::forTemplate3 (ForStmt * loop)
{
  clang::Stmt* init = loop->getInit();
  clang::Expr* cond = loop->getCond();

  StmtExt* ext = Dexter::StmtExt::Get(loop);

  if (init == NULL || cond == NULL) return false;

  if (
    isa<BinaryOperator>(init) &&
    cast<BinaryOperator>(init)->isAssignmentOp() &&
    isa<DeclRefExpr>(cast<BinaryOperator>(init)->getLHS()) &&
    isa<BinaryOperator>(cast<BinaryOperator>(init)->getRHS()) &&
    cast<BinaryOperator>(init)->getLHS()->getType()->isPointerType()
  )
  {
    ext->upperBound(cast<BinaryOperator>(cast<BinaryOperator>(init)->getRHS())->getRHS());
  }

  else
    return false;

  if (
    isa<BinaryOperator>(cond) &&
    cast<BinaryOperator>(cond)->getOpcode() == BO_LT
  )
  {
    ext->lowerBound(Dexter::ASTNodeFactory::IntegerLit(0, ctx));
  }
  else
    return false;

  return true;
}

// for (endPtr += cols; srcPtr != endPtr; srcPtr++)
bool Dexter::ExtractLoopBounds::forTemplate4 (ForStmt * loop)
{
  clang::Stmt* init = loop->getInit();
  clang::Expr* cond = loop->getCond();

  StmtExt* ext = Dexter::StmtExt::Get(loop);

  if (init == NULL || cond == NULL) return false;

  if (
    isa<BinaryOperator>(init) &&
    cast<BinaryOperator>(init)->isCompoundAssignmentOp() &&
    isa<DeclRefExpr>(cast<BinaryOperator>(init)->getLHS()) &&
    cast<BinaryOperator>(init)->getLHS()->getType()->isPointerType()
  )
  {
    clang::Expr * rhs = cast<BinaryOperator>(init)->getRHS();
    while (isa<CastExpr>(rhs))
      rhs = cast<CastExpr>(rhs)->getSubExpr();

    if (isa<DeclRefExpr>(rhs))
      ext->upperBound(rhs);
    else
      return false;
  }

  else
    return false;

  if (
    isa<BinaryOperator>(cond) &&
    cast<BinaryOperator>(cond)->getOpcode() == BO_NE
  )
  {
    ext->lowerBound(Dexter::ASTNodeFactory::IntegerLit(0, ctx));
  }
  else
    return false;

  return true;
}

bool Dexter::ExtractLoopBounds::whileTemplate1 (WhileStmt * loop)
{
  clang::Expr* cond = loop->getCond();

  StmtExt* ext = Dexter::StmtExt::Get(loop);

  if (
    isa<BinaryOperator>(cond) &&
    cast<BinaryOperator>(cond)->getOpcode() == BO_GE &&
    isa<CastExpr>(cast<BinaryOperator>(cond)->getLHS()) &&
    isa<UnaryOperator>(cast<CastExpr>(cast<BinaryOperator>(cond)->getLHS())->getSubExpr()) &&
    isa<ParenExpr>(cast<UnaryOperator>(cast<CastExpr>(cast<BinaryOperator>(cond)->getLHS())->getSubExpr())->getSubExpr()) &&
    isa<DeclRefExpr>(cast<ParenExpr>(cast<UnaryOperator>(cast<CastExpr>(cast<BinaryOperator>(cond)->getLHS())->getSubExpr())->getSubExpr())->getSubExpr())
  )
  {
    ext->lowerBound(cast<BinaryOperator>(cond)->getRHS());
    ext->upperBound(cast<ParenExpr>(cast<UnaryOperator>(cast<CastExpr>(cast<BinaryOperator>(cond)->getLHS())->getSubExpr())->getSubExpr())->getSubExpr());
  }
  else
    return false;

  return true;
}