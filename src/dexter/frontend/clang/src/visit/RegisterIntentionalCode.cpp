/*
 * RegisterIntentionalCode.cpp
 *
 *  Created on: Apr 29, 2017
 *      Author: maazsaf
 */

#include "visit/RegisterIntentionalCode.h"

bool Dexter::RegisterIntentionalCode::VisitFunctionDecl (FunctionDecl *f)
{
  // Get function name
  DeclarationName DeclName = f->getNameInfo().getName();
  std::string FuncName = DeclName.getAsString();

  // Save
  this->fnDecls[FuncName] = f;

  return true;
}

bool Dexter::RegisterIntentionalCode::VisitDecl (Decl *d)
{
  // Check if this declaration is a compiler directive for intentional code
  if (!isa<VarDecl>(d))
    return true;

  clang::VarDecl * vd = cast<clang::VarDecl>(d);

  std::string varName = vd->getNameAsString();

  std::smatch res;
  std::regex pattern("_dexter_(.*)_target");

  if (!std::regex_match (varName, res, pattern))
      return true;

  // Get the target language from declaration
  if (!vd->hasInit()) {
    if (Dexter::Preferences::Mode == 0)
      Dexter::Util::error("Target lang undefined for intentional block: " + res[1].str());
    else
      Dexter::Util::error("Target lang undefined for legacy code block: " + res[1].str());
  }

  clang::Expr * init = vd->getInit();

  if (!isa<DeclRefExpr>(init)) {
    if (Dexter::Preferences::Mode == 0)
      Dexter::Util::error("Target lang undefined for intentional block: " + res[1].str());
    else
      Dexter::Util::error("Target lang undefined for legacy code block: " + res[1].str());
  }

  Decl* init_decl = cast<DeclRefExpr>(init)->getDecl();

  if (!isa<EnumConstantDecl>(init_decl)) {
    if (Dexter::Preferences::Mode == 0)
      Dexter::Util::error("Target lang undefined for intentional block: " + res[1].str());
    else
      Dexter::Util::error("Target lang undefined for legacy code block: " + res[1].str());
  }

  int targetVal = cast<EnumConstantDecl>(init_decl)->getInitVal().getExtValue();
  Dexter::Target::TargetLang target = (Dexter::Target::TargetLang) targetVal;

  // Check if function declaration exists
  if (!this->fnDecls.count(res[1].str()) || !this->fnDecls[res[1].str()]->hasBody()) {
    if (Dexter::Preferences::Mode == 0)
      Dexter::Util::error("Intentional block `" + res[1].str() + "` either not declared or" +
          "registered before declaration.");
    else
      Dexter::Util::error("Legacy block `" + res[1].str() + "` either not declared or" +
                "registered before declaration.");
  }

  FunctionDecl * f = this->fnDecls[res[1].str()];

  // Register as an intentional code block
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);
  ext->lift(true);
  ext->target(target);

  if (Dexter::Preferences::Mode == 0)
    llvm::outs() << "Intentional code block `" << res[1].str() << "` found with target `" << Util::print(init) << "`\n";
  else
    llvm::outs() << "Legacy code block `" << res[1].str() << "` found with target `" << Util::print(init) << "`\n";

  return true;
}

bool Dexter::RegisterIntentionalCode::VisitStmt (Stmt *s)
{
  if (!isa<CallExpr>(s))
    return true;

  Decl * cd = cast<CallExpr>(s)->getCalleeDecl();

  if (cd == NULL || !isa<FunctionDecl>(cd))
    return true;

  std::string name = cast<FunctionDecl>(cd)->getQualifiedNameAsString();

  if (name == "Dexter::Intentional::Execute"){
    int lno = rewriter.getSourceMgr().getSpellingLineNumber(s->getSourceRange().getBegin());

    clang::Expr * arg0 = cast<CallExpr>(s)->getArg(0);

    if (!isa<DeclRefExpr>(arg0))
      Dexter::Util::error("Invalid target lang for intentional block at line " + std::to_string(lno));

    Decl* arg0_decl = cast<DeclRefExpr>(arg0)->getDecl();

    if (!isa<EnumConstantDecl>(arg0_decl))
      Dexter::Util::error("Invalid target lang for intentional block at line " + std::to_string(lno));

    int targetVal = cast<EnumConstantDecl>(arg0_decl)->getInitVal().getExtValue();
    Dexter::Target::TargetLang target = (Dexter::Target::TargetLang) targetVal;

    // Register as an intentional code block
    Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);
    ext->lift(true);
    ext->target(target);

    llvm::outs() << "Lifting intentional code block at line " << lno << " to " << Util::print(arg0) << "\n\n";
  }

  return true;
}