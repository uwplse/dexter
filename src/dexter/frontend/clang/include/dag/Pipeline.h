/*
 * Pipeline.h
 *
 *  Created on: Mar 5, 2018
 *      Author: maazsaf
 */

#ifndef PIPELINE_H_
#define PIPELINE_H_

#include "clang/AST/AST.h"

#include "dag/Stage.h"
#include "Util.h"

#include <vector>
#include <queue>

using namespace clang;

namespace Dexter {

class Pipeline
{
public:
  Pipeline ();

  void createNewStage ();
  void createFork (clang::Expr * cond, Pipeline * cons, Pipeline * altr);
  void addStage (Stage * s);
  void simplify ();
  void print ();

  Stage* getHead();
  Stage* getCurrStage ();
  std::set<Stage*> getAllStages();

protected:
  Stage* head;
  Stage* currStage;
};

}

#endif /* PIPELINE_H_ */