/*
 * Scheduler.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "Scheduler.h"

void Dexter::Scheduler::HandleTranslationUnit (ASTContext & ctx)
{
  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "## Scanning for intentional code blocks... \n\n";
  rc.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Extracting loop bounds... \n\n";
  eb.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Normalizing intentional code blocks... \n\n";
  nc.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Performing live variable analysis... \n\n";
  vs.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Parsing intentional code blocks as dags... \n\n";
  gd.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Extracting constants... \n\n";
  ec.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Extracting input variables... \n\n";
  ei.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Extracting output variables... \n\n";
  eo.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Extracting index variables... \n\n";
  en.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Modelling user-defined functions... \n\n";
  eu.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Running verification conditions generator... \n\n";
  vc.TraverseDecl(ctx.getTranslationUnitDecl());

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "\n## Generating output files... \n\n";
  go.TraverseDecl(ctx.getTranslationUnitDecl());
}