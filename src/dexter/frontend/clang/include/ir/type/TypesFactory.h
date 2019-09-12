/*
 * TypeSystem.h
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#ifndef IR_TYPE_TYPESFACTORY_H_
#define IR_TYPE_TYPESFACTORY_H_

#include "ir/type/IntT.h"
#include "ir/type/Int8T.h"
#include "ir/type/Int16T.h"
#include "ir/type/Int32T.h"
#include "ir/type/UInt8T.h"
#include "ir/type/UInt16T.h"
#include "ir/type/UInt32T.h"
#include "ir/type/BoolT.h"
#include "ir/type/FloatT.h"
#include "ir/type/ClassT.h"
#include "ir/type/ArrayT.h"
#include "ir/type/PtrT.h"
#include "ir/type/BufferT.h"
#include "ir/type/FunctionT.h"

#include "ir/bool/VarExpr.h"

#include <vector>

namespace Dexter {

class TypesFactory
{
public:
  static Dexter::IntT Int;
  static Dexter::Int8T Int8;
  static Dexter::Int16T Int16;
  static Dexter::Int32T Int32;
  static Dexter::UInt8T UInt8;
  static Dexter::UInt16T UInt16;
  static Dexter::UInt32T UInt32;
  static Dexter::BoolT Bool;
  static Dexter::FloatT Float;

  static void init ();
  static Dexter::PtrT ptrT (Dexter::Type elemsT);
  static Dexter::ArrayT arrayT (int dim, Dexter::Type elemsT);
  static Dexter::BufferT bufferT (Dexter::Type elemsT, int dim);
  static Dexter::ClassT classT (std::string name, std::vector<Dexter::VarExpr*> fields);
  static Dexter::ClassT lookupClassT (std::string name);
  static Dexter::FunctionT functionT(std::string name, Dexter::Type retT, std::vector<Dexter::Type> paramsT);
  static Dexter::FunctionT lookupFunctionT (std::string name, Dexter::Type retT, std::vector<Dexter::Type> paramsT);
  static bool isClassT (std::string name);
  static bool isFunctionT (std::string name, Dexter::Type retT, std::vector<Dexter::Type> paramsT);
};

}

#endif /* IR_TYPE_TYPESFACTORY_H_ */