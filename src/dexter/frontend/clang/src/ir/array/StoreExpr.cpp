 /*
 * StoreExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/array/StoreExpr.h"

Dexter::StoreExpr::StoreExpr (Dexter::Expr * array, Dexter::Expr * value, std::vector<Dexter::Expr *> indices) : Dexter::Expr(NULL)
{
	JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<Expr *>::const_iterator sit;
  for (sit = indices.begin(); sit != indices.end(); ++sit)
    e->CallObjectMethod(list, m, (*sit)->obj());

  c = e->FindClass("dexter/ir/array/StoreExpr");
  m = e->GetMethodID(c, "<init>", "(Ldexter/ir/Expr;Ldexter/ir/Expr;Ljava/util/List;)V");
  this->mObj = e->NewObject(c, m, array->obj(), value->obj(), list);
}