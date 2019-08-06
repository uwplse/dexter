/*
 * ArrayT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_ARRAYT_H_
#define IR_TYPE_ARRAYT_H_

#include "Type.h"

namespace Dexter {

class ArrayT : public Dexter::Type
{
public:
  ArrayT (jobject obj) : Dexter::Type(obj) {}
};

}

#endif /* IR_TYPE_ARRAYT_H_ */