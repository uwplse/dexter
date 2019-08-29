/*
 * Util.h
 *
 *  Created on: Oct 18, 2017
 *      Author: maazsaf
 */

#ifndef UTIL_H_
#define UTIL_H_

#include <jni.h>

#include "clang/AST/AST.h"
#include "clang/Lex/Lexer.h"
#include "clang/Rewrite/Core/Rewriter.h"

#include "ir/Expr.h"

using namespace clang;

namespace Dexter {

class Util
{
public:
  static JavaVM * jvm;
  static JNIEnv * env;

  static void initJVM (const std::string & dxjarPath);
  static void shutdownJVM ();

  static void print (Dexter::Expr * e);
  static std::string toString (Dexter::Expr * e);

  static void ASSERT (bool cond, std::string msg);

  static void error (std::string msg);
  static void error (Stmt * s, std::string msg);
  static void error (const clang::Type * t, std::string msg);
  static void error (QualType t, std::string msg);
  static void error (Decl * s, std::string msg);

  static const std::string print (clang::Stmt * s);
  static const std::string print (clang::FunctionDecl * f);
  static const std::string print (jobject p);
  static const std::string printr (FunctionDecl * f, Rewriter rewriter);
  static const std::string printr (CXXRecordDecl * f, Rewriter rewriter);

  static const SourceRange getSourceRange(Stmt * s, Rewriter rewriter);
  static const SourceRange getSourceRange(Decl * s, Rewriter rewriter);

  static Dexter::Expr * substitute(Dexter::Expr * base, Dexter::Expr * from, Dexter::Expr * to);
};

}

#endif /* UTIL_H_ */