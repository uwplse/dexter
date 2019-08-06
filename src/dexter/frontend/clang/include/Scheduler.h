/*
 * Scheduler.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef SCHEDULER_H_
#define SCHEDULER_H_

#include "clang/Rewrite/Core/Rewriter.h"
#include "clang/AST/ASTConsumer.h"
#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Frontend/FrontendActions.h"
#include "clang/Frontend/CompilerInstance.h"

#include "visit/RegisterIntentionalCode.h"
#include "visit/NormalizeCode.h"
#include "visit/ExtractLoopBounds.h"
#include "visit/VarScopeAnalysis.h"
#include "visit/GenerateDAG.h"
#include "visit/ExtractConstants.h"
#include "visit/ExtractInVars.h"
#include "visit/ExtractOutVars.h"
#include "visit/ExtractIndexVars.h"
#include "visit/ExtractUDFs.h"
#include "visit/ComputeVC.h"
#include "visit/GenerateOutputs.h"

using namespace clang;

namespace Dexter {

class Scheduler : public ASTConsumer
{
public:
	Scheduler(Rewriter &R) : rc(R), nc(R), eb(R), vs(R), gd(R), ec(R), ei(R), eo(R), en(R), eu(R), vc(R), go(R) {}

  void HandleTranslationUnit (ASTContext & ctx) override;

protected:
  Dexter::RegisterIntentionalCode rc;
  Dexter::NormalizeCode nc;
  Dexter::ExtractLoopBounds eb;
  Dexter::VarScopeAnalysis vs;
  Dexter::GenerateDAG gd;
  Dexter::ExtractConstants ec;
  Dexter::ExtractInVars ei;
  Dexter::ExtractOutVars eo;
  Dexter::ExtractIndexVars en;
  Dexter::ExtractUDFs eu;
  Dexter::ComputeVC vc;
  Dexter::GenerateOutputs go;
};

class SchedulerAction : public ASTFrontendAction {
public:
  SchedulerAction() {}

  void EndSourceFileAction() override
  {
    SourceManager &SM = rewriter.getSourceMgr();
    llvm::errs() << "** EndSourceFileAction for: "
                 << SM.getFileEntryForID(SM.getMainFileID())->getName() << "\n\n";
  }

  std::unique_ptr<ASTConsumer> CreateASTConsumer(CompilerInstance &CI,
                                                 StringRef file) override
  {
    llvm::errs() << "\n** Creating AST consumer for: " << file << "\n\n";
    rewriter.setSourceMgr(CI.getSourceManager(), CI.getLangOpts());
    return llvm::make_unique<Scheduler>(rewriter);
  }

protected:
  Rewriter rewriter;
};

}

#endif /* SCHEDULER_H_ */