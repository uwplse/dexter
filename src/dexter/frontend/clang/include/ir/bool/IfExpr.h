/*
 * IfExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_IFEXPR_H_
#define IR_BOOL_IFEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class IfExpr : public Dexter::Expr
{
public:

  IfExpr (Dexter::Expr * cond, Dexter::Expr * cons, Dexter::Expr * alt);
};

}

#endif /* IR_BOOL_IFEXPR_H_ */