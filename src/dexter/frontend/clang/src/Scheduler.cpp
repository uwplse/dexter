/*
 * Scheduler.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "Scheduler.h"

void Dexter::Scheduler::HandleTranslationUnit (ASTContext & ctx)
{
  llvm::outs() << "## Scanning for intentional code blocks... \n\n";
  rc.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Extracting loop bounds... \n\n";
  eb.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Normalizing intentional code blocks... \n\n";
  nc.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Performing live variable analysis... \n\n";
  vs.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Parsing intentional code blocks as dags... \n\n";
  gd.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Extracting constants... \n\n";
  ec.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Extracting input variables... \n\n";
  ei.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Extracting output variables... \n\n";
  eo.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Extracting index variables... \n\n";
  en.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Modelling user-defined functions... \n\n";
  eu.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Running verification conditions generator... \n\n";
  vc.TraverseDecl(ctx.getTranslationUnitDecl());

  llvm::outs() << "\n## Generating output files... \n\n";
  go.TraverseDecl(ctx.getTranslationUnitDecl());
}