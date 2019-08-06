 /*
 * SelectExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/array/SelectExpr.h"

Dexter::SelectExpr::SelectExpr (Dexter::Expr * array, std::vector<Dexter::Expr *> indices) : Dexter::Expr(NULL)
{
	JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<Expr *>::const_iterator sit;
  for (sit = indices.begin(); sit != indices.end(); ++sit)
    e->CallObjectMethod(list, m, (*sit)->obj());

	c = e->FindClass("dexter/ir/array/SelectExpr");
	m = e->GetMethodID(c, "<init>", "(Ldexter/ir/Expr;Ljava/util/List;)V");
	this->mObj = e->NewObject(c, m, array->obj(), list);
}

Dexter::Expr * Dexter::SelectExpr::array()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("pdexter/ir/array/SelectExpr");
  jmethodID m = e->GetMethodID(c, "array", "()Ldexter/ir/Expr;");
  jobject id = e->CallObjectMethod(this->mObj, m);
  return (id == NULL) ? NULL : new Dexter::Expr(id);
}

std::vector<Dexter::Expr *> Dexter::SelectExpr::index()
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("dexter/ir/array/SelectExpr");
  jmethodID m = e->GetMethodID(c, "index", "()Ljava/util/List;");
  jobject list = e->CallObjectMethod(this->mObj, m);

  c = e->FindClass("java/util/List");
  m = e->GetMethodID(c, "size", "()I");
  int sz = (int) e->CallIntMethod(list, m);

  std::vector<Dexter::Expr *> idx;

  for (int i=0; i<sz; i++)
  {
    c = e->FindClass("java/util/List");
    m = e->GetMethodID(c, "get", "(I)Ljava/lang/Object;");
    jobject id = e->CallObjectMethod(list, m, i);

    idx.push_back(new Dexter::Expr(id));
  }

  return idx;
}