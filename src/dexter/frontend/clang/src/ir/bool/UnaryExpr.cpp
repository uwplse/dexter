/*
 * UnaryExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/bool/UnaryExpr.h"

Dexter::UnaryExpr::Op * Dexter::UnaryExpr::Op::NOT = new Op();
Dexter::UnaryExpr::Op * Dexter::UnaryExpr::Op::PLUS = new Op();
Dexter::UnaryExpr::Op * Dexter::UnaryExpr::Op::MINUS = new Op();
Dexter::UnaryExpr::Op * Dexter::UnaryExpr::Op::BNOT = new Op();

Dexter::UnaryExpr::UnaryExpr (Op * op, Dexter::Expr * base) : Dexter::Expr(NULL)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/bool/UnaryExpr");
  jmethodID m = e->GetMethodID(c, "<init>", "(Ldexter/ir/bool/UnaryExpr$Op;Ldexter/ir/Expr;)V");
  this->mObj = e->NewObject(c, m, op->obj(), base->obj());
}

void Dexter::UnaryExpr::init ()
{
  JNIEnv * e = Util::env;
  jclass ts = e->FindClass("dexter/ir/bool/UnaryExpr$Op");

  const char * opClass = "Ldexter/ir/bool/UnaryExpr$Op;";

  Op::NOT->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "NOT", opClass));
  Op::PLUS->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "PLUS", opClass));
  Op::MINUS->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "MINUS", opClass));
  Op::BNOT->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "BNOT", opClass));
}