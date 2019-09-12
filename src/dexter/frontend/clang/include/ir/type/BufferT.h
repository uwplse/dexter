/*
 * BufferT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_BUFFERT_H_
#define IR_TYPE_BUFFERT_H_

#include "Type.h"

namespace Dexter {

class BufferT : public Dexter::Type
{
public:
  BufferT (jobject obj) : Dexter::Type(obj) {}
};

}

#endif /* IR_TYPE_BUFFERT_H_ */