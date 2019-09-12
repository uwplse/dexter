/*
 * TypesFactory.cpp
 *
 *  Created on: Oct 19, 2017
 *      Author: maazsaf
 */

#include "ir/type/TypesFactory.h"

Dexter::IntT Dexter::TypesFactory::Int;
Dexter::Int8T Dexter::TypesFactory::Int8;
Dexter::Int16T Dexter::TypesFactory::Int16;
Dexter::Int32T Dexter::TypesFactory::Int32;
Dexter::UInt8T Dexter::TypesFactory::UInt8;
Dexter::UInt16T Dexter::TypesFactory::UInt16;
Dexter::UInt32T Dexter::TypesFactory::UInt32;
Dexter::BoolT Dexter::TypesFactory::Bool;
Dexter::FloatT Dexter::TypesFactory::Float;

void Dexter::TypesFactory::init ()
{
  JNIEnv * e = Util::env;
  jclass ts = e->FindClass("dexter/ir/type/TypesFactory");

  // Reset type factory
  jmethodID m = e->GetStaticMethodID(ts, "reset", "()V");
  e->CallStaticObjectMethod(ts, m);

  // Init types
  Bool.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "Bool", "Ldexter/ir/type/Type;")));
  Int.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "Int", "Ldexter/ir/type/Type;")));
  Int8.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "Int8", "Ldexter/ir/type/Type;")));
  Int16.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "Int16", "Ldexter/ir/type/Type;")));
  Int32.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "Int32", "Ldexter/ir/type/Type;")));
  UInt8.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "UInt8", "Ldexter/ir/type/Type;")));
  UInt16.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "UInt16", "Ldexter/ir/type/Type;")));
  UInt32.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "UInt32", "Ldexter/ir/type/Type;")));
  Float.init(e->GetStaticObjectField(ts, e->GetStaticFieldID(ts, "Float", "Ldexter/ir/type/Type;")));
}

Dexter::PtrT Dexter::TypesFactory::ptrT (Dexter::Type elemsT)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/type/TypesFactory");
  jmethodID m = e->GetStaticMethodID(c, "ptrT", "(Ldexter/ir/type/Type;)Ldexter/ir/type/Type;");
  jobject tobj = e->CallStaticObjectMethod(c, m, elemsT.obj());
  return PtrT(tobj);
}

Dexter::ArrayT Dexter::TypesFactory::arrayT (int dim, Dexter::Type elemsT)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/type/TypesFactory");
  jmethodID m = e->GetStaticMethodID(c, "arrayT", "(ILdexter/ir/type/Type;)Ldexter/ir/type/ArrayT;");
  jobject tobj = e->CallStaticObjectMethod(c, m, (jint) dim, elemsT.obj());
  return ArrayT(tobj);
}

Dexter::BufferT Dexter::TypesFactory::bufferT (Dexter::Type elemsT, int dim)
{
  JNIEnv * e = Util::env;
  jclass c = e->FindClass("dexter/ir/type/TypesFactory");
  jmethodID m = e->GetStaticMethodID(c, "bufferT", "(Ldexter/ir/type/Type;I)Ldexter/ir/type/BufferT;");
  jobject tobj = e->CallStaticObjectMethod(c, m, elemsT.obj(), (jint) dim);
  return BufferT(tobj);
}

Dexter::ClassT Dexter::TypesFactory::classT (std::string name, std::vector<VarExpr*> fields)
{
  JNIEnv * e = Util::env;

  jstring cn = e->NewStringUTF(name.c_str());

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<VarExpr *>::const_iterator sit;
  for (sit = fields.begin(); sit != fields.end(); ++sit)
    e->CallObjectMethod(list, m, (*sit)->obj());

  c = e->FindClass("dexter/ir/type/TypesFactory");
  m = e->GetStaticMethodID(c, "classT", "(Ljava/lang/String;Ljava/util/List;)Ldexter/ir/type/ClassT;");

  jobject tobj = e->CallStaticObjectMethod(c, m, cn, list);
  return ClassT(tobj);
}

Dexter::ClassT Dexter::TypesFactory::lookupClassT(std::string n)
{
	JNIEnv * e = Util::env;
  jclass ts = e->FindClass("dexter/ir/type/TypesFactory");
  jmethodID m = e->GetStaticMethodID(ts, "lookupClassT", "(Ljava/lang/String;)Ldexter/ir/type/ClassT;");
  jstring tn = e->NewStringUTF(n.c_str());
  jobject tobj = e->CallStaticObjectMethod(ts, m, tn);
  return ClassT(tobj);
}

Dexter::FunctionT Dexter::TypesFactory::functionT(std::string n, Dexter::Type retT, std::vector<Dexter::Type> paramsT)
{
  JNIEnv * e = Util::env;

  jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<Dexter::Type>::const_iterator it;
  for (it = paramsT.begin(); it != paramsT.end(); ++it)
    e->CallObjectMethod(list, m, (*it).obj());

  c = e->FindClass("dexter/ir/type/TypesFactory");
  m = e->GetStaticMethodID(c, "functionT", "(Ljava/lang/String;Ldexter/ir/type/Type;Ljava/util/List;)Ldexter/ir/type/FunctionT;");
  jstring tn = e->NewStringUTF(n.c_str());
  jobject tobj = e->CallStaticObjectMethod(c, m, tn, retT.obj(), list);
  return FunctionT(tobj);
}

Dexter::FunctionT Dexter::TypesFactory::lookupFunctionT(std::string n, Dexter::Type retT, std::vector<Dexter::Type> paramsT)
{
	JNIEnv * e = Util::env;

	jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<Dexter::Type>::const_iterator it;
  for (it = paramsT.begin(); it != paramsT.end(); ++it)
    e->CallObjectMethod(list, m, (*it).obj());

  c = e->FindClass("dexter/ir/type/TypesFactory");
  m = e->GetStaticMethodID(c, "lookupFunctionT", "(Ljava/lang/String;Ldexter/ir/type/Type;Ljava/util/List;)Ldexter/ir/type/FunctionT;");
  jstring tn = e->NewStringUTF(n.c_str());
  jobject tobj = e->CallStaticObjectMethod(c, m, tn, retT.obj(), list);
  return FunctionT(tobj);
}

bool Dexter::TypesFactory::isClassT(std::string n)
{
	JNIEnv * e = Util::env;
  jclass ts = e->FindClass("dexter/ir/type/TypesFactory");
  jmethodID m = e->GetStaticMethodID(ts, "isClassT", "(Ljava/lang/String;)Z");
  jstring tn = e->NewStringUTF(n.c_str());
  return (bool) e->CallStaticBooleanMethod(ts, m, tn);
}

bool Dexter::TypesFactory::isFunctionT(std::string n, Dexter::Type retT, std::vector<Dexter::Type> paramsT)
{
	JNIEnv * e = Util::env;

	jclass c = e->FindClass("java/util/ArrayList");
  jmethodID m = e->GetMethodID(c, "<init>", "()V");
  jobject list = e->NewObject(c, m);

  m = e->GetMethodID(c, "add", "(Ljava/lang/Object;)Z");
  std::vector<Dexter::Type>::const_iterator it;
  for (it = paramsT.begin(); it != paramsT.end(); ++it)
    e->CallObjectMethod(list, m, (*it).obj());

  c = e->FindClass("dexter/ir/type/TypesFactory");
  m = e->GetStaticMethodID(c, "isFunctionT", "(Ljava/lang/String;Ldexter/ir/type/Type;Ljava/util/List;)Z");
  jstring tn = e->NewStringUTF(n.c_str());
  return (bool) e->CallStaticBooleanMethod(c, m, tn, retT.obj(), list);
}