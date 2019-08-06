/*
 * BoolLitExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/bool/BoolLitExpr.h"

Dexter::BoolLitExpr * Dexter::BoolLitExpr::True;
Dexter::BoolLitExpr * Dexter::BoolLitExpr::False;

Dexter::BoolLitExpr::BoolLitExpr (bool value) : Dexter::Expr(NULL)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/bool/BoolLitExpr");
  jmethodID m = e->GetMethodID(c, "<init>", "(Z)V");
  this->mObj = e->NewObject(c, m, value);
}

void Dexter::BoolLitExpr::init ()
{
  True = new BoolLitExpr(true);
  False = new BoolLitExpr(false);
}