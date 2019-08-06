/*
 * IncrPtrExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_ARRAY_INCRPTREXPR_H_
#define IR_ARRAY_INCRPTREXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class IncrPtrExpr : public Dexter::Expr
{
public:

  IncrPtrExpr (Dexter::Expr * ptr, Dexter::Expr * incr);

  Dexter::Expr * ptr ();
  Dexter::Expr * incr ();

  ExprClass classT () { return ClassIncrPtrExpr; }
};

}

#endif /* IR_ARRAY_INCRPTREXPR_H_ */