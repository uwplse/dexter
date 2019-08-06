/*
 * FieldExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_FIELDEXPR_H_
#define IR_BOOL_FIELDEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class FieldExpr : public Dexter::Expr
{
public:

  FieldExpr (Dexter::Expr * target, std::string field);
};

}

#endif /* IR_BOOL_FIELDEXPR_H_ */