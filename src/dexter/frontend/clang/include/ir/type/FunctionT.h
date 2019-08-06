/*
 * FunctionT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_FUNCTIONT_H_
#define IR_TYPE_FUNCTIONT_H_

#include "Type.h"

namespace Dexter {

class FunctionT : public Dexter::Type
{
public:
  FunctionT (jobject obj) : Dexter::Type(obj) {}
};

}

#endif /* IR_TYPE_FUNCTIONT_H_ */