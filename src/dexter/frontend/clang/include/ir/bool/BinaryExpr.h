/*
 * BinaryExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_BINARYEXPR_H_
#define IR_BOOL_BINARYEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class BinaryExpr : public Dexter::Expr
{
public:

  struct Op
  {
    jobject mObj;

    jobject obj () const { return this->mObj; }

    static Op * AND;
    static Op * OR;
    static Op * EQ;
    static Op * NEQ;
    static Op * LT;
    static Op * LE;
    static Op * GE;
    static Op * GT;
    static Op * PLUS;
    static Op * MINUS;
    static Op * MULT;
    static Op * DIV;
    static Op * MOD;
    static Op * SHL;
    static Op * LSHR;
    static Op * ASHR;
    static Op * BAND;
    static Op * BOR;
    static Op * BXOR;
  };

  BinaryExpr (Dexter::Expr * left, Op * op, Dexter::Expr * right);

  static void init ();
};

}

#endif /* IR_BOOL_BINARYEXPR_H_ */