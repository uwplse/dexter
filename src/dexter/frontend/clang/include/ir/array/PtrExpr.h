/*
 * PtrExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_ARRAY_PTREXPR_H_
#define IR_ARRAY_PTREXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class PtrExpr : public Dexter::Expr
{
public:

  PtrExpr (Dexter::Expr * data, Dexter::Expr * offset);

  Dexter::Expr * data ();
  Dexter::Expr * offset ();
};

}

#endif /* IR_ARRAY_PTREXPR_H_ */