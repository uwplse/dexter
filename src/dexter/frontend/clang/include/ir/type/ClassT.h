/*
 * ClassT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_CLASST_H_
#define IR_TYPE_CLASST_H_

#include "Type.h"

namespace Dexter {

class ClassT : public Dexter::Type
{
public:
  ClassT (jobject obj) : Dexter::Type(obj) {}
};

}

#endif /* IR_TYPE_CLASST_H_ */