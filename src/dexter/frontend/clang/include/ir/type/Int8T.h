/*
 * Int8T.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_INT8T_H_
#define IR_TYPE_INT8T_H_

#include "Type.h"

namespace Dexter {

class Int8T : public Dexter::Type
{
public:
  Int8T() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_INT8T_H_ */