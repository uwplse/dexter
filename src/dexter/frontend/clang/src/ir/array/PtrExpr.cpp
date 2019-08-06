 /*
 * PtrExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/array/PtrExpr.h"

Dexter::PtrExpr::PtrExpr (Dexter::Expr * data, Dexter::Expr * offset) : Dexter::Expr(NULL)
{
	JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/array/PtrExpr");
	jmethodID m = e->GetMethodID(c, "<init>", "(Ldexter/ir/Expr;Ldexter/ir/Expr;)V");
	this->mObj = e->NewObject(c, m, data->obj(), offset->obj());
}

Dexter::Expr * Dexter::PtrExpr::data ()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/array/PtrExpr");
  jmethodID m = e->GetMethodID(c, "data", "()Ldexter/ir/Expr;");
  jobject id = e->CallObjectMethod(this->mObj, m);
  return (id == NULL) ? NULL : new Dexter::Expr(id);
}

Dexter::Expr * Dexter::PtrExpr::offset ()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/array/PtrExpr");
  jmethodID m = e->GetMethodID(c, "offset", "()Ldexter/ir/Expr;");
  jobject id = e->CallObjectMethod(this->mObj, m);
  return (id == NULL) ? NULL : new Dexter::Expr(id);
}