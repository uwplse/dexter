/*
 * ClangToIRParser.cpp
 *
 *  Created on: Oct 18, 2017
 *      Author: maazsaf
 */

#include "ClangToIRParser.h"

bool Dexter::ClangToIRParser::debug = false;

Dexter::Expr * Dexter::ClangToIRParser::parse (clang::Expr * e, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  if (isa<ExprWithCleanups>(e))
    return parse(cast<ExprWithCleanups>(e)->getSubExpr(), vars);
  if (isa<CXXBindTemporaryExpr>(e))
      return parse(cast<CXXBindTemporaryExpr>(e)->getSubExpr(), vars);
  else if (isa<MaterializeTemporaryExpr>(e))
    return parse(cast<MaterializeTemporaryExpr>(e)->GetTemporaryExpr(), vars);

  else if (isa<BinaryOperator>(e))
  {
    BinaryOperator * b = cast<BinaryOperator>(e);
    BinaryOperator::Opcode op = b->getOpcode();
    BinaryExpr::Op * ir_op = NULL;

    QualType lhsType = b->getLHS()->getType();
    QualType rhsType = b->getRHS()->getType();

    if (op == BO_Assign)
      return parse(b->getRHS(), vars);

    if (op == BO_Add) ir_op = BinaryExpr::Op::PLUS;
    else if (op == BO_Sub) ir_op = BinaryExpr::Op::MINUS;
    else if (op == BO_Mul) ir_op = BinaryExpr::Op::MULT;
    else if (op == BO_Div) ir_op = BinaryExpr::Op::DIV;
    else if (op == BO_LOr) ir_op = BinaryExpr::Op::OR;
    else if (op == BO_LAnd) ir_op = BinaryExpr::Op::AND;
    else if (op == BO_EQ) ir_op = BinaryExpr::Op::EQ;
    else if (op == BO_GT) ir_op = BinaryExpr::Op::GT;
    else if (op == BO_GE) ir_op = BinaryExpr::Op::GE;
    else if (op == BO_LT) ir_op = BinaryExpr::Op::LT;
    else if (op == BO_LE) ir_op = BinaryExpr::Op::LE;
    else if (op == BO_NE) ir_op = BinaryExpr::Op::NEQ;
    else if (op == BO_Rem) ir_op = BinaryExpr::Op::MOD;
    else if (op == BO_Shl) ir_op = BinaryExpr::Op::SHL;
    else if (op == BO_Shr) ir_op = (lhsType->isUnsignedIntegerType() ? BinaryExpr::Op::LSHR : BinaryExpr::Op::ASHR);
    else if (op == BO_And) ir_op = BinaryExpr::Op::BAND;
    else if (op == BO_Or) ir_op = BinaryExpr::Op::BOR;
    else if (op == BO_Xor) ir_op = BinaryExpr::Op::BXOR;
    else if (op == BO_MulAssign) ir_op = BinaryExpr::Op::MULT;
    else if (op == BO_AddAssign) ir_op = BinaryExpr::Op::PLUS;
    else if (op == BO_SubAssign ) ir_op = BinaryExpr::Op::MINUS;
    else if (op == BO_XorAssign ) ir_op = BinaryExpr::Op::BXOR;
    else if (op == BO_ShrAssign) ir_op = (lhsType->isUnsignedIntegerType() ? BinaryExpr::Op::LSHR : BinaryExpr::Op::ASHR);
    else Util::error(e, "don't know how to translate binary operator to IR: ");

    if (debug)
      llvm::outs() << "op is: " << op << " and " << BO_EQ << " now " << ir_op << "\n";

    // Special case
    Dexter::Expr * be = NULL;
    if (lhsType->isPointerType() && rhsType->isIntegerType()){
      Dexter::Expr * ptr = parse(b->getLHS(), vars);
      if (ir_op == BinaryExpr::Op::PLUS)
        be = new IncrPtrExpr(ptr, parse(b->getRHS(), vars));
      else if (ir_op == BinaryExpr::Op::MINUS)
        be = new DecrPtrExpr(ptr, parse(b->getRHS(), vars));
      else
        be = new PtrExpr(new FieldExpr(ptr, "data"), new BinaryExpr(new FieldExpr(ptr, "offset"), ir_op, parse(b->getRHS(), vars)));
    }
    else if (lhsType->isIntegerType() && rhsType->isPointerType()){
      Dexter::Expr * ptr = parse(b->getRHS(), vars);
      if (ir_op == BinaryExpr::Op::PLUS)
        be = new IncrPtrExpr(ptr, parse(b->getLHS(), vars));
      else if (ir_op == BinaryExpr::Op::MINUS)
        be = new DecrPtrExpr(ptr, parse(b->getLHS(), vars));
      else
        be = new PtrExpr(new FieldExpr(ptr, "data"), new BinaryExpr(parse(b->getLHS(), vars), ir_op, new FieldExpr(ptr, "offset")));
    }
    else
      be = new BinaryExpr(parse(b->getLHS(), vars), ir_op, parse(b->getRHS(), vars));

    if (debug)
      llvm::outs() << "after now: " << Util::toString(be) << "\n";

    return be;
  }

  else if (isa<UnaryOperator>(e))
  {
    UnaryOperator * u = cast<UnaryOperator>(e);
    clang::Expr * subExpr = u->getSubExpr();
    UnaryOperator::Opcode op = u->getOpcode();

    if (op == UO_Minus)  // -var
      return new UnaryExpr(UnaryExpr::Op::MINUS, parse(subExpr, vars));

    else if (op == UO_Not) // ~var
      return new UnaryExpr(UnaryExpr::Op::BNOT, parse(subExpr, vars));

    else if (op == UO_LNot)  // !var
      return new UnaryExpr(UnaryExpr::Op::NOT, parse(subExpr, vars));

    else if (op == UO_PostInc || op == UO_PreInc)  // ++var or var++
      if (subExpr->getType()->isPointerType())
        return new IncrPtrExpr(parse(subExpr, vars), new IntLitExpr(1));
      else
        return new BinaryExpr(parse(subExpr, vars), BinaryExpr::Op::PLUS, new IntLitExpr(1));

    else if (op == UO_PostDec || op == UO_PreDec)  // --var or var--
      if (subExpr->getType()->isPointerType())
        return new DecrPtrExpr(parse(subExpr, vars), new IntLitExpr(1));
      else
        return new BinaryExpr(parse(subExpr, vars), BinaryExpr::Op::MINUS, new IntLitExpr(1));

    else if (op == UO_Deref)  // *var
      return new SelectExpr(parse(subExpr, vars), {new IntLitExpr(0)});

    else if (op == UO_AddrOf) // &var
    {
      if (isa<ParenExpr>(subExpr))
        subExpr = cast<ParenExpr>(subExpr)->getSubExpr();

      if (isa<ArraySubscriptExpr>(subExpr))
      {
        SelectExpr * se = (SelectExpr *) parse(u->getSubExpr(), vars);
        return new IncrPtrExpr(se->array(), se->index()[0]);
      }
      else
        Util::error(e, "Dereferencing operator is not currently supported: ");
    }

    else Util::error(e, "don't know how to translate unary operator to IR: ");
  }

  else if (isa<CXXMemberCallExpr>(e)){
    CXXMemberCallExpr* c = cast<CXXMemberCallExpr>(e);
    clang::Expr* obj = c->getImplicitObjectArgument();
    CXXMethodDecl* m_decl = c->getMethodDecl();
    QualType returnT = m_decl->getReturnType();

    if (m_decl->isConst())
    {
      std::string fn_name = m_decl->getNameAsString();
      std::string cls_name = m_decl->getParent()->getNameAsString();
      std::string q_cls_name = m_decl->getParent()->getQualifiedNameAsString();
      std::vector<Dexter::Type> paramsT;
      std::vector<Dexter::Expr*> params;

      paramsT.push_back(toIRType(obj->getType()));
      params.push_back(parse(obj, vars));

      clang::CallExpr::arg_iterator it;
      for (it = c->arg_begin(); it != c->arg_end(); ++it)
      {
        if (isa<CXXDefaultArgExpr>(*it))
          continue;
        paramsT.push_back(toIRType((*it)->getType()));
        params.push_back(parse(*it, vars));
      }

      if (q_cls_name == "Halide::Runtime::Buffer")
      {
        if (fn_name == "width")
          return new FieldExpr(parse(obj, vars), "dim0_extent");
        else if (fn_name == "height")
          return new FieldExpr(parse(obj, vars), "dim1_extent");
        else if (fn_name == "channels")
          return new FieldExpr(parse(obj, vars), "dim2_extent");
        else
          Util::error(e, "NYI: Support for Halide Buffer func `" + fn_name + "`:-\n");
      }
      else if (TypesFactory::isFunctionT(cls_name + "_" + fn_name, toIRType(returnT), paramsT))
        return new Dexter::CallExpr(cls_name + "_" + fn_name, params);
      else
        Util::error(e, "NYI: Inlining member funcs: ");
    }
    else if (returnT->isVoidType())
    {
      std::string fn_name = m_decl->getNameAsString();
      std::string cls_name = m_decl->getParent()->getNameAsString();
      std::vector<Dexter::Type> paramsT;
      std::vector<Dexter::Expr*> params;

      paramsT.push_back(toIRType(obj->getType()));
      params.push_back(parse(obj, vars));

      clang::CallExpr::arg_iterator it;
      for (it = c->arg_begin(); it != c->arg_end(); ++it)
      {
        if (isa<CXXDefaultArgExpr>(*it))
          continue;
        paramsT.push_back(toIRType((*it)->getType()));
        params.push_back(parse(*it, vars));
      }

      if (TypesFactory::isFunctionT(cls_name + "_" + fn_name, toIRType(obj->getType()), paramsT))
        return new Dexter::CallExpr(cls_name + "_" + fn_name, params);
      else
        Util::error(e, "NYI: Inlining member funcs: ");
    }
    else
      Util::error(e, "NYI: Handling of Non-const class methods: ");
  }

  else if (isa<CXXOperatorCallExpr>(e))
  {
    CXXOperatorCallExpr * c = cast<CXXOperatorCallExpr>(e);

    switch (c->getOperator())
    {
      case OO_Call:
        {
          FunctionDecl * fnDecl = c->getDirectCallee();
          if (fnDecl == 0)
            Util::error(e, "NYI: CallExpr Type: ");

          std::string fn_name = "";
          std::vector<Dexter::Type> paramsT;
          std::vector<Dexter::Expr*> params;
          QualType returnT = fnDecl->getReturnType();
          QualType opT = c->getArg(0)->getType();

          // Special handling of Halide buffers
          if (isa<TemplateSpecializationType>(opT)) {
            std::string qtn = cast<TemplateSpecializationType>(opT)->getAliasedType().getAsString();
            if (qtn.rfind("Halide::Runtime::Buffer", 0) == 0) {
              std::vector<Dexter::Expr*> params;
              Dexter::Expr* array = parse(c->getArg(0), vars);
              for (unsigned int i=1; i < c->getNumArgs(); ++i) {
                params.push_back(parse(c->getArg(i), vars));
              }
              return new Dexter::SelectExpr(array, params);
            }
          }

          fn_name = opT.getAsString() + "_OO_Call";

          paramsT.push_back(toIRType(opT));
          params.push_back(parse(c->getArg(0), vars));

          if (TypesFactory::isFunctionT(fn_name, toIRType(returnT), paramsT))
            return new Dexter::CallExpr(fn_name, params);
          else
            Util::error(c, "NYI: Handling of class operator: ");
        }
      default:
        Util::error(c, "NYI: Handling of class operator: ");
    }
  }

  else if (isa<clang::CallExpr>(e)){
    FunctionDecl* fnDecl = cast<clang::CallExpr>(e)->getDirectCallee();
    clang::CallExpr* c = cast<clang::CallExpr>(e);
    if (fnDecl == 0)
      Util::error(e, "FunctionDecl not found for CallExpr: ");

    std::string fn_name = fnDecl->getNameAsString();
    Dexter::Type rType = toIRType(fnDecl->getReturnType());
    std::vector<Dexter::Type> paramsT;
    std::vector<Dexter::Expr*> params;

    clang::CallExpr::arg_iterator it;
    for (it = c->arg_begin(); it != c->arg_end(); ++it)
    {
      if (isa<CXXDefaultArgExpr>(*it))
        continue;
      paramsT.push_back(toIRType((*it)->getType()));
      params.push_back(parse(*it, vars));
    }

    if (TypesFactory::isFunctionT(fn_name, rType, paramsT)) {
      return new Dexter::CallExpr(fn_name, params);
    }

    else
    {
      /*  // Currently for only side-effect free functions
        CallExpr * fn_call = static_cast<CallExpr *>(e);
        FunctionDecl * fn_decl = static_cast<FunctionDecl *>(fn_call->getCalleeDecl());
        ASTContext * ctx = &(fn_decl->getASTContext());
        QualType ret_t = fn_call->getCallReturnType(*ctx);

        if (ret_t == ctx->VoidTy)
          Util::error(e, "expr evaluates to type void: ");

        llvm::outs() << Util::print(fn_decl->getBody()) << "\n";

        // Run compute-vc on function body
        Rewriter r;
        ComputeVC vc(r, true);
        vc.VisitFunctionDecl(fn_decl);

        Util::error(e, "don't know how to translate call expr to IR: ");
      }*/

      llvm::outs() << Util::print(e) << "\n";
      Util::error(e, "NYI: Call Expr: ");
    }
  }

  else if (isa<CXXConstructExpr>(e)){
    CXXConstructExpr* c = cast<CXXConstructExpr>(e);
    CXXConstructorDecl* cons = c->getConstructor();
    if (cons->isDefaultConstructor())
    {
      std::string name = cons->getNameAsString();
      std::vector<Dexter::Expr*> params;
      return new Dexter::CallExpr(name + "_Constructor", params);
    }
    else if (cons->isCopyConstructor())
    {
      return parse(c->getArg(0), vars);
    }
    else
    {
      std::string name = cons->getNameAsString();

      Util::ASSERT(TypesFactory::isClassT(name),"Constructor of an unknown class found: " + name);
      Dexter::Type rType = TypesFactory::lookupClassT(name);

      std::vector<Dexter::Type> paramsT;
      std::vector<Dexter::Expr*> params;

      clang::CallExpr::arg_iterator it;
      for (it = c->arg_begin(); it != c->arg_end(); ++it)
      {
        if (isa<CXXDefaultArgExpr>(*it))
          continue;
        paramsT.push_back(toIRType((*it)->getType()));
        params.push_back(parse(*it, vars));
      }

      if (TypesFactory::isFunctionT(name + "_Constructor", rType, paramsT))
        return new Dexter::CallExpr(name + "_Constructor", params);
      else
        Util::error(e, "NYI: Constructor not found in DSL: ");
    }
  }

  else if (isa<ArraySubscriptExpr>(e))
    return new SelectExpr (
      Dexter::ClangToIRParser::parse(static_cast<ArraySubscriptExpr *>(e)->getBase(), vars),
      {Dexter::ClangToIRParser::parse(static_cast<ArraySubscriptExpr *>(e)->getIdx(), vars)}
    );

  else if (isa<MemberExpr>(e))
    return new FieldExpr(
      parse(cast<MemberExpr>(e)->getBase(), vars),
      cast<MemberExpr>(e)->getMemberDecl()->getNameAsString()
    );

  else if (isa<CXXThisExpr>(e))
    return new Dexter::VarExpr("inp", Dexter::ClangToIRParser::toIRType(e->getType()));

  else if (isa<IntegerLiteral>(e)){
    // e->getType()->dump();
    return new IntLitExpr(cast<IntegerLiteral>(e)->getValue().getSExtValue());
  }

  else if (isa<CXXBoolLiteralExpr>(e))  // only in C++
    return new BoolLitExpr(cast<CXXBoolLiteralExpr>(e)->getValue());

  else if (isa<FloatingLiteral>(e))
    return new FloatLitExpr(cast<FloatingLiteral>(e)->getValueAsApproximateDouble());

  else if (isa<DeclRefExpr>(e))
    return parse(cast<DeclRefExpr>(e), vars);

  else if (isa<CastExpr>(e))
    return parse(cast<CastExpr>(e), vars);

  else if (isa<ConditionalOperator>(e))
    return parse(cast<ConditionalOperator>(e), vars);

  else if (isa<ParenExpr>(e))
    return parse(cast<ParenExpr>(e)->getSubExpr(), vars);

  else Util::error(e, "don't know how to convert expression to IR: ");

  return NULL; // unreached
}

Dexter::Expr * Dexter::ClangToIRParser::parse (ConditionalOperator * e, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  Dexter::Expr * cond = parse(e->getCond(), vars);
  Dexter::Expr * cons = parse(e->getTrueExpr(), vars);
  Dexter::Expr * altr = parse(e->getFalseExpr(), vars);

  return new IfExpr(cond, cons, altr);
}

Dexter::Expr * Dexter::ClangToIRParser::parse (CastExpr * e, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  // Ignore casts on literals
  if (isa<IntegerLiteral>(e->getSubExpr()))
    return parse(e->getSubExpr(), vars);
  else if (isa<FloatingLiteral>(e->getSubExpr()))
    return parse(e->getSubExpr(), vars);

  switch(e->getCastKind())
  {
    case CK_NoOp:
      return parse(e->getSubExpr(), vars);
    case CK_IntegralToFloating:
    case CK_FloatingToIntegral:
    case CK_IntegralCast:
      return integerCast(parse(e->getSubExpr(), vars), e->getSubExpr()->getType(), e->getType());
    case CK_BitCast:
      // TODO
    case CK_LValueToRValue:
      // TODO
      return parse(e->getSubExpr(), vars);
    default:
      // TODO
      return parse(e->getSubExpr(), vars);
      //Util::error(e, "don't know how to handle cast: ");
  }

  return NULL; // unreached
}

Dexter::Expr * Dexter::ClangToIRParser::parse (DeclRefExpr * e, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  return parse(e->getFoundDecl(), vars);
}

Dexter::Expr * Dexter::ClangToIRParser::parse (NamedDecl * decl, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  if (isa<ValueDecl>(decl))
  {
    ValueDecl * v = cast<ValueDecl>(decl);
    if (vars.find(v) == vars.end())
    {
      Dexter::VarExpr * varExpr = new Dexter::VarExpr(v->getNameAsString().c_str(), Dexter::ClangToIRParser::toIRType(v->getType()));
      vars[v] = varExpr;
      return varExpr;
    }
    else
      return vars[v];
  }
  else
    Util::error(decl, "NYI: decl conversion to IR: ");

  // unreachable
  return NULL;
}

Dexter::VarExpr * Dexter::ClangToIRParser::parse (ParmVarDecl * decl)
{
  if (isa<ValueDecl>(decl))
  {
    ValueDecl * v = cast<ValueDecl>(decl);
    return new Dexter::VarExpr(v->getNameAsString().c_str(), Dexter::ClangToIRParser::toIRType(v->getType()));
  }
  else
    Util::error(decl, "NYI: decl conversion to IR: ");

  // unreachable
  return NULL;
}

Dexter::FuncDecl * Dexter::ClangToIRParser::parse (clang::FunctionDecl * fnDecl, ParmVarDecl * outParam)
{
  // Get function name
  std::string fn_name = fnDecl->getNameAsString();

  // Get return type
  Dexter::Type rType = (outParam == NULL ? Dexter::ClangToIRParser::toIRType(fnDecl->getReturnType()) : Dexter::ClangToIRParser::toIRType(outParam->getType()));

  // Get param types
  std::vector<Dexter::VarExpr*> params;
  clang::FunctionDecl::param_iterator it;
  for (it = fnDecl->param_begin(); it != fnDecl->param_end(); ++it)
    params.push_back(parse(*it));

  if (!fnDecl->hasBody()) {
    Util::error(fnDecl, "Source code unavailable for library function: " + Util::print(fnDecl) + "\n");
  }

  Dexter::Expr * body = parseFnBody(fnDecl->getBody(), new Dexter::VarExpr("ret_val_ph", rType));

  return new FuncDecl(fn_name + (outParam != NULL ? "_" + outParam->getNameAsString() : ""), params, rType, body, false);
}

Dexter::FuncDecl * Dexter::ClangToIRParser::parse (std::string cls_name, clang::CXXMethodDecl * mDecl)
{
  // Get function name
  std::string fn_name = mDecl->getNameAsString();

  // Get return type
  Dexter::Type rType = Dexter::ClangToIRParser::toIRType(mDecl->getReturnType());

  // Get param types
  std::vector<Dexter::VarExpr*> params;
  clang::FunctionDecl::param_iterator it;
  for (it = mDecl->param_begin(); it != mDecl->param_end(); ++it)
    params.push_back(parse(*it));

  if (!mDecl->hasBody()) {
    llvm::outs() << Util::print(mDecl) << "\n";
    Util::error(mDecl, "Unable to automatically model function: No body found.");
  }

  Dexter::Expr * body = parseFnBody(mDecl->getBody(), new Dexter::VarExpr("ret_val_ph", rType));

  FuncDecl * fn = new FuncDecl(cls_name + "_" + fn_name, params, rType, body, false);
  return fn;
}

Dexter::Type Dexter::ClangToIRParser::toIRType (QualType cType)
{
  if (isa<ElaboratedType>(cType.getTypePtr()))
    return toIRType(cast<ElaboratedType>(cType.getTypePtr())->getNamedType());

  else if (isa<TypedefType>(cType.getTypePtr()))
      return toIRType(cast<TypedefType>(cType.getTypePtr())->desugar());

  else if (cType->isReferenceType())
    return toIRType(cast<ReferenceType>(cType)->getPointeeType());

  else if (cType->isBooleanType())
      return TypesFactory::Bool;

  else if (cType->isCharType() && cType->isSignedIntegerType())
    return TypesFactory::Int8;

  else if (cType->isCharType())
    return TypesFactory::UInt8;

  else if (isShortType(cType) && cType->isSignedIntegerType())
    return TypesFactory::Int16;

  else if (isShortType(cType))
    return TypesFactory::UInt16;

  else if (cType->isIntegerType() && cType->isSignedIntegerType())
    return TypesFactory::Int32;

  else if (cType->isIntegerType())
    return TypesFactory::UInt32;

  else if (cType->isFloatingType())
    return TypesFactory::Float;

  else if (cType->isArrayType())
    return TypesFactory::arrayT(1, toIRType(cast<ArrayType>(cType)->getElementType()));

  else if (cType->isConstantArrayType())
    return TypesFactory::arrayT(1, toIRType(cast<ArrayType>(cType)->getElementType()));

  else if (cType->isPointerType())
  {
    if (Dexter::Preferences::Mode == 0)
      Util::error(cType, "Error: Pointer types are not supported within intentional code blocks. Use the Halide::Runtime::Buffer class instead.\n");

    QualType elemsT = cast<PointerType>(cType)->getPointeeType();

    // Handling typedefs
    if (isa<TypedefType>(elemsT.getTypePtr()))
    {
      const TypedefType * t = cast<TypedefType>(elemsT.getTypePtr());
      std::string tn = t->getDecl()->getNameAsString();
      if (TypesFactory::isClassT(tn))
        return TypesFactory::lookupClassT(tn);
    }

    if (elemsT->isBuiltinType())
      return TypesFactory::ptrT(toIRType(elemsT));
    else if (elemsT->isRecordType())
    {
      std::string tn = cast<RecordType>(elemsT)->getDecl()->getNameAsString();
      if (TypesFactory::isClassT(tn))
        return TypesFactory::lookupClassT(tn);
      else
       if (tn.compare("__m128i"))
         TypesFactory::lookupClassT("m128i");
       else
        Util::error(elemsT, "Non-builtin type pointer found.");
    }
    else
      Util::error(elemsT, "Non-builtin type pointer found.");

    // Unreachable
    return NULL;
  }

  else if(cType->isRecordType())
  {
    QualType des_cType = cType;

    if (isa<TemplateSpecializationType>(cType.getTypePtr()))
       des_cType = cast<TemplateSpecializationType>(cType.getTypePtr())->desugar();

    RecordDecl* rdecl = cast<RecordType>(des_cType.getTypePtr())->getDecl();

    std::string t = des_cType.getAsString();
    std::string tn = rdecl->getNameAsString();
    std::string tqn = rdecl->getQualifiedNameAsString();

    if (TypesFactory::isClassT(tn))
      return TypesFactory::lookupClassT(tn);
    else if (tqn == "Halide::Runtime::Buffer") {
      std::smatch res;
      std::regex pattern("(const )?class Halide::Runtime::Buffer<(.*),(.*)>");

      if (std::regex_match(t, res, pattern)) {
        std::string eT = getCanonicalName(res[2]);
        int dim = stoi(getCanonicalName(res[3]));

        if (eT == "uint8")
          return TypesFactory::bufferT(Dexter::TypesFactory::UInt8, dim);
        else if (eT == "uint16")
          return TypesFactory::bufferT(Dexter::TypesFactory::UInt16, dim);
        else if (eT == "uint32")
          return TypesFactory::bufferT(Dexter::TypesFactory::UInt32, dim);
        else if (eT == "int8")
          return TypesFactory::bufferT(Dexter::TypesFactory::Int8, dim);
        else if (eT == "int16")
          return TypesFactory::bufferT(Dexter::TypesFactory::Int16, dim);
        else if (eT == "int32")
          return TypesFactory::bufferT(Dexter::TypesFactory::Int32, dim);
        else if (eT == "float")
          return TypesFactory::bufferT(Dexter::TypesFactory::Float, dim);
        else
          Util::error(cType, ">> Don't know how to translate type to IR: ");
      }
      else
        Util::error(des_cType, ">>> Don't know how to translate type to IR: " + t + " ");
    }
    else
      Util::error(cType, "Don't know how to translate type to IR: ");

    // unreachable
    return NULL;
  }

  else
    Util::error(cType, "NYI: type conversion to IR: ");

  // unreachable
  return NULL;
}

bool Dexter::ClangToIRParser::isShortType (QualType cType)
{
  const auto *BT = cType->getAs<BuiltinType>();

  switch (BT->getKind()) {
    case BuiltinType::Short:
    case BuiltinType::UShort:
      return true;//return TypesFactory::Int;
    case BuiltinType::UChar:
    case BuiltinType::Char_U:
    case BuiltinType::WChar_U:
    case BuiltinType::Char_S:
    case BuiltinType::SChar:
    case BuiltinType::WChar_S:
    case BuiltinType::Char8:
    case BuiltinType::Char16:
    case BuiltinType::Char32:
      return false;
    default:
      return false;
  }
}

Dexter::Expr * Dexter::ClangToIRParser::integerCast (Dexter::Expr * subExpr, QualType from, QualType to)
{
  std::string from_cn = getCanonicalName(from.getLocalUnqualifiedType().getAsString());
  std::string to_cn = getCanonicalName(to.getLocalUnqualifiedType().getAsString());

  // Special handling for Halide buffer
  std::smatch res;
  std::regex pattern("Halide::Runtime::Buffer<(.*),.*>::not_void_T");

  if (std::regex_match(to_cn, res, pattern))
    to_cn = getCanonicalName(res[1]);
  if (std::regex_match(from_cn, res, pattern))
    from_cn = getCanonicalName(res[1]);

  //int from_pr = precision(from_cn);
  //int to_pr = precision(to_cn);

  Dexter::Type pType = (from_cn == "float" || from_cn == "double" ? (Dexter::Type) TypesFactory::Float : (Dexter::Type) TypesFactory::Int);
  Dexter::Type rType = (to_cn == "float" || to_cn == "double"  ? (Dexter::Type) TypesFactory::Float : (Dexter::Type) TypesFactory::Int);

  if (from_cn == to_cn)
    return subExpr;
  //else if (to_pr > from_pr && from_cn != "float" && !(from->isSignedIntegerType() && to->isUnsignedIntegerType()))
    //return subExpr;
  else if (TypesFactory::isFunctionT("cast_"+from_cn+"_"+to_cn, rType, {pType}))
    return new Dexter::CallExpr("cast_"+from_cn+"_"+to_cn, {subExpr});
  else
    Util::error("NYI. Casting from " + from_cn + " to " + to_cn);

  return NULL; // unreached
}

std::vector<Dexter::VarExpr *> Dexter::ClangToIRParser::liveVars (Stmt * s, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  std::set<ValueDecl *> lvars = Dexter::StmtExt::Get(s)->liveVars();

  std::vector<Dexter::VarExpr *> irVars;
  std::set<ValueDecl *>::const_iterator it;
  for (it = lvars.begin(); it != lvars.end(); ++it)
    irVars.push_back((Dexter::VarExpr *)Dexter::ClangToIRParser::parse(*it, vars));

  return irVars;
}

std::vector<Dexter::VarExpr *> Dexter::ClangToIRParser::outVars(Stage * s, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  std::set<ValueDecl *> ovars = s->getOutputVars();

  std::vector<Dexter::VarExpr *> irVars;
  std::set<ValueDecl *>::const_iterator it;
  for (it = ovars.begin(); it != ovars.end(); ++it)
    irVars.push_back((Dexter::VarExpr *)Dexter::ClangToIRParser::parse(*it, vars));

  return irVars;
}

std::vector<Dexter::VarExpr *> Dexter::ClangToIRParser::inVars(Stage * s, std::map<NamedDecl *, Dexter::Expr *> &vars)
{
  std::set<ValueDecl *> invars = s->getInputVars();

  std::vector<Dexter::VarExpr *> irVars;
  std::set<ValueDecl *>::const_iterator it;
  for (it = invars.begin(); it != invars.end(); ++it)
    irVars.push_back((Dexter::VarExpr *)Dexter::ClangToIRParser::parse(*it, vars));

  return irVars;
}

int Dexter::ClangToIRParser::precision (std::string tn)
{
  if (tn == "float")  return 0;
  if (tn == "int8")   return 1;
  if (tn == "uint8")  return 1;
  if (tn == "int16")  return 2;
  if (tn == "uint16") return 2;
  if (tn == "int32")  return 3;
  if (tn == "uint32") return 3;
  if (tn == "uint64") return 4;

  Util::error("NYI. Casting to/from type: " + tn);

  return -1; // Unreachable
}

std::string Dexter::ClangToIRParser::getCanonicalName (std::string tn)
{
  if (tn == "uint8_t")        return "uint8";
  if (tn == "unsigned8")      return "uint8";
  if (tn == "unsigned char")  return "uint8";
  if (tn == "Boolean")  return "uint8";
  if (tn == "short")          return "int16";
  if (tn == "unsigned short") return "uint16";
  if (tn == "int16_t")        return "int16";
  if (tn == "unsigned16")     return "uint16";
  if (tn == "int")            return "int32";
  if (tn == "long")           return "int32";
  if (tn == "int32_t")        return "int32";
  if (tn == "unsigned32")     return "uint32";
  if (tn == "unsigned int")   return "uint32";
  if (tn == "unsigned long")  return "uint32";
  if (tn == "float")          return "float";

  return tn;
}

Dexter::Expr * Dexter::ClangToIRParser::parseFnBody(Stmt * s, Dexter::Expr * body)
{
  /*if (isa<ParenExpr>(s))
    vc(cast<ParenExpr>(s)->getSubExpr(), vcGen);

  else if (isa<CastExpr>(s))
    vc(cast<CastExpr>(s)->getSubExpr(), vcGen);

  else if (isa<BinaryOperator>(s))
    vc(cast<BinaryOperator>(s), vcGen);

  else if (isa<UnaryOperator>(s))
    vc(cast<UnaryOperator>(s), vcGen);

  else if (isa<clang::CallExpr>(s))
    vc(cast<clang::CallExpr>(s), vcGen);

  */
  if (isa<ExprWithCleanups>(s))
    return parseFnBody(cast<ExprWithCleanups>(s)->getSubExpr(), body);

  else if (isa<BinaryOperator>(s))
    return parseFnBody(cast<BinaryOperator>(s), body);

  else if (isa<CompoundStmt>(s))
    return parseFnBody(cast<CompoundStmt>(s), body);

  else if (isa<ReturnStmt>(s))
    return parseFnBody(cast<ReturnStmt>(s), body);

  else if (isa<DeclStmt>(s))
    return parseFnBody(cast<DeclStmt>(s), body);

  else if (isa<IfStmt>(s))
    return parseFnBody(cast<IfStmt>(s), body);

  /*else if (isa<ConditionalOperator>(s))
    vc(cast<ConditionalOperator>(s), vcGen);

  else if (isa<WhileStmt>(s))
    vc(cast<WhileStmt>(s), vcGen);

  else if (isa<NullStmt>(s)); // Macros can be null statements; do nothing.

  else if (isa<IntegerLiteral>(s)); // Macros can be null statements; do nothing.
*/
  else {
    llvm::outs() << Util::print(s) << "\n";
    Util::error(s, "don't know how to convert stmt to IR: ");
  }

  // unreachable
  return NULL;
}

Dexter::Expr * Dexter::ClangToIRParser::parseFnBody(BinaryOperator * s, Dexter::Expr * body)
{
  clang::Expr * lhs = s->getLHS();
  clang::Expr * rhs = s->getRHS();

  std::map<NamedDecl *, Dexter::Expr *> vars;

  if (s->isCompoundAssignmentOp())
    body = Util::substitute(body, parse(lhs, vars), parse(s, vars));

  else if (s->isAssignmentOp())
  {
    if (isa<BinaryOperator>(rhs) && cast<BinaryOperator>(rhs)->isAssignmentOp())
    {
      Dexter::Expr ** bodyPtr = &body;
      body = Util::substitute(body, parse(lhs, vars), parseFnBody(cast<BinaryOperator>(rhs), bodyPtr));
    }
    else
      body = Util::substitute(body, parse(lhs, vars), parse(rhs, vars));
  }
  else
    Util::error(s, "Unexpected statement type: ");

  return body;
}

Dexter::Expr * Dexter::ClangToIRParser::parseFnBody(BinaryOperator * s, Dexter::Expr ** bodyPtr)
{
  clang::Expr * lhs = s->getLHS();
  clang::Expr * rhs = s->getRHS();

  std::map<NamedDecl *, Dexter::Expr *> vars;

  if (s->isCompoundAssignmentOp())
    Util::error(s, "Unexpected binary op: ");

  else if (s->isAssignmentOp())
  {
    if (isa<BinaryOperator>(rhs) && cast<BinaryOperator>(rhs)->isAssignmentOp())
    {
      Dexter::Expr * ret = parseFnBody(cast<BinaryOperator>(rhs), bodyPtr);
      *bodyPtr = Util::substitute(*bodyPtr, parse(lhs, vars), ret);
      return ret;
    }
    else
    {
      Dexter::Expr * ret = parse(rhs, vars);
      *bodyPtr = Util::substitute(*bodyPtr, parse(lhs, vars), ret);
      return ret;
    }
  }

  Util::error(s, "Unexpected binary op: ");

  return NULL;
}

Dexter::Expr * Dexter::ClangToIRParser::parseFnBody(CompoundStmt * s, Dexter::Expr * body)
{
  CompoundStmt::const_reverse_body_iterator it;
  for (it = s->body_rbegin(); it != s->body_rend(); ++it)
  {
    body = parseFnBody(*it, body);
  }
  return body;
}

Dexter::Expr * Dexter::ClangToIRParser::parseFnBody(ReturnStmt * s, Dexter::Expr * body)
{
  std::map<NamedDecl *, Dexter::Expr *> vars;
  return parse(s->getRetValue(), vars);
}

Dexter::Expr * Dexter::ClangToIRParser::parseFnBody(IfStmt * s, Dexter::Expr * body)
{
  std::map<NamedDecl *, Dexter::Expr *> vars;

  body = (s->getInit() != NULL ? parseFnBody(s->getInit(), body) : body);

  Dexter::Expr * cond = parse(s->getCond(), vars);
  Dexter::Expr * cons = parseFnBody(s->getThen(), body);
  Dexter::Expr * altr = (s->getElse() != NULL ? parseFnBody(s->getElse(), body) : body);

  return new IfExpr(cond, cons, altr);
}

Dexter::Expr * Dexter::ClangToIRParser::parseFnBody(DeclStmt * s, Dexter::Expr * body)
{
  DeclStmt::const_decl_iterator it;
  for (it = s->decl_begin(); it != s->decl_end(); ++it)
  {
    VarDecl * d = (VarDecl *)*it;
    if (d->hasInit())
    {
      std::map<NamedDecl *, Dexter::Expr *> vars;
      Dexter::VarExpr * v = cast<Dexter::VarExpr>(Dexter::ClangToIRParser::parse(makeDeclRefExpr(d, false), vars));
      body = Util::substitute(body, v, Dexter::ClangToIRParser::parse(d->getInit(), vars));
    }
  }

  return body;
}

DeclRefExpr * Dexter::ClangToIRParser::makeDeclRefExpr(const VarDecl *D, bool RefersToEnclosingVariableOrCapture)
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