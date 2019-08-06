/*
 * StoreExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_ARRAY_STOREEXPR_H_
#define IR_ARRAY_STOREEXPR_H_

#include "ir/Expr.h"

#include <vector>

namespace Dexter {

class StoreExpr : public Dexter::Expr
{
public:

  StoreExpr (Dexter::Expr * array, Dexter::Expr * value, std::vector<Dexter::Expr*> indices);
};

}

#endif /* IR_ARRAY_STOREEXPR_H_ */