/*
 * Int32T.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_INT32T_H_
#define IR_TYPE_INT32T_H_

#include "Type.h"

namespace Dexter {

class Int32T : public Dexter::Type
{
public:
  Int32T() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_INT32T_H_ */