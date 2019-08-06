/*
 * FieldExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/bool/FieldExpr.h"

Dexter::FieldExpr::FieldExpr (Dexter::Expr * target, std::string field) : Dexter::Expr(NULL)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/bool/FieldExpr");
  jmethodID m = e->GetMethodID(c, "<init>", "(Ldexter/ir/Expr;Ljava/lang/String;)V");
  jstring f = e->NewStringUTF(field.c_str()) ;
  this->mObj = e->NewObject(c, m, target->obj(), f);
}