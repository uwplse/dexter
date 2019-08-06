/*
 * Type.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_TYPE_H_
#define IR_TYPE_TYPE_H_

#include <jni.h>

namespace Dexter {

class Type
{
public:
    Type (jobject obj) : mObj(obj) {}

  	jobject obj () const { return this->mObj; }

  	void init (jobject obj) { mObj = obj; }

protected:
	jobject mObj;
};

}

#endif /* IR_TYPE_TYPE_H_ */