/*
 * BoolT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_BOOLT_H_
#define IR_TYPE_BOOLT_H_

#include "Type.h"

namespace Dexter {

class BoolT : public Dexter::Type
{
public:
  BoolT() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_BOOLT_H_ */