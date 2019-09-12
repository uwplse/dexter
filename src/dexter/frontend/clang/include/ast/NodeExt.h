/*
 * NodeExt.h
 *
 *  Created on: Mar 24, 2018
 *      Author: maazsaf
 */

#ifndef NODEEXT_H_
#define NODEEXT_H_

#include "clang/AST/AST.h"

#include "dag/Pipeline.h"

#include <map>

using namespace clang;

namespace Dexter {

namespace Target {
  enum TargetLang {Halide};
}

class NodeExt
{
public:
	NodeExt() { _lift = false; _target = Dexter::Target::Halide; }

  bool lift () { return _lift; }
	void lift (bool l) { _lift = l; }

	Dexter::Target::TargetLang target () { return _target; }
  void target (Dexter::Target::TargetLang t) { _target = t; }

  Dexter::Pipeline* DAG () { return _dag; }
  void DAG (Dexter::Pipeline* d) { _dag = d; }

protected:
  bool _lift;
  Dexter::Target::TargetLang _target;
  Dexter::Pipeline* _dag;
};

}

#endif /* NODEEXT_H_ */