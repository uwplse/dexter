/*
 * VarExpr.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_BOOL_VAREXPR_H_
#define IR_BOOL_VAREXPR_H_

#include "ir/Expr.h"
#include "ir/type/Type.h"

namespace Dexter {

class VarExpr : public Dexter::Expr
{
public:
  VarExpr (const char * name, Dexter::Type type) : Dexter::Expr(NULL)
  {
    JNIEnv * e = Dexter::Util::env;
    jclass c = e->FindClass("dexter/ir/bool/VarExpr");
    jmethodID m = e->GetMethodID(c, "<init>", "(Ljava/lang/String;Ldexter/ir/type/Type;)V");
    this->mObj = e->NewObject(c, m, e->NewStringUTF(name), type.obj());
  }

  const char * name () const
  {
    JNIEnv * e = Util::env;
    jclass c = e->FindClass("dexter/ir/bool/VarExpr");
    jmethodID m = e->GetMethodID(c, "name", "()Ljava/lang/String;");
    jstring str = (jstring)e->CallObjectMethod(mObj, m);
    return e->GetStringUTFChars(str, JNI_FALSE);
  }

  const Dexter::Type * type () const
  {
    JNIEnv * e = Util::env;
    jclass c = e->FindClass("dexter/ir/bool/VarExpr");
    jmethodID m = e->GetMethodID(c, "type", "()Ldexter/ir/type/Type;");
    jobject type = (jstring)e->CallObjectMethod(mObj, m);
    Dexter::Type * t = new Dexter::Type(type);
    return t;
  }

  ExprClass classT () { return ClassVarExpr; }
};

}

#endif /* IR_BOOL_VAREXPR_H_ */