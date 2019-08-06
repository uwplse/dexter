/*
 * BinaryExpr.cpp
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#include "ir/bool/BinaryExpr.h"

Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::AND = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::OR = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::EQ = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::NEQ = new Op();

Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::LT = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::LE = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::GE = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::GT = new Op();

Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::PLUS = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::MINUS = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::MULT = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::DIV = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::MOD = new Op();

Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::SHL = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::LSHR = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::ASHR = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::BAND = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::BOR = new Op();
Dexter::BinaryExpr::Op * Dexter::BinaryExpr::Op::BXOR = new Op();

Dexter::BinaryExpr::BinaryExpr (Dexter::Expr * left, Op * op, Dexter::Expr * right) : Dexter::Expr(NULL)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/bool/BinaryExpr");
  jmethodID m = e->GetMethodID(c, "<init>", "(Ldexter/ir/Expr;Ldexter/ir/bool/BinaryExpr$Op;Ldexter/ir/Expr;)V");
  this->mObj = e->NewObject(c, m, left->obj(), op->obj(), right->obj());
}

void Dexter::BinaryExpr::init ()
{
  JNIEnv * e = Util::env;
  jclass ts = e->FindClass("dexter/ir/bool/BinaryExpr$Op");

  const char * opClass = "Ldexter/ir/bool/BinaryExpr$Op;";

  Op::AND->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "AND", opClass));
  Op::OR->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "OR", opClass));
  Op::EQ->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "EQ", opClass));
  Op::NEQ->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "NEQ", opClass));

  Op::LT->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "LT", opClass));
  Op::LE->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "LE", opClass));
  Op::GE->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "GE", opClass));
  Op::GT->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "GT", opClass));

  Op::PLUS->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "PLUS", opClass));
  Op::MINUS->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "MINUS", opClass));
  Op::MULT->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "MULT", opClass));
  Op::DIV->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "DIV", opClass));
  Op::MOD->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "MOD", opClass));

  Op::SHL->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "SHL", opClass));
  Op::LSHR->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "LSHR", opClass));
  Op::ASHR->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "ASHR", opClass));
  Op::BAND->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "BAND", opClass));
  Op::BOR->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "BOR", opClass));
  Op::BXOR->mObj = e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "BXOR", opClass));
}
