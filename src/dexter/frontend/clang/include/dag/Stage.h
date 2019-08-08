/*
 * Stage.h
 *
 *  Created on: Mar 5, 2018
 *      Author: maazsaf
 */

#ifndef STAGE_H_
#define STAGE_H_

#include "clang/AST/AST.h"

#include "ir/bool/Program.h"
#include "ir/bool/FuncDecl.h"

#include <vector>
#include <set>

using namespace clang;

namespace Dexter {

class Stage
{
public:
  Stage ();

  void addStatement (Stmt * s);
  void setNextStage (Stage * s);
  void containsLoop (bool cl);
  void createFork (clang::Expr * cond, Stage * cons, Stage * altr);

  void setIntConsts (std::set<int> ints);
  void setFloatConsts (std::set<double> floats);
  void setLocalVars (std::set<ValueDecl *> local_vars);
  void setInputVars (std::set<ValueDecl *> in_vars);
  void setOutputVars (std::set<ValueDecl *> out_vars);
  void setOutputArrayVars (std::set<ValueDecl *> out_arr_vars);
  void setOutputBufferVars (std::set<ValueDecl *> out_buf_vars);
  void setIndexVars (std::set<ValueDecl *> idx_vars);
  void setUDFs (std::set<FuncDecl *> udfs);
  void setVCs (Dexter::Program * p);

  bool forks();
  bool containsLoop();
  bool isEmpty();

  int getId();
  std::vector<Stmt*> getStatements ();
  clang::Expr * getCond ();
  Stage * getCons ();
  Stage * getAltr ();
  Stage * getNextStage ();
  std::set<int> getIntConsts ();
  std::set<double> getFloatConsts ();
  std::set<ValueDecl *> getLocalVars ();
  std::set<ValueDecl *> getInputVars ();
  std::set<ValueDecl *> getOutputVars ();
  std::set<ValueDecl *> getOutputArrayVars ();
  std::set<ValueDecl *> getOutputBufferVars ();
  std::set<ValueDecl *> getIndexVars ();
  std::set<FuncDecl *> getUDFs ();
  Program * getVCs ();

public:
  static int NextId;

protected:
  int id;
  bool _forks;
  bool _containsLoop;

  clang::Expr* cond;
  Stage* cons;
  Stage* altr;

  Stage* next;

  std::vector<Stmt*> statements;

  std::set<int> int_consts;
  std::set<double> float_consts;
  std::set<ValueDecl *> local_vars;
  std::set<ValueDecl *> in_vars;
  std::set<ValueDecl *> out_vars;
  std::set<ValueDecl *> out_arr_vars;
  std::set<ValueDecl *> out_buf_vars;
  std::set<ValueDecl *> idx_vars;
  std::set<FuncDecl *> udfs;
  Program * vcs;
};

}

#endif /* STAGE_H_ */