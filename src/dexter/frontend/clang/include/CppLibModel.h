/*
 * CppLibModel.h
 *
 *  Created on: June 23, 2018
 *      Author: maazsaf
 */

#ifndef CPPLIBMODEL_H_
#define CPPLIBMODEL_H_

#include "clang/AST/AST.h"

#include "ir/type/Type.h"
#include "ir/type/TypesFactory.h"

#include "ClangToMLParser.h"

#include <map>
#include <set>

namespace dexter {

class CppLibModel
{

private:
  static std::set<std::string> supportedTypes;
  static std::map<std::string, dexter::Type (*)(QualType)> typeTranslators;

  static std::set<std::string> supportedFunctions;
  static std::map<std::string, dexter::Expr* (*)(clang::CallExpr *, std::map<NamedDecl *, dexter::Expr *> &)> functionParsers;
  static std::map<std::string, void (*)(clang::CallExpr *, dexter::VCGenerator *, std::map<NamedDecl *, dexter::Expr *> &)> functionHandlers;

public:
  static bool isSupportedType(std::string tn);
  static dexter::Type translateType(QualType t);

  static bool isSupportedFunction(std::string fn);
  static dexter::Expr* parseFunction(clang::CallExpr * c, std::map<NamedDecl *, dexter::Expr *> &vars);
  static void handleFunction(clang::CallExpr * c, dexter::VCGenerator * vc, std::map<NamedDecl *, dexter::Expr *> &vars);

  static void init();

private:
  static dexter::Type tr_type_std_vector (QualType t);

  static dexter::Expr* parse_func_max (clang::CallExpr * c, std::map<NamedDecl *, dexter::Expr *> &vars);
  static dexter::Expr* parse_func_min (clang::CallExpr * c, std::map<NamedDecl *, dexter::Expr *> &vars);
  static dexter::Expr* parse_func_abs (clang::CallExpr * c, std::map<NamedDecl *, dexter::Expr *> &vars);

  static void hndl_func_assert (clang::CallExpr * c, dexter::VCGenerator * vc, std::map<NamedDecl *, dexter::Expr *> &vars);

};

}

#endif /* CPPLIBMODEL_H_ */