/*
 * ComputeVC.cpp
 *
 *  Created on: Oct 13, 2017
 *      Author: maazsaf
 */
#include "visit/ComputeVC.h"

bool Dexter::ComputeVC::VisitFunctionDecl (FunctionDecl* f)
{
  Dexter::DeclExt* ext = Dexter::DeclExt::Get(f);

  if (!ext->lift())
    return true;

  Pipeline* pipeline = Dexter::DeclExt::Get(f)->DAG();

  std::set<Stage*> stages = pipeline->getAllStages();

  std::set<Stage*>::iterator stage;
  for (stage = stages.begin(); stage != stages.end(); ++stage)
  {
    if (Dexter::Preferences::Verbosity > 0) {
      if (Dexter::Preferences::Mode == 0)
        llvm::outs() << "Computing VC for intentional code block `" << f->getNameAsString()
                           << "` (Stage " << (*stage)->getId() << ")\n";
      else
        llvm::outs() << "Computing VC for legacy code block `" << f->getNameAsString()
                           << "` (Stage " << (*stage)->getId() << ")\n";
    }

    if ((*stage)->isEmpty())
      continue;

    this->irInVars.clear();
    this->irInVars = Dexter::ClangToIRParser::inVars(*stage, vars);
    this->irOutVars.clear();
    this->irOutVars = Dexter::ClangToIRParser::outVars(*stage, vars);

    Dexter::VCGenerator * vcGenStage = new Dexter::VCGenerator();
    vcGenStage->pc(this->irInVars, this->irOutVars);

    std::vector<Stmt*> stmts = (*stage)->getStatements();
    std::vector<Stmt*>::reverse_iterator stmt;
    for (stmt = stmts.rbegin(); stmt != stmts.rend(); ++stmt) {
      vc(*stmt, vcGenStage);
    }

    Dexter::Program * pStage = vcGenStage->createProgram(this->irInVars, this->irOutVars);

    if (debug)
      llvm::outs() << "final pc is: " << Dexter::Util::toString(pStage) << "\n\n\n";

    (*stage)->setVCs(pStage);
  }

  return true;
}

bool Dexter::ComputeVC::VisitStmt (Stmt* s)
{
  Dexter::StmtExt* ext = Dexter::StmtExt::Get(s);

  if (!ext->lift())
    return true;

  //CallExpr * c = cast<CallExpr>(s);
  Util::error("NYI");

  return true;
}

/*
 * binary operation expression such as "x + y" or "x <= y".
 */
Dexter::Expr * Dexter::ComputeVC::vc (BinaryOperator * s, Dexter::VCGenerator * vcGen)
{
  clang::Expr * lhs = s->getLHS();
  clang::Expr * rhs = s->getRHS();

  if (s->isCompoundAssignmentOp())
  {
    vc(lhs, Dexter::ClangToIRParser::parse(s, vars), vcGen);

    if (debug)
      llvm::outs() << "after binop pc is: " << Dexter::Util::toString(vcGen->vc()) << "\n";
  }
  else if (s->isAssignmentOp())
  {
    if (isa<BinaryOperator>(rhs) && cast<BinaryOperator>(rhs)->isAssignmentOp())
    {
      vc(lhs, vc(cast<BinaryOperator>(rhs), vcGen), vcGen);
    }
    else
    {
      Dexter::Expr * rhsIR = Dexter::ClangToIRParser::parse(rhs, vars);
      vc(lhs, rhsIR, vcGen);
      return rhsIR;
    }

    if (debug)
      llvm::outs() << "after binop pc is: " << Dexter::Util::toString(vcGen->vc()) << "\n";
  }

  return NULL;
}

/*
 * assignment operation x = y
 */
void Dexter::ComputeVC::vc (clang::Expr * from, Dexter::Expr * to, Dexter::VCGenerator * vcGen, bool replace)
{
  if (isa<DeclRefExpr>(from)) {
    if (replace)
      vcGen->replace(Dexter::ClangToIRParser::parse(from, vars), to);
    else
      vcGen->queueReplace(Dexter::ClangToIRParser::parse(from, vars), to);
  }
  else if (isa<MemberExpr>(from))
  {
    clang::Expr * target = cast<MemberExpr>(from)->getBase();
    ValueDecl * field = cast<MemberExpr>(from)->getMemberDecl();

    QualType target_t = target->getType();
    if (isa<RecordType>(target_t))
    {
      RecordDecl * target_decl = cast<RecordType>(target_t)->getDecl();

      std::string class_t = target_decl->getNameAsString();
      std::string field_n = field->getNameAsString();

      std::vector<Dexter::Expr *> fields;
      clang::RecordDecl::field_iterator it;
      for (it = target_decl->field_begin(); it != target_decl->field_end(); ++it)
        fields.push_back(it->getNameAsString().compare(field_n) == 0 ? to : (Dexter::VarExpr *)Dexter::ClangToIRParser::parse(*it, vars));

      vc(target, new Dexter::CallExpr(class_t, fields), vcGen, replace);
    }
    else
      Dexter::Util::error(target->getType(), "NYI. Handling for field target type: ");
  }
  else if (isa<ArraySubscriptExpr>(from))
  {
    // Untested for multi-dimensional arrays
    Dexter::Expr * arr = Dexter::ClangToIRParser::parse(cast<ArraySubscriptExpr>(from)->getBase(), vars);
    Dexter::Expr * idx = Dexter::ClangToIRParser::parse(cast<ArraySubscriptExpr>(from)->getIdx(), vars);

    vc(cast<ArraySubscriptExpr>(from)->getBase(), new Dexter::StoreExpr(arr, to, {idx}), vcGen, replace);
  }
  else if (isa<CXXOperatorCallExpr>(from))
  {
    CXXOperatorCallExpr * c = cast<CXXOperatorCallExpr>(from);
    QualType opT = c->getArg(0)->getType();

    // Special handling of Halide buffers
    if (isa<TemplateSpecializationType>(opT)) {
      std::string qtn = cast<TemplateSpecializationType>(opT)->getAliasedType().getAsString();
      if (qtn.rfind("Halide::Runtime::Buffer", 0) == 0) {
        Dexter::Expr * buffer = Dexter::ClangToIRParser::parse(c->getArg(0), vars);

        std::vector<Dexter::Expr*> params;
        for (unsigned int i=1; i < c->getNumArgs(); ++i) {
          params.push_back(Dexter::ClangToIRParser::parse(c->getArg(i), vars));
        }

        vc(c->getArg(0), new Dexter::StoreExpr(buffer, to, params), vcGen, replace);
        return;
      }
    }

    Dexter::Util::error(from, "NYI. lhs handling for: ");
  }
  else if (isa<UnaryOperator>(from) && cast<UnaryOperator>(from)->getOpcode() == UO_Deref)
  {
    Dexter::Expr * arr = Dexter::ClangToIRParser::parse(cast<UnaryOperator>(from)->getSubExpr(), vars);
    vc(cast<UnaryOperator>(from)->getSubExpr(), new Dexter::StoreExpr(arr, to, {new Dexter::IntLitExpr(0)}), vcGen);
    //vc(cast<UnaryOperator>(from)->getSubExpr(), to, vcGen, replace);
  }
  else if (isa<ParenExpr>(from))
    vc(cast<ParenExpr>(from)->getSubExpr(), to, vcGen, replace);
  else if (isa<CastExpr>(from))
    vc(cast<CastExpr>(from)->getSubExpr(), to, vcGen, replace);
  else {
    Util::print(to);
    Dexter::Util::error(from, "NYI. lhs handling for: ");
  }
}

/*
 * unary operation expression such as "x++" or "--y".
 */
void Dexter::ComputeVC::vc (UnaryOperator * s, Dexter::VCGenerator * vcGen)
{
  clang::Expr * expr = s->getSubExpr();

  if (s->isIncrementDecrementOp())
  {
    vc(expr, Dexter::ClangToIRParser::parse(s, vars), vcGen);

    if (debug)
      llvm::outs() << "after uop pc is: " << Dexter::Util::toString(vcGen->vc()) << "\n";
  }
  else
    Dexter::Util::error(s, "unknown UnaryOp: ");
}

void Dexter::ComputeVC::vc (clang::CallExpr * s, Dexter::VCGenerator * vcGen)
{
  if (isa<CXXMemberCallExpr>(s))
  {
    CXXMemberCallExpr* c = cast<CXXMemberCallExpr>(s);
    clang::Expr* obj = c->getImplicitObjectArgument();
    CXXMethodDecl* m_decl = c->getMethodDecl();
    QualType returnT = m_decl->getReturnType();

    if (m_decl->isConst())
      Dexter::Util::error(s, "Unexpected method call: ");
    else if (returnT->isVoidType()) {
      vc(obj, Dexter::ClangToIRParser::parse(s, vars), vcGen);
    }
    else
      Dexter::Util::error(s, "NYI: Handling of Non-const class methods: ");
  }
  else
  {
    FunctionDecl * fnDecl = s->getDirectCallee();
    if (!fnDecl)
      Dexter::Util::error(s, "Function declaration not found: ");

    std::string fn = fnDecl->getQualifiedNameAsString();

    if (fn == "Dexter::Precondition") {
      vcGen->addAssumption(Dexter::ClangToIRParser::parse(s->getArg(0), vars));
      return;
    }

    // Generate pc expr for function
    std::vector<Dexter::Expr*> pc_args;
    std::map<Dexter::Expr*,Dexter::Expr*> args_map;

    clang::FunctionDecl::param_iterator pit = fnDecl->param_begin();
    clang::CallExpr::arg_iterator ait = s->arg_begin();
    while (pit != fnDecl->param_end())
    {
      Dexter::Expr * param = Dexter::ClangToIRParser::parse(*pit);
      Dexter::Expr * arg = Dexter::ClangToIRParser::parse(*ait, vars);

      QualType paramT = (*pit)->getOriginalType();
      if (paramT->isReferenceType() || paramT->isPointerType())
        pc_args.push_back(param);

      args_map[param] = arg;

      ++pit;
      ++ait;
    }

    Dexter::CallExpr * fn_pc = new Dexter::CallExpr("pc", pc_args);

    // Save current state
    std::vector<Dexter::VarExpr *> irOutVarsOuter = this->irOutVars;

    // Run computeVC on function body
    Dexter::VCGenerator * vcGenFunc = new Dexter::VCGenerator();
    vcGenFunc->vc(fn_pc);
    vc(fnDecl->getBody(), vcGenFunc);

    // Restore old state
    this->irOutVars = irOutVarsOuter;

    // Update VC
    if (debug)
      llvm::outs() << "before func call: " << Dexter::Util::toString(vcGen->vc()) << "\n\n";

    std::map<Dexter::Expr*,Dexter::Expr*>::iterator mit;
    for (mit = args_map.begin(); mit != args_map.end(); ++mit)
      vcGenFunc->replace(mit->first, mit->second);

    if (debug)
      llvm::outs() << "func call vc: " << Dexter::Util::toString(vcGenFunc->vc()) << "\n\n";

    Dexter::CallExpr * fn_vc = (Dexter::CallExpr *) vcGenFunc->vc();
    std::vector<Dexter::Expr*> fn_vc_args = fn_vc->args();

    std::vector<Dexter::Expr*>::iterator vit;

    pit = fnDecl->param_begin();
    ait = s->arg_begin();
    vit = fn_vc_args.begin();
    while (ait != s->arg_end())
    {
      QualType paramT = (*pit)->getOriginalType();
      if (paramT->isPointerType())
      {
        clang::Expr * from = getArray(*ait);
        Dexter::Expr * to = *vit;

        Dexter::Expr * arg = Dexter::ClangToIRParser::parse(*ait, vars);

        if (arg->classT() == Expr::ClassVarExpr)
          vc(from, to, vcGen, false);
        else if (arg->classT() == Expr::ClassIncrPtrExpr)
          vc(from, new Dexter::PtrExpr(new Dexter::FieldExpr(to, "data"), new Dexter::FieldExpr(Dexter::ClangToIRParser::parse(from, vars), "offset")), vcGen, false);
        else if (arg->classT() == Expr::ClassDecrPtrExpr)
          vc(from, new Dexter::PtrExpr(new Dexter::FieldExpr(to, "data"), new Dexter::FieldExpr(Dexter::ClangToIRParser::parse(from, vars), "offset")), vcGen, false);
        else
          vc(from, to, vcGen, false);

        ++vit;
      }
      else if (paramT->isReferenceType())
      {
        clang::Expr * from = *ait;
        Dexter::Expr * to = *vit;

        vc(from, to, vcGen, false);

        ++vit;
      }

      ++pit;
      ++ait;

      vcGen->runQueue();

      if (debug)
        llvm::outs() << "after func call pc is: " << Dexter::Util::toString(vcGen->vc()) << "\n\n";
    }
  }
}

clang::Expr * Dexter::ComputeVC::getArray (clang::Expr * e)
{
  if (isa<DeclRefExpr>(e))
    return e;

  else if (isa<CastExpr>(e))
    return getArray(cast<CastExpr>(e)->getSubExpr());

  else if (isa<ParenExpr>(e))
    return getArray(cast<ParenExpr>(e)->getSubExpr());

  else if (isa<ArraySubscriptExpr>(e))
    return getArray(cast<ArraySubscriptExpr>(e)->getBase());

  else if (isa<UnaryOperator>(e) && cast<UnaryOperator>(e)->getOpcode() == UO_AddrOf)
    return getArray(cast<UnaryOperator>(e)->getSubExpr());

  else
    Dexter::Util::error(e, "NYI. getArray handling for: ");

  return e;
}

void Dexter::ComputeVC::vc (ConditionalOperator * s, Dexter::VCGenerator * vcGen)
{
  Dexter::VCGenerator * vcGen_con = new Dexter::VCGenerator(vcGen);
  Dexter::VCGenerator * vcGen_alt = new Dexter::VCGenerator(vcGen);

  // consequent cannot be null
  vc(s->getTrueExpr(), vcGen_con);
  vc(s->getFalseExpr(), vcGen_alt);

  vcGen->conditional(Dexter::ClangToIRParser::parse(s->getCond(), vars), vcGen_con, vcGen_alt);

  if (debug)
    llvm::outs() << "after cond-expr pc is: " << Dexter::Util::toString(vcGen->vc()) << "\n";
}

void Dexter::ComputeVC::vc (IfStmt * s, Dexter::VCGenerator * vcGen)
{
  Dexter::VCGenerator * vcGen_con = new Dexter::VCGenerator(vcGen);
  Dexter::VCGenerator * vcGen_alt;

  // consequent cannot be null
  vc(s->getThen(), vcGen_con);

  if(s->getElse() != NULL)
  {
    vcGen_alt = new Dexter::VCGenerator(vcGen);
    vc(s->getElse(), vcGen_alt);
  }
  else
    vcGen_alt = vcGen;

  vcGen->conditional(Dexter::ClangToIRParser::parse(s->getCond(), vars), vcGen_con, vcGen_alt);

  if (debug)
    llvm::outs() << "after if pc is: " << Dexter::Util::toString(vcGen->vc()) << "\n";
}

void Dexter::ComputeVC::vc (WhileStmt * s, Dexter::VCGenerator * vcGen)
{
  // Each loop has a loop invariant
  std::vector<Dexter::VarExpr *> irVars = Dexter::ClangToIRParser::liveVars(s, vars);
  //std::vector<VarExpr *> irOutVars = Dexter::ClangToIRParser::outVars(s, vars);

  Dexter::Expr * inv = vcGen->invariant(this->irInVars, this->irOutVars);

  if (debug)
    llvm::outs() << "loop invariant is: " << Dexter::Util::toString(inv) << "\n";

  Dexter::Expr * tCond = Dexter::ClangToIRParser::parse(s->getCond(), vars);

  Dexter::VCGenerator *vcGen_body = new Dexter::VCGenerator(vcGen);
  vcGen_body->vc(inv);

  vc(s->getBody(), vcGen_body);

  vcGen->mergeDecls(vcGen_body);
  vcGen->mergePrior(vcGen_body);

  vcGen->loop(tCond, inv, vcGen_body->vc(), this->irOutVars);
}


/*
 * This represents a group of statements like { stmt stmt }.
 */
void Dexter::ComputeVC::vc (CompoundStmt * s, Dexter::VCGenerator * vcGen)
{
  if (vcGen->vc() == NULL)
  {
    std::vector<Dexter::VarExpr *> irVars = Dexter::ClangToIRParser::liveVars(s, vars);
    //std::vector<VarExpr *> irOutVars = Dexter::ClangToIRParser::outVars(s, vars);

    vcGen->pc(this->irInVars, this->irOutVars);

    if (debug)
      llvm::outs() << "initial pc for block is: " << Dexter::Util::toString(vcGen->vc()) << "\n";
  }

  CompoundStmt::const_reverse_body_iterator it;
  for (it = s->body_rbegin(); it != s->body_rend(); ++it)
  {
    vc(*it, vcGen);
  }
}

/*
 * process initializers in declarations, e.g., int x = 1;
 */
void Dexter::ComputeVC::vc (DeclStmt * s, Dexter::VCGenerator * vcGen)
{
  DeclStmt::const_decl_iterator it;
  for (it = s->decl_begin(); it != s->decl_end(); ++it)
  {
    VarDecl * d = (VarDecl *)*it;
    if (d->hasInit())
    {
      Dexter::VarExpr * v = (Dexter::VarExpr*) (Dexter::ClangToIRParser::parse(makeDeclRefExpr(d, false), vars));
      std::string v_init_n = v->name();
      v_init_n = v_init_n + "_init";
      Dexter::VarExpr * v_init = new Dexter::VarExpr(v_init_n.c_str(), *v->type());

      // Special handling for Halide Buffers
      // TODO: Replace this garbage hack with a principled implementation
      clang::Expr * init = d->getInit();
      if (isa<CXXConstructExpr>(init)) {
        CXXConstructExpr* cons = cast<CXXConstructExpr>(init);
        CXXConstructorDecl* consDecl = cons->getConstructor();

        QualType consT = cons->getType();
        if (isa<TemplateSpecializationType>(consT))
          consT = cast<TemplateSpecializationType>(consT)->desugar();

        QualType consDeclT = consDecl->getType();

        if (
          std::regex_match(consT.getAsString(), std::regex("class Halide::Runtime::Buffer<.*>")) &&
          std::regex_match(consDeclT.getAsString(), std::regex("void \\(int, int\\)"))
        ) {
          Dexter::Expr* w = Dexter::ClangToIRParser::parse(cons->getArg(0), vars);
          Dexter::Expr* h = Dexter::ClangToIRParser::parse(cons->getArg(1), vars);
          vcGen->registerBounds(v, w, h);
          return;
        }
      }

      vcGen->replace(v, Dexter::ClangToIRParser::parse(d->getInit(), vars));
      vcGen->replace(v_init, Dexter::ClangToIRParser::parse(d->getInit(), vars));
    }
  }
}

// copied from https://clang.llvm.org/doxygen/BodyFarm_8cpp_source.html

DeclRefExpr * Dexter::ComputeVC::makeDeclRefExpr(const VarDecl *D, bool RefersToEnclosingVariableOrCapture)
{
  QualType Type = D->getType().getNonReferenceType();

  DeclRefExpr *DR = DeclRefExpr::Create(
      D->getASTContext(),
      D->getQualifierLoc(),
      //NestedNameSpecifierLoc(),
      D->getLocStart(),
      //SourceLocation(),
      const_cast<VarDecl *>(D),
      RefersToEnclosingVariableOrCapture,
      D->getLocStart(),
      //SourceLocation(),
      Type, VK_LValue);

  return DR;
}

/*
 * top level vc function
 */
void Dexter::ComputeVC::vc (Stmt * o, Dexter::VCGenerator * vcGen)
{
  Stmt* s = Dexter::ASTNodeFactory::GetNormalizedNode(o);

  if (debug) {
    if (vcGen->vc() != NULL) Dexter::Util::print(vcGen->vc());
    llvm::outs() << Dexter::Util::print(s) << "\n";
  }

  if (isa<ExprWithCleanups>(s))
    vc(cast<ExprWithCleanups>(s)->getSubExpr(), vcGen);

  else if (isa<ParenExpr>(s))
    vc(cast<ParenExpr>(s)->getSubExpr(), vcGen);

  else if (isa<CastExpr>(s))
    vc(cast<CastExpr>(s)->getSubExpr(), vcGen);

  else if (isa<BinaryOperator>(s))
    vc(cast<BinaryOperator>(s), vcGen);

  else if (isa<UnaryOperator>(s))
    vc(cast<UnaryOperator>(s), vcGen);

  else if (isa<clang::CallExpr>(s))
    vc(cast<clang::CallExpr>(s), vcGen);

  else if (isa<CompoundStmt>(s))
    vc(cast<CompoundStmt>(s), vcGen);

  else if (isa<ConditionalOperator>(s))
    vc(cast<ConditionalOperator>(s), vcGen);

  else if (isa<IfStmt>(s))
    vc(cast<IfStmt>(s), vcGen);

  else if (isa<DeclStmt>(s))
    vc(cast<DeclStmt>(s), vcGen);

  else if (isa<WhileStmt>(s))
    vc(cast<WhileStmt>(s), vcGen);

  else if (isa<DeclRefExpr>(s)); // Macros can be null statements; do nothing.

  else if (isa<NullStmt>(s)); // Macros can be null statements; do nothing.

  else if (isa<IntegerLiteral>(s)); // Macros can be null statements; do nothing.

  else if (isa<StringLiteral>(s)); // Macros can be null statements; do nothing.

  else{
    llvm::outs() << Dexter::Util::print(s) << "\n";
    Dexter::Util::error(s, "don't know how to compute VC for: ");
  }
}