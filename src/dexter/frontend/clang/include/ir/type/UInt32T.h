/*
 * UInt32T.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_UINT32T_H_
#define IR_TYPE_UINT32T_H_

#include "Type.h"

namespace Dexter {

class UInt32T : public Dexter::Type
{
public:
  UInt32T() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_UINT32T_H_ */