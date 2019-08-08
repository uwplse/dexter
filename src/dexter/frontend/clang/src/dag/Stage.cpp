/*
 * Stage.cpp
 *
 *  Created on: Apr 29, 2017
 *      Author: maazsaf
 */

#include "dag/Stage.h"

int Dexter::Stage::NextId = 0;

Dexter::Stage::Stage ()
{
  this->id = ++NextId;
  this->_forks = false;
  this->_containsLoop = false;
  this->cond = NULL;
  this->cons = NULL;
  this->altr = NULL;
  this->next = NULL;
}

void Dexter::Stage::addStatement (Stmt * s)
{
  this->statements.push_back(s);
}

void Dexter::Stage::setNextStage (Dexter::Stage * s)
{
  this->next = s;
}

Dexter::Stage* Dexter::Stage::getNextStage ()
{
  return this->next;
}

void Dexter::Stage::containsLoop(bool cl)
{
  this->_containsLoop = cl;
}

void Dexter::Stage::createFork (clang::Expr * cond, Dexter::Stage * cons, Dexter::Stage * altr)
{
  this->_forks = true;
  this->cond = cond;
  this->cons = cons;
  this->altr = altr;
}

int Dexter::Stage::getId()
{
  return this->id;
}

std::vector<Stmt*> Dexter::Stage::getStatements()
{
  return this->statements;
}

bool Dexter::Stage::isEmpty()
{
  return this->statements.empty();
}

bool Dexter::Stage::forks()
{
  return this->_forks;
}

bool Dexter::Stage::containsLoop()
{
  return this->_containsLoop;
}

clang::Expr* Dexter::Stage::getCond ()
{
  return this->cond;
}

Dexter::Stage* Dexter::Stage::getCons ()
{
  return this->cons;
}

Dexter::Stage* Dexter::Stage::getAltr ()
{
  return this->altr;
}

void Dexter::Stage::setIntConsts (std::set<int> ints)
{
  this->int_consts = ints;
}

void Dexter::Stage::setFloatConsts (std::set<double> floats)
{
  this->float_consts = floats;
}

void Dexter::Stage::setLocalVars (std::set<ValueDecl *> local)
{
  this->local_vars = local;
}

void Dexter::Stage::setInputVars (std::set<ValueDecl *> in)
{
  this->in_vars = in;
}

void Dexter::Stage::setOutputVars (std::set<ValueDecl *> out)
{
  this->out_vars = out;
}

void Dexter::Stage::setOutputArrayVars (std::set<ValueDecl *> out_arr)
{
  this->out_arr_vars = out_arr;
}

void Dexter::Stage::setOutputBufferVars (std::set<ValueDecl *> out_buf)
{
  this->out_buf_vars = out_buf;
}

void Dexter::Stage::setIndexVars (std::set<ValueDecl *> idx)
{
  this->idx_vars = idx;
}

void Dexter::Stage::setUDFs (std::set<Dexter::FuncDecl *> fns)
{
  this->udfs = fns;
}

void Dexter::Stage::setVCs (Dexter::Program * p)
{
  this->vcs = p;
}

std::set<int> Dexter::Stage::getIntConsts ()
{
  return this->int_consts;
}

std::set<double> Dexter::Stage::getFloatConsts ()
{
  return this->float_consts;
}

std::set<ValueDecl *> Dexter::Stage::getLocalVars ()
{
  return this->local_vars;
}

std::set<ValueDecl *> Dexter::Stage::getInputVars ()
{
  return this->in_vars;
}

std::set<ValueDecl *> Dexter::Stage::getOutputVars ()
{
  return this->out_vars;
}

std::set<ValueDecl *> Dexter::Stage::getOutputArrayVars ()
{
  return this->out_arr_vars;
}

std::set<ValueDecl *> Dexter::Stage::getOutputBufferVars ()
{
  return this->out_buf_vars;
}

std::set<ValueDecl *> Dexter::Stage::getIndexVars ()
{
  return this->idx_vars;
}

std::set<Dexter::FuncDecl *> Dexter::Stage::getUDFs ()
{
  return this->udfs;
}

Dexter::Program * Dexter::Stage::getVCs ()
{
  return this->vcs;
}