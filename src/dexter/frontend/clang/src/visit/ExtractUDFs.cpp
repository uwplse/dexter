/*
 * ExtractUDFs.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "visit/ExtractUDFs.h"

bool Dexter::ExtractUDFs::VisitFunctionDecl (FunctionDecl* f)
{
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  if (Dexter::Preferences::Verbosity > 0)
    llvm::outs() << "Analyzing intentional code block `" << f->getNameAsString() << "`\n";

  Pipeline* pipeline = Dexter::DeclExt::Get(f)->DAG();
  std::set<Stage*> stages = pipeline->getAllStages();

  // Run analysis on each stage
  std::set<Stage*>::iterator stage;
  for (stage = stages.begin(); stage != stages.end(); ++stage)
  {
    udfs.clear();

    std::vector<Stmt*> stmts = (*stage)->getStatements();
    std::vector<Stmt*>::iterator stmt;
    for (stmt = stmts.begin(); stmt != stmts.end(); ++stmt)
      Extract(*stmt);

    (*stage)->setUDFs(udfs);
  }

  return true;
}

bool Dexter::ExtractUDFs::VisitStmt (Stmt* s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

void Dexter::ExtractUDFs::Extract (Stmt * o)
{
  if (o == NULL) return;

  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  if (debug)
  {
    llvm::outs() << Util::print(s) << "\n";
    //s->dump();
  }

  if (isa<CXXMemberCallExpr>(s))
  {
    CXXMemberCallExpr* c = cast<CXXMemberCallExpr>(s);
    clang::Expr* obj = c->getImplicitObjectArgument();
    CXXMethodDecl* mDecl = c->getMethodDecl();

    QualType objT = obj->getType();
    if (std::regex_match(objT.getAsString(), std::regex("const class Halide::Runtime::Buffer<.*>")))
      return;

    // First traverse body
    Extract(mDecl->getBody());

    GenerateUDF(obj, mDecl);
  }
  else if (isa<CXXOperatorCallExpr>(s))
  {
    // Currently not handled
  }
  else if (isa<clang::CallExpr>(s))
  {
    // Get function declaration
    FunctionDecl* fnDecl = cast<clang::CallExpr>(s)->getDirectCallee();
    if (fnDecl == 0)
      Util::error(s, "Function Declaration not found for: ");

    // First traverse body
    Extract(fnDecl->getBody());

    GenerateUDF(fnDecl);
  }
  else
  {
    Stmt::child_iterator it;
    for (it = s->child_begin(); it != s->child_end(); ++it)
      Extract(*it);
  }
}

void Dexter::ExtractUDFs::GenerateUDF (FunctionDecl * fnDecl)
{
  if (debug) {
    llvm::outs() << "Porting Fn: \n" << Util::print(fnDecl);
  }

  // Get function name
  std::string fn_name = fnDecl->getQualifiedNameAsString();

  // Edge case. Special handling for assert functions.
  if (fn_name == "assert")
    return;

  // Get param types
  bool hasComplexParams = false;
  std::vector<ParmVarDecl *> outParams;
  std::vector<Dexter::Type> paramsT;

  clang::FunctionDecl::param_iterator it;
  for (it = fnDecl->param_begin(); it != fnDecl->param_end(); ++it)
  {
    QualType paramT = (*it)->getOriginalType();

    if (paramT->isReferenceType())
      outParams.push_back(*it);
    else if (paramT->isPointerType())
      outParams.push_back(*it);
    else if (!paramT->isBuiltinType())
      hasComplexParams = true;

    paramsT.push_back(ClangToIRParser::toIRType(paramT));
  }

  // Do we know how to model this function?
  if (hasComplexParams)
    Util::error(fnDecl, "NYI: Dynamic modelling of functions that take non-builtin type parameters: ");

  QualType rTypeClang = fnDecl->getReturnType();

  // Model return value computation as a metalift function
  if (!rTypeClang->isVoidType() && outParams.size() == 0)
  {
    // Get return type
    Dexter::Type rType = ClangToIRParser::toIRType(fnDecl->getReturnType());

    // Type already exists?
    if (TypesFactory::isFunctionT(fn_name, rType, paramsT))
      return;

    // Translate function body to IR
    FuncDecl * irDecl = ClangToIRParser::parse(fnDecl);

    // Save UDF
    this->udfs.insert(irDecl);

    // Add function type to TypesFactory
    TypesFactory::functionT(fn_name, rType, paramsT);
  }
}

void Dexter::ExtractUDFs::GenerateUDF (clang::Expr * container, CXXMethodDecl * mDecl)
{
  // Get method and class names
  std::string m_name = mDecl->getNameAsString();
  std::string cls_name = mDecl->getParent()->getNameAsString();
  std::string ir_name = cls_name + "_" + m_name;

  // Get return type
  QualType returnT = mDecl->getReturnType();
  Dexter::Type retTML = (returnT->isVoidType() ? ClangToIRParser::toIRType(container->getType()) : ClangToIRParser::toIRType(returnT));

  // Get param types
  bool hasRefParams = false, hasPointerParams = false, hasComplexParams = false;
  std::vector<Dexter::Type> paramsT;
  paramsT.push_back(ClangToIRParser::toIRType(container->getType()));
  clang::FunctionDecl::param_iterator it;
  for (it = mDecl->param_begin(); it != mDecl->param_end(); ++it)
  {
    QualType paramT = (*it)->getOriginalType();

    if (paramT->isReferenceType())
      hasRefParams = true;
    else if (paramT->isPointerType())
      hasPointerParams = true;
    else if (!paramT->isBuiltinType())
      hasComplexParams = true;

    paramsT.push_back(ClangToIRParser::toIRType(paramT));
  }

  // Type already exists?
  if (TypesFactory::isFunctionT(ir_name, retTML, paramsT))
    return;

  // Auto-model type
  if (hasRefParams)
    Util::error(mDecl, "NYI: Dynamic modelling of methods that take reference type parameters: ");
  else if (hasPointerParams)
    Util::error(mDecl, "NYI: Dynamic modelling of methods that take pointer type parameters: ");
  else if (hasComplexParams)
    Util::error(mDecl, "NYI: Dynamic modelling of methods that take non-builtin type parameters: ");

  if (mDecl->isConst())
  {
    // Translate function body to ML
    FuncDecl * irDecl = ClangToIRParser::parse(cls_name, mDecl);

    // Save UDF
    this->udfs.insert(irDecl);

    // Add function type to TypesFactory
    TypesFactory::functionT(ir_name, retTML, paramsT);
  }
  else
    Util::error(mDecl, "NYI: Dynamic modelling of non-const methods: ");
}