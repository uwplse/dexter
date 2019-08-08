/*
 * ExtractOutVars.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "visit/ExtractOutVars.h"

bool Dexter::ExtractOutVars::VisitFunctionDecl (FunctionDecl* f)
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
    // Push scope
    this->scopes.push(new ExtractOutVars::LexicalScope());

    std::vector<Stmt*> stmts = (*stage)->getStatements();
    std::vector<Stmt*>::iterator stmt;
    for (stmt = stmts.begin(); stmt != stmts.end(); ++stmt)
      Extract(*stmt);

    // Pop scope
    std::set<ValueDecl *> vars = scopes.top()->outVars();
    std::set<ValueDecl *> arrVars = scopes.top()->outArrVars();
    std::set<ValueDecl *> bufVars = scopes.top()->outBufVars();
    this->scopes.pop();

    if (debug) {
      for (std::set<ValueDecl *>::const_iterator it = vars.begin(); it != vars.end(); ++it)
        llvm::outs() << (*it)->getNameAsString() << "\n";

      llvm::outs() << "---------\n";

      for (std::set<ValueDecl *>::const_iterator it = arrVars.begin(); it != arrVars.end(); ++it)
        llvm::outs() << (*it)->getNameAsString() << "\n";

      llvm::outs() << "---------\n";

      for (std::set<ValueDecl *>::const_iterator it = bufVars.begin(); it != bufVars.end(); ++it)
        llvm::outs() << (*it)->getNameAsString() << "\n";

      llvm::outs() << "##########\n";
    }

    // Save output variables for this scope
    (*stage)->setOutputVars(vars);
    (*stage)->setOutputArrayVars(arrVars);
    (*stage)->setOutputBufferVars(bufVars);
  }

  return true;
}

void Dexter::ExtractOutVars::Extract (Stmt *o)
{
  if (o == NULL) return;

  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  if (debug)
  {
    llvm::outs() << Util::print(s) << "\n";
    s->dump();
  }

  if (isa<BinaryOperator>(s))
  {
    BinaryOperator * bo = cast<BinaryOperator>(s);
    clang::Expr * lhs = bo->getLHS();

    if (bo->isCompoundAssignmentOp())
      Extract(lhs);

    else if (bo->isAssignmentOp())
      Extract(lhs);
  }
  else if (isa<UnaryOperator>(s))
  {
    UnaryOperator * uo = cast<UnaryOperator>(s);
    clang::Expr * expr = uo->getSubExpr();

    if (uo->isIncrementDecrementOp())
      Extract(expr);
  }

  Stmt::child_iterator it;
  for (it = s->child_begin(); it != s->child_end(); ++it)
    Extract(*it);
}

void Dexter::ExtractOutVars::Extract (clang::Expr * e)
{
  if (isa<DeclRefExpr>(e))
    this->scopes.top()->outputVar((ValueDecl *) cast<DeclRefExpr>(e)->getFoundDecl());

  else if (isa<MemberExpr>(e))
    Extract(cast<MemberExpr>(e)->getBase());

  else if (isa<CastExpr>(e))
    Extract(cast<CastExpr>(e)->getSubExpr());

  else if (isa<ParenExpr>(e))
    Extract(cast<ParenExpr>(e)->getSubExpr());

  else if (isa<ArraySubscriptExpr>(e))
    ExtractArray(cast<ArraySubscriptExpr>(e)->getBase());

  else if (isa<UnaryOperator>(e))
  {
    UnaryOperator * uo = cast<UnaryOperator>(e);
    if (uo->getOpcode() == UO_Deref || uo->getOpcode() == UO_AddrOf)
      ExtractArray(uo->getSubExpr());
    else
      Extract(uo->getSubExpr());
  }
  else if (isa<CXXOperatorCallExpr>(e))
  {
    // Handle assignment to Halide::Runtime::Buffer using () operator
    if (cast<CXXOperatorCallExpr>(e)->getOperator() == OO_Call)
    {
      FunctionDecl* opDecl = cast<CXXOperatorCallExpr>(e)->getDirectCallee();
      if (isa<CXXMethodDecl>(opDecl))
      {
         CXXMethodDecl* mDecl = cast<CXXMethodDecl>(opDecl);
         std::string cls = mDecl->getParent()->getQualifiedNameAsString();

        if (cls == "Halide::Runtime::Buffer") {
          ExtractBuffer(cast<CXXOperatorCallExpr>(e)->getArg(0));
          return;
        }
      }
    }

    llvm::outs() << Util::print(e) << "\n";
    Util::error(e, "NYI. lhs handling for: ");
  }
  else
  {
    llvm::outs() << Util::print(e) << "\n";
    Util::error(e, "NYI. lhs handling for: ");
  }
}

void Dexter::ExtractOutVars::ExtractArray(clang::Expr * e)
{
  if (isa<DeclRefExpr>(e))
  {
    this->scopes.top()->outputVar((ValueDecl *) cast<DeclRefExpr>(e)->getFoundDecl());
    this->scopes.top()->outputArrVar((ValueDecl *) cast<DeclRefExpr>(e)->getFoundDecl());
  }

  else if (isa<MemberExpr>(e))
    ExtractArray(static_cast<MemberExpr *>(e)->getBase());

  else if (isa<CastExpr>(e))
    ExtractArray(static_cast<CastExpr *>(e)->getSubExpr());

  else if (isa<ParenExpr>(e))
    ExtractArray(static_cast<ParenExpr *>(e)->getSubExpr());

  else if (isa<ArraySubscriptExpr>(e))
    ExtractArray(cast<ArraySubscriptExpr>(e)->getBase());

  else
  {
    llvm::outs() << Util::print(e) << "\n";
    Util::error(e, "NYI. Extract array handling for: ");
  }
}

void Dexter::ExtractOutVars::ExtractBuffer(clang::Expr * e)
{
  if (isa<DeclRefExpr>(e))
  {
    this->scopes.top()->outputVar((ValueDecl *) cast<DeclRefExpr>(e)->getFoundDecl());
    this->scopes.top()->outputBufVar((ValueDecl *) cast<DeclRefExpr>(e)->getFoundDecl());
  }

  else
  {
    llvm::outs() << Util::print(e) << "\n";
    Util::error(e, "NYI. Extract buffer handling for: ");
  }
}