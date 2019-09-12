/*
 * UInt16T.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_UINT16T_H_
#define IR_TYPE_UINT16T_H_

#include "Type.h"

namespace Dexter {

class UInt16T : public Dexter::Type
{
public:
  UInt16T() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_UINT16T_H_ */