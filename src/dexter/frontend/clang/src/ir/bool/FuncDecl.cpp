/*
 * FuncDecl.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/bool/FuncDecl.h"

Dexter::FuncDecl::FuncDecl (std::string name, std::vector<VarExpr *> params, Dexter::Type retType, Dexter::Expr * body, bool isGen) : Dexter::Expr(NULL)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator sit;
  for (sit = params.begin(); sit != params.end(); ++sit)
    e->CallObjectMethod(list, m, (*sit)->obj());

  jstring n = e->NewStringUTF(name.c_str());

	c = e->FindClass("dexter/ir/bool/FuncDecl");
	m = e->GetMethodID(c, "<init>", "(Ljava/lang/String;Ljava/util/List;Ldexter/ir/type/Type;Ldexter/ir/Expr;Z)V");
	this->mObj = e->NewObject(c, m, n, list, retType.obj(), body->obj(), isGen);
}