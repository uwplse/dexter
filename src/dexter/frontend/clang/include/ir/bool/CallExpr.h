/*
 * CallExpr.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_CALLEXPR_H_
#define IR_BOOL_CALLEXPR_H_

#include "ir/Expr.h"

namespace Dexter {

class CallExpr : public Dexter::Expr
{
public:

  CallExpr (std::string name, std::vector<Dexter::Expr *> args);
  std::vector<Dexter::Expr *> args();
};

}

#endif /* IR_BOOL_CALLEXPR_H_ */