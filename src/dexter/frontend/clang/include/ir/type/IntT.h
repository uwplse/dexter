/*
 * IntT.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_INTT_H_
#define IR_TYPE_INTT_H_

#include "Type.h"

namespace Dexter {

class IntT : public Dexter::Type
{
public:
  IntT() : Dexter::Type(NULL) {}
};

}

#endif /* IR_TYPE_INTT_H_ */