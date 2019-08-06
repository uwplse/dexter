 /*
 * DecrPtrExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/array/DecrPtrExpr.h"

Dexter::DecrPtrExpr::DecrPtrExpr (Dexter::Expr * data, Dexter::Expr * offset) : Dexter::Expr(NULL)
{
	JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/array/DecrPtrExpr");
	jmethodID m = e->GetMethodID(c, "<init>", "(Ldexter/ir/Expr;Ldexter/ir/Expr;)V");
	this->mObj = e->NewObject(c, m, data->obj(), offset->obj());
}

Dexter::Expr * Dexter::DecrPtrExpr::ptr ()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/array/DecrPtrExpr");
  jmethodID m = e->GetMethodID(c, "ptr", "()Ldexter/ir/Expr;");
  jobject id = e->CallObjectMethod(this->mObj, m);
  return (id == NULL) ? NULL : new Dexter::Expr(id);
}

Dexter::Expr * Dexter::DecrPtrExpr::decr ()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/array/DecrPtrExpr");
  jmethodID m = e->GetMethodID(c, "decr", "()Ldexter/ir/Expr;");
  jobject id = e->CallObjectMethod(this->mObj, m);
  return (id == NULL) ? NULL : new Dexter::Expr(id);
}