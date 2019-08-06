/*
 * BoolLitExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_BOOLLITEXPR_H_
#define IR_BOOL_BOOLLITEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class BoolLitExpr : public Dexter::Expr
{
public:

  BoolLitExpr (bool value);

  static BoolLitExpr * True;
  static BoolLitExpr * False;

  static void init ();
};

}

#endif /* IR_BOOL_BOOLLITEXPR_H_ */