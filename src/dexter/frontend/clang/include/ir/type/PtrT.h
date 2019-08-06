/*
 * PtrT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_PTRT_H_
#define IR_TYPE_PTRT_H_

#include "Type.h"

namespace Dexter {

class PtrT : public Dexter::Type
{
public:
  PtrT (jobject obj) : Dexter::Type(obj) {}
};

}

#endif /* IR_TYPE_PTRT_H_ */