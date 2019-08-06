/*
 * UnaryExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_UNARYEXPR_H_
#define IR_BOOL_UNARYEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class UnaryExpr : public Dexter::Expr
{
public:

  struct Op
  {
    jobject mObj;

    jobject obj () const { return this->mObj; }

    static Op * NOT;
    static Op * PLUS;
    static Op * MINUS;
    static Op * BNOT;
  };

  UnaryExpr (Op * op, Dexter::Expr * base);

  static void init ();
};

}

#endif /* IR_BOOL_UNARYEXPR_H_ */