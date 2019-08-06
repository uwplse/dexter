/*
 * DecrPtrExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_ARRAY_DECRPTREXPR_H_
#define IR_ARRAY_DECRPTREXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class DecrPtrExpr : public Dexter::Expr
{
public:

  DecrPtrExpr (Dexter::Expr * ptr, Dexter::Expr * decr);

  Dexter::Expr * ptr ();
  Dexter::Expr * decr ();

  ExprClass classT () { return ClassDecrPtrExpr; }
};

}

#endif /* IR_ARRAY_DECRPTREXPR_H_ */