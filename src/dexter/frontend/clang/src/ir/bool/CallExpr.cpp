/*
 * CallExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/bool/CallExpr.h"

Dexter::CallExpr::CallExpr (std::string name, std::vector<Dexter::Expr *> args) : Dexter::Expr(NULL)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<Expr *>::const_iterator sit;
  for (sit = args.begin(); sit != args.end(); ++sit)
    e->CallObjectMethod(list, m, (*sit)->obj());

  jstring n = e->NewStringUTF(name.c_str());

	c = e->FindClass("dexter/ir/bool/CallExpr");
	m = e->GetMethodID(c, "<init>", "(Ljava/lang/String;Ljava/util/List;)V");
	this->mObj = e->NewObject(c, m, n, list);
}

std::vector<Dexter::Expr *> Dexter::CallExpr::args()
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("dexter/ir/bool/CallExpr");
  jmethodID m = e->GetMethodID(c, "args", "()Ljava/util/List;");
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