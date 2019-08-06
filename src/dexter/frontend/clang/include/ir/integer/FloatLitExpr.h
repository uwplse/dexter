/*
 * FloatLitExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_INTEGER_FLOATLITEXPR_H_
#define IR_INTEGER_FLOATLITEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class FloatLitExpr : public Dexter::Expr
{
public:
  FloatLitExpr (double value) : Dexter::Expr(NULL)
  {
    JNIEnv * e = Util::env;
    jclass c = e->FindClass("dexter/ir/integer/FloatLitExpr");
    jmethodID m = e->GetMethodID(c, "<init>", "(D)V");
    this->mObj = e->NewObject(c, m, value);
  }
};

}

#endif /* IR_INTEGER_FLOATLITEXPR_H_ */