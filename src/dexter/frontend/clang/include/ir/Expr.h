/*
 * PtrExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_EXPR_H_
#define IR_EXPR_H_

namespace Dexter {
  class Expr;
}

#include "Util.h"

namespace Dexter {

class Expr
{
public:

  enum ExprClass {
    ClassDecrPtrExpr,
    ClassIncrPtrExpr,
    ClassVarExpr,
    ClassExpr
  };

  Expr (jobject obj) : mObj(obj) {}

  jobject obj () const { return this->mObj; }

  virtual ExprClass classT () { return ClassExpr; }

protected:
  jobject mObj;
};

}

#endif /* IR_EXPR_H_ */