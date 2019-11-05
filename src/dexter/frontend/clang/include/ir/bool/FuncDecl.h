/*
 * FuncDecl.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_FUNCDECL_H_
#define IR_BOOL_FUNCDECL_H_

#include "ir/Expr.h"
#include "ir/bool/VarExpr.h"

namespace Dexter {

class FuncDecl : public Dexter::Expr
{
public:

  FuncDecl (std::string name, std::vector<VarExpr *> params, Dexter::Type retType, Dexter::Expr * body, bool isGen);
};

}

#endif /* IR_BOOL_CALLEXPR_H_ */