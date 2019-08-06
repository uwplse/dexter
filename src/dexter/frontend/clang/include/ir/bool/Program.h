/*
 * Program.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef IR_BOOL_PROGRAM_H_
#define IR_BOOL_PROGRAM_H_

#include "ir/Expr.h"

namespace Dexter {

class Program : public Dexter::Expr
{
public:
  Program (jobject obj) : Dexter::Expr(obj) {}
};

}

#endif /* IR_BOOL_PROGRAM_H_ */