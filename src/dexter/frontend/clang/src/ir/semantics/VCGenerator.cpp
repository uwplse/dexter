/*
 * VCGenerator.cpp
 *
 *  Created on: Oct 21, 2017
 *      Author: maazsaf
 */

#include "ir/semantics/VCGenerator.h"

Dexter::VCGenerator::VCGenerator ()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  this->mObj = e->NewObject(c, m);
}

Dexter::VCGenerator::VCGenerator (Dexter::VCGenerator * other)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "<init>", "(Ldexter/ir/semantics/VCGenerator;)V");
  this->mObj = e->NewObject(c, m, other->mObj);
}

Dexter::Expr * Dexter::VCGenerator::vc ()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "vc", "()Ldexter/ir/Expr;");
  jobject id = e->CallObjectMethod(this->mObj, m);
  return (id == NULL) ? NULL : new Dexter::Expr(id);
}

void Dexter::VCGenerator::vc (Dexter::Expr * vc)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "vc", "(Ldexter/ir/Expr;)V");
  e->CallObjectMethod(this->mObj, m, vc->obj());
}

Dexter::Program * Dexter::VCGenerator::createProgram (std::vector<VarExpr *> vars, std::vector<VarExpr *> &outVars)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/HashSet");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject set = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator sit;
  for (sit = vars.begin(); sit != vars.end(); ++sit)
    e->CallObjectMethod(set, m, (*sit)->obj());

  c = e->FindClass("java/util/HashSet");
  m = e->GetMethodID(c, "<init>", "()V");
  jobject seto = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator it;
  for (it = outVars.begin(); it != outVars.end(); ++it)
    e->CallObjectMethod(seto, m, (*it)->obj());

  c = e->FindClass("dexter/ir/semantics/VCGenerator");
  m = e->GetMethodID(c, "createProgram", "(Ljava/util/Set;Ljava/util/Set;)Ldexter/ir/bool/Program;");

  jobject id = e->CallObjectMethod(this->mObj, m, set, seto);
  return (id == NULL) ? NULL : new Dexter::Program(id);
}

void Dexter::VCGenerator::pc (std::vector<VarExpr *> &vars, std::vector<VarExpr *> &outVars)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator sit;
  for (sit = vars.begin(); sit != vars.end(); ++sit)
    e->CallObjectMethod(list, m, (*sit)->obj());

  c = e->FindClass("java/util/HashSet");
  m = e->GetMethodID(c, "<init>", "()V");
  jobject set = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator it;
  for (it = outVars.begin(); it != outVars.end(); ++it)
    e->CallObjectMethod(set, m, (*it)->obj());

  c = e->FindClass("dexter/ir/semantics/VCGenerator");
  m = e->GetMethodID(c, "pc", "(Ljava/util/List;Ljava/util/Set;)V");

  //e->CallObjectMethod(this->mObj, m, emptyList);
  e->CallObjectMethod(this->mObj, m, list, set);
}

Dexter::Expr * Dexter::VCGenerator::invariant (std::vector<VarExpr *> &vars, std::vector<VarExpr *> &outVars)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator sit;
  for (sit = vars.begin(); sit != vars.end(); ++sit)
    e->CallObjectMethod(list, m, (*sit)->obj());

  c = e->FindClass("java/util/HashSet");
  m = e->GetMethodID(c, "<init>", "()V");
  jobject set = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator it;
  for (it = outVars.begin(); it != outVars.end(); ++it)
    e->CallObjectMethod(set, m, (*it)->obj());

  c = e->FindClass("dexter/ir/semantics/VCGenerator");
  m = e->GetMethodID(c, "invariant", "(Ljava/util/List;Ljava/util/Set;)Ldexter/ir/bool/CallExpr;");

  jobject id = e->CallObjectMethod(this->mObj, m, list, set);
  return (id == NULL) ? NULL : new Dexter::Expr(id);
}

void Dexter::VCGenerator::mergeDecls (VCGenerator * other)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "mergeDecls", "(Ldexter/ir/semantics/VCGenerator;)V");
  e->CallObjectMethod(this->mObj, m, other->obj());
}

void Dexter::VCGenerator::mergePrior (Dexter::VCGenerator * other)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "mergePrior", "(Ldexter/ir/semantics/VCGenerator;)V");
  e->CallObjectMethod(this->mObj, m, other->obj());
}

void Dexter::VCGenerator::loop (Dexter::Expr * cond, Dexter::Expr * invariant, Dexter::Expr * continuation, std::vector<VarExpr *> &outVars)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/HashSet");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject set = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator sit;
  for (sit = outVars.begin(); sit != outVars.end(); ++sit)
    e->CallObjectMethod(set, m, (*sit)->obj());

  c = e->FindClass("dexter/ir/semantics/VCGenerator");
  m = e->GetMethodID(c, "loop", "(Ldexter/ir/Expr;Ldexter/ir/Expr;Ldexter/ir/Expr;Ljava/util/Set;)V");
  e->CallObjectMethod(this->mObj, m, cond->obj(), invariant->obj(), continuation->obj(), set);
}

void Dexter::VCGenerator::ploop (Dexter::Expr * cond, Dexter::Expr * invariant, Dexter::Expr * continuation, std::vector<VarExpr *> &outVars, Dexter::Expr * outerInv)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/HashSet");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject set = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator sit;
  for (sit = outVars.begin(); sit != outVars.end(); ++sit)
    e->CallObjectMethod(set, m, (*sit)->obj());

  c = e->FindClass("dexter/ir/semantics/VCGenerator");
  m = e->GetMethodID(c, "ploop", "(Ldexter/ir/Expr;Ldexter/ir/Expr;Ldexter/ir/Expr;Ljava/util/Set;Ldexter/ir/Expr;)V");
  e->CallObjectMethod(this->mObj, m, cond->obj(), invariant->obj(), continuation->obj(), set, outerInv->obj());
}

void Dexter::VCGenerator::conditional (Dexter::Expr * cond, Dexter::VCGenerator * consVC, Dexter::VCGenerator * altVC)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "conditional", "(Ldexter/ir/Expr;Ldexter/ir/semantics/VCGenerator;Ldexter/ir/semantics/VCGenerator;)V");
  e->CallObjectMethod(this->mObj, m, cond->obj(), consVC->mObj, altVC->mObj);
}

void Dexter::VCGenerator::replace (Dexter::Expr * from, Dexter::Expr * to)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "replace", "(Ldexter/ir/Expr;Ldexter/ir/Expr;)V");
  e->CallObjectMethod(this->mObj, m, from->obj(), to->obj());
}

void Dexter::VCGenerator::queueReplace (Dexter::Expr * from, Dexter::Expr * to)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "queueReplace", "(Ldexter/ir/Expr;Ldexter/ir/Expr;)V");
  e->CallObjectMethod(this->mObj, m, from->obj(), to->obj());
}

void Dexter::VCGenerator::runQueue ()
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "runQueue", "()V");
  e->CallObjectMethod(this->mObj, m);
}

void Dexter::VCGenerator::addAssumption (Dexter::Expr * a)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "addAssumption", "(Ldexter/ir/Expr;)V");
  e->CallObjectMethod(this->mObj, m, a->obj());
}

void Dexter::VCGenerator::registerBounds (Dexter::VarExpr * v, Dexter::Expr * w, Dexter::Expr * h)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/semantics/VCGenerator");
  jmethodID m = e->GetMethodID(c, "registerBounds", "(Ldexter/ir/bool/VarExpr;Ldexter/ir/Expr;Ldexter/ir/Expr;)V");
  e->CallObjectMethod(this->mObj, m, v->obj(), w->obj(), h->obj());
}