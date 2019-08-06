/*
 * FloatT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_FLOATT_H_
#define IR_TYPE_FLOATT_H_

#include "Type.h"

namespace Dexter {

class FloatT : public Dexter::Type
{
public:
  FloatT() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_FLOATT_H_ */