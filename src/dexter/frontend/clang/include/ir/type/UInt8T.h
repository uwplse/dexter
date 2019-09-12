/*
 * UInt8T.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_UINT8T_H_
#define IR_TYPE_UINT8T_H_

#include "Type.h"

namespace Dexter {

class UInt8T : public Dexter::Type
{
public:
  UInt8T() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_UINT8T_H_ */