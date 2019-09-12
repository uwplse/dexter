/*
 * ClangToIRParser.h
 *
 *  Created on: Oct 18, 2017
 *      Author: maazsaf
 */

#ifndef CLANGTOIRPARSER_H_
#define CLANGTOIRPARSER_H_

#include "clang/AST/AST.h"

#include "dag/Stage.h"
#include "ast/StmtExt.h"
#include "ir/Expr.h"
#include "ir/type/Type.h"
#include "ir/type/FunctionT.h"
#include "ir/type/TypesFactory.h"
#include "ir/bool/VarExpr.h"
#include "ir/bool/UnaryExpr.h"
#include "ir/bool/BinaryExpr.h"
#include "ir/bool/BoolLitExpr.h"
#include "ir/bool/IfExpr.h"
#include "ir/bool/VarExpr.h"
#include "ir/bool/FieldExpr.h"
#include "ir/bool/CallExpr.h"
#include "ir/bool/FuncDecl.h"
#include "ir/integer/IntLitExpr.h"
#include "ir/integer/FloatLitExpr.h"
#include "ir/array/SelectExpr.h"
#include "ir/array/PtrExpr.h"
#include "ir/array/IncrPtrExpr.h"
#include "ir/array/DecrPtrExpr.h"
#include "ir/semantics/VCGenerator.h"

#include <regex>

namespace Dexter {

class ClangToIRParser
{
public:

  static Dexter::Expr * parse (CastExpr * e, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static Dexter::Expr * parse (NamedDecl * e, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static Dexter::Expr * parse (clang::Expr * e, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static Dexter::Expr * parse (DeclRefExpr * e, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static Dexter::Expr * parse (ConditionalOperator * e, std::map<NamedDecl *, Dexter::Expr *> &vars);

  static Dexter::FuncDecl * parse (FunctionDecl * f, ParmVarDecl * out = NULL);
  static Dexter::FuncDecl * parse (std::string cls_name, clang::CXXMethodDecl * mDecl);
  static Dexter::VarExpr * parse (ParmVarDecl * p);

  static Dexter::Type toIRType (QualType cType);

  static Dexter::Expr * integerCast(Dexter::Expr * e, QualType from, QualType to);
  static int precision (std::string tn);
  static std::string getCanonicalName (std::string tn);

  static std::vector<VarExpr *> liveVars(Stmt * s, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static std::vector<VarExpr *> scopeVars(Stmt * s, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static std::vector<VarExpr *> outVars(Stmt * s, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static std::vector<VarExpr *> outVars(Stage * s, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static std::vector<VarExpr *> inVars(Stmt * s, std::map<NamedDecl *, Dexter::Expr *> &vars);
  static std::vector<VarExpr *> inVars(Stage * s, std::map<NamedDecl *, Dexter::Expr *> &vars);

private:

  static Dexter::Expr * parseFnBody(Stmt * s, Dexter::Expr * body);
  static Dexter::Expr * parseFnBody(IfStmt * s, Dexter::Expr * body);
  static Dexter::Expr * parseFnBody(DeclStmt * s, Dexter::Expr * body);
  static Dexter::Expr * parseFnBody(ReturnStmt * s, Dexter::Expr * body);
  static Dexter::Expr * parseFnBody(CompoundStmt * s, Dexter::Expr * body);
  static Dexter::Expr * parseFnBody(BinaryOperator * s, Dexter::Expr * body);
  static Dexter::Expr * parseFnBody(BinaryOperator * s, Dexter::Expr ** bodyPtr);

  static DeclRefExpr * makeDeclRefExpr(const VarDecl *D, bool RefersToEnclosingVariableOrCapture);

  static bool isShortType (QualType cType);

  static bool debug;

};

}

#endif /* CLANGTOIRPARSER_H_ */