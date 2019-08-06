/*
 * IntLitExpr.h
 *
 *  Created on: Oct 22, 2017
 *      Author: maazsaf
 */

#ifndef IR_INTEGER_INTLITEXPR_H_
#define IR_INTEGER_INTLITEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class IntLitExpr : public Dexter::Expr
{
public:
  IntLitExpr (int value) : Dexter::Expr(NULL)
  {
    JNIEnv * e = Util::env;
    jclass c = e->FindClass("dexter/ir/integer/IntLitExpr");
    jmethodID m = e->GetMethodID(c, "<init>", "(I)V");
    this->mObj = e->NewObject(c, m, value);
  }

  int value () const
  {
    JNIEnv * e = Util::env;
    jclass c = e->FindClass("dexter/ir/integer/IntLitExpr");
    jmethodID m = e->GetMethodID(c, "value", "()I");
    jint v = e->CallIntMethod(mObj, m);
    return v;
  }
};

}

#endif /* IR_INTEGER_INTLITEXPR_H_ */