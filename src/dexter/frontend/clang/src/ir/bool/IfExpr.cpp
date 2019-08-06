/*
 * IfExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/bool/IfExpr.h"

Dexter::IfExpr::IfExpr (Dexter::Expr * cond, Dexter::Expr * cons, Dexter::Expr * alt) : Dexter::Expr(NULL)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/bool/IfExpr");
	jmethodID m = e->GetMethodID(c, "<init>", "(Ldexter/ir/Expr;Ldexter/ir/Expr;Ldexter/ir/Expr;)V");
	this->mObj = e->NewObject(c, m, cond->obj(), cons->obj(), alt->obj());
}