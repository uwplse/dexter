/*
 * SelectExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_ARRAY_SELECTEXPR_H_
#define IR_ARRAY_SELECTEXPR_H_

#include "ir/Expr.h"

#include <vector>

namespace Dexter {

class SelectExpr : public Dexter::Expr
{
public:

  SelectExpr (Dexter::Expr * array, std::vector<Dexter::Expr*> indices);
  Dexter::Expr * array();
  std::vector<Dexter::Expr *> index();
};

}

#endif /* IR_ARRAY_SELECTEXPR_H_ */