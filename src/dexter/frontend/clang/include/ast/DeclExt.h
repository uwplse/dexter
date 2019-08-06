/*
 * DeclExt.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef DECLEXT_H_
#define DECLEXT_H_

#include "NodeExt.h"

namespace Dexter {

class DeclExt : public NodeExt
{
public:
	DeclExt() {  }

	static DeclExt* Get (clang::Decl* d);

protected:
  static std::map<clang::Decl*, DeclExt*> exts;
};

}

#endif /* DECLEXT_H_ */